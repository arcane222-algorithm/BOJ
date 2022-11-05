package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * 친구비 - BOJ 16562
 * -----------------
 * Input 1
 * 5 3 20
 * 10 10 20 20 30
 * 1 3
 * 2 4
 * 5 4
 *
 * Output 1
 * 20
 * -----------------
 * Input 2
 * 5 3 10
 * 10 10 20 20 30
 * 1 3
 * 2 4
 * 5 4
 *
 * Output 2
 * Oh no
 * -----------------
 * Input 3
 * 5 3 20
 * 10 5 20 20 10
 * 1 3
 * 2 4
 * 5 4
 *
 * Output 3
 * 15
 * -----------------
 */
public class BOJ16562 {

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
         * @param n Size of DisjointSet
         */
        public DisjointSet(int n) {
            parents = new int[n];   // 0 ~ n - 1
            ranks = new int[n];
            nodeCounts = new int[n];
            for(int i = 0; i < n; i++) {
                parents[i] = i;
                nodeCounts[i] = 1;
            }
        }

        public void setRanks(int[] ranks) {
            this.ranks = ranks;
        }

        /**
         * Union both 'a' and 'b' nodes
         * @param a index of 'a' node
         * @param b index of 'b' node
         */
        public void union(int a, int b) {
            // Size out of bounds
            if(a < 0 || a > parents.length - 1) return;
            if(b < 0 || b > parents.length - 1) return;

            // Get parents
            int aRoot = find2(a);
            int bRoot = find2(b);

            // same root
            if(aRoot == bRoot) return;

            if(ranks[aRoot] > ranks[bRoot]) {
                int tmp = aRoot;
                aRoot = bRoot;
                bRoot = tmp;
            }

            parents[bRoot] = aRoot;
            nodeCounts[aRoot] += nodeCounts[bRoot];
        }

        /**
         * Find a parent of 'a' node
         * @param a index of 'a' node
         * @return parent of 'a' node
         */
        public int find(int a) {
            if(a < 0 || a > parents.length - 1) return -1;
            return parents[a];
        }

        /**
         * Find a parent of 'a' node
         * Path compression is performed with find
         * @param a index of 'a' node
         * @return root parent of 'a' node
         */
        public int find2(int a) {
            if(a < 0 || a > parents.length - 1) return -1;
            if(a == parents[a]) return a;
            else return parents[a] = find2(parents[a]);  // path compression
        }

        /**
         * Compare parent of both 'a' and 'b' nodes
         * @param a index of 'a' node
         * @param b index of 'b' node
         * @return
         */
        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
        }

        /**
         * Compare parent of both 'a' and 'b' nodes
         * Path compression is performed with find
         * @param a index of 'a' node
         * @param b index of 'b' node
         * @return
         */
        public boolean compareParent2(int a, int b) {
            return find2(a) == find2(b);
        }

        /**
         * Get node count of children
         * @param a index of 'a' node
         * @return
         */
        public int getNodeCount(int a) {
            return nodeCounts[a];
        }

        /**
         * Size of DisjointSet (Node count of DisjointSet)
         * @return
         */
        public int size() {
            return parents.length;
        }

        /**
         * Print information of DisjointSet
         * @param type
         */
        public void dump(DumpType type) {
            StringBuilder sb = new StringBuilder();

            switch(type) {
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
                    dump(DumpType.PARENTS);
                    dump(DumpType.RANKS);
                    dump(DumpType.NODE_COUNTS);
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

    static int N, M, K;
    static int[] friendCosts;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N, M, K
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        friendCosts = new int[N + 1];

        // parse friend costs
        st = new StringTokenizer(br.readLine());
        for(int i = 1; i < N + 1; i++) {
            friendCosts[i] = Integer.parseInt(st.nextToken());
        }

        DisjointSet dSet = new DisjointSet(N + 1);  // 0: myself, 1 ~ N: friends
        dSet.setRanks(friendCosts);
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            dSet.union(v, w);
        }

        String result = null;
        int costSum = 0;
        for(int i = 1; i < N + 1; i++) {
            int root = dSet.find2(i);
            if(root != 0) {
                dSet.union(0, root);
                costSum += friendCosts[root];
            }

            if(K < costSum) {
                result = "Oh no";
                break;
            }

            if(dSet.getNodeCount(0) - 1 == N) {
                result = String.valueOf(costSum);
                break;
            }
        }

        bw.write(result);

        // close the buffer
        br.close();
        bw.close();
    }
}