package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * Tree - BOJ13244
 * -----------------
 *
 * 각 원들의 정보를 저장한 후 Disjoint-Set을 이용하여
 * 원 중심간 거리 <= 두 원의 반지름의 합 인 원들의 경우 같은 그룹으로 처리한다.
 * Math.sqrt 메소드의 경우 비싼 연산 이므로 부등식의 양변을 제곱하여 원 중심간 거리를 구할 때 sqrt를 사용하지 않고 계산한다.
 *
 * -----------------
 * Input 1
 * 2
 * 4
 * 3
 * 2 1
 * 3 4
 * 1 3
 * 3
 * 3
 * 1 2
 * 1 2
 * 3 2
 *
 * Output 1
 * tree
 * graph
 * -----------------
 * Input 2
 * 2
 * 7
 * 5
 * 7 2
 * 2 4
 * 4 3
 * 5 6
 * 6 1
 * 7
 * 6
 * 7 2
 * 2 4
 * 4 3
 * 4 5
 * 6 5
 * 1 6
 *
 * Output 2
 * graph
 * tree
 * -----------------
 */
public class BOJ13244 {

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
            clear(n);
        }

        public void clear() {
            if (parents != null) {
                int n = parents.length;
                clear(n);
            }
        }

        public void clear(int n) {
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

    static int T, N, M, A, B;
    static DisjointSet dSet;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            M = Integer.parseInt(br.readLine());
            dSet = new DisjointSet(N + 1);

            boolean isGraph = false;
            for(int j = 0; j < M; j++) {
                st = new StringTokenizer(br.readLine());
                A = Integer.parseInt(st.nextToken());
                B = Integer.parseInt(st.nextToken());

                if (!dSet.compareParent2(A, B)) {
                    dSet.union(A, B);
                } else {
                    isGraph = true;
                }
            }
            HashSet<Integer> set = new HashSet<>();
            for(int j = 1; j < N + 1; j++) {
                set.add(dSet.find2(j));
            }
            if (set.size() > 1)
                isGraph = true;

            result.append(isGraph ? "graph" : "tree").append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}