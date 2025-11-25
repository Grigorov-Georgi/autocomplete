package org.example;

import java.util.*;

public class Trie {
    private Node root;

    private static final int CHAR_A_ASCII_POSITION = 97;

    public Trie() {
        this.root = new Node();
    }

    public void addWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            return;
        }

        word = word.toLowerCase();

        Node current = root;
        for (char c : word.toCharArray()) {
            int position = c - CHAR_A_ASCII_POSITION;
            if (position < 0 || position >= current.children.length) {
                return;
            }
            if (current.children[position] == null) {
                current.children[position] = new Node();
            }
            current = current.children[position];
        }

        current.isWord = true;
        current.frequency++;
    }

    public void removeWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            return;
        }

        word = word.toLowerCase();

        Node current = root;
        Node[] path = new Node[word.length() + 1];
        int[] idxPath = new int[word.length()];
        path[0] = root;

        int depth = 0;
        for (char c : word.toCharArray()) {
            int pos = c - CHAR_A_ASCII_POSITION;
            if (pos < 0 || pos >= current.children.length) {
                return;
            }
            if (current.children[pos] == null) {
                return;
            }
            idxPath[depth] = pos;
            depth++;
            current = current.children[pos];
            path[depth] = current;
        }

        if (!current.isWord) {
            return;
        }

        current.isWord = false;
        current.frequency = 0;

        for (int i = depth - 1; i >= 0; i--) {
            Node child = path[i + 1];
            Node parent = path[i];
            if (child.hasChildren() || child.isWord) {
                break;
            }
            parent.children[idxPath[i]] = null;
        }
    }

    public List<String> findWithPrefix(String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }

        System.out.println("Find all words starting with [ " + prefix + " ]");
        prefix = prefix.trim().toLowerCase();
        if (prefix.isEmpty()) {
            return Collections.emptyList();
        }

        Node curr = root;
        for (char c : prefix.toCharArray()) {
            int pos = c - CHAR_A_ASCII_POSITION;
            if (pos < 0 || pos >= curr.children.length) {
                return Collections.emptyList();
            }
            if (curr.children[pos] == null) {
                return Collections.emptyList();
            }
            curr = curr.children[pos];
        }

        List<WordWithFreq> tmp = new ArrayList<>();
        collectWords(curr, prefix, tmp);

        tmp.sort(
                Comparator
                        .comparingLong(WordWithFreq::frequency)
                        .reversed()
                        .thenComparing(WordWithFreq::word));

        List<String> result = new ArrayList<>(tmp.size());
        for (WordWithFreq wf : tmp) {
            result.add(wf.word());
        }

        return result;
    }

    private void collectWords(Node node, String prefix, List<WordWithFreq> acc) {
        if (node.isWord) {
            acc.add(new WordWithFreq(prefix, node.frequency++));
        }

        for (int i = 0; i < node.children.length; i++) {
            Node child = node.children[i];
            if (child != null) {
                char ch = (char) (i + CHAR_A_ASCII_POSITION);
                collectWords(child, prefix + ch, acc);
            }
        }
    }

    public void printAllWords() {
        System.out.println("Print all words");

        record State(Node node, String prefix) {
        }

        ArrayDeque<State> stack = new ArrayDeque<>();
        stack.push(new State(root, ""));

        while (!stack.isEmpty()) {
            State state = stack.pop();
            Node node = state.node();
            String prefix = state.prefix();

            if (node.isWord && !prefix.isEmpty()) {
                System.out.println(prefix);
            }

            for (int i = node.children.length - 1; i >= 0; i--) {
                var child = node.children[i];
                if (child != null) {
                    char ch = (char) (i + CHAR_A_ASCII_POSITION);
                    stack.push(new State(child, prefix + ch));
                }
            }
        }
    }

    public void dfsPrint() {
        System.out.println("DFS print");
        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            for (int i = 0; i < curr.children.length; i++) {
                var currChild = curr.children[i];
                if (currChild != null) {
                    stack.push(curr.children[i]);
                    System.out.println((char) (i + CHAR_A_ASCII_POSITION));
                }
            }
        }
    }

    public void bfsPrint() {
        System.out.println("BFS print");
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (int i = 0; i < curr.children.length; i++) {
                var currChild = curr.children[i];
                if (currChild != null) {
                    queue.offer(currChild);
                    System.out.println((char) (i + CHAR_A_ASCII_POSITION));
                }
            }
        }
    }

    private static class WordWithFreq {
        final String word;
        final long frequency;

        WordWithFreq(String word, long frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        String word() {
            return word;
        }

        long frequency() {
            return frequency;
        }
    }
}

class Node {
    Node[] children;
    boolean isWord;
    long frequency;

    public Node() {
        this.children = new Node[26];
        this.isWord = false;
        this.frequency = 0;
    }

    boolean hasChildren() {
        for (Node child : children) {
            if (child != null) {
                return true;
            }
        }
        return false;
    }
}
