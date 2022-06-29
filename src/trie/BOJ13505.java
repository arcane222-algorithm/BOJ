package trie;

import java.io.*;
import java.util.*;


/**
 * 두 수 XOR - BOJ13505
 * -----------------
 * Input 1
 * 5
 * 1 2 3 4 5
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 5
 * 0 1 0 1 0
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 6
 * 1 2 4 8 16 32
 *
 * Output 3
 * 48
 * -----------------
 */
public class BOJ13505 {

    private static class Trie {
        public static final int MAX_HEIGHT = 30;
        private Trie[] nodes;

        public Trie() {
            nodes = new Trie[2];
        }

        public void add(int value) {
            Trie current = this;

            for(int i = MAX_HEIGHT; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 1 : 0;
                if(current.nodes[idx] == null) {
                    current.nodes[idx] = new Trie();
                }
                current = current.nodes[idx];
            }
        }
    }

    static int N;
    static int[] nums;

    public static int XOR(Trie trie, int value) {
        Trie current = trie;
        int result = 0;

        for(int i = Trie.MAX_HEIGHT; i > -1; i--) {
            int mask = 1 << i;
            int bit = value & mask;
            int idx = bit > 0 ? 0 : 1; // because it is XOR, so it is opponent of add
            if(current.nodes[idx] == null) {
                idx = bit > 0 ? 1 : 0;
            } else {
                result += mask;
            }
            current = current.nodes[idx];
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N
        N = Integer.parseInt(br.readLine());
        nums = new int[N];
        Trie trie = new Trie();

        // parse numbers
        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
            trie.add(nums[i]);
        }

        // XOR each numbers
        int max = XOR(trie, nums[0]);
        for(int i = 1; i < N; i++) {
            int res = XOR(trie, nums[i]);
            if(max < res) max = res;
        }

        // write the result
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}