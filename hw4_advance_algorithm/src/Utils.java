package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private final static String outJsonPath = "./resource/output.json";
    private final static String inJsonPath = "./resource/input.json";

    public static List<JsonAddressObject> readJSONData() {
        List<JsonAddressObject> dataList = new ArrayList<>();
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(inJsonPath, StandardCharsets.UTF_8)) {
            JsonReader jsonReader = new JsonReader(fileReader);
            JsonAddressObject[] listObject = gson.fromJson(jsonReader, JsonAddressObject[].class);
            dataList = Arrays.asList(listObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void writeJSONData(List<JsonAddressObject> output) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(outJsonPath, StandardCharsets.UTF_8)) {
            gson.toJson(output, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] preProcess(String jsonTerm) {
        String[] wordArray = Arrays
                // split text follow regex
                .stream(jsonTerm.split("([A-X][,.-]\\s|[,.-]\\s|[A-Z]\\s)"))
                // remove empty element
                .filter(s -> !(s.isBlank() || s.isEmpty()))
                // lower case, remove white space, remove accent
                .map(s -> Normalizer.normalize(
                                s.toLowerCase().trim().replaceAll("\\s+", ""),
                                Normalizer.Form.NFKD
                        ).replaceAll("\\p{M}", "")
                )
                .toArray(String[]::new);
        System.out.println(Arrays.toString(wordArray));
        return wordArray;
    }

    public static void main(String[] args) {
        List<JsonAddressObject> readJsonData = Utils.readJSONData();
        readJsonData.forEach(object -> object.setDistrict("Binh Thuan"));
        preProcess(readJsonData.get(0).getPred_no_corret());
        Utils.writeJSONData(readJsonData);
    }
}
