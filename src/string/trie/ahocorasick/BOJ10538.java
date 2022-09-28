package string.trie.ahocorasick;

import java.io.*;
import java.util.*;


/**
 * 빅 픽쳐 - BOJ10538
 * -----------------
 * category: string (문자열)
 *           trie (트라이)
 *           kmp (Knuth–morris–pratt)
 *           aho-corasick (아호-코라식)
 * -----------------
 * -----------------
 * Input 1
 * 4 4 10 10
 * oxxo
 * xoox
 * xoox
 * oxxo
 * xxxxxxoxxo
 * oxxoooxoox
 * xooxxxxoox
 * xooxxxoxxo
 * oxxoxxxxxx
 * ooooxxxxxx
 * xxxoxxoxxo
 * oooxooxoox
 * oooxooxoox
 * xxxoxxoxxo
 *
 * Output 1
 * 11
 * 8
 * -----------------
 * Input 2
 * 3 3 3 3
 * ooo
 * ooo
 * ooo
 * ooo
 * ooo
 * ooo
 *
 * Output 2
 * 1
 * -----------------
 */
public class BOJ10538 {

    private static class AhoCorasick {

        private static class Trie {

            boolean isEnd, isRoot;
            int picIdx;
            Trie failureLink;
            Trie[] nodes;

            public Trie(boolean isRoot) {
                this.picIdx = -1;
                this.isRoot = isRoot;
                this.nodes = new Trie[2];
            }

            public void add(String word, int picIdx) {
                Trie curr = this;

                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    int idx = c == 'x' ? 0 : 1;
                    Trie nxt = curr.nodes[idx];
                    if (nxt == null) {
                        nxt = new Trie(false);
                        curr.nodes[idx] = nxt;
                    }
                    curr = nxt;
                }
                curr.picIdx = picIdx;
                curr.isEnd = true;
            }

            public void clear() {
                for (Trie child : nodes) {
                    child.clear();
                }
                nodes[0] = nodes[1] = null;
                nodes = null;
            }
        }


        private static Trie root = new Trie(true);
        private static boolean isInit;

        public static void add(String pattern, int picIdx) {
            if (root == null) return;
            isInit = false;
            root.add(pattern, picIdx);
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

        private static void buildFailureLink() {
            Queue<Trie> queue = new LinkedList<>();
            root.failureLink = root;
            queue.add(root);

            while (!queue.isEmpty()) {
                Trie curr = queue.poll();

                for (int i = 0; i < curr.nodes.length; i++) {
                    Trie nxt = curr.nodes[i];
                    if (nxt == null) continue;

                    if (curr.isRoot) {
                        nxt.failureLink = root;
                    } else {
                        Trie parentLink = curr.failureLink;

                        while (!parentLink.isRoot && parentLink.nodes[i] == null) {
                            parentLink = parentLink.failureLink;
                        }

                        if (parentLink.nodes[i] != null) {
                            parentLink = parentLink.nodes[i];
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

        public static List<Integer> search(String word, int h) {
            if (root == null || !isInit) {
                return null;
            }

            List<Integer> idxes = new ArrayList<>();
            Trie curr = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c == 'x' ? 0 : 1;
                while (!curr.isRoot && curr.nodes[idx] == null) {
                    curr = curr.failureLink;
                }

                if (curr.nodes[idx] != null) {
                    curr = curr.nodes[idx];
                }

                if (curr.isEnd) {
                    bigPictures[h][i] = curr.picIdx;
                }
            }

            return idxes;
        }
    }

    static int hp, wp, hm, wm;
    static int[] picIdxes;
    static int[][] bigPictures;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        hp = Integer.parseInt(st.nextToken());
        wp = Integer.parseInt(st.nextToken());
        hm = Integer.parseInt(st.nextToken());
        wm = Integer.parseInt(st.nextToken());
        bigPictures = new int[hm][wm];

        picIdxes = new int[hp];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < hp; i++) {
            String word = br.readLine();
            int hash = word.hashCode();

            if (!map.containsKey(hash)) {
                map.put(hash, map.size() + 1);
            }
            picIdxes[i] = map.get(hash);
            AhoCorasick.add(word, picIdxes[i]);
        }


        AhoCorasick.build();
        for (int i = 0; i < hm; i++) {
            String word = br.readLine();
            AhoCorasick.search(word, i);
        }

        // build pi table (kmp)
        int[] pi = new int[picIdxes.length];
        int match = 0, search = 1, count = 0;
        for (; search < picIdxes.length; search++) {
            while (match > 0 && picIdxes[search] != picIdxes[match]) {
                match = pi[match - 1];
            }

            if (picIdxes[search] == picIdxes[match]) {
                pi[search] = ++match;
            }
        }

        // search the picIdxes patterns (kmp)
        for (int i = 0; i < wm; i++) {
            match = 0;
            search = 0;
            for (; search < hm; search++) {
                while (match > 0 && bigPictures[search][i] != picIdxes[match]) {
                    match = pi[match - 1];
                }

                if (bigPictures[search][i] == picIdxes[match]) {
                    if (match == picIdxes.length - 1) {
                        count++;
                        match = pi[match];
                    } else {
                        match++;
                    }
                }
            }
        }
        bw.write(String.valueOf(count));


        // close the buffer
        br.close();
        bw.close();
    }
}