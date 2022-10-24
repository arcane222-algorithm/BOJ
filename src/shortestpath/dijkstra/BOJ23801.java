package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 두 단계 최단 경로 2 - BOJ23801
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 13 19
 * 1 2 100
 * 1 3 100
 * 1 4 1
 * 2 5 1
 * 3 6 1
 * 3 4 1
 * 4 6 1
 * 4 7 1
 * 5 6 10
 * 5 8 10
 * 6 9 10
 * 7 10 1
 * 8 9 10
 * 8 11 1
 * 9 11 1
 * 9 12 1
 * 10 12 2
 * 11 13 1
 * 12 13 3
 * 1 13
 * 3
 * 8 9 10
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 13 19
 * 1 2 1
 * 1 3 2
 * 1 4 10
 * 2 5 1
 * 3 6 10
 * 3 4 10
 * 4 6 1
 * 4 7 1
 * 5 6 10
 * 5 8 1
 * 6 9 1
 * 7 10 1
 * 8 9 1
 * 8 11 100
 * 9 11 100
 * 9 12 100
 * 10 12 1
 * 11 13 100
 * 12 13 1
 * 1 13
 * 3
 * 8 9 10
 *
 * Output 2
 * 10
 * -----------------
 * Input 3
 * 4 3
 * 1 2 5
 * 2 3 6
 * 3 4 7
 * 1 3
 * 1
 * 4
 *
 * Output 3
 * 25
 * -----------------
 */
public class BOJ23801 {

    private static class Node implements Comparable<Node> {
        int dest;
        long w;

        public Node(int dest, long w) {
            this.dest = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Node n) {
            return Long.compare(w, n.w);
        }
    }

    static int N, M;
    static int X, Y, Z, P;
    static List<List<Node>> graph;
    static StringBuilder result = new StringBuilder();

    public static long[] dijkstra(int depart) {
        long[] dist = new long[N + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[depart] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(depart, 0));

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

        return dist;
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
        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            graph.get(u).add(new Node(v, w));
            graph.get(v).add(new Node(u, w));
        }

        st = new StringTokenizer(br.readLine());
        X = Integer.parseInt(st.nextToken());
        Z = Integer.parseInt(st.nextToken());
        long[] xTo = dijkstra(X);
        long[] zTo = dijkstra(Z);

        P = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        long minDist = Long.MAX_VALUE;
        for (int i = 0; i < P; i++) {
            Y = Integer.parseInt(st.nextToken());
            long xToY = xTo[Y];
            long zToY = zTo[Y];

            if (xToY == Long.MAX_VALUE || zToY == Long.MAX_VALUE) continue;
            minDist = Math.min(minDist, xToY + zToY);
        }
        bw.write(String.valueOf(minDist == Long.MAX_VALUE ? -1 : minDist));

        // close the buffer
        br.close();
        bw.close();
    }
}