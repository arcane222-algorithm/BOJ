package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 커피숍 2 - BOJ1275
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5 2
 * 1 2 3 4 5
 * 2 3 3 1
 * 3 5 4 1
 *
 * Output 1
 * 5
 * 10
 * -----------------
 */
public class BOJ1275 {

    private static class IndexedTree {
        long[] nodes;
        int S;

        public IndexedTree(long[] arr) {
            S = 1;
            while (S < arr.length) {
                S = S << 1;
            }
            nodes = new long[S << 1];

            for (int i = 0; i < arr.length; i++) {
                nodes[S + i] = arr[i];
            }

            for (int i = S - 1; i > 0; i--) {
                nodes[i] = nodes[i << 1] + nodes[(i << 1) + 1];
            }
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, node * 2, queryLeft, queryRight);
                long rightValue = query(mid + 1, right, node * 2 + 1, queryLeft, queryRight);

                return leftValue + rightValue;
            }
        }

        public void update(int left, int right, int node, int target, long diff) {
            if (left > target || right < target) {
                return;
            } else {
                nodes[node] += diff;
                if (left != right) { // 자식이 있으면 left == right
                    int mid = (left + right) >> 1;
                    update(left, mid, (node << 1), target, diff);
                    update(mid + 1, right, (node << 1) + 1, target, diff);
                }
            }
        }
    }

    static int N, Q;
    static long[] nums;
    static IndexedTree indexedTree;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        nums = new long[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Long.parseLong(st.nextToken());
        }
        indexedTree = new IndexedTree(nums);

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            if (x > y) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            int a = Integer.parseInt(st.nextToken());
            long b = Long.parseLong(st.nextToken());
            result.append(indexedTree.query(1, indexedTree.S, 1, x, y)).append('\n');
            indexedTree.update(1, indexedTree.S, 1, a, b - indexedTree.nodes[indexedTree.S + a - 1]);
        }

        bw.write(result.toString());


        // close the buffer
        br.close();
        bw.close();
    }
}