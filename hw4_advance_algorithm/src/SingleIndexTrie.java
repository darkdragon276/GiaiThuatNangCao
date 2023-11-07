package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleIndexTrie {
    private final SingleIndexNode root;

    public SingleIndexTrie() {
        this.root = new SingleIndexNode("root", '.');
    }

    public void insertAddress(String word, String refId) {
        if (Utils.isNumeric(word))
            return;
        SingleIndexNode pCrawl = this.root;
        for (int i = word.length() - 1; i >= 0; i--) {
            pCrawl.setChild(word.charAt(i), new SingleIndexNode(word.substring(i), word.charAt(i)));
            pCrawl = pCrawl.getChild(word.charAt(i));
        }
        pCrawl.setLeafRefId(refId);
    }

    public List<String> searchAddressId(String word) {
        SingleIndexNode pCrawl = this.root;
        HashMap<Character, SingleIndexNode> tempChildren = new HashMap<>();
        String tempWord = "";
        List<String> retListRefId = new ArrayList<>();
        for (int i = word.length() - 1; i >= 0; i--) {
            if (pCrawl.getChild(word.charAt(i)) == null) {
                // check to can access word.charAt(i - 1)
                if (i - 1 >= 0) {
                    // for case not extend but wrong current character
                    if (pCrawl.getChild(word.charAt(i - 1)) == null) {
                        tempChildren = pCrawl.getChildren();
                        tempWord = word.substring(0, i);
                        break;
                        // for case extend a character
                    } else {
                        pCrawl = pCrawl.getChild(word.charAt(--i));
                    }
                    // for case not extend but wrong current character
                } else {
                    tempChildren = pCrawl.getChildren();
                    tempWord = word.substring(0, i);
                    break;
                }
                // true character, continue searching
            } else {
                pCrawl = pCrawl.getChild(word.charAt(i));
            }
            // return all refId when reach the leaf node. for case redundant trailing character
            if (!pCrawl.getLeafRefId().isBlank() && !pCrawl.getLeafRefId().isEmpty()) {
                retListRefId.add(pCrawl.getLeafRefId());
            }
        }

        // normal case
        if (tempWord.isEmpty()) {
            // return all refId when reach the leaf node. for normal case
            if (!pCrawl.getLeafRefId().isBlank() && !pCrawl.getLeafRefId().isEmpty()) {
                retListRefId.add(pCrawl.getLeafRefId());
            }
            // for case not extend but wrong current character
        } else {
            for (Map.Entry<Character, SingleIndexNode> entry : tempChildren.entrySet()) {
                SingleIndexNode pCrawlSubString = entry.getValue();
                for (int j = tempWord.length() - 1; j >= 0; j--) {
                    if (pCrawlSubString.getChild(tempWord.charAt(j)) == null) {
                        break;
                    }
                    pCrawlSubString = pCrawlSubString.getChild(tempWord.charAt(j));
                }
                // return all refIds when reach the leaf node.
                if (!pCrawlSubString.getLeafRefId().isBlank() && !pCrawlSubString.getLeafRefId().isEmpty()) {
                    retListRefId.add(pCrawlSubString.getLeafRefId());
                }
            }
        }
        return retListRefId;
    }

    public void insertException(String word, String refId) {
        SingleIndexNode pCrawl = this.root;
        for (int i = 0; i < word.length(); i++) {
            pCrawl.setChild(word.charAt(i), new SingleIndexNode(word.substring(i), word.charAt(i)));
            pCrawl = pCrawl.getChild(word.charAt(i));
        }
        pCrawl.setLeafRefId(refId);
    }

    public int searchException(String word) {
        SingleIndexNode pCrawl = this.root;
        int index = 0;
        for (int i = 0; i < word.length(); i++) {
            if (pCrawl.getChild(word.charAt(i)) == null) {
                if (pCrawl.getLeafRefId() == "common_nouns") {
                    index = i;
                }
                return index;
            }
            pCrawl = pCrawl.getChild(word.charAt(i));

        }
        if (pCrawl.getLeafRefId() == "common_nouns") {
            index = word.length() - 1;
        }
        return index;
    }

    public boolean searchNormalException(String word) {
        SingleIndexNode pCrawl = this.root;
        if (word.length() > 1) {
            for (int i = 0; i < word.length(); i++) {
                if (pCrawl.getChild(word.charAt(i)) == null) {
                    return false;
                }
                pCrawl = pCrawl.getChild(word.charAt(i));
            }
        } else {
            if (pCrawl.getChild(word.charAt(0)) == null) {
                return false;
            } else {
                pCrawl = pCrawl.getChild(word.charAt(0));
            }
        }
        return (pCrawl.getLeafRefId().compareTo("common_nouns") == 0);
    }
}
