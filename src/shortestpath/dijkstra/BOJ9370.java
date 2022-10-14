package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 미확인 도착지 - BOJ9370
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 2
 * 5 4 2
 * 1 2 3
 * 1 2 6
 * 2 3 2
 * 3 4 4
 * 3 5 3
 * 5
 * 4
 * 6 9 2
 * 2 3 1
 * 1 2 1
 * 1 3 3
 * 2 4 4
 * 2 5 5
 * 3 4 3
 * 3 6 2
 * 4 5 4
 * 4 6 3
 * 5 6 7
 * 5
 * 6
 *
 * Output 1
 * 4 5
 * 6
 * -----------------
 */
public class BOJ9370 {

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

    static int T;
    static ArrayList<ArrayList<Node>> graph;
    static StringBuilder result = new StringBuilder();

    public static long[] dijkstra(int depart, int size) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        long[] dist = new long[size + 1];

        pq.add(new Node(depart, 0));
        Arrays.fill(dist, Integer.MAX_VALUE);
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

        return dist;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());

            graph = new ArrayList<>();
            for (int j = 0; j < n + 1; j++) {
                graph.add(new ArrayList<>());
            }

            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int g = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());

            int path = 0;
            for (int j = 0; j < m; j++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                if ((a == g && b == h) || (a == h && b == g)) path = d;
                graph.get(a).add(new Node(b, d));
                graph.get(b).add(new Node(a, d));
            }

            List<Integer> idxes = new ArrayList<>();
            for (int j = 0; j < t; j++) {
                idxes.add(Integer.parseInt(br.readLine()));
            }
            idxes.sort(Comparator.comparingInt(Integer::intValue));

            long[] arr = dijkstra(s, n);
            long distG = arr[g];     // begin -> g
            long distH = arr[h];     // begin -> h

            long[] arrG = dijkstra(g, n);   // g -> destination
            long[] arrH = dijkstra(h, n);   // h -> destination

            for (int j = 1; j < n + 1; j++) {
                arrG[j] += path + distH;
                arrH[j] += path + distG;
            }

            for (int idx : idxes) {
                long min = Math.min(arrG[idx], arrH[idx]);
                if (arr[idx] == min) {
                    result.append(idx).append(' ');
                }
            }
            result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
