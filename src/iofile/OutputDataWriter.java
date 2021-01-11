package iofile;

import database.ConsumersDatabase;
import database.DistributorsDatabase;
import database.ProducersDatabase;
import entities.Consumers;
import entities.Contract;
import entities.Distributors;
import entities.Producers;
import entities.MonthlyStat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings({"deprecation", "unchecked"})
public final class OutputDataWriter {
    private OutputDataWriter() {

    }

    /**
     * Adauga informatiile consumatorilor din umra simularii in JSON Object pentru scrierea
     * in fisier JSON
     * @param outFile instanta de tip JSONObject pentru scrierea in fisier JSON
     * @param consumerDB baza de date a consumatorilor
     */
    private static void writeConsumersData(JSONObject outFile, ConsumersDatabase consumerDB) {
        JSONArray consumerArr = new JSONArray();
        for (Consumers c : consumerDB.getConsumers()) {
            JSONObject consumerDetails = new JSONObject();
            consumerDetails.put("id", c.getId());
            consumerDetails.put("isBankrupt", c.isBankrupt());
            consumerDetails.put("budget", c.getBudget());
            consumerArr.add(consumerDetails);
        }
        outFile.put("consumers", consumerArr);
    }

    /**
     * Adauga informatiile distribuitorilor din umra simularii in JSON Object pentru scrierea
     * in fisier JSON
     * @param outFile instanta de tip JSONObject pentru scrierea in fisier JSON
     * @param distributorsDB baza de date a distribuitorilor
     */
    private static void writeDistributorsData(JSONObject outFile,
                                              DistributorsDatabase distributorsDB) {
        JSONArray distributorArr = new JSONArray();
        for (Distributors d : distributorsDB.getDistributors()) {
            JSONObject distributorDetails = new JSONObject();
            distributorDetails.put("id", d.getId());
            distributorDetails.put("energyNeededKW", d.getEnergyNeededKW());
            distributorDetails.put("contractCost", d.getContractCost());
            distributorDetails.put("budget", d.getBudget());
            distributorDetails.put("producerStrategy", d.getProducerStrategy().getLabel());
            distributorDetails.put("isBankrupt", d.isBankrupt());
            distributorDetails.put("contracts", contractData(d.getContracts()));
            distributorArr.add(distributorDetails);
        }
        outFile.put("distributors", distributorArr);
    }

    /**
     * @param contracts lista de contracte a contractelor
     * @return informatiile contractelor a unui distribuior scris in JSON array
     */
    private static JSONArray contractData(ArrayList<Contract> contracts) {
        JSONArray contractArr = new JSONArray();
        for (Contract contract : contracts) {
            JSONObject contractDetails = new JSONObject();
            contractDetails.put("consumerId", contract.getConsumerId());
            contractDetails.put("price", contract.getPrice());
            contractDetails.put("remainedContractMonths",
                    contract.getRemainedContractMonths());
            contractArr.add(contractDetails);
        }
        return contractArr;
    }

    /**
     * Adauga informatiile producatorilor din urma simularii in JSON Object pentru ascrierea in
     * fisier JSON
     * @param outFile instanta de tip JSONObject pentru scrierea in fisier JSON
     * @param producersDB baza de date a producatorilor
     */
    private static void writeProducersData(JSONObject outFile, ProducersDatabase producersDB) {
        JSONArray producersArr = new JSONArray();
        for (Producers p : producersDB.getProducers()) {
            JSONObject producerDetails = new JSONObject();
            producerDetails.put("id", p.getId());
            producerDetails.put("maxDistributors", p.getMaxDistributors());
            producerDetails.put("priceKW", p.getPriceKW());
            producerDetails.put("energyType", p.getEnergyType().getLabel());
            producerDetails.put("energyPerDistributor", p.getEnergyPerDistributor());
            producerDetails.put("monthlyStats", monthlyStatsData(p.getMonthlyStats()));
            producersArr.add(producerDetails);
        }
        outFile.put("energyProducers", producersArr);
    }

    /**
     * @param monthlyStats statisticile lunare a unui producator
     * @return statisticile lunare scrise in JSON array
     */
    private static JSONArray monthlyStatsData(ArrayList<MonthlyStat> monthlyStats) {
        JSONArray monthlyStatsArr = new JSONArray();
        for (MonthlyStat monthlyStat : monthlyStats) {
            JSONObject monthlyStatDetails = new JSONObject();
            monthlyStatDetails.put("month", monthlyStat.getMonth());
            Collections.sort(monthlyStat.getDistributorsIds());
            monthlyStatDetails.put("distributorsIds", monthlyStat.getDistributorsIds());
            monthlyStatsArr.add(monthlyStatDetails);
        }
        return monthlyStatsArr;
    }

    /**
     * Scrie in fisier JSON rezultatele simularii sistemului energetic
     * @param outputFile fisierul de output
     * @param consumerDB baza de date a consumatorilor
     * @param distributorsDB baza de date a distribuitorilor
     * @throws Exception exceptie scrire fisier
     */
    public static void writeFile(final String outputFile, final ConsumersDatabase consumerDB,
                                 final DistributorsDatabase distributorsDB,
                                 final ProducersDatabase producersDB) throws Exception {
        JSONObject outFile = new JSONObject();
        writeConsumersData(outFile, consumerDB);
        writeDistributorsData(outFile, distributorsDB);
        writeProducersData(outFile, producersDB);
        FileWriter file = new FileWriter(outputFile);
        file.write(outFile.toJSONString());
        file.flush();
        file.close();
    }
}
