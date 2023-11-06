package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WardNode {
    private final HashMap<Character, WardNode> children;
    private String content;
    private List<String> leafRefId; // leafRefId = "" mean not leaf
    private char index;

    public WardNode(String content, char index) {
        this.content = content;
        this.leafRefId = new ArrayList<>();
        this.children = new HashMap<Character, WardNode>();
        this.index = index;
    }

    public WardNode() {
        this.content = "";
        this.leafRefId = new ArrayList<>();
        this.children = new HashMap<Character, WardNode>();
        this.index = '.';
    }

    public char getIndex() {
        return index;
    }

    public void setIndex(char index) {
        this.index = index;
    }

    public HashMap<Character, WardNode> getChildren() {
        return children;
    }

    public WardNode getChild(char charIndex) {
        return children.get(charIndex);
    }

    public void setChild(char charIndex, WardNode trie) {
        children.putIfAbsent(charIndex, trie);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLeafRefId() {
        return this.leafRefId.stream()
                .filter(s -> !(s.isBlank() || s.isEmpty()))
                .collect(Collectors.toList());
    }

    public void setLeafRefId(String leafRefId) {
        this.leafRefId.add(leafRefId);
    }
}
