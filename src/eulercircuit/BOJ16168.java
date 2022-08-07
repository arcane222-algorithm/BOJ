package eulercircuit;

import java.io.*;
import java.util.*;



/**
 * 퍼레이드 - BOJ16168
 * -----------------
 *
 * 연결 구간을 두번 지나지 않으면서 모든 구간을 지나도록 퍼레이드를 만들기 위해서는
 * 해당 그래프가 오일러 경로나 오일러 회로를 만족해야 한다.
 * 오일러 회로의 경우 무향그래프로 이루어진 각 Vertex의 degree 값이 모두 짝수이어야 한다.
 * 오일러 경로의 경우 무향그래프로 이루어진 각 Vertex의 degree 값중 두 Vertex만 홀수이고 나머지는 짝수이어야 한다.
 *
 * DisjointSet을 이용하여 주어지는 두 Vertex들을 연결하여 각 vertex의 parent 값이 모두 같다면 모든 Vertex가 연결된 그래프 이므로
 * 이 점을 이용하여 주어진 입력이 연결그래프인지 확인한다.
 *
 * 이후 각 Vertex의 degree값을 조사하여 홀수 degree 값을 가지는 vertex가 2개거나 전부 짝수 degree 값을 가질 경우 (즉, 오일러 경로, 회로를 가질 경우)
 * 연결 구간을 두번 지나지 않으면서 모든 구간을 지나는 퍼레이드가 완성되므로 YES를 출력하고 위 두가지 경우를 만족하지 못하면 NO를 출력한다.
 *
 * -----------------
 * Input 1
 * 4 5
 * 1 2
 * 1 3
 * 1 4
 * 2 3
 * 2 4
 *
 * Output 1
 * YES
 * -----------------
 * Input 2
 * 5 8
 * 1 2
 * 1 3
 * 1 4
 * 1 5
 * 2 3
 * 2 4
 * 3 5
 * 4 5
 *
 * Output 2
 * NO
 * -----------------
 */
public class BOJ16168 {

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

    static int V, E;
    static int[] degree;
    static DisjointSet dSet;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        degree = new int[V];
        dSet = new DisjointSet(V);

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int va = Integer.parseInt(st.nextToken()) - 1;
            int vb = Integer.parseInt(st.nextToken()) - 1;
            degree[va] += 1;
            degree[vb] += 1;
            dSet.union(va, vb);
        }

        Set<Integer> set = new HashSet<>();
        int oddCnt = 0;
        for (int i = 0; i < V; i++) {
            set.add(dSet.find(i));
            if (degree[i] % 2 == 1) {
                oddCnt++;
            }
        }

        bw.write((oddCnt == 0 || oddCnt == 2) && set.size() == 1 ? "YES" : "NO");

        // close the buffer
        br.close();
        bw.close();
    }
}
