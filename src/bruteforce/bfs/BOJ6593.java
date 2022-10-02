package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 상범 빌딩 - BOJ6593
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비우선탐색)
 * -----------------
 * -----------------
 * Input 1
 * 3 4 5
 * S....
 * .###.
 * .##..
 * ###.#
 *
 * #####
 * #####
 * ##.##
 * ##...
 *
 * #####
 * #####
 * #.###
 * ####E
 *
 * 1 3 3
 * S##
 * #E#
 * ###
 *
 * 0 0 0
 *
 * Output 1
 * Escaped in 11 minute(s).
 * Trapped!
 * -----------------
 */
public class BOJ6593 {

    private static class Vec3 {
        int x, y, z, dist;

        public Vec3(int x, int y, int z, int dist) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dist = dist;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Vec3={");
            builder.append("x=").append(x);
            builder.append(", y=").append(y);
            builder.append(", z=").append(z);
            builder.append(", dist=").append(dist).append('}');

            return builder.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vec3) {
                Vec3 pos = (Vec3) o;
                return x == pos.x && y == pos.y && z == pos.z;
            }
            return false;
        }
    }

    static int[] dirX = {1, -1, 0, 0, 0, 0};
    static int[] dirY = {0, 0, -1, 1, 0, 0};
    static int[] dirZ = {0, 0, 0, 0, -1, 1};

    static int L, R, C;
    static char[][][] map;
    static boolean[][][] visited;
    static Vec3 depart, dest;
    static StringBuilder resBuilder = new StringBuilder();

    public static boolean canGo(int x, int y, int z) {
        if (x < 0 || x > C - 1) return false;
        if (y < 0 || y > R - 1) return false;
        if (z < 0 || z > L - 1) return false;
        return map[z][y][x] != '#';
    }

    public static int bfs() {
        int result = Integer.MAX_VALUE;
        Queue<Vec3> queue = new LinkedList<>();
        queue.add(depart);
        visited[depart.z][depart.y][depart.x] = true;

        while (!queue.isEmpty()) {
            Vec3 curr = queue.poll();

            if (curr.equals(dest)) {
                result = Math.min(result, curr.dist);
            }

            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];
                int nxtZ = curr.z + dirZ[i];

                if (canGo(nxtX, nxtY, nxtZ) && !visited[nxtZ][nxtY][nxtX]) {
                    queue.add(new Vec3(nxtX, nxtY, nxtZ, curr.dist + 1));
                    visited[nxtZ][nxtY][nxtX] = true;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            st = new StringTokenizer(br.readLine());
            L = Integer.parseInt(st.nextToken());
            R = Integer.parseInt(st.nextToken());
            C = Integer.parseInt(st.nextToken());
            if (L == 0 && R == 0 && C == 0) break;

            map = new char[L][R][C];
            visited = new boolean[L][R][C];
            for (int z = 0; z < L; z++) {
                for (int y = 0; y < R; y++) {
                    String line = br.readLine();
                    for (int x = 0; x < C; x++) {
                        char c = line.charAt(x);
                        if (c == 'S') depart = new Vec3(x, y, z, 0);
                        if (c == 'E') dest = new Vec3(x, y, z, 0);
                        map[z][y][x] = c;
                    }
                }
                br.readLine();  // read empty line
            }

            int result = bfs();
            if(result == Integer.MAX_VALUE) {
                resBuilder.append("Trapped!");
            } else {
                resBuilder.append("Escaped in ").append(result).append(" minute(s).");
            }
            resBuilder.append('\n');
        }
        bw.write(resBuilder.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
