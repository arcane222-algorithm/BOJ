package disjointset;

import java.io.*;
import java.util.*;


/**
 * 중량 제한 - BOJ 1939
 * -----------------
 * Input 1
 * 3 3
 * 1 2 2
 * 3 1 3
 * 2 3 2
 * 1 3
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 5 8
 * 1 2 5
 * 1 3 4
 * 1 4 4
 * 2 3 4
 * 2 4 6
 * 3 4 3
 * 2 5 2
 * 4 5 4
 * 1 5
 *
 * Output 2
 * 4
 * -----------------
 */
public class BOJ1939 {

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
            int aRoot = find2(a);
            int bRoot = find2(b);

            // same root
            if (aRoot == bRoot) return;

            parents[bRoot] = aRoot;
            nodeCounts[aRoot] += nodeCounts[bRoot];
        }

        /**
         * Find a parent of 'a' node
         *
         * @param a index of 'a' node
         * @return parent of 'a' node
         */
        public int find(int a) {
            if (a < 0 || a > parents.length - 1) return -1;
            return parents[a];
        }

        /**
         * Find a parent of 'a' node
         * Path compression is performed with find
         *
         * @param a index of 'a' node
         * @return root parent of 'a' node
         */
        public int find2(int a) {
            if (a < 0 || a > parents.length - 1) return -1;
            if (a == parents[a]) return a;
            else return parents[a] = find2(parents[a]);  // path compression
        }

        /**
         * Compare parent of both 'a' and 'b' nodes
         *
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
         *
         * @param a index of 'a' node
         * @param b index of 'b' node
         * @return
         */
        public boolean compareParent2(int a, int b) {
            return find2(a) == find2(b);
        }

        /**
         * Get node count of children
         *
         * @param a index of 'a' node
         * @return
         */
        public int getNodeCount(int a) {
            return nodeCounts[a];
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
        public void dump(DumpType type) {
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

    private static class Edge implements Comparable<Edge> {
        int v1, v2, cost;

        public Edge(int v1, int v2, int cost) {
            this.v1 = v1;
            this.v2 = v2;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(cost, e.cost);
        }
    }

    static int N, M;
    static int F1, F2;

    public static boolean bfs(int begin, int end, int pivot) {
        return true;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // parse N, M
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        DisjointSet dSet = new DisjointSet(N + 1);
        List<Edge> bridges = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());
            bridges.add(new Edge(A, B, C));
        }
        st = new StringTokenizer(br.readLine());
        F1 = Integer.parseInt(st .nextToken());
        F2 = Integer.parseInt(st .nextToken());
        Collections.sort(bridges, (o1, o2) -> Integer.compare(o2.cost, o1.cost));

        // Maximum spanning tree (kruskal)
        int maximumCost = Integer.MAX_VALUE;
        for(int i = 0; i < bridges.size(); i++) {
            Edge edge = bridges.get(i);
            int v1 = edge.v1;
            int v2 = edge.v2;
            if(!dSet.compareParent2(v1, v2)) {
                dSet.union(v1, v2);
            }
            if(edge.cost < maximumCost) {
                maximumCost = edge.cost;
            }
            if(dSet.compareParent2(F1, F2)) {
                break;
            }
        }

        // write the result
        bw.write(String.valueOf(maximumCost));

        // close the buffer
        br.close();
        bw.close();
    }
}