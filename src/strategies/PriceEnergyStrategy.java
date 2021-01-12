package strategies;

import comparators.IDComparator;
import comparators.PriceComparator;
import database.ProducersDatabase;
import entities.Distributors;
import entities.Producers;
import utils.Util;

import java.util.ArrayList;

/**
 * Distribuitorul isi alege producatorii dupa strategia "PRICE"
 */
public final class PriceEnergyStrategy implements EnergyChoiceStrategy {
    @Override
    public void energyChoice(Distributors distributor, ProducersDatabase producersDB) {
        ArrayList<Producers> producers = producersDB.getProducers();
        producers.sort(new PriceComparator());
        double cost = 0;
        long quantityNeeded = 0;
        // sunt parcursi toti producatorii din baza de date
        for (Producers p : producers) {
            // se verifica cantitatea necesara distribuitorului
            if (quantityNeeded > distributor.getEnergyNeededKW()) {
                break;
            }
            // se verifica daca producatorul poate oferi energie distribuitorului
            if (p.getDistributors().size() < p.getMaxDistributors()) {
                quantityNeeded = quantityNeeded + p.getEnergyPerDistributor();
                cost = cost +  p.getEnergyPerDistributor() * p.getPriceKW();
                p.addDistributor(distributor);
                p.addObserver(distributor);
            }
        }
        distributor.setProductionCost(Math.round(Math.floor(cost / Util.CONSTANT)));
        distributor.setChangeProducers(false);
        producers.sort(new IDComparator());
    }
}
