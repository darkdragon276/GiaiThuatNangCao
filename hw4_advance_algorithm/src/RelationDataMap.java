package src;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RelationDataMap {
    private final HashMap<String, String> ProvincesMap; // Tinh, Thanh Pho
    private final HashMap<String, String[]> DistrictsMap; // Quan, Huyen
    private final HashMap<String, String[]> WardsMap; // Xa, Phuong, Thi tran
    private final String filePath = "./resource/AccentData.csv";

    public RelationDataMap() {
        this.ProvincesMap = new HashMap<String, String>();
        this.DistrictsMap = new HashMap<String, String[]>();
        this.WardsMap = new HashMap<String, String[]>();
        try {
            FileReader filereader = new FileReader(this.filePath, StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                setProvincesMap(row[1], row[0]);
                setDistrictsMap(row[3], row[2], row[1]);
                setWardsMap(row[5], row[4], row[3]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("Level1");
        getProvincesMap().forEach((s, s2) -> System.out.println(s + "-" + s2 + ", "));
        System.out.println("Level2");
        getDistrictsMap().forEach((s, s2) -> System.out.println(s + "-" + Arrays.toString(s2) + ", "));
        System.out.println("Level3");
        getWardsMap().forEach((s, s2) -> System.out.println(s + "-" + Arrays.toString(s2) + ", "));
    }

    public HashMap<String, String> getProvincesMap() {
        return ProvincesMap;
    }

    public void setProvincesMap(String index, String tinhThanhPho) {
        ProvincesMap.putIfAbsent(index, tinhThanhPho);
    }

    public HashMap<String, String[]> getDistrictsMap() {
        return DistrictsMap;
    }

    public void setDistrictsMap(String index, String quanHuyen, String parentIndex) {
        if (index.isBlank() || quanHuyen.isBlank())
            return;
        DistrictsMap.putIfAbsent(index, new String[]{quanHuyen, parentIndex});
    }

    public HashMap<String, String[]> getWardsMap() {
        return WardsMap;
    }

    public void setWardsMap(String index, String xaPhuong, String parentIndex) {
        if (index.isBlank() || xaPhuong.isBlank())
            return;
        WardsMap.putIfAbsent(index, new String[]{xaPhuong, parentIndex});
    }
}
