package disjointset;

import java.io.*;
import java.util.*;


/**
 * 전국시대 - BOJ15809
 * -----------------
 *
 * 두 국가간 동맹을 한다면 두 국가를 union 해주면서 각 국가의 ranks 값을 합쳐준다.
 * union 시 ranks가 큰쪽으로 union되도록 ranks 값을 비교하여 swap 작업을 해준다.
 * 두 국가가 전쟁을 한다면 각 국가의 parent의 ranks 값을 비교하여 같다면 두 국가 모두 멸망한 것이므로 0번째 node에 union 해준다.
 * 두 국가의 parent의 ranks 값이 다르다면 두 국가를 union 해주고, 해당 국가의 parent 값은 ranks 값의 차 만큼으로 설정해준다.
 * Set을 이용하여 parent 값이 0이 아닌 국가는 멸망하지 않은 국가 이므로 해당 국가의 수를 계산할 수 있고,
 * List를 이용하여 멸망하지 않은 국가의 ranks 값들을 읽어와 오름차순 정렬하여 출력한다.
 *
 * -----------------
 * Input 1
 * 5 3
 * 10
 * 20
 * 30
 * 40
 * 50
 * 1 1 2
 * 1 3 4
 * 2 1 3
 *
 * Output 1
 * 2
 * 40 50
 * -----------------
 * Input 2
 * 5 3
 * 30
 * 40
 * 30
 * 40
 * 50
 * 1 1 2
 * 1 3 4
 * 2 1 3
 *
 * Output 2
 * 1
 * 50
 * -----------------
 */
public class BOJ15809 {

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

            if (ranks[aRoot] > ranks[bRoot]) {
                int tmp = aRoot;
                aRoot = bRoot;
                bRoot = tmp;
            }

            parents[aRoot] = bRoot;
            nodeCounts[bRoot] += nodeCounts[aRoot];
            if (bRoot != 0) {
                ranks[bRoot] += ranks[aRoot];
            }
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        dSet = new DisjointSet(N + 1);
        for (int i = 1; i < N + 1; i++) {
            dSet.ranks[i] = Integer.parseInt(br.readLine());
        }
        dSet.ranks[0] = Integer.MAX_VALUE;

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int O = Integer.parseInt(st.nextToken());
            int P = Integer.parseInt(st.nextToken());
            int Q = Integer.parseInt(st.nextToken());
            if (O == 1) {
                dSet.union(P, Q);
            } else {
                int w1 = dSet.ranks[dSet.find(P)];
                int w2 = dSet.ranks[dSet.find(Q)];
                if (w1 == w2) {
                    dSet.union(0, P);
                    dSet.union(0, Q);
                } else {
                    dSet.union(P, Q);
                    dSet.ranks[dSet.find(P)] = Math.abs(w1 - w2);
                }
            }
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 1; i < N + 1; i++) {
            int parent = dSet.find(i);
            if (parent != 0) {
                set.add(dSet.find(i));
            }
        }

        List<Integer> list = new ArrayList<>();
        for (int i : set) {
            list.add(dSet.ranks[i]);
        }
        Collections.sort(list);

        result.append(set.size()).append('\n');
        for (int i : list) {
            result.append(i).append(' ');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}