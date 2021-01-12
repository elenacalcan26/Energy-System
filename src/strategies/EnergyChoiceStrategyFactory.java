package strategies;

public final class EnergyChoiceStrategyFactory {
    private static EnergyChoiceStrategyFactory instance = null;

    /**
     * @return instanta clasei
     */
    public static EnergyChoiceStrategyFactory getInstance() {
        if (instance == null) {
            instance = new EnergyChoiceStrategyFactory();
        }
        return instance;
    }

    /**
     * Creeaza strategia data ca parametru
     * @param strategyType tipul strategiei
     * @return strategia creata
     */
    public EnergyChoiceStrategy createEnergyChoiceStrategy(EnergyChoiceStrategyType strategyType) {
        return switch (strategyType) {
            case GREEN -> new GreenEnergyStrategy();
            case PRICE -> new PriceEnergyStrategy();
            case QUANTITY -> new QuantityEnergyStrategy();
        };
    }

}
