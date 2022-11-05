package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 세부 - BOJ13905
 * -----------------
 *
 * 두 Vertex를 잇는 Edge들을 weight 기준 내림차순으로 정렬한다.
 * 이후 s와 e지점이 연결될 때까지 (즉, disjoint-set에서의 parent가 같을 때 까지)
 * MST (최대 스패닝 트리) 를 구하며 max값의 최솟값을 찾으면 된다.
 *
 * -----------------
 * Input 1
 * 7 9
 * 1 5
 * 1 2 2
 * 1 7 4
 * 2 3 5
 * 3 7 5
 * 4 6 1
 * 6 7 4
 * 5 6 3
 * 5 7 1
 * 3 5 2
 *
 * Output 1
 * 3
 * -----------------
 */
public class BOJ13905 {

    private static class Edge implements Comparable<Edge> {
        int v1, v2, w;

        public Edge(int v1, int v2, int w) {
            this.v1 = v1;
            this.v2 = v2;
            this.w = w;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(w, o.w);
        }
    }

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

    static int N, M, S, E;
    static DisjointSet dSet;
    static List<Edge> edges = new ArrayList<>();
    static StringBuilder result = new StringBuilder();


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        dSet = new DisjointSet(N + 1);

        st = new StringTokenizer(br.readLine());
        S = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int h1 = Integer.parseInt(st.nextToken());
            int h2 = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            edges.add(new Edge(h1, h2, k));
        }
        Collections.sort(edges, Collections.reverseOrder());

        int max = Integer.MAX_VALUE;
        for (int i = 0; i < M; i++) {
            Edge e = edges.get(i);
            if (!dSet.compareParent2(e.v1, e.v2)) {
                dSet.union(e.v1, e.v2);
                if (max > e.w) {
                    max = e.w;
                }
            }
            if (dSet.compareParent2(S, E)) {
                break;
            }
        }
        result.append(max);

        // write the result
        bw.write(dSet.compareParent2(S, E) ? result.toString() : "0");

        // close the buffer
        br.close();
        bw.close();
    }
}