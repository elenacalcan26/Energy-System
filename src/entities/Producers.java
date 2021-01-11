package entities;

import java.util.ArrayList;
import java.util.Observable;


@SuppressWarnings("deprecation")
public final class Producers extends Observable {
    private final long id;
    private final EnergyType energyType;
    private final long maxDistributors;
    private final double priceKW;
    private long energyPerDistributor;
    private final ArrayList<MonthlyStat> monthlyStats;
    private final ArrayList<Distributors> distributors;

    public Producers(final long id, final String energyType, final long maxDistributors,
                     final double priceKW, final long energyPerDistributor) {
        this.id = id;
        this.energyType = EnergyType.valueOf(energyType);
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        monthlyStats = new ArrayList<>();
        distributors = new ArrayList<>();
    }

    public void setEnergyPerDistributor(long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    /**
     * Adauga statisticile producatorului dintr-o luna
     * @param monthlyStat statisticile producatorului
     */
    public void setMonthlyStats(MonthlyStat monthlyStat) {
        monthlyStats.add(monthlyStat);
    }

    public long getId() {
        return id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public ArrayList<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public ArrayList<Distributors> getDistributors() {
        return distributors;
    }

    /**
     * Adauga un distribuitor in lista de distribuitori a producatorului
     * @param distributor distribuitorul adaugat
     */
    public void addDistributor(Distributors distributor) {
        distributors.add(distributor);
    }

    /**
     * Anunta distribuitorii de schimbarile producatorului
     */
    public void monthUpdate() {
        setChanged();
        notifyObservers();
    }
}
