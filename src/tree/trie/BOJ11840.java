package tree.trie;

import java.io.*;
import java.util.*;


/**
 * XOR - BOJ11840
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 4 6
 * 6 1 2 4
 *
 * Output 1
 * 2 3
 * -----------------
 * Input 2
 * 4 7
 * 1 2 3 7
 *
 * Output 2
 * 1 4
 * -----------------
 */
public class BOJ11840 {

    private static class BinaryTrie {

        static final int MAX_BIT_SIZE = 30;

        BinaryTrie[] nodes;
        int prefixIdx = Integer.MAX_VALUE;
        int value = 0;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public void add(int value, int prefixIdx) {
            BinaryTrie current = this;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bit = (value >> i) & 1;
                int bitIdx = bit > 0 ? 1 : 0;
                if (current.nodes[bitIdx] == null) {
                    current.nodes[bitIdx] = new BinaryTrie();
                }
                current = current.nodes[bitIdx];
                current.prefixIdx = Math.min(current.prefixIdx, prefixIdx);
            }
            current.value = value;
        }

        public int search(int value, int bitSize, int x) {
            if (isLeaf()) return prefixIdx;

            int res = Integer.MAX_VALUE;
            int bitIdx = (value >> bitSize) & 1;
            int opposite = bitIdx ^ 1;
            int mask = 1 << bitSize;

            BinaryTrie current = this;
            BinaryTrie child = current.nodes[bitIdx];
            BinaryTrie oppositeChild = current.nodes[opposite];

            if (x >= mask) {
                if (oppositeChild != null) {
                    res = Math.min(oppositeChild.search(value, bitSize - 1, x - mask), res);
                }
            } else {
                if (child != null && oppositeChild != null) {
                    if (child.prefixIdx < oppositeChild.prefixIdx) {
                        res = Math.min(child.search(value, bitSize - 1, x), res);
                    } else {
                        res = Math.min(oppositeChild.search(value, bitSize - 1, x - mask), res);
                    }
                } else if (child == null) {
                    res = Math.min(oppositeChild.search(value, bitSize - 1, x - mask), res);
                } else {
                    res = Math.min(child.search(value, bitSize - 1, x), res);
                }
            }

            return res;
        }

        public boolean isLeaf() {
            return nodes[0] == null && nodes[1] == null;
        }

        public void printAll() {
            if (isLeaf()) {
                System.out.println(value);
            } else {
                for (int i = 0; i < 2; i++) {
                    if (nodes[i] != null) {
                        nodes[i].printAll();
                    }
                }
            }
        }
    }

    static int N, x;
    static BinaryTrie root = new BinaryTrie();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        x = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        root.add(0, 0);
        int prefixXor = 0, gap = 0, minI = Integer.MAX_VALUE;
        for (int idx = 1; idx < N + 1; idx++) {
            prefixXor ^= Integer.parseInt(st.nextToken());
            int i = root.search(prefixXor, BinaryTrie.MAX_BIT_SIZE - 1, x);
            if (idx - i > gap) {
                gap = idx - i;
                minI = i;
            } else if (idx - i == gap) {
                minI = Math.min(i, minI);
            }
            root.add(prefixXor, idx);
        }
        result.append(minI + 1).append(' ').append(gap);
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}