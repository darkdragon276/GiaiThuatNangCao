package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MultiIndexNode {
    private final HashMap<Character, MultiIndexNode> children;
    private final List<String> leafRefId; // leafRefId = "" mean not leaf
    private String content;
    private char index;

    public MultiIndexNode(String content, char index) {
        this.content = content;
        this.leafRefId = new ArrayList<>();
        this.children = new HashMap<Character, MultiIndexNode>();
        this.index = index;
    }

    public MultiIndexNode() {
        this.content = "";
        this.leafRefId = new ArrayList<>();
        this.children = new HashMap<Character, MultiIndexNode>();
        this.index = '.';
    }

    public char getIndex() {
        return index;
    }

    public void setIndex(char index) {
        this.index = index;
    }

    public HashMap<Character, MultiIndexNode> getChildren() {
        return children;
    }

    public MultiIndexNode getChild(char charIndex) {
        return children.get(charIndex);
    }

    public void setChild(char charIndex, MultiIndexNode trie) {
        children.putIfAbsent(charIndex, trie);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLeafRefId() {
        return this.leafRefId.stream().filter(s -> !(s.isBlank() || s.isEmpty())).collect(Collectors.toList());
    }

    public void setLeafRefId(String leafRefId) {
        this.leafRefId.add(leafRefId);
    }
}
