package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 편의점 - BOJ14221
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 6 9
 * 1 4 1
 * 1 5 2
 * 1 6 3
 * 2 4 2
 * 2 5 3
 * 2 6 1
 * 3 4 3
 * 3 5 1
 * 3 6 2
 * 3 3
 * 1 2 3
 * 4 5 6
 *
 * Output 1
 * 1
 * -----------------
 */
public class BOJ14221 {

    private static class Node implements Comparable<Node> {
        int idx, w;

        public Node(int dest, int w) {
            this.idx = dest;
            this.w = w;
        }

        @Override
        public int compareTo(Node e) {
            return Integer.compare(w, e.w);
        }
    }

    static int n, m, p, q;
    static int minIdx, minDist;
    static List<List<Node>> graph = new ArrayList<>();
    static Set<Integer> set = new HashSet<>();
    static PriorityQueue<Node> pq = new PriorityQueue<>();
    static List<Integer> departures = new ArrayList<>();

    public static void dijkstra(List<Integer> depart) {
        int[] dist = new int[n + 1];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Arrays.fill(dist, Integer.MAX_VALUE);

        for (int i : depart) {
            dist[i] = 0;
            pq.offer(new Node(i, 0));
        }

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (dist[curr.idx] < curr.w) continue;

            if (set.contains(curr.idx) && dist[curr.idx] != 0) {
                if (dist[curr.idx] < minDist) {
                    minIdx = curr.idx;
                    minDist = dist[curr.idx];
                } else if (dist[curr.idx] == minDist) {
                    if (curr.idx < minIdx) {
                        minIdx = curr.idx;
                    }
                }
            }

            for (Node adj : graph.get(curr.idx)) {
                if (dist[adj.idx] > curr.w + adj.w) {
                    dist[adj.idx] = curr.w + adj.w;
                    pq.offer(new Node(adj.idx, dist[adj.idx]));
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        for (int i = 0; i <= n; i++) {
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
        p = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < p; i++) {
            set.add(Integer.parseInt(st.nextToken()));
        }

        st = new StringTokenizer(br.readLine());
        minIdx = Integer.MAX_VALUE;
        minDist = Integer.MAX_VALUE;
        for (int i = 0; i < q; i++) {
            int depart = Integer.parseInt(st.nextToken());
            departures.add(depart);
        }

        dijkstra(departures);
        bw.write(String.valueOf(minIdx));

        // close the buffer
        br.close();
        bw.close();
    }
}
