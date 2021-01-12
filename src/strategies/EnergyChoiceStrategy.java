package strategies;

import database.ProducersDatabase;
import entities.Distributors;

public interface EnergyChoiceStrategy {
    /**
     * Distribuitorul isi alege producatorul in functie de strategia pe care o are
     * @param distributor distributorul care isi alege producatorul
     * @param producersDB baza de date a producatorilor
     */
    void energyChoice(Distributors distributor, ProducersDatabase producersDB);
}
