package src;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

class Main {
    static AddressSearchTrie provincesTrie = new AddressSearchTrie();
    static AddressSearchTrie districtsTrie = new AddressSearchTrie();
    static AddressSearchTrie wardsTrie = new AddressSearchTrie();
    static String provincesPath = "./resource/Provinces.csv";
    static String districtsPath = "./resource/Districts.csv";
    static String wardsPath = "./resource/Wards.csv";

    public static void buildAddressTree(String filePath, AddressSearchTrie addressTrie) {
        try (FileReader filereader = new FileReader(filePath, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                addressTrie.insertAddress(row[0], row[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("hello world");
        // build data
        buildAddressTree(Main.provincesPath, Main.provincesTrie);
        buildAddressTree(Main.districtsPath, Main.districtsTrie);
        buildAddressTree(Main.wardsPath, Main.wardsTrie);
        RelationDataMap relationData = new RelationDataMap();
//        relationData.print();

        // process data
        String refId = Main.provincesTrie.searchAddressId("binhthuan").get(0);
        if (refId != null) System.out.println(relationData.getProvincesMap().get(refId));
    }
}
