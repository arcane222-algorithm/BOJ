package graphtheory.shortestpath.dijkstra;

import java.io.*;
import java.util.*;


/**
 * 녹색 옷 입은 애가 젤다지? - BOJ4485
 * -----------------
 * category: graph theory (그래프 이론)
 *           dijkstra (데이크스트라)
 * -----------------
 * Input 1
 * 3
 * 5 5 4
 * 3 9 1
 * 3 2 7
 * 5
 * 3 7 2 0 1
 * 2 8 0 9 1
 * 1 2 1 8 1
 * 9 8 9 2 0
 * 3 6 5 1 5
 * 7
 * 9 0 5 1 1 5 3
 * 4 1 2 1 6 5 3
 * 0 7 6 1 6 8 5
 * 1 1 7 8 3 2 3
 * 9 4 0 7 6 4 1
 * 5 8 3 2 4 8 3
 * 7 4 8 4 8 3 4
 * 0
 *
 * Output 1
 * Problem 1: 20
 * Problem 2: 19
 * Problem 3: 36
 * -----------------
 */
public class BOJ4485 {

    private static class Node implements Comparable<Node> {
        int destX, destY, w;

        public Node(int destX, int destY, int w) {
            this.destX = destX;
            this.destY = destY;
            this.w = w;
        }

        @Override
        public int compareTo(Node e) {
            return Long.compare(w, e.w);
        }
    }

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};
    static int N;
    static int[][] map;
    static StringBuilder result = new StringBuilder();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > N - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return true;
    }

    public static int dijkstra(int[][] map) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        pq.offer(new Node(0, 0, map[0][0]));
        dist[0][0] = map[0][0];

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (dist[curr.destY][curr.destX] < curr.w) continue;

            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.destX + dirX[i];
                int nxtY = curr.destY + dirY[i];
                if (!canGo(nxtX, nxtY)) continue;

                if (dist[nxtY][nxtX] > curr.w + map[nxtY][nxtX]) {
                    dist[nxtY][nxtX] = curr.w + map[nxtY][nxtX];
                    pq.add(new Node(nxtX, nxtY, dist[nxtY][nxtX]));
                }
            }
        }

        return dist[N - 1][N - 1];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        int testCase = 1;
        while (true) {
            N = Integer.parseInt(br.readLine());
            if (N == 0) break;

            map = new int[N][N];
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    map[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            int cost = dijkstra(map);
            result.append("Problem ").append(testCase).append(": ").append(cost).append('\n');
            testCase++;
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}