package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 공주님을 구해라! - BOJ17836
 * -----------------
 * category: bfs (너비우선탐색)
 * -----------------
 * -----------------
 * Input 1
 * 6 6 16
 * 0 0 0 0 1 1
 * 0 0 0 0 0 2
 * 1 1 1 0 1 0
 * 0 0 0 0 0 0
 * 0 1 1 1 1 1
 * 0 0 0 0 0 0
 *
 * Output 1
 * 10
 * -----------------
 * Input 2
 * 3 4 100
 * 0 0 0 0
 * 1 1 1 1
 * 0 0 2 0
 *
 * Output 2
 * Fail
 * -----------------
 */
public class BOJ17836 {

    private static class Vec2 {
        int x, y, dist, hasWeapon;

        public Vec2(int x, int y, int dist, int hasWeapon) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.hasWeapon = hasWeapon;
        }
    }

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static int N, M, T;
    static int[][] map;
    static boolean[][][] visited;

    public static boolean canGo(int x, int y, int hasWeapon) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return hasWeapon == 1 || map[y][x] != 1;
    }

    public static int bfs() {
        Queue<Vec2> queue = new LinkedList<>();
        visited[0][0][0] = true;
        queue.add(new Vec2(0, 0, 0, 0));

        int dist = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Vec2 curr = queue.poll();

            if (curr.x == M - 1 && curr.y == N - 1) {
                dist = Math.min(curr.dist, dist);
            }

            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];
                if (canGo(nxtX, nxtY, curr.hasWeapon) && !visited[nxtY][nxtX][curr.hasWeapon]) {
                    Vec2 nxt = new Vec2(nxtX, nxtY, curr.dist + 1, curr.hasWeapon);
                    if (map[nxtY][nxtX] == 2) nxt.hasWeapon = 1;
                    visited[nxtY][nxtX][curr.hasWeapon] = true;
                    queue.add(nxt);
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

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        map = new int[N][M];
        visited = new boolean[N][M][2];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int value = Integer.parseInt(st.nextToken());
                map[i][j] = value;
            }
        }

        int result = bfs();
        bw.write(result > T ? "Fail" : String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}