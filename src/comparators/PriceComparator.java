package comparators;

import entities.Producers;

import java.util.Comparator;

/**
 * Sorteaza producatorii crescator dupa pret si descrescator dupa cantitate
 */
public final class PriceComparator implements Comparator<Producers> {
    @Override
    public int compare(Producers o1, Producers o2) {
        if (Double.compare(o1.getPriceKW(), o2.getPriceKW()) != 0) {
            return Double.compare(o1.getPriceKW(), o2.getPriceKW());
        }
        if (o1.getEnergyPerDistributor() != o2.getEnergyPerDistributor()) {
            return (int) (o2.getEnergyPerDistributor() - o1.getEnergyPerDistributor());
        }
        return (int) (o1.getId() - o2.getId());
    }
}
