package io.github.vcvitaly.algo.strings._01_suffix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TrieMatchingExtended implements Runnable {

    private static final char TERMINAL_SIGN = '$';

    List<Integer> solve(String text, String[] patterns) {
        Set<Integer> result = new LinkedHashSet<>();

        TrieNode root = buildTrie(patterns);

        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            TrieNode currentNode = root;
            for (int j = i; j < chars.length && currentNode.hasEdge(chars[j]); j++) {
                currentNode = currentNode.followEdge(chars[j]);
            }
            if (currentNode.isALeaf()) {
                result.add(i);
            }
        }

        return new ArrayList<>(result);
    }

    TrieNode buildTrie(String[] patterns) {
        AtomicInteger counter = new AtomicInteger(1);
        TrieNode root = new TrieNode();

        for (String pattern : patterns) {
            TrieNode currentNode = root;

            for (char currentSymbol : pattern.toCharArray()) {
                if (currentNode.edges.containsKey(currentSymbol)) {
                    currentNode = currentNode.edges.get(currentSymbol);
                } else {
                    TrieNode newNode = new TrieNode(counter.getAndIncrement());
                    currentNode.edges.put(currentSymbol, newNode);
                    currentNode = newNode;
                }
            }
            currentNode.edges.put(TERMINAL_SIGN, new TrieNode(counter.getAndIncrement()));
        }

        return root;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine();
            int n = Integer.parseInt(in.readLine());
            String[] patterns = new String[n];
            for (int i = 0; i < n; i++) {
                patterns[i]= in.readLine();
            }

            List<Integer> ans = solve(text, patterns);

            for (int j = 0; j < ans.size(); j++) {
                System.out.print("" + ans.get(j));
                System.out.print(j + 1 < ans.size() ? " " : "\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Thread(new TrieMatchingExtended()).start();
    }
}
