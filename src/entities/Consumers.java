package entities;

import utils.Util;

public final class Consumers {
    private final long id;
    private long budget;
    private final long monthlyIncome;
    private boolean isBankrupt;
    private boolean hasContract;
    private Distributors penaltyDistributor;
    private long penaltyPrice;

    public Consumers(final long id, final long budget, final long monthlyIncome) {
        this.id = id;
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
        isBankrupt = false;
        hasContract = false;
        penaltyDistributor = null;
        penaltyPrice = 0;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public void setHasContract(boolean hasContract) {
        this.hasContract = hasContract;
    }

    public void setPenaltyDistributor(Distributors penaltyDistributor) {
        this.penaltyDistributor = penaltyDistributor;
    }

    public void setPenaltyPrice(long penaltyPrice) {
        this.penaltyPrice =  Math.round(Math.floor(Util.PENALTY * penaltyPrice));
    }

    public long getId() {
        return id;
    }

    public long getBudget() {
        return budget;
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public boolean isHasContract() {
        return hasContract;
    }

    public Distributors getPenaltyDistributor() {
        return penaltyDistributor;
    }

    public long getPenaltyPrice() {
        return penaltyPrice;
    }

    /**
     * Calculeaza bugetul consumatorului dupa salariu
     */
    public void budgetAfterIncome() {
        budget += monthlyIncome;
    }

    /**
     * Calculeaza bugetul consumatorului dupa ce a platit factura lunara
     * @param expense cheltuielile pe care le face intr-o luna
     */
    public void monthlyExpenses(final long expense) {
        budget -= expense;
    }

}
