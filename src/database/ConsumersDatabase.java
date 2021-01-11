package database;

import entities.Consumers;
import entities.Contract;
import entities.Distributors;

import java.util.ArrayList;

public final class ConsumersDatabase {
    private final ArrayList<Consumers> consumers;

    public ConsumersDatabase() {
        consumers = new ArrayList<>();
    }

    public ArrayList<Consumers> getConsumers() {
        return consumers;
    }

    /**
     * Adauga un consumator in baza de date
     * @param consumer consumator adaugat
     */
    public void setConsumers(Consumers consumer) {
        consumers.add(consumer);
    }

    /**
     * Face un contract intre consumatori si distribuitor. Se parcurg toti consumatorii si se
     * verifica daca acesta poate face contractul cu distribuitorul.
     * Contractul se face atunci cand consumatorul nu este falimentat si nu are un alt contract
     * @param distributor distributor distribuitorul cu care se face contractul
     * @param distributorsDB distributorsDB baza de date a dsitribuitorilor
     */
    public void makeContract(final Distributors distributor,
                             final DistributorsDatabase distributorsDB) {
        long initialContractPrice = distributor.contractPrice();
        for (Consumers c : consumers) {
            if (!c.isBankrupt() && (distributorsDB.findConsumerContractLength(c) <= 0)) {
                distributor.setConsumers(c);
                Contract contract = new Contract(c.getId(), initialContractPrice,
                        distributor.getContractLength());
                distributor.setContracts(contract);
                c.setHasContract(true);
            }
        }
    }
}
