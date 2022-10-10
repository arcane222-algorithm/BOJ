package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 불 - BOJ5427
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * Input 1
 * 5
 * 4 3
 * ####
 * #*@.
 * ####
 * 7 6
 * ###.###
 * #*#.#*#
 * #.....#
 * #.....#
 * #..@..#
 * #######
 * 7 4
 * ###.###
 * #....*#
 * #@....#
 * .######
 * 5 5
 * .....
 * .***.
 * .*@*.
 * .***.
 * .....
 * 3 3
 * ###
 * #@#
 * ###
 *
 * Output 1
 * 2
 * 5
 * IMPOSSIBLE
 * IMPOSSIBLE
 * IMPOSSIBLE
 * -----------------
 */
public class BOJ5427 {
    private static class Vec2 {
        int x, y, dist;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vec2(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static int T, W, H;
    static char[][] map;
    static boolean[][] visited;
    static Queue<Vec2> queue = new LinkedList<>();
    static Queue<Vec2> queue2 = new LinkedList<>();
    static StringBuilder result = new StringBuilder();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > W - 1) return false;
        if (y < 0 || y > H - 1) return false;
        return true;
    }

    public static int bfs() {
        int dist = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            final int len = queue.size();
            for (int i = 0; i < len; i++) {
                Vec2 curr = queue.poll();
                if (map[curr.y][curr.x] == 'F') continue;

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = curr.x + dirX[j];
                    int nxtY = curr.y + dirY[j];
                    if (canGo(nxtX, nxtY)) {
                        if (map[nxtY][nxtX] == '.' && !visited[nxtY][nxtX]) {
                            visited[nxtY][nxtX] = true;
                            queue.add(new Vec2(nxtX, nxtY, curr.dist + 1));
                        }
                    } else {
                        dist = Math.min(dist, curr.dist);
                    }
                }
            }

            final int len2 = queue2.size();
            for (int i = 0; i < len2; i++) {
                Vec2 curr = queue2.poll();

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = curr.x + dirX[j];
                    int nxtY = curr.y + dirY[j];
                    if (canGo(nxtX, nxtY)) {
                        if (map[nxtY][nxtX] == '.') {
                            map[nxtY][nxtX] = 'F';
                            queue2.add(new Vec2(nxtX, nxtY));
                        }
                    }
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
            W = Integer.parseInt(st.nextToken());
            H = Integer.parseInt(st.nextToken());
            map = new char[H][W];
            visited = new boolean[H][W];

            queue.clear();
            queue2.clear();
            for (int j = 0; j < H; j++) {
                String line = br.readLine();
                for (int k = 0; k < W; k++) {
                    char c = line.charAt(k);
                    map[j][k] = c;
                    if (c == '@') {
                        queue.add(new Vec2(k, j, 0));
                        visited[j][k] = true;
                        map[j][k] = '.';
                    } else if (c == '*') {
                        queue2.add(new Vec2(k, j));
                    }
                }
            }

            int dist = bfs();
            result.append(dist == Integer.MAX_VALUE ? "IMPOSSIBLE" : dist + 1);
            if (i < T - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}