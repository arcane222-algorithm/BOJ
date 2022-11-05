package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * CTP 왕국은 한솔 왕국을 이길 수 있을까? - BOJ15789
 * -----------------
 *
 * Disjoint-Set 자료구조에서 두 node를 union 할 때, 각 node의 nodeCount를 한쪽으로 더해주는 작업을 잘 처리하면
 * 쉽게 해결이 가능하다.
 * 두 왕국의 관계가 주어지므로, ctp와 한솔 왕국과 동맹을 한 왕국이 있을 것이고,
 * 각 왕국의 parent를 조사하여 ctp와 한솔 왕국과 동맹이 아닌 (parent값이 다른) 왕국들을 찾아
 * nodeCount 기준으로 내림차순 정렬 후 K의 값만큼 ctp 왕국에 union 해주면
 * ctp 왕국의 nodeCount값이 정답이 된다.
 *
 * -----------------
 * Input 1
 * 10 7
 * 1 2
 * 1 3
 * 2 3
 * 1 4
 * 5 6
 * 8 10
 * 7 9
 * 5 9 1
 *
 * Output 1
 * 6
 * -----------------
 * Input 2
 * 10 7
 * 1 2
 * 1 3
 * 2 3
 * 1 4
 * 5 6
 * 8 10
 * 7 9
 * 5 1 1
 *
 * Output 2
 * 4
 * -----------------
 */
public class BOJ15789 {

    private static class Node {
        int parent, nodeCount;

        public Node(int parent, int nodeCount) {
            this.parent = parent;
            this.nodeCount = nodeCount;
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

    static int N, M, X, Y;
    static int C, H, K;
    static DisjointSet dSet;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        dSet = new DisjointSet(N + 1);

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            X = Integer.parseInt(st.nextToken());
            Y = Integer.parseInt(st.nextToken());
            dSet.union(X, Y);
        }
        st = new StringTokenizer(br.readLine());
        C = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        int ctp = dSet.find2(C);
        int hansol = dSet.find2(H);

        HashSet<Integer> set = new HashSet<>();
        for (int i = 1; i < N + 1; i++) {
            int parent = dSet.find2(i);
            if (parent != ctp && parent != hansol) {
                set.add(parent);
            }
        }

        List<Node> list = new ArrayList<>();
        for (Iterator<Integer> it = set.iterator(); it.hasNext(); ) {
            int parent = it.next();
            list.add(new Node(parent, dSet.nodeCounts[parent]));
        }
        Collections.sort(list, (o1, o2) -> Integer.compare(o2.nodeCount, o1.nodeCount));

        for (int i = 0; i < K; i++) {
            dSet.union(ctp, list.get(i).parent);
        }

        int result = dSet.nodeCounts[ctp];
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}
