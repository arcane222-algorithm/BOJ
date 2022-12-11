package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * 말해 xor NO! - BOJ24397
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 5 5 6
 * 1 2 3 4 5
 * 3 4 5 6 7
 *
 * Output 1
 * 17
 * -----------------
 * Input 2
 * 5 5 5
 * 1 2 3 4 5
 * 1 1 1 1 1
 *
 * Output 2
 * 20
 * -----------------
 */
public class BOJ24397 {

    private static class BinaryTrie {

        static final int MAX_BIT_SIZE = 30;

        BinaryTrie[] nodes;
        int count;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public void add(int value) {
            BinaryTrie curr = this;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bitIdx = (value >> i) & 1;
                if (curr.nodes[bitIdx] == null) {
                    curr.nodes[bitIdx] = new BinaryTrie();
                }
                curr.count++;
                curr = curr.nodes[bitIdx];
            }
            curr.count++;
        }

        public int count(int value, int K) {
            BinaryTrie curr = this;
            int count = curr.count;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int bitIdx = (value >> i) & 1;
                int oppositeIdx = bitIdx ^ 1;
                int mask = 1 << i;
                if (curr == null) break;

                if (mask <= K - 1) {
                    K -= mask;
                    curr = curr.nodes[oppositeIdx];
                } else {
                    if (curr.nodes[oppositeIdx] != null) {
                        count -= curr.nodes[oppositeIdx].count;
                    }
                    curr = curr.nodes[bitIdx];
                }
            }

            return count;
        }
    }

    static int N, M, K;
    static long cnt;
    static BinaryTrie root = new BinaryTrie();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());
            root.add(num);
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            int num = Integer.parseInt(st.nextToken());
            cnt += root.count(num, K);
        }
        bw.write(String.valueOf(cnt));

        // close the buffer
        br.close();
        bw.close();
    }
}
