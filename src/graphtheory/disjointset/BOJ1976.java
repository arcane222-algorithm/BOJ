package graphtheory.disjointset;

import java.io.*;
import java.util.*;


/**
 * 여행 가자 - BOJ1976
 * -----------------
 * -----------------
 * Input 1
 * 3
 * 3
 * 0 1 0
 * 1 0 1
 * 0 1 0
 * 1 2 3
 *
 * Output 1
 * YES
 * -----------------
 */
public class BOJ1976 {


    private static class DisjointSet {
        private int[] parents;
        private int[] ranks;
        private int[] nodeCounts;

        public DisjointSet(int n) {
            parents = new int[n + 1];   // 0 ~ n
            ranks = new int[n + 1];
            nodeCounts = new int[n + 1];
            for(int i = 0; i < n + 1; i++) {
                parents[i] = i;
                nodeCounts[i] = 1;
            }
        }

        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
        }

        public int getNodeCount(int a) {
            return nodeCounts[a];
        }

        public void union(int a, int b) {
            a = find(a);
            b = find(b);

            if(a == b) return;  // same root

            // rank compression (use depth)
            if(ranks[a] < ranks[b]) {
                //swap
                int temp = a;
                a = b;
                b = temp;
            }
            parents[b] = a;
            nodeCounts[a] += nodeCounts[b];
            nodeCounts[b] = 1;

            if(ranks[a] == ranks[b]) {
                ranks[a]++;
            }
        }

        public int find(int a) {
            if(a == parents[a]) return a;
            else return parents[a] = find(parents[a]);  // path compression
        }

        public void getCounts() {
            System.out.println(Arrays.toString(nodeCounts));
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

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        M = Integer.parseInt(br.readLine());

        DisjointSet dSet = new DisjointSet(N);
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                if(st.nextToken().equals("1")) {
                    dSet.union(i + 1, j + 1);
                }
            }
        }

        st = new StringTokenizer(br.readLine());
        int[] plan = new int[M];
        for(int i = 0; i < M; i++) {
            plan[i] = Integer.parseInt(st.nextToken());
        }

        String result = "YES";
        for(int i = 0; i < M - 1; i++) {
            int a = plan[i];
            int b = plan[i + 1];
            if(!dSet.compareParent(a, b)) {
                result = "NO";
                break;
            }
        }

        // Write the result
        bw.write(result);
        bw.flush();

        // Close the I/O stream
        br.close();
        bw.close();
    }
}
