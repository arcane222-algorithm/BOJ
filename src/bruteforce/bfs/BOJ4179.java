package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 불! - BOJ4179
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비우선탐색)
 * -----------------
 * -----------------
 * Input 1
 * 4 4
 * ####
 * #JF#
 * #..#
 * #..#
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 7 7
 * #######
 * #J###F#
 * #.....#
 * #.#.#.#
 * #.#.#.#
 * F.#.#.#
 * #####.#
 *
 * Output 2
 * IMPOSSIBLE
 * -----------------
 * Input 3
 * 5 4
 * ####
 * #...
 * #.##
 * #.J#
 * ####
 *
 * Output 3
 * 6
 * -----------------
 */
public class BOJ4179 {
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

    static int R, C, plrX, plrY, fireX, fireY;
    static char[][] map;
    static boolean[][] visited;
    static Queue<Vec2> queue = new LinkedList<>();
    static Queue<Vec2> queue2 = new LinkedList<>();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > C - 1) return false;
        if (y < 0 || y > R - 1) return false;
        return true;
    }

    public static int bfs() {
        int dist = Integer.MAX_VALUE;
        while (!queue.isEmpty() || !queue2.isEmpty()) {
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

        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        map = new char[R][C];
        visited = new boolean[R][C];

        for (int i = 0; i < R; i++) {
            String line = br.readLine();
            for (int j = 0; j < C; j++) {
                char c = line.charAt(j);
                map[i][j] = c;
                if (c == 'J') {
                    queue.add(new Vec2(j, i, 0));
                    visited[i][j] = true;
                    map[i][j] = '.';
                } else if (c == 'F') {
                    queue2.add(new Vec2(j, i));
                }
            }
        }
        int result = bfs();
        bw.write(result == Integer.MAX_VALUE ? "IMPOSSIBLE" : String.valueOf(result + 1));

        // close the buffer
        br.close();
        bw.close();
    }
}
