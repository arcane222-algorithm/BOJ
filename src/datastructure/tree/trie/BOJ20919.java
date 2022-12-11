package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * XOR 자료구조 - BOJ20919
 * -----------------
 *
 *
 *
 * -----------------
 * Input 1
 * 3
 * 4 7
 * 1 1 3 3
 * 1 2
 * 2 2
 * 3 2
 * 4
 * 5
 * 1 2
 * 2 2
 * 10 11
 * 1 3 5 7 9 2 4 6 8 10
 * 1 6
 * 1 8
 * 2 6
 * 2 8
 * 3 10
 * 4
 * 5
 * 1 2
 * 1 17
 * 2 2
 * 2 17
 * 5 11
 * 2 5 8 13 17
 * 1 6
 * 1 8
 * 2 6
 * 2 8
 * 3 10
 * 4
 * 5
 * 1 2
 * 1 17
 * 2 2
 * 2 17
 *
 * Output 1
 * 1
 * 3
 * 3
 * 1
 * 3
 * 0
 * 0
 * 0
 * 0
 * 15
 * 15
 * 10
 * 1
 * 10
 * 0
 * 18
 * 11
 * 25
 * 3
 * 0
 * 23
 * 25
 * 6
 * 2
 * 17
 * 7
 * 20
 * 15
 * 28
 * -----------------
 */
public class BOJ20919 {

    private static class BinaryTrie {
        static final int MAX_BIT_SIZE = 25;
        BinaryTrie[] nodes;
        BinaryTrie parent;
        int bitIdx;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public boolean isLeaf() {
            return nodes[0] == null && nodes[1] == null;
        }

        public void add(int value) {
            BinaryTrie curr = this;
            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bitIdx = (value >> i) & 1;
                if (curr.nodes[bitIdx] == null) {
                    BinaryTrie child = new BinaryTrie();
                    child.parent = curr;
                    child.bitIdx = bitIdx;
                    curr.nodes[bitIdx] = child;
                }
                curr = curr.nodes[bitIdx];
            }
        }

        public void remove(int value) {
            BinaryTrie curr = this;
            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bitIdx = (value >> i) & 1;
                if (curr.nodes[bitIdx] == null) {
                    return;
                }
                curr = curr.nodes[bitIdx];
            }

            while (curr.isLeaf()) {
                int bitIdx = curr.bitIdx;
                curr = curr.parent;

                if (curr != null) {
                    curr.nodes[bitIdx] = null;
                } else {
                    break;
                }
            }
        }

        public int xorMin(int value) {
            BinaryTrie curr = this;
            int xorValue = 0;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bitIdx = (value >> i) & 1;
                if (curr.nodes[bitIdx] == null) {
                    bitIdx ^= 1;
                    xorValue += (1 << i);
                }
                curr = curr.nodes[bitIdx];
            }

            return xorValue;
        }

        public int xorMax(int value) {
            BinaryTrie curr = this;
            int xorValue = 0;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int oppositeIdx = ~(value >> i) & 1;
                if (curr.nodes[oppositeIdx] == null) {
                    oppositeIdx ^= 1;
                } else {
                    xorValue += (1 << i);
                }
                curr = curr.nodes[oppositeIdx];
            }

            return xorValue;
        }
    }

    static int T, n, q;
    static TreeSet<Integer> treeSet;
    static BinaryTrie root;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            q = Integer.parseInt(st.nextToken());

            treeSet = new TreeSet<>();
            root = new BinaryTrie();

            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                int num = Integer.parseInt(st.nextToken());
                treeSet.add(num);
                root.add(num);
            }

            for (int j = 0; j < q; j++) {
                st = new StringTokenizer(br.readLine());
                int type = Integer.parseInt(st.nextToken());
                switch (type) {
                    case 1:
                        result.append(root.xorMin(Integer.parseInt(st.nextToken())));
                        break;

                    case 2:
                        result.append(root.xorMax(Integer.parseInt(st.nextToken())));
                        break;

                    case 3:
                        int num = Integer.parseInt(st.nextToken());
                        if (!treeSet.contains(num)) {
                            treeSet.add(num);
                            root.add(num);
                        }
                        result.append(treeSet.size());
                        break;

                    case 4:
                        int min = treeSet.first();
                        treeSet.remove(min);
                        root.remove(min);
                        result.append(min);
                        break;

                    case 5:
                        int max = treeSet.last();
                        treeSet.remove(max);
                        root.remove(max);
                        result.append(max);
                        break;
                }
                result.append('\n');
            }
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
