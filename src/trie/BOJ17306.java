package trie;

import java.io.*;
import java.util.*;

/**
 * 전쟁 중의 삶 - BOJ17306
 * -----------------
 *
 * Trie를 구성할 때 입력 값을 이진 수로 변환하여 삽입한다.
 * 기존 XOR 문제들과 다르게 이진 수 삽입 시 좌측 비트를 0으로 채우는 것이 아닌 있는 그대로 삽입한다. (즉, 5 = 101이고, 101 그대로 삽입)
 * 이 경우 Root 노드는 무조건 1에서부터 시작하며 그 아래로 채워지게 된다.
 * Trie에 값을 넣을 때, 거쳐가는 노드들에 대해 unitCount (군부대 수)를 증가 시켜 준 후
 * bfs를 통해 트리를 탐색하며 각 노드들에 대해 군부대 수가 [1, N - 1] 사이로 존재하는 마을의 경우 위험한 마을 이므로 count를 증가시켜준다.
 * (해당 마을이 위험하다는 것은, 해당 마을을 Root로 하는 sub-tree 안에 군부대가 적어도 1개 이상 있고, 그 외의 sub-tree 안에 군부대가 적어도 1개 이상
 * 존재하여야 한다.)
 * (because, 해당 마을이 위험한 마을이려면 해당 마을을 거쳐가는 군부대 수가 2개 이상이어여 하는데,
 * 해당 마을로 들어올 수 있는 길은 해당 마을을 Root로 하는 sub-tree 2개 + 해당 마을의 부모 node 1개
 * 총 3개가 존재하므로 sub-tree 안에 군부대가 적어도 1개 이상 존재해야 위험한 마을이 될 수 있다.)
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