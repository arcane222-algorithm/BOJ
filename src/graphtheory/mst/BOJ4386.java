package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 별자리 만들기 - BOJ4386
 * -----------------
 * Input 1
 * 3
 * 1.0 1.0
 * 2.0 2.0
 * 2.0 4.0
 *
 * Output 1
 * 3.41
 * -----------------
 */
public class BOJ4386 {

    private static class DisjointSet {
        int[] parents;

        public DisjointSet(int n) {
            parents = new int[n];
            for(int i = 0; i < n; i++) {
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

    private static class Star {
        int idx;
        double x, y;

        public Star(int idx, double x, double y) {
            this.idx = idx;
            this.x = x;
            this.y = y;
        }
    }

    private static class Edge implements Comparable<Edge> {
        Star s1, s2;
        double cost;

        public Edge(Star s1, Star s2) {
            this.s1 = s1;
            this.s2 = s2;
            this.cost = Math.sqrt((s1.x - s2.x) * (s1.x - s2.x) + (s1.y - s2.y) * (s1.y - s2.y));
        }

        @Override
        public int compareTo(Edge e) {
            return Double.compare(cost, e.cost);
        }
    }


    public static int N;
    public static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        Star[] stars = new Star[N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            stars[i] = new Star(i, Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
        }

        DisjointSet dSet = new DisjointSet(N);
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for(int i = 0; i < N - 1; i++) {
            for(int j = i + 1; j < N; j++) {
                Edge e = new Edge(stars[i], stars[j]);
                pq.add(e);
            }
        }

        double ans = 0;
        while(!pq.isEmpty()) {
            Edge e = pq.poll();
            if(!dSet.compareParent(e.s1.idx, e.s2.idx)) {
                ans += e.cost;
                dSet.union(e.s1.idx, e.s2.idx);
            }
        }

        // write the result
        bw.write(String.format("%.2f", ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
