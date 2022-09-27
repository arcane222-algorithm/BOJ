package string.trie.ahocorasick;

import java.io.*;
import java.util.*;


/**
 * 돌연변이 - BOJ10256
 * -----------------
 * category: string (문자열)
 *           trie (트라이)
 *           aho-corasick (아호-코라식)
 * -----------------
 * -----------------
 * Input 1
 * 2
 * 6 4
 * ATGGAT
 * AGGT
 * 6 4
 * ATGGAT
 * AGCT
 *
 * Output 1
 * 3
 * 0
 * -----------------
 * Input 2
 * 2
 * 6 4
 * ATGGAT
 * AGCT
 * 8 2
 * GTACCTAC
 * CT
 *
 * Output 2
 * 0
 * 1
 * -----------------
 */
public class BOJ10256 {

    private static class AhoCorasick {
        private static Trie root = new Trie(true);
        private static boolean isInit;

        public static void buildFailureLink() {
            Queue<Trie> queue = new LinkedList<>();
            root.failureLink = root;
            queue.add(root);

            while (!queue.isEmpty()) {
                Trie curr = queue.poll();

                for (Map.Entry<Character, Trie> child : curr.nodes.entrySet()) {
                    Trie next = child.getValue();

                    if (curr.isRoot) {
                        next.failureLink = root;
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

        public static long search(String word) {
            if (root == null || !isInit) return 0;

            Trie curr = root;
            long count = 0;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                while (!curr.isRoot && !curr.nodes.containsKey(c)) {
                    curr = curr.failureLink;
                }

                if (curr.nodes.containsKey(c)) {
                    curr = curr.nodes.get(c);
                }

                if (curr.isEnd) {
                    count++;
                }
            }

            return count;
        }

        public static void add(String word) {
            if (root == null) return;
            isInit = false;
            root.add(word);
        }

        public static void addAll(String[] words) {
            if (root == null) return;
            isInit = false;
            for (String word : words) {
                root.add(word);
            }
        }

        public static void build() {
            isInit = true;
            buildFailureLink();
        }

        public static void clear() {
            isInit = false;
            root.nodes = new HashMap<>();
        }
    }

    private static class Trie {
        boolean isRoot, isEnd;
        Trie failureLink;
        HashMap<Character, Trie> nodes;

        public Trie(boolean isRoot) {
            this.isRoot = isRoot;
            this.nodes = new HashMap<>();
        }

        public void add(String word) {
            Trie curr = this;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                Trie nxt = curr.nodes.get(c);
                if (nxt == null) {
                    nxt = new Trie(false);
                    curr.nodes.put(c, nxt);
                }
                curr = nxt;
            }
            curr.isEnd = true;
        }

        public void clear() {
            for (Trie child : nodes.values()) {
                child.clear();
            }
            this.isRoot = false;
            this.nodes.clear();
            this.nodes = new HashMap<>();
        }
    }

    static int T;
    static int n, m;
    static StringBuilder result = new StringBuilder();

    public static String reverse(String str, int begin, int len) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < begin; i++)
            builder.append(str.charAt(i));

        for (int i = begin + len - 1; i > begin - 1; i--)
            builder.append(str.charAt(i));

        for (int i = begin + len; i < str.length(); i++)
            builder.append(str.charAt(i));

        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            String text = br.readLine();
            String marker = br.readLine();

            AhoCorasick.clear();
            AhoCorasick.add(marker);
            for (int j = 2; j < marker.length() + 1; j++) {
                for (int k = 0; k < marker.length() - j + 1; k++) {
                    String reverse = reverse(marker, k, j);
                    AhoCorasick.add(reverse);
                }
            }

            AhoCorasick.build();
            long count = AhoCorasick.search(text);
            result.append(count);
            if (i < T - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}