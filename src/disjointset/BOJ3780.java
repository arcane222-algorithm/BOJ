package disjointset;

import java.io.*;
import java.util.*;


/**
 * 네트워크 연결 - BOJ3780
 * -----------------
 *
 * 클러스터 A를 제공하는 센터 I와 클러스터 B를 제공하는 회사 j를 연결한다는 것은
 * 분리집합에서 집합 A의 parent node인 i를 집합 B에 속하는 parent이거나 아닌 node j에 연결하는 것이다.
 * 일반적으로 분리집합에서 union 연산 시 연결하려는 각 node의 parent를 구해 parent를 직접 연결하여 하나의 집합으로 만들지만
 * 이 문제는 연결 후 각 node의 parent 까지의 거리를 계산 하여야 하므로 parent 바로 연결하게 되면 문제가 발생한다.
 * 그러므로 union 연산 시 find 연산을 통한 parent를 구해 직접 연결하는 방식으로 처리하는 것이 아니라 그냥 해당 node에 바로 연결한다.
 *
 * union 연산 시 두 노드 i, j를 직접 연결하며 문제에 주어진 i와 j와의 거리인 |i - j| % 1000인 값을 i에 해당하는 위치에 저장한 후
 * E 명령을 처리할 때 find 연산을 수행하여 해당 node의 parent 값과 distance 값을 path compression 방식을 통해 함께 갱신하여 준다.
 * 재귀함수를 돌며 현재 자신의 distance 값에 부모의 distance 값 (distance[parents[i]])을 더해주는 식으로 구현하면
 * 결과적으로 마지막 부모의 값 부터 자기자신에 위치에 올때까지 재귀적으로 거리값이 누적되어 저장되게 된다.
 *
 * + 아래 코드 상에서는 distance 값을 ranks 배열을 통해 선언하였다.
 *
 * -----------------
 * Input 1
 * 1
 * 4
 * E 3
 * I 3 1
 * E 3
 * I 1 2
 * E 3
 * I 2 4
 * E 3
 * O
 *
 * Output 1
 * 0
 * 2
 * 3
 * 5
 * -----------------
 * Input 2
 * 1
 * 6
 * I 3 1
 * I 1 2
 * I 4 5
 * I 5 6
 * I 2 5
 * E 1
 * E 3
 * O
 *
 * Output 2
 * 5
 * 7
 * -----------------
 */
public class BOJ3780 {

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
        public void union(int a, int b, int dist) {
            // Size out of bounds
            if (a < 0 || a > parents.length - 1) return;
            if (b < 0 || b > parents.length - 1) return;

            parents[a] = b;
            ranks[a] = dist;
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
            if (a == parents[a]) {
                return a;
            } else {
                int parent = find(parents[a]);
                ranks[a] += ranks[parents[a]];
                return parents[a] = parent;  // path compression
            }
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

    static int T, N, I, J;
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
            dSet = new DisjointSet(N + 1);

            while (true) {
                st = new StringTokenizer(br.readLine());
                char inst = st.nextToken().charAt(0);
                if (inst == 'E') {
                    I = Integer.parseInt(st.nextToken());
                    dSet.find(I);
                    result.append(dSet.ranks[I]).append('\n');
                } else if (inst == 'I') {
                    I = Integer.parseInt(st.nextToken());
                    J = Integer.parseInt(st.nextToken());
                    dSet.union(I, J, Math.abs(I - J) % 1000);
                } else {
                    break;
                }
            }
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}