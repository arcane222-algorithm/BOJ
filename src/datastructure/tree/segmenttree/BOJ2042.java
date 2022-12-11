package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 구간 합 구하기 - BOJ 2042
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5 2 2
 * 1
 * 2
 * 3
 * 4
 * 5
 * 1 3 6
 * 2 2 5
 * 1 5 2
 * 2 3 5
 *
 * Output 1
 * 17
 * 12
 * -----------------
 */
public class BOJ2042 {
    private static class IndexedTree {
        long[] nodes;
        int S;

        public IndexedTree(long[] arr) {
            S = 1;
            while(S < arr.length) {
                S = S << 1;
            }
            nodes = new long[S << 1];

            for(int i = 0; i < arr.length; i++) {
                nodes[S + i] = arr[i];
            }

            for(int i = S - 1; i > 0; i--) {
                nodes[i] = nodes[i << 1] + nodes[(i << 1) + 1];
            }
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if(queryLeft > right || queryRight < left) {
                return 0;
            } else if(queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, node * 2, queryLeft, queryRight);
                long rightValue = query(mid + 1, right, node * 2 + 1, queryLeft, queryRight);

                return leftValue + rightValue;
            }
        }

        public void update(int left, int right, int node, int target, long diff) {
            if(left > target || right < target) {
                return;
            } else {
                nodes[node] += diff;
                if(left != right) { // 자식이 있으면 left == right
                    int mid = (left +  right) >> 1;
                    update(left, mid, (node << 1), target, diff);
                    update(mid + 1, right, (node << 1) + 1, target, diff);
                }
            }
        }
    }
    static int N, M, K;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());   // Count of updating segment
        K = Integer.parseInt(st.nextToken());   // Count of querying segment
        long[] nums = new long[(int) N];
        for(int i = 0; i < N; i++) {
            nums[i] = Long.parseLong(br.readLine());
        }

        IndexedTree idxTree = new IndexedTree(nums);
        for(int i = 0; i < (M + K); i++) {
            st = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            long c = Long.parseLong(st.nextToken());

            switch(a) {
                case 1:
                    idxTree.update(1, idxTree.S, 1, b, c - idxTree.nodes[idxTree.S + b - 1]);
                    break;

                case 2:
                    result.append(idxTree.query(1, idxTree.S, 1, b, (int) c));
                    result.append('\n');
                    break;
            }
        }

        // Write the result
        result.deleteCharAt(result.length() - 1);
        bw.write(result.toString());
        bw.flush();

        // Close the I/O stream
        br.close();
        bw.close();
    }
}