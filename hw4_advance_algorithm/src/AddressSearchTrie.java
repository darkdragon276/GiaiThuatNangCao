package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressSearchTrie {
    private final AddressNode root;

    public AddressSearchTrie() {
        this.root = new AddressNode("root", '.');
    }

    public void insertAddress(String word, String refId) {
        if (Utils.isNumeric(word)) return;
        AddressNode pCrawl = this.root;
        for (int i = word.length() - 1; i >= 0; i--) {
            pCrawl.setChild(word.charAt(i), new AddressNode(word.substring(i), word.charAt(i)));
            pCrawl = pCrawl.getChild(word.charAt(i));
        }
        pCrawl.setLeafRefId(refId);
    }

    public List<String> searchAddressId(String word) {
        AddressNode pCrawl = this.root;
        HashMap<Character, AddressNode> tempChildren = new HashMap<>();
        String tempWord = "";
        List<String> retListRefId = new ArrayList<>();
        for (int i = word.length() - 1; i >= 0; i--) {
            System.out.println(pCrawl.getContent());
            if (pCrawl.getChild(word.charAt(i)) == null) {
                tempChildren = pCrawl.getChildren();
                tempWord = word.substring(0, i);
                break;
            }
            pCrawl = pCrawl.getChild(word.charAt(i));
            if (!pCrawl.getLeafRefId().isBlank() && !pCrawl.getLeafRefId().isEmpty()) {
                retListRefId.add(pCrawl.getLeafRefId());
                return retListRefId;
            }
        }
        if (tempWord.isEmpty()) {
            retListRefId.add(pCrawl.getLeafRefId());
        } else {
            for (Map.Entry<Character, AddressNode> entry : tempChildren.entrySet()) {
                AddressNode pCrawlSubString = entry.getValue();
                for (int j = tempWord.length() - 1; j >= 0; j--) {
                    System.out.println(pCrawlSubString.getContent());
                    if (pCrawlSubString.getChild(tempWord.charAt(j)) == null) {
                        break;
                    }
                    pCrawlSubString = pCrawlSubString.getChild(tempWord.charAt(j));
                }
                if (!pCrawlSubString.getLeafRefId().isBlank() && !pCrawlSubString.getLeafRefId().isEmpty()) {
                    retListRefId.add(pCrawlSubString.getLeafRefId());
                }
            }
        }
        return retListRefId;
    }
}
