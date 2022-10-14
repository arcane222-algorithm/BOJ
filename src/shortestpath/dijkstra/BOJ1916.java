package shortestpath.dijkstra;

import java.io.*;
import java.util.*;



/**
 * 최소비용 구하기 - BOJ1916
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 5
 * 8
 * 1 2 2
 * 1 3 3
 * 1 4 1
 * 1 5 10
 * 2 4 2
 * 3 4 1
 * 3 5 1
 * 4 5 3
 * 1 5
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ1916 {

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
    static ArrayList<ArrayList<Node>> graph;

    public static int dijkstra(int depart, int dest) {
        int[] dist = new int[N + 1];
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

        N = Integer.parseInt(br.readLine());
        M = Integer.parseInt(br.readLine());

        graph = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
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
        int depart = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());
        int dist = dijkstra(depart, dest);

        bw.write(String.valueOf(dist));

        // close the buffer
        br.close();
        bw.close();
    }
}