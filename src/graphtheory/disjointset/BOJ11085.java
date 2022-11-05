package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * 군사 이동 - BOJ11085
 * -----------------
 *
 * c에서 v로 가는 길을 찾기 위해서 c와 v의 parent 값이 같아질 때 까지 edge들의 vertex1, vertex2를 union 해준다.
 * 이때, 경로 상에 있는 길 중 너비가 가장 좁은 길의 너비를 최대화해야 하므로 Edge들의 weight를 내림차순으로 정렬해서
 * 큰 값부터 골라가면 너비가 가장 좁은 길의 너비가 최대화 된다.
 * union 작업 전 두 vertex의 parent 값이 같다면 cycle이 생기는 길이므로 제외하고 넘어간다.
 *
 * -----------------
 * Input 1
 * 7 11
 * 3 5
 * 0 1 15
 * 0 2 23
 * 1 2 16
 * 1 3 27
 * 2 4 3
 * 2 6 21
 * 3 4 14
 * 3 5 10
 * 4 5 50
 * 4 6 9
 * 5 6 42
 *
 * Output 1
 * 16
 * -----------------
 * Input 2
 * 3 3
 * 1 2
 * 0 1 1
 * 0 2 2
 * 1 2 100
 *
 * Output 2
 * 100
 * -----------------
 */
public class BOJ11085 {

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

    private static class Edge implements Comparable<Edge> {
        int v1, v2, w;

        public Edge(int v1, int v2, int w) {
            this.v1 = v1;
            this.v2 = v2;
            this.w = w;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(w, e.w);
        }
    }

    static int p, w, c, v;
    static DisjointSet dSet;
    static List<Edge> edges = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        p = Integer.parseInt(st.nextToken());
        w = Integer.parseInt(st.nextToken());
        dSet = new DisjointSet(p);

        st = new StringTokenizer(br.readLine());
        c = Integer.parseInt(st.nextToken());
        v = Integer.parseInt(st.nextToken());

        for (int i = 0; i < w; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            edges.add(new Edge(s, e, w));
        }
        Collections.sort(edges, Collections.reverseOrder());

        int minWeight = Integer.MAX_VALUE;
        for (int i = 0; i < w; i++) {
            if (dSet.compareParent(c, v)) break;
            Edge e = edges.get(i);
            if (!dSet.compareParent(e.v1, e.v2)) {
                dSet.union(e.v1, e.v2);
                if (minWeight > e.w) {
                    minWeight = e.w;
                }
            }
        }

        bw.write(String.valueOf(minWeight));

        // close the buffer
        br.close();
        bw.close();
    }
}