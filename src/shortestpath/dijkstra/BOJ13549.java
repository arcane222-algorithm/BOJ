package shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 숨바꼭질 3 - BOJ13549
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 5 17
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 1 2
 *
 * Output 2
 * 0
 * -----------------
 */
public class BOJ13549 {

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

    static final int MAX = (int) 1e5 + 1;
    static int N, K;

    public static int dijkstra(int depart, int dest) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int[] dist = new int[MAX];

        pq.add(new Node(depart, 0));
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[depart] = 0;

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.dest] < curr.w) continue;

            int nxt = curr.dest + 1;
            if (nxt <= MAX - 1 && dist[nxt] > curr.w + 1) {
                dist[nxt] = curr.w + 1;
                pq.add(new Node(nxt, dist[nxt]));
            }

            nxt = curr.dest - 1;
            if (nxt >= 0 && dist[nxt] > curr.w + 1) {
                dist[nxt] = curr.w + 1;
                pq.add(new Node(nxt, dist[nxt]));
            }

            nxt = curr.dest * 2;
            if (nxt <= MAX - 1 && dist[nxt] > curr.w) {
                dist[nxt] = curr.w;
                pq.add(new Node(nxt, dist[nxt]));
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
        K = Integer.parseInt(st.nextToken());

        bw.write(String.valueOf(dijkstra(N, K)));

        // close the buffer
        br.close();
        bw.close();
    }
}
