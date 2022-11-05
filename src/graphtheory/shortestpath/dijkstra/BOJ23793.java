package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 두 단계 최단 경로 1 - BOJ23793
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 6 8
 * 1 2 2
 * 1 3 3
 * 1 5 10
 * 2 4 3
 * 3 6 5
 * 4 1 4
 * 4 6 4
 * 5 6 1
 * 1 2 6
 *
 * Output 1
 * 9 8
 * -----------------
 * Input 2
 * 6 8
 * 1 2 2
 * 1 3 3
 * 1 5 10
 * 2 4 3
 * 3 6 5
 * 4 1 4
 * 4 6 4
 * 5 6 1
 * 1 2 4
 *
 * Output 2
 * 5 -1
 * -----------------
 */
public class BOJ23793 {

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
    static int X, Y, Z;
    static List<List<Node>> graph;
    static StringBuilder result = new StringBuilder();

    public static int dijkstra(int depart, int dest, boolean passMidPoint) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(depart, 0));

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.dest] < curr.w) continue;

            for (Node adj : graph.get(curr.dest)) {
                if (passMidPoint && adj.dest == Y) continue;

                if (dist[adj.dest] > curr.w + adj.w) {
                    dist[adj.dest] = curr.w + adj.w;
                    pq.add(new Node(adj.dest, dist[adj.dest]));
                }
            }
        }

        return dist[dest] == Integer.MAX_VALUE ? -1 : dist[dest];
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
        }

        st = new StringTokenizer(br.readLine());
        X = Integer.parseInt(st.nextToken());
        Y = Integer.parseInt(st.nextToken());
        Z = Integer.parseInt(st.nextToken());

        int xToY = dijkstra(X, Y, false);
        int yToZ = dijkstra(Y, Z, false);
        int xToZ = dijkstra(X, Z, true);

        result.append(xToY == -1 || yToZ == -1 ? -1 : xToY + yToZ);
        result.append(' ');
        result.append(xToZ);
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
