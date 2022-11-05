package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 간선 이어가기 2 - BOJ14284
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 8 9
 * 1 2 3
 * 1 3 2
 * 1 4 4
 * 2 5 2
 * 3 6 1
 * 4 7 3
 * 5 8 6
 * 6 8 2
 * 7 8 7
 * 1 8
 *
 * Output 1
 * 5
 * -----------------
 */
public class BOJ14284 {

    private static class Node implements Comparable<Node> {
        int dest, w;

        public Node(int dest, int w) {
            this.dest = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Node n) {
            return Integer.compare(w, n.w);
        }
    }

    static int n, m, s, t;
    static List<List<Node>> graph;

    public static int dijkstra(int depart, int dest) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(depart, 0));

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
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        graph = new ArrayList<>();
        for (int i = 0; i < n + 1; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            graph.get(a).add(new Node(b, c));
            graph.get(b).add(new Node(a, c));
        }

        st = new StringTokenizer(br.readLine());
        s = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        int result = dijkstra(s, t);
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}