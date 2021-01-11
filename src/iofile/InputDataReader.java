package iofile;

import database.ConsumersDatabase;
import database.DistributorsDatabase;
import database.MonthlyUpdatesDatabase;
import database.ProducersDatabase;
import entities.Consumers;
import entities.Distributors;
import entities.Producers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public final class InputDataReader {
    private InputDataReader() {

    }

    /**
     * Citeste din fisierul JSON numarul de runde a simularii
     * @param file fisierul de input
     * @return numarul de runde
     */
    public static long readNumberOfTurns(String file) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        assert jsonObject != null;
        return  (long) jsonObject.get("numberOfTurns");
    }

    /**
     * Citeste din fisier JSON informatiile despre consumatori si ii adauga in baza de date
     * @param jsonConsumers informatiile consumatorilor din fisierul JSON
     * @param consumersDB baza de date a consumatorilor
     */
    private static void readConsumers(final JSONArray jsonConsumers,
                                     final ConsumersDatabase consumersDB) {
        for (Object jsonConsumer : jsonConsumers) {
            long id = (long) ((JSONObject) jsonConsumer).get("id");
            long budget = (long) ((JSONObject) jsonConsumer).get("initialBudget");
            long income = (long) ((JSONObject) jsonConsumer).get("monthlyIncome");
            Consumers consumer = new Consumers(id, budget, income);
            consumersDB.setConsumers(consumer);
        }
    }

    /**
     * Citeste informatiile despre distribuitori din fisier JSON si ii adauga in baza de date
     * @param jsonDistributors informatiile distribuitorilo din fisierul JSON
     * @param distributorsDB baza de date a distribuitorilor
     */
   private static void readDistributors(final JSONArray jsonDistributors,
                                        final DistributorsDatabase distributorsDB) {
        for (Object jsonDistributor : jsonDistributors) {
            long id = (long) ((JSONObject) jsonDistributor).get("id");
            long contractLen = (long) ((JSONObject) jsonDistributor).get("contractLength");
            long budget = (long) ((JSONObject) jsonDistributor).get("initialBudget");
            long infrastructureCost = (long)
                    ((JSONObject) jsonDistributor).get("initialInfrastructureCost");
            long energyNeededKW = (long) ((JSONObject) jsonDistributor).get("energyNeededKW");
            String producerStrategy = (String)
                    ((JSONObject) jsonDistributor).get("producerStrategy");
            Distributors distributor = new Distributors(id, contractLen, budget,
                    infrastructureCost, energyNeededKW, producerStrategy);
            distributorsDB.setDistributors(distributor);
        }
    }

    /**
     * Citeste informatiile despre producatori din fisier JSON si ii adauga in baza de date
     * @param jsonProducers  informatiile despre producatori din fisierul JSON
     * @param producersDB baza de date a producatorilor
     */
   private static void readProducers(final  JSONArray jsonProducers,
                                     final ProducersDatabase producersDB) {
        for (Object jsonProducer : jsonProducers) {
            long id = (long) ((JSONObject) jsonProducer).get("id");
            String energyType = (String) ((JSONObject) jsonProducer).get("energyType");
            long maxDistributors = (long) ((JSONObject) jsonProducer).get("maxDistributors");
            double priceKW = (double) ((JSONObject) jsonProducer).get("priceKW");
            long energyPerDistributor = (long)
                    ((JSONObject) jsonProducer).get("energyPerDistributor");
            Producers producer = new
                    Producers(id, energyType, maxDistributors, priceKW, energyPerDistributor);
            producersDB.setProducers(producer);
        }
   }

    /**
     * Citeste update-urile pe care le fac consumatorii si le adauaga in baza de date
     * @param updates update-urile
     * @param file fisierul de input
     * @param monthlyUpdates baza de date a update-urilor lunare
     */
    private static void readConsumersUpdates(final JSONArray updates, String file,
                                            final MonthlyUpdatesDatabase monthlyUpdates) {


        long months = readNumberOfTurns(file);
        for (int i = 0; i < months; i++) {
            JSONArray newConsumers = ((JSONArray)
                    ((JSONObject) updates.get(i)).get("newConsumers"));
            if (!newConsumers.isEmpty()) {
                for (Object newConsumer : newConsumers) {
                    long id = (long) (((JSONObject) newConsumer).get("id"));
                    long budget = (long) (((JSONObject) newConsumer).get("initialBudget"));
                    long income = (long) (((JSONObject) newConsumer).get("monthlyIncome"));
                    monthlyUpdates.setNewConsumers(new Consumers(id, budget, income));
                }
            } else {
                monthlyUpdates.setNewConsumers(null);
            }
        }
    }

    /**
     * Citeste update-urile pe care le fac distribuitorii si le adauga in baza de date
     * @param updates update-urile
     * @param file fisierul de input
     * @param monthlyUpdates baza de dare a update-urilor lunare
     */
    private static void readDistributorsUpdates(final JSONArray updates, final String file,
                                              final MonthlyUpdatesDatabase monthlyUpdates) {

        long months = readNumberOfTurns(file);
        for (int i = 0; i < months; i++) {
            JSONArray distributorsChanges = ((JSONArray)
                    ((JSONObject) updates.get(i)).get("distributorChanges"));
            if (!distributorsChanges.isEmpty()) {
                HashMap<Long, Long> changes = new HashMap<>();
                for (Object distributorsChange : distributorsChanges) {
                    long id = (long) ((JSONObject) distributorsChange).get("id");
                    long updatedInf = (long)
                            ((JSONObject) distributorsChange).get("infrastructureCost");
                    changes.put(id, updatedInf);
                }
                monthlyUpdates.addUpdatedDistributors(changes);
            } else {
                monthlyUpdates.addUpdatedDistributors(null);
            }
        }
    }

    /**
     * Citeste update-urile pe care le fac producatorii si le adauga in baza de date
     * @param updates update-urile
     * @param file fisierul de input
     * @param monthlyUpdates baza de date a update-urlor lunare
     */
    private static void readProducersUpdates(final JSONArray updates, final String file,
                                            final MonthlyUpdatesDatabase monthlyUpdates) {
        long months = readNumberOfTurns(file);
        for (int i = 0; i < months; i++) {
            JSONArray producersChanges = ((JSONArray)
                    ((JSONObject) updates.get(i)).get("producerChanges"));
            if (!producersChanges.isEmpty()) {
                HashMap<Long, Long> changes = new HashMap<>();
                for (Object producersChange : producersChanges) {
                    long id = (long) ((JSONObject) producersChange).get("id");
                    long newEnergy = (long)
                            ((JSONObject) producersChange).get("energyPerDistributor");
                    changes.put(id, newEnergy);
                }
                monthlyUpdates.addUpdatedProducers(changes);
            } else {
                monthlyUpdates.addUpdatedProducers(null);
            }
        }
    }

    /**
     * Citeste din fisier JSON informatiile entitatilor folosite in simulare si le adauga in baa de
     * date corespunzatoare
     * @param file fisier de input
     * @param consumersDB baza de date a consumatoril
     * @param distributorsDB baza de date a distribuitorilor
     * @param producersDB baza de date a producatorilor
     * @param monthlyUpdates baza de date a update-urilor lunare
     * @throws Exception eroare cand se face citirea din fisier JSON
     */
   public static void readEntities(String file, ConsumersDatabase consumersDB,
                                   DistributorsDatabase distributorsDB,
                                   ProducersDatabase producersDB,
                                   MonthlyUpdatesDatabase monthlyUpdates) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
        JSONObject data = (JSONObject) jsonObject.get("initialData");
        JSONArray updates = (JSONArray) jsonObject.get("monthlyUpdates");

        readConsumers((JSONArray) data.get("consumers"), consumersDB);
        readDistributors((JSONArray) data.get("distributors"), distributorsDB);
        readProducers((JSONArray) data.get("producers"), producersDB);
        readConsumersUpdates(updates, file, monthlyUpdates);
        readDistributorsUpdates(updates, file, monthlyUpdates);
        readProducersUpdates(updates, file, monthlyUpdates);
    }
}
