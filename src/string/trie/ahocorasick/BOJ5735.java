package string.trie.ahocorasick;

import java.io.*;
import java.util.*;


/**
 * Emoticons :-) - BOJ5735
 * -----------------
 * category: string (문자열)
 *           trie (트라이)
 *           aho-corasick (아호-코라식)
 * -----------------
 * -----------------
 * Input 1
 * 4 6
 * :-)
 * :-(
 * (-:
 * )-:
 * Hello uncle John! :-) :-D
 * I am sad or happy? (-:-(?
 * I feel so happy, my head spins
 * (-:-)(-:-)(-:-)(-:-) :-) (-: :-)
 * but then sadness comes :-(
 * Loves you, Joanna :-)))))
 * 3 1
 * :)
 * ):
 * ))
 * :):)):)):)):(:((:(((:):)
 * 0 0
 *
 * Output 1
 * 11
 * 8
 * -----------------
 * Input 2
 * 4 1
 * abcd
 * abcde
 * cdef
 * ef
 * abcdef
 * 0 0
 *
 * Output 2
 * 2
 * -----------------
 */
public class BOJ5735 {

    private static class AhoCorasick {
        private static Trie root;
        private static boolean isInit;

        public static void add(String pattern) {
            if (root == null) return;
            isInit = false;
            root.add(pattern);
        }

        public static void allAll(String[] patterns) {
            if (root == null) return;
            isInit = false;
            for (String pattern : patterns) {
                root.add(pattern);
            }
        }

        public static void clear() {
            if (root != null) root.clear();
            isInit = false;
            root = new Trie(true);
        }

        public static void build() {
            isInit = true;
            buildFailureLink();
        }

        public static int search(String word) {
            Trie curr = root;
            int count = 0;
            if (root == null || !isInit) {
                return 0;
            }

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
                    curr = root;
                }
            }

            return count;
        }

        private static void buildFailureLink() {
            Queue<Trie> queue = new LinkedList<>();
            root.failureLink = root;
            queue.add(root);

            while (!queue.isEmpty()) {
                Trie curr = queue.poll();

                for (Map.Entry<Character, Trie> child : curr.nodes.entrySet()) {
                    Trie nxt = child.getValue();

                    if (curr.isRoot) {
                        nxt.failureLink = root;
                    } else {
                        Trie parentLink = curr.failureLink;

                        while (!parentLink.isRoot && !parentLink.nodes.containsKey(child.getKey())) {
                            parentLink = parentLink.failureLink;
                        }

                        if (parentLink.nodes.containsKey(child.getKey())) {
                            parentLink = parentLink.nodes.get(child.getKey());
                        }

                        nxt.failureLink = parentLink;
                    }

                    if (nxt.failureLink.isEnd) {
                        nxt.isEnd = true;
                    }

                    queue.add(nxt);
                }
            }
        }
    }

    private static class Trie {

        boolean isEnd, isRoot;
        int depth;
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
                    nxt.depth = curr.depth + 1;
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
            nodes.clear();
            nodes = null;
        }
    }

    static int N, M;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            if (N == 0 && M == 0) break;

            AhoCorasick.clear();
            for (int i = 0; i < N; i++) {
                String pattern = br.readLine();
                AhoCorasick.add(pattern);
            }
            AhoCorasick.build();

            int count = 0;
            for (int i = 0; i < M; i++) {
                String text = br.readLine();
                count += AhoCorasick.search(text);
            }
            result.append(count).append('\n');
        }
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}