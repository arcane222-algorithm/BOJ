package disjointset;

import java.io.*;
import java.util.*;


/**
 * 거짓말 - BOJ 1043
 * -----------------
 * Input 1
 * 4 3
 * 0
 * 2 1 2
 * 1 3
 * 3 2 3 4
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 8 5
 * 3 1 2 7
 * 2 3 4
 * 1 5
 * 2 5 6
 * 2 6 8
 * 1 8
 *
 * Output 2
 * 5
 * -----------------
 * Input 3
 * 3 4
 * 1 3
 * 1 1
 * 1 2
 * 2 1 2
 * 3 1 2 3
 *
 * Output 3
 * 0
 * -----------------
 */
public class BOJ1043 {

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
         * @param n Size of DisjointSet
         */
        public DisjointSet(int n) {
            parents = new int[n];   // 0 ~ n - 1
            ranks = new int[n];
            nodeCounts = new int[n];
            for(int i = 0; i < n; i++) {
                parents[i] = i;
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

            // same root
            if(aRoot == bRoot) return;

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

    static int N, M, T;
    static int[] knowsTheTruth;
    static List<Integer>[] partyMembers;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N, M
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // parse T, knows the truth
        DisjointSet dSet = new DisjointSet(N + 1);
        st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());
        if(T > 0) knowsTheTruth = new int[T];
        for(int i = 0; i < T; i++) {
            knowsTheTruth[i] = Integer.parseInt(st.nextToken());
        }

        // parse party members
        partyMembers = new List[M];
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            final int MEMBER_CNT = Integer.parseInt(st.nextToken());
            partyMembers[i] = new ArrayList<>();

            for(int j = 0; j < MEMBER_CNT; j++) {
                partyMembers[i].add(Integer.parseInt(st.nextToken()));
            }
        }

        // Union the party members
        for(int i = 0; i < M; i++) {
            for(int j = 0; j < partyMembers[i].size() - 1; j++) {
                dSet.union(partyMembers[i].get(j), partyMembers[i].get(j + 1));
            }
        }

        // Union the truth members
        for(int i = 0; i < T; i++) {
            int truthRoot = dSet.find2(knowsTheTruth[i]);
            dSet.union(0, truthRoot);
        }

        // write the answer
        int answer = 0;
        for(int i = 0; i < M; i++) {
            boolean isTruthParty = false;
            for(int j = 0; j < partyMembers[i].size(); j++) {
                int member = partyMembers[i].get(j);
                if(dSet.find2(member) == 0) {
                    isTruthParty = true;
                    break;
                }
            }

            if(!isTruthParty) answer++;
        }
        bw.write(String.valueOf(answer));

        // close the buffer
        br.close();
        bw.close();
    }
}
