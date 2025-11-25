package org.example;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {

        Trie trie = new Trie();
        trie.addWord("apple");
        trie.addWord("application");
        trie.addWord("app");
        trie.addWord("apply");
        trie.addWord("apt");
        trie.addWord("banana");
        trie.addWord("band");
        trie.addWord("bandana");
        trie.addWord("cat");
        trie.addWord("car");

        trie.bfsPrint();
        trie.dfsPrint();
        trie.printAllWords();

        System.out.println(Arrays.toString(trie.findWithPrefix("apply").toArray()));
        System.out.println(Arrays.toString(trie.findWithPrefix("app").toArray()));

        trie.removeWord("app");
        System.out.println(Arrays.toString(trie.findWithPrefix("app").toArray()));

        System.out.println(Arrays.toString(trie.findWithPrefix("bandana").toArray()));
        System.out.println(Arrays.toString(trie.findWithPrefix("ba").toArray()));
    }
}
