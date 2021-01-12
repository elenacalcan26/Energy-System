package simulation;

import database.ConsumersDatabase;
import database.DistributorsDatabase;
import database.MonthlyUpdatesDatabase;
import database.ProducersDatabase;
import entities.Consumers;
import entities.Contract;
import entities.Distributors;
import entities.Producers;

import java.util.HashMap;
import java.util.Map;

public final class EnergySystem {
    public EnergySystem() {

    }

    /**
     * Se realizeaza runda initiala  a simularii. Distribuitorii isi aleg producatorii.
     * Consumatorii isi aleg distribuitorii si ii platesc.
     * @param distributorsDB baza de date a distribuitorilor
     * @param consumersDB baza de date a consumatorilor
     * @param producersDB baza de date a producatorilor
     */
    private void initialRound(DistributorsDatabase distributorsDB, ConsumersDatabase consumersDB,
                              ProducersDatabase producersDB) {

        distributorsDB.chooseProducers(producersDB);
        long price = distributorsDB.cheapDistributor().contractPrice();
        consumersDB.makeContract(distributorsDB.cheapDistributor(), distributorsDB);
         // cheltuieli consumatori runda 0
        for (Consumers c : distributorsDB.cheapDistributor().getConsumers()) {
            c.budgetAfterIncome();
            // verific daca poate plati contractul
            if (c.getBudget() < price) {
                // are penalizare
                c.setPenaltyPrice(price);
                c.setPenaltyDistributor(distributorsDB.cheapDistributor());
            } else {
                c.monthlyExpenses(price);
                distributorsDB.cheapDistributor().budgetAfterPayment(price);
            }
        }
        // se calculeaza cheltuielile lunare pentru fiecare distribuitor
        for (Distributors d : distributorsDB.getDistributors()) {
            d.monthlyExpenses();
        }
        // a trecut o luna din contract
        for (Contract c : distributorsDB.cheapDistributor().getContracts()) {
            c.setRemainedContractMonths(c.getRemainedContractMonths() - 1);
        }

    }

    /**
     * Extrage din baza de date update-urile pe care la fac distribuitorii in luna curenta
     * @param distributorsDB baza de date a distribuitorilor
     * @param updates baza de date a update-urilor lunare
     * @param currentMonth runda curenta
     */
    private void distributorsUpdates(int currentMonth, DistributorsDatabase distributorsDB,
                                     MonthlyUpdatesDatabase updates) {
        HashMap<Long, Long> changes = updates.getUpdatedDistributors().get(currentMonth);
        if (changes != null) {
            for (Map.Entry<Long, Long> change : changes.entrySet()) {
                long id = change.getKey();
                long infrastructure = change.getValue();
                Distributors updatedDistributor = distributorsDB.findDistributor(id);
                assert updatedDistributor != null;
                updatedDistributor.setInfrastructureCost(infrastructure);
            }
        }
    }

    /**
     * Extrage din baza de date update-urile pe care la fac consumatorii in luna curenta
     * @param currentMonth luna curenta
     * @param consumersDB baza de date a consumatorilor
     * @param updates baza de date a update-urilor curente
     */
    private void consumersUpdates(int currentMonth, ConsumersDatabase consumersDB,
                                  MonthlyUpdatesDatabase updates) {
        if (updates.getNewConsumers().get(currentMonth) != null) {
            consumersDB.setConsumers(updates.getNewConsumers().get(currentMonth));
        }
    }

    /**
     * Extrage din baza de date update-urile pe care la fac producatorii in luna curenta
     * @param currentMonth luna curenta
     * @param producersDB baza de date a producatorilor
     * @param updates baza de date a update-urilor curente
     */
    private void producerUpdates(int currentMonth, ProducersDatabase producersDB,
                                 MonthlyUpdatesDatabase updates) {
        HashMap<Long, Long> producersChanges = updates.getUpdatedProducers().get(currentMonth);
        if (producersChanges != null) {
            for (Map.Entry<Long, Long> changes : producersChanges.entrySet()) {
                long id = changes.getKey();
                long energy = changes.getValue();
                Producers updatedProducer = producersDB.findProducer(id);
                assert updatedProducer != null;
                updatedProducer.setEnergyPerDistributor(energy);
                updatedProducer.monthUpdate();
            }
        }
    }

