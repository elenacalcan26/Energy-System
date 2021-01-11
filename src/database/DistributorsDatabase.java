package database;

import entities.Consumers;
import entities.Contract;
import entities.Distributors;
import entities.Producers;
import strategies.EnergyChoiceStrategyFactory;

import java.util.ArrayList;
import java.util.Iterator;

public final class DistributorsDatabase {
    private final ArrayList<Distributors> distributors;

    public DistributorsDatabase() {
        distributors = new ArrayList<>();
    }

    public ArrayList<Distributors> getDistributors() {
        return distributors;
    }

    /**
     * Adauga un distribuitor in baza de date
     * @param distributor distribuitorul adaugat
     */
    public void setDistributors(Distributors distributor) {
        distributors.add(distributor);
    }

    /**
     * Fiecare distribuitor isi alege producatorii in functie de strategia pe care o are
     * @param producersDB baza de date a producatorilor
     */
    public void chooseProducers(ProducersDatabase producersDB) {

        EnergyChoiceStrategyFactory energy = new EnergyChoiceStrategyFactory();

        for (Distributors d : distributors) {
            if (d.isChangeProducers()) {
                d.setChangeProducers(false);
                energy.createEnergyChoiceStrategy(d.getProducerStrategy()).energyChoice(d,
                        producersDB);
            }
        }
    }

    /**
     * Calculeaza pretul contractului pentru fiecare distribuitor
     */
    public void distributorsContractPrices() {
        for (Distributors d : distributors) {
            d.setContractCost(d.contractPrice());
        }
    }

    /**
     * Cauta in baza de date distribuitorul cu cel mai mic pret al contractului.
     * @return distribuitorul cel mai ieftin
     */
    public Distributors cheapDistributor() {
        Distributors distributor = null;
        for (Distributors d : this.distributors) {
            if (!d.isBankrupt()) {
                distributor = d;
                break;
            }
        }
        for (Distributors d : this.distributors) {
            if (!d.isBankrupt()) {
                assert distributor != null;
                if (distributor.contractPrice() > d.contractPrice()) {
                    distributor = d;
                }
            }
        }
        return distributor;
    }

    /**
     *  Cauta si elimina consumatorii care sunt falimentati si sterge contractul cu acestia
     */
    public void findBankruptConsumers() {
        for (Distributors d : distributors) {
            Iterator<Consumers> it =  d.getConsumers().iterator();
            while (it.hasNext()) {
                Consumers c = it.next();
                if (c.isBankrupt()) {
                    d.getContracts().remove(d.getConsumers().indexOf(c));
                    it.remove();
                }
            }
        }
    }

    /**
     * Elimina contractele terminate si sterge consumatorii din lista distribuitorilor
     */
    public void findEndedContracts() {
        for (Distributors d : distributors) {
            Iterator<Contract> it =  d.getContracts().iterator();
            while (it.hasNext()) {
                Contract c = it.next();
                if (c.getRemainedContractMonths() == 0) {
                    d.getConsumers().get(d.getContracts().indexOf(c)).setHasContract(false);
                    d.getConsumers().remove(d.getContracts().indexOf(c));
                    it.remove();
                }
            }
        }
    }

    /**
     * Cauta in baza de date distribuitorul cu id-ul dat ca parametru
     * @param id id-ul distribuitorului
     * @return distribuitorul cu id-ul respectiv
     */
    public Distributors findDistributor(final long id) {
        for (Distributors d : distributors) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    /**
     * Cauta in baza de date contractul unui consumator cu un anumit distribuitor
     * @param consumer consumatorul caruia i se cauta contractul
     * @return numarul lunilor ramase din contract
     *         -1 consumatorul nu are contract
     */
    public long findConsumerContractLength(Consumers consumer) {
        for (Distributors d : this.distributors) {
            if (d.getConsumers().contains(consumer)) {
                return d.getContracts().get(d.getConsumers().indexOf(consumer))
                        .getRemainedContractMonths();
            }
        }
        return -1;
    }

    /**
     * Distribuitorii sunt eliminati din lista producatorilor in care apar si isi cauta alti
     * producatori cand un producator al distribuitorului isi face un update
     * @param producersDB baza de date a producatorilor
     */
    public void changeProducer(ProducersDatabase producersDB) {
        for (Distributors d : distributors) {
            if (d.isChangeProducers()) {
                for (Producers p : producersDB.getProducers()) {
                    if (p.getDistributors().contains(d)) {
                        p.getDistributors().remove(d);
                        p.deleteObserver(d);
                    }
                }
                EnergyChoiceStrategyFactory energy = EnergyChoiceStrategyFactory.getInstance();
                energy.createEnergyChoiceStrategy(d.getProducerStrategy()).energyChoice(d,
                        producersDB);
            }
        }
    }

}
