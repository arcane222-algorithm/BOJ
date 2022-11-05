package graphtheory.disjointset;

import java.io.*;
import java.util.*;

/**
 * 교수님은 기다리지 않는다 - BOJ 3830
 * -----------------
 * Input 1
 * 2 2
 * ! 1 2 1
 * ? 1 2
 * 2 2
 * ! 1 2 1
 * ? 2 1
 * 4 7
 * ! 1 2 100
 * ? 2 3
 * ! 2 3 100
 * ? 2 3
 * ? 1 3
 * ! 4 3 150
 * ? 4 1
 * 0 0
 *
 * Output 1
 * 1
 * -1
 * UNKNOWN
 * 100
 * 200
 * -50
 * -----------------
 */
public class BOJ3830 {

    private static class DisjointSet {
        int[] parents;
        long[] weights;

        public DisjointSet(int n) {
            parents = new int[n + 1];   // 0 ~ n
            weights = new long[n + 1];

            for(int i = 1; i < n + 1; i++) {
                parents[i] = i;
            }
        }

        public long getWeight(int a) {
            return weights[a];
        }

        public void union(int a, int b, int w) {
            int aRoot = find(a);
            int bRoot = find(b);

            // same root
            if(aRoot == bRoot) return;

            parents[bRoot] = aRoot;
            weights[bRoot] = weights[a] - weights[b] + w;
        }

        public int find(int a) {
            if(a == parents[a]) {
                return a;
            } else {
                // path compression
                int parent = find(parents[a]);
                weights[a] += weights[parents[a]];
                return parents[a] = parent;
            }
        }

        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
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
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String line = null;
        DisjointSet dSet = null;
        while(!(line = br.readLine()).equals("0 0")) {
            st = new StringTokenizer(line);
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            dSet = new DisjointSet(N);

            for(int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());

                String type = st.nextToken();
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                int gap;

                switch(type) {
                    case "!":
                        gap = Integer.parseInt(st.nextToken());
                        dSet.union(a, b, gap);
                        break;

                    case "?":
                        if(!dSet.compareParent(a, b)) {
                            result.append("UNKNOWN");
                        } else {
                            result.append(dSet.getWeight(b) - dSet.getWeight(a));
                        }
                        result.append('\n');
                        break;
                }
            }
        }
        // Write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());
        bw.flush();

        // Close the I/O stream
        br.close();
        bw.close();
    }
}
