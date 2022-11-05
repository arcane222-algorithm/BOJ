package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 전기가 부족해 - BOJ10423
 * -----------------
 * Input 1
 * 9 14 3
 * 1 2 9
 * 1 3 3
 * 1 4 8
 * 2 4 10
 * 3 4 11
 * 3 5 6
 * 4 5 4
 * 4 6 10
 * 5 6 5
 * 5 7 4
 * 6 7 7
 * 6 8 4
 * 7 8 5
 * 7 9 2
 * 8 9 5
 *
 * Output 1
 * 22
 * -----------------
 * Input 2
 * 4 5 1
 * 1
 * 1 2 5
 * 1 3 5
 * 1 4 5
 * 2 3 10
 * 3 4 10
 *
 * Output 2
 * 15
 * -----------------
 * Input 3
 * 10 9 5
 * 1 4 6 9 10
 * 1 2 3
 * 2 3 8
 * 3 4 5
 * 4 5 1
 * 5 6 2
 * 6 7 6
 * 7 8 3
 * 8 9 4
 * 9 10 1
 *
 * Output 3
 * 16
 * -----------------
 */
public class BOJ10423 {

    private static class DisjointSet {
        int[] parents;

        public DisjointSet(int n) {
            parents = new int[n + 1];
            for(int i = 1; i < n + 1; i++) {
                parents[i] = i;
            }
        }

        public int find(int a) {
            if(a == parents[a])
                return a;
            else
                return parents[a] = find(parents[a]);
        }

        public void union(int a, int b) {
            a = find(a);
            b = find(b);

            parents[b] = a;
        }

        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
        }
    }

    private static class Edge implements Comparable<Edge> {
        int v1, v2, cost;
        public Edge(int v1, int v2, int cost) {
            this.v1 = v1;
            this.v2 = v2;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(cost ,e.cost);
        }
    }

    public static int N, M, K;
    public static StringBuilder result = new StringBuilder();

    public static boolean allConnected(int[] parents) {
        for(int i = 0; i < parents.length; i++) {
            if(parents[i] != -1)
                return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // N, M, K
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        DisjointSet dSet = new DisjointSet(N);
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        // Init energy plants
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < K; i++) {
            dSet.parents[Integer.parseInt(st.nextToken())] = 0;
        }

        // Init cities
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            pq.add(new Edge(u, v, w));
        }

        int ans = 0;
        while(!pq.isEmpty()) {
            Edge e = pq.poll();

            if(!dSet.compareParent(e.v1, e.v2)) {
                ans += e.cost;
                dSet.union(e.v1, e.v2);
            }

            if(allConnected(dSet.parents)) {
                break;
            }
        }

        // write the result
        bw.write(String.valueOf(ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}