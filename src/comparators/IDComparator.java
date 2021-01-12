package comparators;

import entities.Producers;

import java.util.Comparator;

/**
 * Sorteaza producatorii in ordine crescatoare a ID-ului
 */
public final class IDComparator implements Comparator<Producers> {
    @Override
    public int compare(Producers o1, Producers o2) {
        return (int) (o1.getId() - o2.getId());
    }
}
