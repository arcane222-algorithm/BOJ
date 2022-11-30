package string.trie;

import java.io.*;
import java.util.*;


/**
 * SAVEZ - BOJ11590
 * -----------------
 * category: data structure (자료구조)
 *           tree (트리)
 *           trie (트라이)
 * -----------------
 * Input 1
 * 5
 * A
 * B
 * AA
 * BBB
 * AAA
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 5
 * A
 * ABA
 * BBB
 * ABABA
 * AAAAAB
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * 6
 * A
 * B
 * A
 * B
 * A
 * B
 *
 * Output 3
 * 3
 * -----------------
 */
public class BOJ11590 {

    private static class Trie {
        private List<Trie> nodes;
        private int dp;
        private char prefixChar, suffixChar;
        private boolean isEnd;
        private String word;

        public int add(String word) {
            Trie curr = this;
            int max = 0;
            for (int i = 0; i < word.length(); i++) {
                char pChar = word.charAt(i);
                char sChar = word.charAt(word.length() - 1 - i);

                Trie nxt = curr.contains(pChar, sChar);
                if (nxt == null) {
                    nxt = new Trie();
                    nxt.prefixChar = pChar;
                    nxt.suffixChar = sChar;

                    if (curr.nodes == null) {
                        curr.nodes = new LinkedList<>();
                    }
                    curr.nodes.add(nxt);
                }
                curr = nxt;

                if (curr.isEnd) {
                    max = Math.max(max, curr.dp);
                }
            }

            curr.isEnd = true;
            curr.dp = max + 1;
            curr.word = word;

            return curr.dp;
        }

        public Trie contains(char pChar, char sChar) {
            if (nodes == null) return null;
            for (Trie child : nodes) {
                if (child.prefixChar == pChar && child.suffixChar == sChar) {
                    return child;
                }
            }
            return null;
        }

        public void printAll() {
            Queue<Trie> queue = new LinkedList<>();
            queue.add(this);

            while (!queue.isEmpty()) {
                Trie curr = queue.poll();
                if (curr.isEnd)
                    System.out.println(curr.word + " / " + curr.dp);

                if (curr.nodes != null)
                    queue.addAll(curr.nodes);

            }
        }
    }

    static int N;
    static Trie root;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        List<String> words = new ArrayList<>();
        root = new Trie();
        for (int i = 0; i < N; i++) {
            words.add(br.readLine());
        }

        int max = 0;
        for (String word : words) {
            max = Math.max(max, root.add(word));
        }
        bw.write(max + "");

        // close the buffer
        br.close();
        bw.close();
    }
}