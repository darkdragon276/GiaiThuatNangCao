package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WardSearchTrie {
    private final WardNode root;

    public WardSearchTrie() {
        this.root = new WardNode("root", '.');
    }

    public void insertAddress(String word, String refId) {
        if (Utils.isNumeric(word)) return;
        WardNode pCrawl = this.root;
        for (int i = word.length() - 1; i >= 0; i--) {
            pCrawl.setChild(word.charAt(i), new WardNode(word.substring(i), word.charAt(i)));
            pCrawl = pCrawl.getChild(word.charAt(i));
        }
        pCrawl.setLeafRefId(refId);
    }

    public List<String> searchAddressId(String word) {
        WardNode pCrawl = this.root;
        HashMap<Character, WardNode> tempChildren = new HashMap<>();
        String tempWord = "";
        List<String> retListRefId = new ArrayList<>();
        for (int i = word.length() - 1; i >= 0; i--) {
            if (pCrawl.getChild(word.charAt(i)) == null) {
                if(i - 1 >= 0) {
                    if(pCrawl.getChild(word.charAt(i-1)) == null) {
                        tempChildren = pCrawl.getChildren();
                        tempWord = word.substring(0, i);
                        break;
                    } else {
                        pCrawl = pCrawl.getChild(word.charAt(--i));
                    }
                } else {
                    tempChildren = pCrawl.getChildren();
                    tempWord = word.substring(0, i);
                    break;
                }
            } else {
            	pCrawl = pCrawl.getChild(word.charAt(i));
            }
            if (!pCrawl.getLeafRefId().isEmpty()) {
                retListRefId.addAll(pCrawl.getLeafRefId());
            }
        }
        if (tempWord.isEmpty()) {
            if (!pCrawl.getLeafRefId().isEmpty()) {
                retListRefId.addAll(pCrawl.getLeafRefId());
            }
        } else {
            for (Map.Entry<Character, WardNode> entry : tempChildren.entrySet()) {
                WardNode pCrawlSubString = entry.getValue();
                System.out.println(tempWord);
                for (int j = tempWord.length() - 1; j >= 0; j--) {
                    if (pCrawlSubString.getChild(tempWord.charAt(j)) == null) {
                        break;
                    }
                    pCrawlSubString = pCrawlSubString.getChild(tempWord.charAt(j));
                }
                if (!pCrawlSubString.getLeafRefId().isEmpty()) {
                    retListRefId.addAll(pCrawlSubString.getLeafRefId());
                }
            }
        }
        return retListRefId;
    }
}
