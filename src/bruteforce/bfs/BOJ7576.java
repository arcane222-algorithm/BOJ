package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 토마토 - BOJ7576
 * -----------------
 *
 * N x M 격자에 -1, 0, 1이 있다. -1은 토마토가 없는 칸이고, 0은 토마토가 아직 익지 않은 칸, 1은 토마토가 익은 칸이다.
 * 처음 초기화 시 0또는 1인 경우를 세어 전체 토마토 수를 저장해주고 후에 익은 토마토 수를 세어 전체 토마토수와 같다면 토마토가 익는데
 * 걸리는 최소 날짜를 출력해주면 된다. 같지 않다면 -1을 출력하면 된다.
 *
 * 각 토마토의 위치를 queue에 삽입하고 bfs를 수행하며 주변 상, 하, 좌, 우에 토마토가 있다면 해당 토마토를 익게 하면 된다 (0에서 1로 바꿈)
 * 이 때, 그날 확산을 적용해야 하는 토마토들은 현재 있는 토마토들에 대해서만 적용해야 하므로 (새로 추가되는 토마토에 대해서는 X)
 * bfs를 수행하며 while 반복 수행 시, 내부에 for문을 추가하여 현재 queue에 담겨있던 토마토의 크기만큼만 탐색을 수행하고 이것을 1일치로 계산한다.
 *
 * -----------------
 * Input 1
 * 6 4
 * 0 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 1
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 6 4
 * 0 -1 0 0 0 0
 * -1 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 1
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 6 4
 * 1 -1 0 0 0 0
 * 0 -1 0 0 0 0
 * 0 0 0 0 -1 0
 * 0 0 0 0 -1 1
 *
 * Output 3
 * 6
 * -----------------
 * Input 4
 * 5 5
 * -1 1 0 0 0
 * 0 -1 -1 -1 0
 * 0 -1 -1 -1 0
 * 0 -1 -1 -1 0
 * 0 0 0 0 0
 *
 * Output 4
 * 14
 * -----------------
 * Input 5
 * 2 2
 * 1 -1
 * -1 1
 *
 * Output 5
 * 0
 * -----------------
 */
public class BOJ7576 {

    private static class Vec2 {
        int x, y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Vec2) {
                Vec2 vec2 = (Vec2) obj;
                return x == vec2.x && y == vec2.y;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Vec2={");
            builder.append("x=").append(x);
            builder.append(", y=").append(y).append('}');

            return builder.toString();
        }
    }

    static final int[] dirX = {1, -1, 0, 0};
    static final int[] dirY = {0, 0, 1, -1};

    static int M, N, count, totalCount;
    static int[][] box;
    static Queue<Vec2> queue = new LinkedList<>();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        if (box[y][x] != 0) return false;
        return true;
    }

    public static int bfs() {
        int result = 0;

        while (!queue.isEmpty()) {
            final int size = queue.size();
            for (int i = 0; i < size; i++) {
                Vec2 node = queue.poll();
                int x = node.x;
                int y = node.y;

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = x + dirX[j];
                    int nxtY = y + dirY[j];
                    if (canGo(nxtX, nxtY)) {
                        box[nxtY][nxtX] = 1;
                        count++;
                        queue.add(new Vec2(nxtX, nxtY));
                    }
                }

                if (queue.isEmpty()) break;
            }
            result++;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        box = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int num = Integer.parseInt(st.nextToken());
                box[i][j] = num;
                if (num > -1) {
                    if (num == 1) {
                        queue.add(new Vec2(j, i));
                        count++;
                    }
                    totalCount++;
                }
            }
        }

        int result = bfs();
        bw.write(String.valueOf(count == totalCount ? result - 1 : -1));


        // close the buffer
        br.close();
        bw.close();
    }
}