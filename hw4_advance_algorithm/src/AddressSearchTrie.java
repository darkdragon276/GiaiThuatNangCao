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
            System.out.print(pCrawl.getContent() + "\n");
            if (pCrawl.getChild(word.charAt(i)) == null) {
                tempChildren = pCrawl.getChildren();
                tempWord = word.substring(0, i);
                break;
            }
            pCrawl = pCrawl.getChild(word.charAt(i));
        }

        if (tempWord.isEmpty()) {
            retListRefId.add(pCrawl.getLeafRefId());
        } else {
            for (Map.Entry<Character, AddressNode> entry : tempChildren.entrySet()) {
                for (int j = tempWord.length() - 1; j >= 0; j--) {

                }
            }
        }
        return retListRefId;
    }
}
