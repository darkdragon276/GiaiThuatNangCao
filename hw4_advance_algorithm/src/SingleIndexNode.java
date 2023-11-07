package src;

import java.util.HashMap;

public class SingleIndexNode {
    private final HashMap<Character, SingleIndexNode> children;
    private String content;
    private String leafRefId; // leafRefId = "" mean not leaf
    private char index;

    public SingleIndexNode(String content, char index) {
        this.content = content;
        this.leafRefId = "";
        this.children = new HashMap<Character, SingleIndexNode>();
        this.index = index;
    }

    public SingleIndexNode() {
        this.content = "";
        this.leafRefId = "";
        this.children = new HashMap<Character, SingleIndexNode>();
        this.index = '.';
    }

    public char getIndex() {
        return index;
    }

    public void setIndex(char index) {
        this.index = index;
    }

    public HashMap<Character, SingleIndexNode> getChildren() {
        return children;
    }

    public SingleIndexNode getChild(char charIndex) {
        return children.get(charIndex);
    }

    public void setChild(char charIndex, SingleIndexNode trie) {
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
