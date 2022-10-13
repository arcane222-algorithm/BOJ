package shortestpath.dijkstra;

import java.io.*;
import java.util.*;



public class Dijkstra {

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

    /**
     * Shortest path algorithm
     * Dijkstra v1
     * Used visited, distance array
     * Time-complexity: O(V^2)
     */
    private static class V1 {
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

            // distance, visited array
            int[] dist = new int[V + 1];
            boolean[] visited = new boolean[V + 1];

            // init
            Arrays.fill(dist, Integer.MAX_VALUE);   // set infinity
            dist[K] = 0;

            for (int i = 1; i < V + 1; i++) {
                int currIdx = 1;
                int currDist = Integer.MAX_VALUE;

                // 1 ~ V 에서 방문하지 않은 노드 중 거리의 최솟값을 찾는다
                for (int j = 1; j < V + 1; j++) {
                    if (visited[j]) continue;
                    if (dist[j] < currDist) {
                        currIdx = j;
                        currDist = dist[j];
                    }
                }
                visited[currIdx] = true;

                for (Edge edge : graph.get(currIdx)) {
                    if (dist[edge.dest] > dist[currIdx] + edge.w) {
                        dist[edge.dest] = dist[currIdx] + edge.w;
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

    /**
     * Shortest path algorithm
     * Dijkstra v2
     * Used distance array,
     * Time-complexity: O((V+E)logV)
     */
    private static class V2 {
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
}