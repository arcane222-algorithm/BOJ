package disjointset;

import java.io.*;
import java.util.*;

/**
 * 민서의 응급 수술 - BOJ20955
 * -----------------
 *
 * 뉴런을 시냅스로 연결할 때 (즉, Disjoint-Set의 union 작업을 해줄 때), 만약 두 뉴런이 같은 집합이라면 (즉, parent값이 같다면)
 * 사이클이 형성되어 트리가 아니라 그래프가 되므로 끊어주어야 한다. 이 경우는 union을 하지 않고 끊어주는 작업을 하는 것으로 생각하여
 * 작업의 횟수를 1증가시켜 준다.
 * parent 가 다른 두 뉴런만 union 작업을 모두 해준 후, set에 각 뉴런들의 parent 값을 저장하여 parent의 종류 - 1을 결과에 더해주면 답이 된다.
 *
 * why?) 문제에서 주어지는 뉴런간 union 과정은 이미 연결이 되어있다고 제시되어 있다.
 *       위와 같이 사이클이 생기지 않도록 union을 수행하였으므로 각기 다른종류의 트리들이 생겼을 것이고, 이 트리들의 부모를 직접 union 하면
 *       마찬가지로 사이클이 생기지 않는 트리가 된다. 그러므로 전부 union 하여 생긴 트리의 종류 (parent 의 종류 or 가짓수 = set의 크기) - 1만큼
 *       union을 해줘야 비로소 전체 뉴런을 시냅스로 모두 연결한 트리가 완성되므로 최종 결과에 위의 추가 연결 작업을 더해주어야 하는 것이다.
 *
 * -----------------
 * Input 1
 * 4 2
 * 1 2
 * 3 4
 *
 * Output 1
 * 1
 * -----------------
 */
public class BOJ20955 {

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

        int result = 0;
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            if(dSet.compareParent(u, v)) {
                result++;
            } else {
                dSet.union(u, v);
            }
        }

        Set<Integer> set = new HashSet<>();
        for(int i = 1; i < N + 1; i++) {
            set.add(dSet.find(i));
        }
        result += set.size() - 1;

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}