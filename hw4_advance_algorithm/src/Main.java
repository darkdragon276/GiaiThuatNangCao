package src;

import java.util.Arrays;
import java.util.List;

class Main {
    static AddressSearchTrie provincesTrie = new AddressSearchTrie();
    static AddressSearchTrie districtsTrie = new AddressSearchTrie();
    static WardSearchTrie wardsTrie = new WardSearchTrie();
    static AddressSearchTrie exceptionTrie = new AddressSearchTrie();
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
        List<JsonAddressObject> listInputAddress = Utils.readJSONData();
        for (JsonAddressObject inputAddress : listInputAddress) {
            Result result = mainWorking(inputAddress.getText());
            inputAddress.setResult(result);
        }

        // write data
        Utils.writeJSONData(listInputAddress);
        mainWorking("x2Trung Sơn,,TỉnhNghê An");

    }

    public static Result mainWorking(String text) {
        String[] listAddress = Utils.preProcess(text);
        // remove exception
        listAddress = Arrays.stream(listAddress)
                .filter(s -> Main.exceptionTrie.normalSearch(s).isBlank()
                        || Main.exceptionTrie.normalSearch(s).isEmpty())
                .toArray(String[]::new);
        Result result = new Result(listAddress, "", "", "");
        System.out.println("main " + Arrays.toString(listAddress));

        String inputString = "";
        int i = listAddress.length - 1;
        String provinceId = "";
        String districtId = "";

        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            System.out.println("main " + inputString);
            List<String> refIds = Main.provincesTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && listAddress.length - i <= 4) {
                continue;
            } else if (listAddress.length - i > 4) {
                i = listAddress.length - 1;
                inputString = "";
                break;
            } else {
                System.out.println("main " + refIds.get(0));
                provinceId = String.format("%02d", Integer.parseInt(refIds.get(0)));
                result.setProvince(relationData.getProvincesMap().get(provinceId));
                inputString = "";
                i--;
                break;
            }
        }
        System.out.println("main " + i);
        int iStore = i;
        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            System.out.println("main " + inputString);
            List<String> refIds = Main.districtsTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && iStore - i <= 4) {
                continue;
            } else if (iStore - i > 4) {
                i = iStore;
                inputString = "";
                break;
            } else {
                System.out.println("main " + refIds.get(0));
                districtId = String.format("%03d", Integer.parseInt(refIds.get(0)));
                result.setDistrict(relationData.getDistrictsMap().get(districtId)[0]);
                inputString = "";
                i--;
                break;
            }
        }
        System.out.println("main " + i);
        iStore = i;
        outerLoop:
        for (; i >= 0; i--) {
            inputString = listAddress[i] + inputString;
            System.out.println("main " + inputString);
            List<String> refIds = Main.wardsTrie.searchAddressId(inputString);
            if (refIds.isEmpty() && iStore - i <= 4) {
                continue;
            } else if (iStore - i > 4) {
                // no need
                break;
            } else {
                for (String refId : refIds) {
                    System.out.println("main " + refId);
                    String wardId = String.format("%05d", Integer.parseInt(refId));
                    if (relationData.getWardsMap().get(wardId)[1].compareTo(districtId) == 0) {
                        result.setWard(relationData.getWardsMap().get(wardId)[0]);
                        break outerLoop;
                    }
                }
            }
        }
        return result;
    }
}
