package database;

import entities.Distributors;
import entities.MonthlyStat;
import entities.Producers;

import java.util.ArrayList;

public final class ProducersDatabase {
    private final ArrayList<Producers> producers;

    public ProducersDatabase() {
        producers = new ArrayList<>();
    }

    public ArrayList<Producers> getProducers() {
        return producers;
    }

    /**
     * Adauga un producator in baza de date
     * @param producer producatorul adaugat
     */
    public void setProducers(Producers producer) {
        producers.add(producer);
    }

    /**
     * Salveaza statisticile producatorilor din luna curenta
     *
     * @param currentMonth luna curenta
     */
    public void monthStat(final long currentMonth) {
        for (Producers p : producers) {
            ArrayList<Long> distributorsIds = new ArrayList<>();
            if (!p.getDistributors().isEmpty()) {
                for (Distributors d : p.getDistributors()) {
                    distributorsIds.add(d.getId());
                }
                MonthlyStat stat = new MonthlyStat(currentMonth, distributorsIds);
                p.setMonthlyStats(stat);
            } else {
                MonthlyStat stat = new MonthlyStat(currentMonth, new ArrayList<>());
                p.setMonthlyStats(stat);
            }
        }
    }

    /**
     * Cauta un producator in baza de date dupa numarul id-ului
     * @param id id-ul producatorului
     * @return producatorul dorit
     *          null cand nu exista producatorul cu id-ul respectiv
     */
    public Producers findProducer(final long id) {
        for (Producers p : producers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
