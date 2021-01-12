package comparators;

import entities.Producers;

import java.util.Comparator;

/**
 * Sorteaza producatorii dupa cantitatea de energie pe care le ofera distribuitorilor
 */
public final class QuantityComparator implements Comparator<Producers> {
    @Override
    public int compare(Producers o1, Producers o2) {
        if (o1.getEnergyPerDistributor() != o2.getEnergyPerDistributor()) {
            return (int) (o2.getEnergyPerDistributor() - o1.getEnergyPerDistributor());
        }
        return (int) (o1.getId() - o2.getId());
    }
}
