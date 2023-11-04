package src;

import java.util.HashMap;

public class AddressNode {
    private final HashMap<Character, AddressNode> children;
    private String content;
    private String leafRefId; // leafRefId = "" mean not leaf
    private char index;

    public AddressNode(String content, char index) {
        this.content = content;
        this.leafRefId = "";
        this.children = new HashMap<Character, AddressNode>();
        this.index = index;
    }

    public AddressNode() {
        this.content = "";
        this.leafRefId = "";
        this.children = new HashMap<Character, AddressNode>();
        this.index = '.';
    }

    public char getIndex() {
        return index;
    }

    public void setIndex(char index) {
        this.index = index;
    }

    public HashMap<Character, AddressNode> getChildren() {
        return children;
    }

    public AddressNode getChild(char charIndex) {
        return children.get(charIndex);
    }

    public void setChild(char charIndex, AddressNode trie) {
        children.putIfAbsent(charIndex, trie);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLeafRefId() {
        return leafRefId;
    }

    public void setLeafRefId(String leafRefId) {
        this.leafRefId = leafRefId;
    }
}
