package graphtheory.mst;

import java.io.*;
import java.util.*;


/**
 * 행성 터널 - BOJ2887
 * -----------------
 * Input 1
 * 5
 * 11 -15 -15
 * 14 -5 -15
 * -1 -1 -5
 * 10 -4 -1
 * 19 -4 19
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ2887 {
    private static class DisjointSet {
        int[] parents;
        int[] ranks;

        public DisjointSet(int n) {
            parents = new int[n];   // 0 ~ n
            ranks = new int[n];
            for(int i = 0; i < n; i++) {
                parents[i] = i;
                ranks[i] = 1;
            }
        }

        public int find(int a) {
            if(parents[a] == a) return a;
            else return parents[a] = find(parents[a]);   // path compression
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

    private static class Vertex {
        int idx;
        int x, y, z;

        public Vertex(int idx, int x, int y, int z) {
            this.idx = idx;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class Edge implements Comparable<Edge> {
        Vertex v1, v2;
        int cost;

        public Edge(Vertex v1, Vertex v2, int cost) {
            this.v1 = v1;
            this.v2 = v2;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(cost, e.cost);
        }
    }

    static int N;

    public static int abs(int a)  {
        return a < 0 ? -a : a;
    }

    public static PriorityQueue<Edge> findEdges(Vertex[] vertices) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        Arrays.sort(vertices, Comparator.comparing(v -> v.x));
        for(int i = 0; i < vertices.length - 1; i++) {
            pq.add(new Edge(vertices[i], vertices[i + 1], abs(vertices[i].x - vertices[i + 1].x)));
        }

        Arrays.sort(vertices, Comparator.comparing(v -> v.y));
        for(int i = 0; i < vertices.length - 1; i++) {
            pq.add(new Edge(vertices[i], vertices[i + 1], abs(vertices[i].y - vertices[i + 1].y)));
        }

        Arrays.sort(vertices, Comparator.comparing(v -> v.z));
        for(int i = 0; i < vertices.length - 1; i++) {
            pq.add(new Edge(vertices[i], vertices[i + 1], abs(vertices[i].z - vertices[i + 1].z)));
        }

        return pq;
    }

    public static long kruskal(int n, PriorityQueue<Edge> edges) {
        long weight = 0, cnt = 0;
        DisjointSet dSet = new DisjointSet(n);

        while(!edges.isEmpty()) {
            if(cnt == n - 1) break;
            Edge e = edges.poll();
            if(!dSet.compareParent(e.v1.idx, e.v2.idx)) {
                weight += e.cost;
                dSet.union(e.v1.idx, e.v2.idx);
                cnt++;
            }
        }

        return weight;
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());   // count of planet (vertex)
        Vertex[] vertices = new Vertex[N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            vertices[i] = new Vertex(i, x, y, z);
        }
        PriorityQueue<Edge> pq = findEdges(vertices);

        // Write the result
        bw.write(String.valueOf(kruskal(N, pq)));
        bw.flush();

        // Close the I/O stream
        br.close();
        bw.close();
    }
}