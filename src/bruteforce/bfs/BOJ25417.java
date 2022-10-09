package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 고속의 숫자 탐색 - BOJ25417
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * Input 1
 * 0 0 1 0 0
 * 0 7 -1 0 0
 * 0 0 0 0 0
 * 0 0 -1 0 0
 * 0 0 0 -1 0
 * 4 1
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 0 0 1 0 0
 * 0 7 -1 0 0
 * 0 0 0 0 0
 * 0 0 -1 0 0
 * 0 0 0 -1 0
 * 4 4
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * -1 7 0 7 0
 * 0 -1 7 7 7
 * 0 0 -1 7 7
 * 0 0 0 -1 1
 * 0 0 0 0 -1
 * 1 0
 *
 * Output 3
 * -1
 * -----------------
 * Input 4
 * -1 -1 7 0 0
 * 0 7 -1 0 -1
 * 0 7 0 0 7
 * 0 7 7 -1 7
 * -1 7 -1 1 0
 * 3 0
 *
 * Output 4
 * 6
 * -----------------
 */
public class BOJ25417 {

    private static class Vec2 {
        int x, y, dist;
        Vec2 prev;

        public Vec2(int x, int y, int dist, Vec2 prev) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vec2) {
                Vec2 v = (Vec2) o;
                return x == v.x && y == v.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return y + ", " + x;
        }
    }

    static final int W = 5, H = 5;
    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};


    static int[][] map = new int[H][W];
    static boolean[][] visited = new boolean[H][W];
    static int R, C;

    static Vec2 dest;

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > W - 1) return false;
        if (y < 0 || y > H - 1) return false;
        return map[y][x] != -1;
    }

    public static Vec2 run(int dx, int dy, Vec2 curr) {
        int nxtX = curr.x + dx;
        int nxtY = curr.y + dy;

        while (true) {
            if (!canGo(nxtX, nxtY)) {
                return new Vec2(nxtX - dx, nxtY - dy, curr.dist + 1, curr);
            } else {
                if (map[nxtY][nxtX] == 7) {
                    return new Vec2(nxtX, nxtY, curr.dist + 1, curr);
                }
            }

            nxtX += dx;
            nxtY += dy;
        }
    }

    public static int bfs() {
        Queue<Vec2> queue = new LinkedList<>();
        queue.add(new Vec2(C, R, 0, null));
        visited[C][R] = true;

        int dist = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Vec2 curr = queue.poll();
            if (curr.equals(dest)) {
//                Vec2 prev = curr;
//                while (prev != null) {
//                    System.out.println(prev);
//                    prev = prev.prev;
//                }
                dist = Math.min(dist, curr.dist);
            }

            for (int i = 0; i < dirX.length; i++) {
                Vec2 nxt = run(dirX[i], dirY[i], curr);
                if(!visited[nxt.y][nxt.x]) {
                    queue.add(nxt);
                    visited[nxt.y][nxt.x] = true;
                }
            }

            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];

                if (!canGo(nxtX, nxtY)) continue;
                if (!visited[nxtY][nxtX] && map[nxtY][nxtX] != -1) {
                    queue.add(new Vec2(nxtX, nxtY, curr.dist + 1, curr));
                    visited[nxtY][nxtX] = true;
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

        for (int i = 0; i < H; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < W; j++) {
                int curr = Integer.parseInt(st.nextToken());
                if (curr == 1) dest = new Vec2(j, i, 0, null);
                map[i][j] = curr;
            }
        }
        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        int result = bfs();
        bw.write(String.valueOf(result));


        // close the buffer
        br.close();
        bw.close();
    }
}