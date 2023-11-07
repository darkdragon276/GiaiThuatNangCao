package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private final static String outJsonPath = "./export/output.json";
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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String[] preProcess(String jsonTerm) {
        String[] wordArray = Arrays
                // split text follow regex
                .stream(jsonTerm.split("[-,\\s]"))
                // remove empty element
                .filter(s -> !(s.isBlank() || s.isEmpty()))
                .map(s -> s.toLowerCase().replaceAll("[.-]", ""))
                .toArray(String[]::new);

        return Arrays.stream(filterException(wordArray))
                .filter(s -> !Main.exceptionTrie.searchNormalException(s))
                // lower case, remove white space, remove accent
                .map(s -> Normalizer.normalize(
                                s.trim().replaceAll("\\s+", ""),
                                Normalizer.Form.NFKD
                        ).replaceAll("\\p{M}", "").replaceAll("đ", "d")
                )
                .toArray(String[]::new);
    }

//    public static void main(String[] args) {
//        Utils.buildExceptionTree(Main.exceptionPath, Main.exceptionTrie);
//        System.out.println(Main.exceptionTrie.searchNormalException("h"));
//    }

    public static String[] filterException(String[] listWord) {
        List<String> removedPrefixWords = new ArrayList<>();
        for (int i = 0; i < listWord.length; i++) {
            String twinWord = (i < listWord.length - 1) ? listWord[i] + listWord[i + 1] : listWord[i];
            int resultSearchValue = Main.exceptionTrie.searchException(twinWord);
            if (resultSearchValue <= 3) {
                removedPrefixWords.add(listWord[i]);
            } else if (resultSearchValue < twinWord.length() - 1) {
                removedPrefixWords.add(twinWord.substring(resultSearchValue));
                i++;
            } else {
                i++;
            }
        }
        return removedPrefixWords.toArray(String[]::new);
    }

    public static void buildAddressTree(String filePath, SingleIndexTrie addressTrie) {
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

    public static void buildAddressTree(String filePath, MultiIndexTrie wardTrie) {
        try (FileReader filereader = new FileReader(filePath, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                wardTrie.insertAddress(row[0], row[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buildExceptionTree(String filePath, SingleIndexTrie addressTrie) {
        try (FileReader filereader = new FileReader(filePath, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                addressTrie.insertException(row[0], "common_nouns");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
