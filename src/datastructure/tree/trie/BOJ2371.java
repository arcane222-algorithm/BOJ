package datastructure.tree.trie;

import java.io.*;
import java.util.*;



/**
 * 파일 구별하기 - BOJ2371
 * -----------------
 * category: data structures (자료구조)
 *           tree (트리)
 *           trie (트라이)
 * -----------------
 * Input 1
 * 3
 * 1 2 3 -1
 * 1 2 3 4 -1
 * 1 2 4 5 -1
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ2371 {

    private static class Trie {
        private final HashMap<Integer, Trie> nodes;

        public Trie() {
            nodes = new HashMap<>();
        }

        public int add(List<Integer> values) {
            Trie curr = this;
            int res = 0;

            for (Integer val : values) {
                if (curr.nodes.containsKey(val)) {
                    curr = curr.nodes.get(val);
                    res++;
                } else {
                    Trie t = new Trie();
                    curr.nodes.put(val, t);
                    curr = t;
                }
            }

            return res + 1;
        }
    }

    static int N;
    static Trie root;

    public static List<Integer> getValues(StringTokenizer st) {
        List<Integer> list = new ArrayList<>();
        while (st.hasMoreTokens()) {
            int val = Integer.parseInt(st.nextToken());
            if (val == -1)
                break;

            list.add(val);
        }

        return list;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        root = new Trie();
        N = Integer.parseInt(br.readLine());
        int res = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            res = Math.max(res, root.add(getValues(st)));
        }
        bw.write(res + "");

        // close the buffer
        br.close();
        bw.close();
    }
}