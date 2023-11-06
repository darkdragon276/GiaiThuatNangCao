package src;

import jdk.jshell.execution.Util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

class Main {
    static AddressSearchTrie provincesTrie = new AddressSearchTrie();
    static AddressSearchTrie districtsTrie = new AddressSearchTrie();
    static AddressSearchTrie wardsTrie = new AddressSearchTrie();
    static String provincesPath = "./resource/Provinces.csv";
    static String districtsPath = "./resource/Districts.csv";
    static String wardsPath = "./resource/Wards.csv";

    public static void main(String[] args) {

        System.out.println("hello world");
        // build data
        Utils.buildAddressTree(Main.provincesPath, Main.provincesTrie);
        Utils.buildAddressTree(Main.districtsPath, Main.districtsTrie);
        Utils.buildAddressTree(Main.wardsPath, Main.wardsTrie);
        RelationDataMap relationData = new RelationDataMap();
//        relationData.print();

        // read data
        List<JsonAddressObject> listInputAddress = Utils.readJSONData();
        for (JsonAddressObject inputAddress: listInputAddress) {
            String[] listAddress = Utils.preProcess(inputAddress.getText());
            System.out.println(Arrays.toString(listAddress));

            List<String> refIds = Main.provincesTrie.searchAddressId(listAddress[listAddress.length - 1]);
            System.out.println(Arrays.toString(refIds.toArray()));

            if(refIds.isEmpty()) continue;
            String provinceId = refIds.get(0);
            inputAddress.setResult(new Result(relationData.getProvincesMap().get(provinceId), "", ""));
        }

        // write data
        Utils.writeJSONData(listInputAddress);
    }
}
