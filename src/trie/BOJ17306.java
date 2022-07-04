package trie;

import java.io.*;
import java.util.*;

/**
 * 전쟁 중의 삶 - BOJ17306
 * -----------------
 *
 *
 * -----------------
 * Input 1
 * 4
 * 4 5 6 7
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 2
 * 1 4294967296
 *
 * Output 2
 * 33
 * -----------------
 * Input 3
 * 2
 * 8 10
 *
 * Output 3
 * 5
 * -----------------
 * Input 4
 * 20
 * 12312 12531789 9830458 26213897 123124790 1230293 146728 6985 353709 93539 11659 530549 23482309 30492034 938434 294234234 23489 23409 46098 45723
 *
 * Output 4
 * 302
 * -----------------
 */
public class BOJ17306 {

    private static class Trie {
        private Trie[] nodes;
        private int unitCnt;

        public Trie() {
            nodes = new Trie[2];
        }

        private long getMsb(long value) {
            long mask = 1;
            while (value > 0) {
                value = value >> 1;
                mask <<= 1;
            }
            mask >>= 1;

            return mask;
        }

        public void add(long value) {
            Trie current = this;
            long mask = getMsb(value);

            while(mask > 0) {
                int idx = (value & mask) > 0 ? 1 : 0;
                if (current.nodes[idx] == null) {
                    current.nodes[idx] = new Trie();
                }
                current = current.nodes[idx];
                current.unitCnt++;
                mask >>= 1;
            }
        }
    }

    static int N;
    static long[] nums;

    public static long bfs(Trie trie) {
        Queue<Trie> queue = new LinkedList<>();
        queue.add(trie);

        long count = 0;
        while (!queue.isEmpty()) {
            Trie current = queue.poll();

            if (current.unitCnt > 0 && current.unitCnt < N) {
                count++;
            }

            for(int i = 0; i < current.nodes.length; i++) {
                if(current.nodes[i] != null) {
                    queue.add(current.nodes[i]);
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

        // parse N
        N = Integer.parseInt(br.readLine());
        nums = new long[N];

        // init trie
        Trie trie = new Trie();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Long.parseLong(st.nextToken());
            trie.add(nums[i]);
        }

        // write the result
        long count = bfs(trie) + 1;
        bw.write(String.valueOf(count));

        // close the buffer
        br.close();
        bw.close();
    }
}