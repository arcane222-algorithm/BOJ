package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 최소 스패닝 트리 - BOJ1197
 * -----------------
 * -----------------
 * Input 1
 * 3 3
 * 1 2 1
 * 2 3 2
 * 1 3 3
 *
 * Output 1
 * 3
 * -----------------
 */
public class BOJ1197 {

    private static class DisjointSet {
        int[] parents;
        int[] ranks;

        public DisjointSet(int n) {
            parents = new int[n + 1];   // 0 ~ n
            ranks = new int[n + 1];
            for(int i = 1; i < n + 1; i++) {
                parents[i] = i;
                ranks[i] = 1;
            }
        }

        public int find(int a) {
            if(parents[a] == a)
                return a;
            else
                return parents[a] = find(parents[a]);   // path compression
        }

        public void union(int a, int b) {
            a = find(a);
            b = find(b);

            if(a == b) return;  // same parent

            if(ranks[a] < ranks[b]) {
                int tmp = a;
                a = b;
                b = tmp;    // swap, rank compression
            }
            ranks[a] += ranks[b];
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
            return Integer.compare(cost, e.cost);
        }
    }

    static int V, E;
    static StringBuilder result = new StringBuilder();

    public static int kruskal(int n, List<Edge> edges) {
        int weight = 0;
        DisjointSet dSet = new DisjointSet(n);
        for(Edge e : edges) {
            if(!dSet.compareParent(e.v1, e.v2)) {
                weight += e.cost;
                dSet.union(e.v1, e.v2);
            }
        }

        return weight;
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        List<Edge> edges = new ArrayList<>();
        for(int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(st.nextToken());
            int v2 = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            edges.add(new Edge(v1, v2, cost));
        }
        Collections.sort(edges);

        // Write the result
        bw.write(String.valueOf(kruskal(V, edges)));
        bw.flush();

        // Close the I/O stream
        br.close();
        bw.close();
    }
}