package database;

import entities.Consumers;

import java.util.ArrayList;
import java.util.HashMap;

public final class MonthlyUpdatesDatabase {
    private final ArrayList<Consumers> newConsumers;
    private final ArrayList<HashMap<Long, Long>> updatedDistributors;
    private final ArrayList<HashMap<Long, Long>> updatedProducers;

    public MonthlyUpdatesDatabase() {
        newConsumers = new ArrayList<>();
        updatedDistributors = new ArrayList<>();
        updatedProducers = new ArrayList<>();
    }

    /**
     * Adauga update-urile pe care le fac consumatorii intr-o luna
     * @param newConsumer noul consumator
     */
    public void setNewConsumers(Consumers newConsumer) {
        this.newConsumers.add(newConsumer);
    }

    /**
     * Adauga update-urile pe care le fac distribuitorii intr-o luna
     * @param distributorUpdate distribuitorii care si-au facut update
     */
    public void addUpdatedDistributors(HashMap<Long, Long> distributorUpdate) {
        this.updatedDistributors.add(distributorUpdate);
    }

    /**
     * Adauga update-urile pe care le fac producatorii intr-o luna
     * @param updatedProducer producatorii care si-au facut update
     */
    public void addUpdatedProducers(HashMap<Long, Long> updatedProducer) {
        this.updatedProducers.add(updatedProducer);
    }

    public ArrayList<HashMap<Long, Long>> getUpdatedProducers() {
        return updatedProducers;
    }

    public ArrayList<HashMap<Long, Long>> getUpdatedDistributors() {
        return updatedDistributors;
    }

    public ArrayList<Consumers> getNewConsumers() {
        return newConsumers;
    }
}
