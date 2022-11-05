package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 택배 배송 - BOJ5972
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 6 8
 * 4 5 3
 * 2 4 0
 * 4 1 4
 * 2 1 1
 * 5 6 1
 * 3 6 2
 * 3 2 6
 * 3 4 4
 *
 * Output 1
 * 5
 * -----------------
 */
public class BOJ5972 {

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

    static int N, M;
    static List<List<Node>> graph;

    public static int dijkstra(int depart, int dest) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 1;

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
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        graph = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            graph.get(a).add(new Node(b, c));
            graph.get(b).add(new Node(a, c));
        }
        bw.write(String.valueOf(dijkstra(1, N)));

        // close the buffer
        br.close();
        bw.close();
    }
}
