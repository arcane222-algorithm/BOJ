package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * 트리 - BOJ 4803
 * -----------------
 * Input 1
 * 6 3
 * 1 2
 * 2 3
 * 3 4
 * 6 5
 * 1 2
 * 2 3
 * 3 4
 * 4 5
 * 5 6
 * 6 6
 * 1 2
 * 2 3
 * 1 3
 * 4 5
 * 5 6
 * 6 4
 * 0 0
 *
 * Output 1
 * Case 1: A forest of 3 trees.
 * Case 2: There is one tree.
 * Case 3: No trees.
 * -----------------
 */
public class BOJ4803 {

    private static class DisjointSet {
        private int[] parents;
        private int[] ranks;
        private int[] nodeCounts;

        public enum DumpType {
            PARENTS,
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
                parents[i]  = i;
                nodeCounts[i] = 1;
            }
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

            if(aRoot > bRoot) {
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

                case NODE_COUNTS:
                    sb.append("Node counts: ");
                    sb.append(Arrays.toString(nodeCounts));
                    break;


                case ALL:
                    dump(DumpType.PARENTS);
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

    static int N, M;
    static DisjointSet dSet;
    static HashSet<Integer> treeRootSet;


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder result = new StringBuilder();

        StringTokenizer st = null;
        int caseCount = 1;
        while(true) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            if(N == 0 && M == 0) break;

            dSet = new DisjointSet(N + 1);
            treeRootSet = new HashSet<>();
            for(int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                int v1 = Integer.parseInt(st.nextToken());
                int v2 = Integer.parseInt(st.nextToken());

                if(dSet.compareParent2(v1, v2)) {
                    dSet.union(v1, 0);
                } else {
                    dSet.union(v1, v2);
                }
            }

            // Store roots to set
            for(int i = 1; i < N + 1; i++) {
                int root = dSet.find2(i);
                if(root != 0) {
                    treeRootSet.add(root);
                }
            }
            int treeCount = treeRootSet.size();

            // Write the result
            if(treeCount == 0) {
                result.append(String.format("Case %d: No trees.", caseCount));
            } else if(treeCount == 1) {
                result.append(String.format("Case %d: There is one tree.", caseCount));
            } else {
                result.append(String.format("Case %d: A forest of %d trees.", caseCount, treeCount));
            }
            result.append('\n');
            caseCount++;
        }

        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}