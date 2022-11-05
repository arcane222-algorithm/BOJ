package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 최단경로 - BOJ1753
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 5 6
 * 1
 * 5 1 1
 * 1 2 2
 * 1 3 3
 * 2 3 4
 * 2 4 5
 * 3 4 6
 *
 * Output 1
 * 0
 * 2
 * 3
 * 7
 * INF
 * -----------------
 */
public class BOJ1753 {

    private static class Edge implements Comparable<Edge> {
        int dest, w;    // destination, weight

        public Edge(int dest, int w) {
            this.dest = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(w, e.w);
        }
    }

    static int V, E, K;
    static ArrayList<ArrayList<Edge>> graph;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(br.readLine());

        graph = new ArrayList<>();
        for (int i = 0; i < V + 1; i++) {   // Vertex number 1 ~ V, 0 is dummy ArrayList
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            graph.get(u).add(new Edge(v, w));
        }

        // distance array
        int[] dist = new int[V + 1];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        // init
        Arrays.fill(dist, Integer.MAX_VALUE);   // set infinity
        pq.offer(new Edge(K, 0));
        dist[K] = 0;

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();

            if (dist[curr.dest] < curr.w) continue;

            for (Edge edge : graph.get(curr.dest)) {
                if (dist[edge.dest] > curr.w + edge.w) {
                    dist[edge.dest] = curr.w + edge.w;
                    pq.offer(new Edge(edge.dest, dist[edge.dest]));
                }
            }
        }

        for (int i = 1; i < V + 1; i++) {
            if (dist[i] == Integer.MAX_VALUE) result.append("INF");
            else result.append(dist[i]);

            if (i < V) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