    /**
     * Realizeaza actiunile consumatorului din timpul simularii. Consumatorul primeste salariu si
     * trebuie sa plateasca factura distribuitorului la care se afla. Daca el nu poate plati
     * distribuitorul, atunci concumatorul falimenteaza
     * @param consumer consumatorul care efectueaza actiunea
     * @param distributor distribuitorul la care este consumatorul
     * @param contract contractul consumatorului cu distribuitorul
     */
    private void consumerAction(Consumers consumer, Distributors distributor, Contract contract) {
        // primeste salariul
        consumer.budgetAfterIncome();
        long price = contract.getPrice();
        if (consumer.getBudget() > price && consumer.getPenaltyDistributor() == null) {
            // plateste pretul contractului
            consumer.monthlyExpenses(price);
            distributor.budgetAfterPayment(price);
        } else if (consumer.getBudget() < price) {
            // are penalizare
            consumer.setPenaltyDistributor(distributor);
            consumer.setPenaltyPrice(price);
        } else if (consumer.getPenaltyDistributor().equals(distributor)
                && consumer.getBudget() > consumer.getPenaltyPrice() + price) {
            // are penalizare la acelasi distribuitor si o poate plati
            consumer.monthlyExpenses(consumer.getPenaltyPrice() + price);
            distributor.budgetAfterPayment(consumer.getPenaltyPrice() + price);
            consumer.setPenaltyDistributor(null);
            consumer.setPenaltyPrice(0);
        } else if (!consumer.getPenaltyDistributor().equals(distributor)
                && consumer.getBudget() > consumer.getPenaltyPrice()) {
            // are penalizare, iar consumatorul si-a schimbat distribuitorul
            consumer.monthlyExpenses(consumer.getPenaltyPrice());
            consumer.getPenaltyDistributor().budgetAfterPayment(consumer.getPenaltyPrice());
            // se amaneaza plata facturii cu distribuitorul curent, rezultand penalizare
            consumer.setPenaltyDistributor(distributor);
            consumer.setPenaltyPrice(price);
        } else {
            // consumatorul a falimentat
            consumer.setBankrupt(true);
        }
        // a trecut o luna din contract
        long remained = contract.getRemainedContractMonths();
        contract.setRemainedContractMonths(remained - 1);
    }

    /**
     * Se realizeaza simularea sistemului energetic.
     * @param turns numarul de runde a simularii
     * @param consumersDB baza de date a consumatorilor
     * @param distributorsDB baza de date a distribuitorilor
     * @param producersDB baza de date a producatorilor
     * @param monthlyUpdates baza de date a update-urilor lunare
     */
    public void simulation(long turns, ConsumersDatabase consumersDB,
                             DistributorsDatabase distributorsDB, ProducersDatabase producersDB,
                             MonthlyUpdatesDatabase monthlyUpdates) {

        initialRound(distributorsDB, consumersDB, producersDB);
        // sunt parcurse celelalte runde
        for (int i = 0; i < Math.toIntExact(turns); i++) {
            distributorsUpdates(i, distributorsDB, monthlyUpdates);
            consumersUpdates(i, consumersDB, monthlyUpdates);
            distributorsDB.distributorsContractPrices();
            consumersDB.makeContract(distributorsDB.cheapDistributor(), distributorsDB);
            distributorsDB.findEndedContracts();
            // sunt parcursi toti distribuitorii din baza de date
            for (Distributors distributor : distributorsDB.getDistributors()) {
                // se verifica daca distribuitorul este falimentat
                if (!distributor.isBankrupt()) {
                    // sunt parcursi toti consumatorii distribuitorului
                    for (int j = 0; j < distributor.getConsumers().size(); j++) {
                        Consumers consumer = distributor.getConsumers().get(j);
                        Contract contract = distributor.getContracts().get(j);
                        consumerAction(consumer, distributor, contract);
                    }
                }
                // cheltuieli pe care le face distribuitorul
                distributor.monthlyExpenses();
                // verific daca distribuitorul a falimentat
                if (distributor.getBudget() < 0) {
                    distributor.setBankrupt(true);
                }
            }
            distributorsDB.findBankruptConsumers();
            producerUpdates(i, producersDB, monthlyUpdates);
            distributorsDB.changeProducer(producersDB);
            producersDB.monthStat(i + 1);
        }
    }
}
