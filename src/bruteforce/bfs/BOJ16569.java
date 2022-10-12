package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 화산쇄설류 - BOJ16569
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * Input 1
 * 8 8 8
 * 5 8
 * 58 34 30 23 12 44 18 30
 * 4 62 26 42 64 39 44 25
 * 64 34 6 10 0 25 46 34
 * 42 3 62 48 20 25 25 41
 * 35 30 4 33 35 39 41 38
 * 7 43 37 3 0 25 20 23
 * 20 59 18 43 1 14 16 11
 * 17 50 12 19 59 48 7 4
 * 4 5 4
 * 2 6 4
 * 5 1 2
 * 8 8 3
 * 5 6 2
 * 8 2 2
 * 5 2 1
 * 3 5 2
 *
 * Output 1
 * 46 3
 * -----------------
 * Input 2
 * 8 8 8
 * 1 8
 * 7 9 1 60 5 49 19 27
 * 38 25 18 1 52 43 22 0
 * 20 35 39 43 10 17 34 43
 * 21 50 13 34 64 57 24 48
 * 64 18 14 40 62 11 3 58
 * 64 22 60 15 5 16 59 8
 * 1 61 19 9 13 53 50 14
 * 5 30 7 13 44 25 15 63
 * 2 3 2
 * 2 7 2
 * 4 6 2
 * 2 8 2
 * 5 8 2
 * 4 7 2
 * 5 2 5
 * 6 3 1
 *
 * Output 2
 * 49 2
 * -----------------
 * Input 3
 * 3 3 2
 * 1 1
 * 0 0 9
 * 0 0 0
 * 0 0 0
 * 2 3 9
 * 3 3 0
 *
 * Output 3
 * 0 0
 * -----------------
 * Input 4
 * 3 3 2
 * 3 1
 * 1000 0 0
 * 0 0 0
 * 0 0 0
 * 2 1 100
 * 2 2 100
 *
 * Output 4
 * 1000 6
 * -----------------
 */
public class BOJ16569 {

    private static class Volcano implements Comparable<Volcano> {
        int x, y, t;

        public Volcano(int x, int y, int t) {
            this.x = x;
            this.y = y;
            this.t = t;
        }

        @Override
        public int compareTo(Volcano v) {
            return Integer.compare(t, v.t);
        }
    }

    private static class Node {
        int x, y, h, dist;

        public Node(int x, int y, int h, int dist) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.dist = dist;
        }
    }

    static final int[] dirX = {-1, 1, 0, 0};
    static final int[] dirY = {0, 0, -1, 1};

    static int[][] map, spreadTimes;
    static boolean[][] visited;
    static boolean[][] visited2;
    static int M, N, V, X, Y;
    static List<Volcano> volcanoes;
    static PriorityQueue<Volcano> pq = new PriorityQueue<>();

    public static void setSpreadTimes() {
        for (Volcano v : volcanoes) {
            spreadTimes[v.x][v.y] = 0;
            for (int t = 1; t < 100; t++) {
                boolean changed = false;

                for (int i = -t; i <= t; i++) {
                    for (int j = -t; j <= t; j++) {
                        if (Math.abs(i) + Math.abs(j) == t) {
                            int nxtX = v.x + i;
                            int nxtY = v.y + j;
                            if (canGo(nxtX, nxtY)) {
                                changed = true;
                                spreadTimes[nxtX][nxtY] = Math.min(spreadTimes[nxtX][nxtY], t + v.t);
                            }
                        }
                    }
                }

                if (!changed) break;
            }
        }
    }

    public static boolean canGo(int x, int y) {
        if (x < 1 || x > M) return false;
        if (y < 1 || y > N) return false;
        return true;
    }

    public static String bfs() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(X, Y, map[X][Y], 0));
        visited[X][Y] = true;

        int maxH = 0, minD = Integer.MAX_VALUE, t = 0;
        while (!queue.isEmpty()) {
            while (!pq.isEmpty()) {
                if (pq.peek().t == t) {
                    Volcano curr = pq.poll();

                    for (int i = 0; i < dirX.length; i++) {
                        int nxtX = curr.x + dirX[i];
                        int nxtY = curr.y + dirY[i];

                        if (canGo(nxtX, nxtY) && !visited[nxtX][nxtY] && !visited2[nxtX][nxtY]) {
                            spreadTimes[nxtX][nxtY] = Math.min(t, spreadTimes[nxtX][nxtY]);
                            pq.add(new Volcano(nxtX, nxtY, t + 1));
                            visited2[nxtX][nxtY] = true;
                        }
                    }
                } else {
                    break;
                }
            }

            final int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node curr = queue.poll();
                if (maxH < curr.h) {
                    maxH = curr.h;
                    minD = curr.dist;
                } else if (maxH == curr.h) {
                    minD = Math.min(minD, curr.dist);
                }

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = curr.x + dirX[j];
                    int nxtY = curr.y + dirY[j];
                    if (!canGo(nxtX, nxtY) || visited[nxtX][nxtY]) continue;
                    if (t >= spreadTimes[nxtX][nxtY]) continue;

                    Node nxt = new Node(nxtX, nxtY, map[nxtX][nxtY], curr.dist + 1);
                    queue.add(nxt);
                    visited[nxtX][nxtY] = true;
                }
            }
            t++;
        }

        StringBuilder result = new StringBuilder();
        result.append(maxH).append(' ').append(minD);

        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        V = Integer.parseInt(st.nextToken());
        map = new int[M + 1][N + 1];
        spreadTimes = new int[M + 1][N + 1];
        visited = new boolean[M + 1][N + 1];
        visited2 = new boolean[M + 1][N + 1];
        volcanoes = new ArrayList<>(V);

        st = new StringTokenizer(br.readLine());
        X = Integer.parseInt(st.nextToken());
        Y = Integer.parseInt(st.nextToken());

        for (int x = 1; x < M + 1; x++) {
            st = new StringTokenizer(br.readLine());
            for (int y = 1; y < N + 1; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
            }
            Arrays.fill(spreadTimes[x], Integer.MAX_VALUE);
        }

        for (int i = 0; i < V; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            spreadTimes[x][y] = -1;
            pq.add(new Volcano(x, y, t));
        }

        bw.write(bfs());

        // close the buffer
        br.close();
        bw.close();
    }
}
