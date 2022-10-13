package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 특정한 최단 경로 - BOJ1504
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 4 6
 * 1 2 3
 * 2 3 3
 * 3 4 1
 * 1 3 5
 * 2 4 5
 * 1 4 4
 * 2 3
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 4 5
 * 1 2 3
 * 1 3 1
 * 1 4 1
 * 2 3 3
 * 3 4 4
 * 2 3
 *
 * Output 2
 * 8
 * -----------------
 * Input 3
 * 5 6
 * 1 2 3
 * 1 3 1
 * 1 4 1
 * 2 3 3
 * 3 4 4
 * 4 5 1
 * 2 3
 *
 * Output 3
 * 9
 * -----------------
 */
public class BOJ1504 {

    private static class Node implements Comparable<Node> {
        int dest;
        long w;

        public Node(int dest, long w) {
            this.dest = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Node e) {
            return Long.compare(w, e.w);
        }
    }

    static int N, E;
    static int node1, node2;
    static ArrayList<ArrayList<Node>> graph;

    public static long dijkstra(int depart, int dest) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        long[] dist = new long[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        pq.offer(new Node(depart, 0));
        dist[depart] = 0;

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.dest] < curr.w) continue;

            for (Node adj : graph.get(curr.dest)) {
                if (dist[adj.dest] > curr.w + adj.w) {
                    dist[adj.dest] = curr.w + adj.w;
                    pq.add(new Node(adj.dest, dist[adj.dest]));
                }
            }
        }

        return dist[dest];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        graph = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            graph.get(a).add(new Node(b, c));
            graph.get(b).add(new Node(a, c));
        }

        st = new StringTokenizer(br.readLine());
        node1 = Integer.parseInt(st.nextToken());
        node2 = Integer.parseInt(st.nextToken());

        long case1 = dijkstra(1, node1) + dijkstra(node1, node2) + dijkstra(node2, N);
        long case2 = dijkstra(1, node2) + dijkstra(node2, node1) + dijkstra(node1, N);
        long result = case1 >= Integer.MAX_VALUE && case2 >= Integer.MAX_VALUE ? -1 : Math.min(case1, case2);

        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}
