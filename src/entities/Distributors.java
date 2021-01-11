package entities;

import strategies.EnergyChoiceStrategyType;
import utils.Util;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "deprecation"})
public final class Distributors implements Observer {
    private final long id;
    private final long contractLength;
    private long budget;
    private long infrastructureCost;
    private final long energyNeededKW;
    private final EnergyChoiceStrategyType producerStrategy;
    private long productionCost;
    private boolean isBankrupt;
    private final ArrayList<Consumers> consumers;
    private final ArrayList<Contract> contracts;

    private long contractCost;

    private boolean changeProducers;


    public Distributors(final long id, final long contractLength, final long initialBudget,
                        final long initialInfrastructureCost, final long energyNeededKW,
                        final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = initialBudget;
        this.infrastructureCost = initialInfrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = EnergyChoiceStrategyType.valueOf(producerStrategy);
        isBankrupt = false;
        consumers = new ArrayList<>();
        contracts = new ArrayList<>();
        changeProducers = true;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void setInfrastructureCost(long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setProductionCost(long productionCost) {
        this.productionCost = productionCost;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public void setChangeProducers(boolean changeProducers) {
        this.changeProducers = changeProducers;
    }

    /**
     * Adauga un consumator in lista de consumatori a distribuitorului
     * @param consumer consumatorul adaugat
     */
    public void setConsumers(Consumers consumer) {
        consumers.add(consumer);
    }

    /**
     * Adauga un contract in lista de contracte a distribuitorului
     * @param contract contractul adaugat
     */
    public void setContracts(Contract contract) {
        contracts.add(contract);
    }

    public long getId() {
        return id;
    }

    public long getContractLength() {
        return contractLength;
    }

    public long getBudget() {
        return budget;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public ArrayList<Consumers> getConsumers() {
        return consumers;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public boolean isChangeProducers() {
        return changeProducers;
    }

    public long getContractCost() {
        return contractCost;
    }

    public void setContractCost(long contractCost) {
        this.contractCost = contractCost;
    }

    /**
     * @return profitul distribuitorului
     */
    private long profit() {
        return Math.round(Math.floor(Util.PROFIT * productionCost));
    }

    /**
     * @return pretul contractului pe care il are distribuitorul
     */
    public long contractPrice() {
        if (!consumers.isEmpty()) {
            return Math.round(Math.floor(infrastructureCost / consumers.size()))
                    + productionCost + profit();
        }
        return infrastructureCost + productionCost + profit();
    }

    /**
     * Calculeaa bugetul distribuitorului dupa ce consumatorul isi plateste factura
     * @param consumerPayment pretul contractului cu un consumator
     */
    public void budgetAfterPayment(final long consumerPayment) {
        budget = budget + consumerPayment;
    }

    /**
     * Calculeaza bugetul distribuitorului dupa cheltuielile pe care le face intr-o luna
     */
    public void monthlyExpenses() {
        budget = budget - infrastructureCost - productionCost * consumers.size();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.changeProducers = true;
    }

}
