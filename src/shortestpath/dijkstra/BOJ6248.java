package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * Bronze Cow Party - BOJ6248
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 4 8 2
 * 1 2 7
 * 1 3 8
 * 1 4 4
 * 2 1 3
 * 2 3 1
 * 3 1 2
 * 3 4 6
 * 4 2 2
 *
 * Output 1
 * 6
 * -----------------
 */
public class BOJ6248 {

    private static class Node implements Comparable<Node> {
        int idx, w;

        public Node(int idx, int w) {
            this.idx = idx;
            this.w = w;
        }

        @Override
        public int compareTo(Node n) {
            return Integer.compare(w, n.w);
        }
    }

    public static int[] dijkstra(int depart) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(depart, 0));

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.idx] < curr.w) continue;

            for (Node adj : graph.get(curr.idx)) {
                if (dist[adj.idx] > curr.w + adj.w) {
                    dist[adj.idx] = curr.w + adj.w;
                    pq.add(new Node(adj.idx, dist[adj.idx]));
                }
            }
        }

        return dist;
    }

    static int N, M, X;
    static List<List<Node>> graph;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        X = Integer.parseInt(st.nextToken());

        graph = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int ai = Integer.parseInt(st.nextToken());
            int bi = Integer.parseInt(st.nextToken());
            int ti = Integer.parseInt(st.nextToken());

            graph.get(ai).add(new Node(bi, ti));
            graph.get(bi).add(new Node(ai, ti));
        }

        int[] dist = dijkstra(X);
        int max = 0;
        for(int i = 1; i <= N; i++) {
            max = Math.max(max, dist[i]);
        }
        bw.write(String.valueOf(max * 2));

        // close the buffer
        br.close();
        bw.close();
    }
}