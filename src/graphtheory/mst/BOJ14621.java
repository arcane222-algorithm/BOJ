package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 나만 안되는 연애 - BOJ14621
 * -----------------
 * Input 1
 * 5 7
 * M W W W M
 * 1 2 12
 * 1 3 10
 * 4 2 5
 * 5 2 5
 * 2 5 10
 * 3 4 3
 * 5 4 7
 *
 * Output 1
 * 34
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
public class BOJ14621 {

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
        int v1, v2;
        long cost;

        public Edge(int v1, int v2, long cost) {
            this.v1 = v1;
            this.v2 = v2;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge e) {
            return Long.compare(cost ,e.cost);
        }
    }

    public static int N, M;
    public static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int[] types = new int[N + 1];
        st = new StringTokenizer(br.readLine());
        for(int i = 1; i < N + 1; i++) {
            if(st.nextToken().charAt(0) == 'M')
                types[i] = 1;
            else
                types[i] = 0;
        }

        DisjointSet dSet = new DisjointSet(N);
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            pq.add(new Edge(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken())));
        }

        int ans = 0, cnt = 0;
        while(!pq.isEmpty()) {
            Edge e = pq.poll();

            if(!dSet.compareParent(e.v1, e.v2)) {
                if(types[e.v1] != types[e.v2]) {
                    dSet.union(e.v1, e.v2);
                    ans += e.cost;
                    cnt++;
                }
            }
        }

        // write the result
        bw.write(String.valueOf(cnt == N - 1 ? ans : -1));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
