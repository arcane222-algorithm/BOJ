package disjointset;

import java.io.*;
import java.util.*;


/**
 * 은하철도 - BOJ17250
 * -----------------
 * Input 1
 * 5 4
 * 3
 * 9
 * 10
 * 11
 * 15
 * 1 2
 * 2 3
 * 4 5
 * 4 3
 *
 * Output 1
 * 12
 * 22
 * 26
 * 48
 * -----------------
 * Input 2
 * 5 4
 * 3
 * 1
 * 4
 * 15
 * 9
 * 1 2
 * 3 1
 * 2 3
 * 2 4
 *
 * Output 2
 * 4
 * 8
 * 8
 * 23
 * -----------------
 */
public class BOJ17250 {

    private static class DisjointSet {
        private int[] parents;
        private int[] ranks;
        private int[] nodeCounts;

        public enum DumpType {
            PARENTS,
            RANKS,
            NODE_COUNTS,
            ALL
        }

        /**
         * Constructor of DisjointSet
         *
         * @param n Size of DisjointSet
         */
        public DisjointSet(int n) {
            parents = new int[n];   // 0 ~ n - 1
            ranks = new int[n];
            nodeCounts = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
                nodeCounts[i] = 1;
            }
        }

        /**
         * Union both 'a' and 'b' nodes
         *
         * @param a index of 'a' node
         * @param b index of 'b' node
         */
        public void union(int a, int b) {
            // Size out of bounds
            if (a < 0 || a > parents.length - 1) return;
            if (b < 0 || b > parents.length - 1) return;

            // Get parents
            int aRoot = find(a);
            int bRoot = find(b);

            // same root
            if (aRoot == bRoot) return;

            if (nodeCounts[aRoot] > nodeCounts[bRoot]) {
                int tmp = aRoot;
                aRoot = bRoot;
                bRoot = tmp;
            }

            parents[aRoot] = bRoot;
            nodeCounts[bRoot] += nodeCounts[aRoot];
        }

        /**
         * Find a parent of 'a' node
         * Path compression is performed with find
         *
         * @param a index of 'a' node
         * @return root parent of 'a' node
         */
        public int find(int a) {
            if (a < 0 || a > parents.length - 1) return -1;
            if (a == parents[a]) return a;
            else return parents[a] = find(parents[a]);  // path compression
        }

        /**
         * Compare parent of both 'a' and 'b' nodes
         * Path compression is performed with find
         *
         * @param a index of 'a' node
         * @param b index of 'b' node
         * @return
         */
        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
        }

        /**
         * Size of DisjointSet (Node count of DisjointSet)
         *
         * @return
         */
        public int size() {
            return parents.length;
        }

        /**
         * Print information of DisjointSet
         *
         * @param type
         */
        public void dump(DisjointSet.DumpType type) {
            StringBuilder sb = new StringBuilder();

            switch (type) {
                case PARENTS:
                    sb.append("Parents: ");
                    sb.append(Arrays.toString(parents));
                    break;

                case RANKS:
                    sb.append("Ranks: ");
                    sb.append(Arrays.toString(ranks));
                    break;

                case NODE_COUNTS:
                    sb.append("Node counts: ");
                    sb.append(Arrays.toString(nodeCounts));
                    break;

                case ALL:
                    dump(DisjointSet.DumpType.PARENTS);
                    dump(DisjointSet.DumpType.RANKS);
                    dump(DisjointSet.DumpType.NODE_COUNTS);
                    break;
            }

            System.out.println(sb);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DisjointSet{");
            sb.append("parents=");
            sb.append(Arrays.toString(parents));
            sb.append('}');

            return sb.toString();
        }
    }

    static int N, M;
    static DisjointSet dSet;
    static StringBuilder result = new StringBuilder();

    public static boolean outOfRange(int x) {
        return x > N || x < 1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        dSet = new DisjointSet(N + 1);
        for (int i = 1; i < N + 1; i++) {
            int cnt = Integer.parseInt(br.readLine());
            dSet.nodeCounts[i] = cnt;
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            dSet.union(a, b);
            result.append(dSet.nodeCounts[dSet.find(b)]).append('\n');
        }

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
