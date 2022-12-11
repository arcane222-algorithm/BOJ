package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * 부분 수열 XOR - BOJ13445
 * -----------------
 *
 *
 * 
 *
 *
 * -----------------
 * Input 1
 * 5 2
 * 4 1 3 2 7
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 10 50
 * 15 10 8 3 7 30 96 27 86 17
 *
 * Output 2
 * 30
 * -----------------
 * Input 3
 * 20 55555
 * 10 100 1000 10000 100000 50000 33333 22222 1111 34567 1 2 3 4 5 6 7 8 9 10
 *
 * Output 3
 * 130
 * -----------------
 */
public class BOJ13445 {

    private static class BinaryTrie {

        private static final int MAX_BIT_SIZE = 31;
        int count;
        BinaryTrie[] nodes;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public void add(int num) {
            BinaryTrie curr = this;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int mask = 1 << i;
                int bit = num & mask;
                int idx = bit > 0 ? 1 : 0;
                BinaryTrie next = curr.nodes[idx];
                if (next == null) {
                    next = new BinaryTrie();
                    curr.nodes[idx] = next;
                }

                curr.count++;
                curr = next;
            }
            curr.count++;
        }

        public int count(int num, int K) {
            BinaryTrie curr = this;
            int count = curr.count;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int mask = 1 << i;
                int bit = num & mask;
                int idx = bit > 0 ? 1 : 0;
                int opposite = idx == 0 ? 1 : 0;
                if (curr == null) break;
                if (mask <= K - 1) {
                    K -= mask;
                    curr = curr.nodes[opposite];
                } else {
                    if (curr.nodes[opposite] != null) {
                        count -= curr.nodes[opposite].count;
                    }
                    curr = curr.nodes[idx];
                }
            }
            return count;
        }
    }

    static int N, K;
    static int[] prefixSum;
    static BinaryTrie root = new BinaryTrie();

    public static int naiveSolve() {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N + 1; j++) {
                int res = prefixSum[i] ^ prefixSum[j];
                if (res < K) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        prefixSum = new int[N + 1];

        st = new StringTokenizer(br.readLine());
        long cnt = 0;
        for (int i = 0; i < N + 1; i++) {
            if (i == 0) root.add(0);
            else {
                prefixSum[i] = prefixSum[i - 1] ^ Integer.parseInt(st.nextToken());
                cnt += root.count(prefixSum[i], K);
                root.add(prefixSum[i]);
            }
        }

//        System.out.println(Arrays.toString(prefixSum));
//        System.out.println("naive: " + naiveSolve());
        bw.write(String.valueOf(cnt));

        // close the buffer
        br.close();
        bw.close();
    }
}
