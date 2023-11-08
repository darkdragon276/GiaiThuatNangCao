package src;

import java.util.Arrays;
import java.util.List;

class Main {
    static SingleIndexTrie provincesTrie = new SingleIndexTrie();
    static MultiIndexTrie districtsTrie = new MultiIndexTrie();
    static MultiIndexTrie wardsTrie = new MultiIndexTrie();
    static SingleIndexTrie exceptionTrie = new SingleIndexTrie();
    static String provincesPath = "./resource/Provinces.csv";
    static String districtsPath = "./resource/Districts.csv";
    static String wardsPath = "./resource/Wards.csv";
    static String exceptionPath = "./resource/Exception.csv";
    static RelationDataMap relationData = new RelationDataMap();

    public static void main(String[] args) {

        System.out.println("hello world");
        // build data
        Utils.buildAddressTree(Main.provincesPath, Main.provincesTrie);
        Utils.buildAddressTree(Main.districtsPath, Main.districtsTrie);
        Utils.buildAddressTree(Main.wardsPath, Main.wardsTrie);
        Utils.buildExceptionTree(Main.exceptionPath, Main.exceptionTrie);
//        relationData.print();

        // read data
        long startTime = System.nanoTime();
        List<JsonAddressObject> listInputAddress = Utils.readJSONData();
        for (JsonAddressObject inputAddress : listInputAddress) {
            Result result = mainWorking(inputAddress.getText());
            inputAddress.setResult(result);
        }

        // write data
        Utils.writeJSONData(listInputAddress);
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("total time for all request: " + totalTime/1000000.0 + "ms");
        // total record = 450
        startTime = System.nanoTime();
        mainWorking("Van Điểm, H hường TLín, Thành phốHa Nội");
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("one request time: " + totalTime/1000000.0 + "ms");
    }

    public static Result mainWorking(String text) {
        String[] listAddress = Utils.preProcess(text);
        Result result = new Result("", "", "");
//        Result result = new Result(listAddress, "", "", "");

        String inputString = "";
        int i = listAddress.length - 1;
        String provinceId = "";
        String districtId = "";

        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            List<String> refIds = Main.provincesTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && listAddress.length - i <= 4) {
                if (i == 0) {
                    i = listAddress.length - 1;
                    inputString = "";
                    break;
                }
                continue;
            } else if (refIds.isEmpty() && listAddress.length - i > 4) {
                i = listAddress.length - 1;
                inputString = "";
                break;
            } else {
                provinceId = String.format("%02d", Integer.parseInt(refIds.get(0)));
                result.setProvince(relationData.getProvincesMap().get(provinceId));
                inputString = "";
                i--;
                break;
            }
        }
        int iStore = i;
        inputString = "";
        outerDistrictLoop:
        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            List<String> refIds = Main.districtsTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && iStore - i <= 5) {
                if (i == 0) {
                    i = iStore;
                    inputString = "";
                    break;
                }
                continue;
            } else if (refIds.isEmpty() && iStore - i > 5) {
                i = iStore;
                inputString = "";
                break;
            } else {
                for (String refId : refIds) {
                    districtId = String.format("%03d", Integer.parseInt(refId));
                    String relateProvinceId = relationData.getDistrictsMap().get(districtId)[1];
                    if (relateProvinceId.compareTo(provinceId) == 0 || provinceId.isEmpty()) {
                        result.setDistrict(relationData.getDistrictsMap().get(districtId)[0]);
                        inputString = "";
                        i--;
                        break outerDistrictLoop;
                    }
                }
            }
        }
        iStore = i;
        inputString = "";
        outerLoop:
        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            List<String> refIds = Main.wardsTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && iStore - i <= 4) {
                continue;
            } else if (refIds.isEmpty() && iStore - i > 4) {
                // no need
                break;
            } else {
                for (String refId : refIds) {
                    String wardId = String.format("%05d", Integer.parseInt(refId));
                    String relateDistrictId = relationData.getWardsMap().get(wardId)[1];
                    String relateProvinceId = relationData.getDistrictsMap().get(relateDistrictId)[1];
                    if (relateDistrictId.compareTo(districtId) == 0 || relateProvinceId.compareTo(provinceId) == 0) {
                        result.setWard(relationData.getWardsMap().get(wardId)[0]);
                        break outerLoop;
                    }
                }
            }
        }
        return result;
    }
}
