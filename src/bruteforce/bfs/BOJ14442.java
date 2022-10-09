package bruteforce.bfs;

import java.io.*;
import java.util.*;



/**
 * 벽 부수고 이동하기 2 - BOJ14442
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * Input 1
 * 6 4 1
 * 0100
 * 1110
 * 1000
 * 0000
 * 0111
 * 0000
 *
 * Output 1
 * 15
 * -----------------
 * Input 2
 * 6 4 2
 * 0100
 * 1110
 * 1000
 * 0000
 * 0111
 * 0000
 *
 * Output 2
 * 9
 * -----------------
 * Input 3
 * 4 4 3
 * 0111
 * 1111
 * 1111
 * 1110
 *
 * Output 3
 * -1
 * -----------------
 */
public class BOJ14442 {
    private static class Vec2 {
        int x, y, dist, destroy;

        public Vec2(int x, int y, int dist, int destroy) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.destroy = destroy;
        }
    }

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static int N, M, K;
    static int[][] map;
    static boolean[][][] visited;

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return true;
    }

    public static int bfs() {
        Queue<Vec2> queue = new LinkedList<>();
        queue.add(new Vec2(0, 0, 1, 0));
        visited[0][0][0] = true;

        int dist = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Vec2 curr = queue.poll();

            if (curr.x == M - 1 && curr.y == N - 1) {
                dist = Math.min(dist, curr.dist);
            }

            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];

                if (!canGo(nxtX, nxtY)) continue;

                if (map[nxtY][nxtX] == 1) {
                    if (curr.destroy < K && !visited[nxtY][nxtX][curr.destroy + 1]) {
                        queue.add(new Vec2(nxtX, nxtY, curr.dist + 1, curr.destroy + 1));
                        visited[nxtY][nxtX][curr.destroy + 1] = true;
                    }
                } else {
                    if (!visited[nxtY][nxtX][curr.destroy]) {
                        queue.add(new Vec2(nxtX, nxtY, curr.dist + 1, curr.destroy));
                        visited[nxtY][nxtX][curr.destroy] = true;
                    }
                }

            }
        }

        return dist == Integer.MAX_VALUE ? -1 : dist;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        visited = new boolean[N][M][K + 1];
        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = line.charAt(j) - '0';
            }
        }

        bw.write(String.valueOf(bfs()));

        // close the buffer
        br.close();
        bw.close();
    }
}
