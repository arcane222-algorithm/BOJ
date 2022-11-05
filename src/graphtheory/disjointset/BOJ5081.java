package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * Constellations - BOJ5081
 * -----------------
 * category: data structures (자료 구조)
 *           graph theory (그래프 이론)
 *           brute-force (브루트포스 알고리즘)
 *           disjoint set (분리 집합)
 * -----------------
 * Input 1
 * 10
 * 0 1
 * 16 3
 * 1 0
 * 2 7
 * 9 0
 * 4 1
 * 2 2
 * 8 1
 * 9 3
 * 15 5
 * 5
 * 10 10
 * 10 11
 * 20 10
 * 20 11
 * 15 5
 * 0
 *
 * Output 1
 * Sky 1 contains 3 constellations.
 * Sky 2 contains 1 constellations.
 * -----------------
 * Input 2
 * 4
 * 0 0
 * 1 1
 * 0 2
 * 3 1
 * 0
 *
 * Output 2
 * Sky 1 contains 1 constellations.
 * -----------------
 */
public class BOJ5081 {

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

    static int N;
    static int[] xPos;
    static int[] yPos;
    static DisjointSet dSet;
    static StringBuilder result = new StringBuilder();

    public static int distSquare(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        return dx * dx + dy * dy;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        int t = 1;
        while (true) {
            N = Integer.parseInt(br.readLine());
            if (N == 0) break;

            xPos = new int[N];
            yPos = new int[N];
            dSet = new DisjointSet(N);
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                xPos[i] = Integer.parseInt(st.nextToken());
                yPos[i] = Integer.parseInt(st.nextToken());
            }

            for (int i = 0; i < N; i++) {

                int minDist = Integer.MAX_VALUE;
                List<Integer> temp = new LinkedList<>();
                for (int j = 0; j < N; j++) {
                    if (i == j) continue;

                    int dist = distSquare(xPos[i], yPos[i], xPos[j], yPos[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        temp.clear();
                        temp.add(j);
                    } else if (dist == minDist) {
                        temp.add(j);
                    }
                }

                for (int idx : temp) {
                    if (!dSet.compareParent(idx, i)) {
                        dSet.union(idx, i);
                    }
                }
            }

            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < N; i++) {
                set.add(dSet.find(i));
            }
            result.append("Sky ").append(t).append(" contains ").append(set.size()).append(" constellations.\n");
            t++;
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}