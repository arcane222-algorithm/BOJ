import java.io.*;
import java.util.*;

public class Main {

    private static class Trie {
        private static final int MAX_LENGTH = 30;

        private Trie[] nodes;

        public Trie() {
            nodes = new Trie[2];
        }

        public void add(int value) {
            Trie current = this;
            for(int i = MAX_LENGTH; i > -1; i--) {
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

    static int T, N;
    static int[] prefixSums;

    public static int XORMax(Trie trie, int value) {
        Trie current = trie;
        int result = 0;

        for(int i = Trie.MAX_LENGTH; i > -1; i--) {
            int mask = 1 << i;
            int bit = value & mask;
            int idx = bit > 0 ? 0 : 1;
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
        StringTokenizer st = null;

        // parse T
        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            // parse N
            N = Integer.parseInt(br.readLine());
            prefixSums = new int[N];
            Trie trie = new Trie();
            trie.add(0);

            st = new StringTokenizer(br.readLine());
            prefixSums[0] = Integer.parseInt(st.nextToken());
            trie.add(prefixSums[0]);
            for(int j = 1; j < N; j++) {
                prefixSums[j] = prefixSums[j - 1] ^ Integer.parseInt(st.nextToken());
                trie.add(prefixSums[j]);
            }

            int max = 0;
            for(int j = 0; j < N; j++) {
                int result = XORMax(trie, prefixSums[j]);
                if(max < result) max = result;
            }

            bw.write(String.valueOf(max));
            bw.write('\n');
        }

        // close the buffer
        br.close();
        bw.close();
    }
}