package string.trie.ahocorasick;

import java.io.*;
import java.util.*;


/**
 * 문자열 집합 판별 - BOJ9250
 * -----------------
 * category: string (문자열)
 *           trie (트라이)
 *           aho-corasick (아호-코라식)
 * -----------------
 * -----------------
 * Input 1
 * 3
 * www
 * woo
 * jun
 * 3
 * myungwoo
 * hongjun
 * dooho
 *
 * Output 1
 * YES
 * YES
 * NO
 * -----------------
 */
public class BOJ9250 {

    private static class AhoCorasick {
        private static Trie root;
        private static boolean isInit;

        public static void add(String word) {
            if (root == null) return;
            root.add(word);
            isInit = false;
        }

        public static void build() {
            root.buildFailureLink();
            isInit = true;
        }

        public static void build(String[] patterns) {
            root = new Trie(true);
            for (String pattern : patterns) {
                root.add(pattern);
            }
            build();
        }

        public static boolean search(String word) {
            if (root == null || !isInit) return false;

            Trie curr = root;
            boolean find = false;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                while (!curr.isRoot && !curr.nodes.containsKey(c)) {
                    curr = curr.failureLink;
                }

                if (curr.nodes.containsKey(c)) {
                    curr = curr.nodes.get(c);
                }

                if (curr.isEnd) {
                    find = true;
                }
            }

            return find;
        }
    }

    private static class Trie {
        char c;
        boolean isRoot, isEnd;
        Trie failureLink;
        HashMap<Character, Trie> nodes;

        public Trie(boolean isEnd) {
            this.isRoot = isEnd;
            this.nodes = new HashMap<>();
        }

        public void add(String word) {
            Trie curr = this;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                Trie next = curr.nodes.get(c);
                if (next == null) {
                    next = new Trie(false);
                    next.c = c;
                    curr.nodes.put(c, next);
                }
                curr = next;
            }
            curr.isEnd = true;
        }

        public void buildFailureLink() {
            Queue<Trie> queue = new LinkedList<>();
            this.failureLink = this;
            queue.add(this);

            while (!queue.isEmpty()) {
                Trie curr = queue.poll();

                for (Map.Entry<Character, Trie> child : curr.nodes.entrySet()) {
                    Trie next = child.getValue();

                    if (curr.isRoot) {
                        next.failureLink = this;
                    } else {
                        Trie prevLink = curr.failureLink;
                        while (!prevLink.isRoot && !prevLink.nodes.containsKey(child.getKey())) {
                            prevLink = prevLink.failureLink;
                        }

                        if (prevLink.nodes.containsKey(child.getKey())) {
                            prevLink = prevLink.nodes.get(child.getKey());
                        }

                        next.failureLink = prevLink;
                    }

                    if (next.failureLink.isEnd) {
                        next.isEnd = true;
                    }

                    queue.add(next);
                }
            }
        }
    }

    static int N, Q;
    static String[] patterns;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        patterns = new String[N];
        for (int i = 0; i < N; i++) {
            patterns[i] = br.readLine();
        }
        AhoCorasick.build(patterns);

        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; i++) {
            String text = br.readLine();
            result.append(AhoCorasick.search(text) ? "YES\n" : "NO\n");
        }

        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}