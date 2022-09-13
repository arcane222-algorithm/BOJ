package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 벽 부수고 이동하기 - BOJ2206
 * -----------------
 *
 *
 *
 * -----------------
 * Input 1
 * 6 4
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
 * 4 4
 * 0111
 * 1111
 * 1111
 * 1110
 *
 * Output 2
 * -1
 * -----------------
 */
public class BOJ2206 {

    private static class Vec2 {
        int x, y, pathCount;
        boolean destroyed;

        public Vec2(int x, int y, int pathCount, boolean destroyed) {
            this.x = x;
            this.y = y;
            this.pathCount = pathCount;
            this.destroyed = destroyed;
        }
    }

    static final int DIR_COUNT = 4;
    static int N, M;
    static char[][] map;
    static boolean[][][] visited;
    static int[] dirX = {0, 0, -1, 1};
    static int[] dirY = {-1, 1, 0, 0};

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return true;
    }

    public static int bfs() {
        Queue<Vec2> queue = new LinkedList<>();
        queue.add(new Vec2(0, 0, 1, false));
        visited[0][0][0] = true;

        int cnt = -1;
        while (!queue.isEmpty()) {
            Vec2 curr = queue.poll();

            if (curr.x == M - 1 && curr.y == N - 1) {
                return curr.pathCount;
            }

            for (int i = 0; i < DIR_COUNT; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];
                if (canGo(nxtX, nxtY)) {
                    if (map[nxtY][nxtX] == '0') {
                        if (!curr.destroyed && !visited[nxtY][nxtX][0]) {
                            queue.add(new Vec2(nxtX, nxtY, curr.pathCount + 1, false));
                            visited[nxtY][nxtX][0] = true;
                        } else if (curr.destroyed && !visited[nxtY][nxtX][1]) {
                            queue.add(new Vec2(nxtX, nxtY, curr.pathCount + 1, true));
                            visited[nxtY][nxtX][1] = true;
                        }
                    } else {
                        if (!curr.destroyed && !visited[nxtY][nxtX][1]) {
                            queue.add(new Vec2(nxtX, nxtY, curr.pathCount + 1, true));
                            visited[nxtY][nxtX][1] = true;
                        }
                    }
                }
            }
        }

        return cnt;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new char[N][M];
        visited = new boolean[N][M][2];
        for (int i = 0; i < N; i++) {
            map[i] = br.readLine().toCharArray();
        }
        bw.write(String.valueOf(bfs()));

        // close the buffer
        br.close();
        bw.close();
    }
}