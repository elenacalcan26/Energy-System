import database.ConsumersDatabase;
import database.DistributorsDatabase;
import database.MonthlyUpdatesDatabase;
import database.ProducersDatabase;
import iofile.InputDataReader;
import iofile.OutputDataWriter;
import simulation.EnergySystem;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        ConsumersDatabase consumersDB = new ConsumersDatabase();
        DistributorsDatabase distributorsDB = new DistributorsDatabase();
        ProducersDatabase producersDB = new ProducersDatabase();
        MonthlyUpdatesDatabase monthlyUpdates = new MonthlyUpdatesDatabase();
        InputDataReader.readEntities(args[0], consumersDB, distributorsDB, producersDB,
                monthlyUpdates);
        long turns = InputDataReader.readNumberOfTurns(args[0]);
        EnergySystem energySystem = new EnergySystem();
        energySystem.simulation(turns, consumersDB, distributorsDB, producersDB, monthlyUpdates);
        OutputDataWriter.writeFile(args[1], consumersDB, distributorsDB, producersDB);
    }
}
