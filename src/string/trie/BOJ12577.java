package string.trie;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * File Fix-it (Small) - BOJ12577
 * -----------------
 * category: data structures (자료구조)
 *           string (문자열)
 *           tree (트리)
 *           trie (트라이)
 * -----------------
 * Input 1
 * 3
 * 0 2
 * /home/gcj/finals
 * /home/gcj/quals
 * 2 1
 * /chicken
 * /chicken/egg
 * /chicken
 * 1 3
 * /a
 * /a/b
 * /a/c
 * /b/b
 *
 * Output 1
 * Case #1: 4
 * Case #2: 0
 * Case #3: 4
 * -----------------
 */
public class BOJ12577 {

    private static class Trie {
        private final HashMap<String, Trie> nodes;
        private boolean isEnd;

        public Trie() {
            nodes = new HashMap<>();
        }

        public void add(String[] words) {
            Trie curr = this;
            for (String word : words) {
                if (word.isEmpty())
                    continue;

                curr = curr.nodes.computeIfAbsent(word, v -> new Trie());
            }

            curr.isEnd = true;
        }

        public int search(String[] words) {
            Trie curr = this;

            AtomicInteger res = new AtomicInteger(words.length - 1);
            for (String word : words) {
                if (word.isEmpty())
                    continue;

                if (curr.nodes.containsKey(word)) {
                    curr = curr.nodes.get(word);
                    res.decrementAndGet();
                } else {
                    Trie t = new Trie();
                    curr.nodes.put(word, t);
                    curr = t;
                }
            }

            return res.get();
        }
    }

    static int N, M, T;
    static Trie root;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 1; i <= T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            root = new Trie();

            for (int j = 0; j < N; j++) {
                String[] words = br.readLine().split("/");
                root.add(words);
            }

            int sum = 0;
            for (int j = 0; j < M; j++) {
                String[] words = br.readLine().split("/");
                sum += root.search(words);
            }

            bw.write("Case #" + i + ": " + sum + "\n");
        }
        // close the buffer
        br.close();
        bw.close();
    }
}