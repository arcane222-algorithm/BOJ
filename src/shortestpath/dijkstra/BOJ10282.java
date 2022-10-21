package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 해킹 - BOJ10282
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 2
 * 3 2 2
 * 2 1 5
 * 3 2 5
 * 3 3 1
 * 2 1 2
 * 3 1 8
 * 3 2 4
 *
 * Output 1
 * 2 5
 * 3 6
 * -----------------
 */
public class BOJ10282 {

    private static class Node implements Comparable<Node> {
        int dest, w;

        public Node(int dest, int w) {
            this.dest = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Node e) {
            return Integer.compare(w, e.w);
        }
    }


    static int T, N, D, C;
    static List<List<Node>> graph;
    static StringBuilder result = new StringBuilder();

    public static void dijkstra(int depart) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(depart, 0));

        int idx = 0;
        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.dest] < curr.w) continue;

            for (Node adj : graph.get(curr.dest)) {
                if (dist[adj.dest] > curr.w + adj.w) {
                    dist[adj.dest] = curr.w + adj.w;
                    pq.add(new Node(adj.dest, dist[adj.dest]));
                    idx = adj.dest;
                }
            }
        }

        int count = 0;
        int last = 0;
        for(int i = 1; i < N + 1; i++) {
            if(dist[i] != Integer.MAX_VALUE) {
                count++;
                last = Math.max(last, dist[i]);
            }
        }

        result.append(count).append(' ').append(last).append('\n');
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            D = Integer.parseInt(st.nextToken());
            C = Integer.parseInt(st.nextToken());

            graph = new ArrayList<>();
            for (int j = 0; j < N + 1; j++) {
                graph.add(new ArrayList<>());
            }

            for (int j = 0; j < D; j++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                int s = Integer.parseInt(st.nextToken());
                graph.get(b).add(new Node(a, s));
            }

            dijkstra(C);
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}