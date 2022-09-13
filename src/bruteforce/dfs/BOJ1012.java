package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 유기농 배추 - BOJ1012
 * -----------------
 *
 * 상하좌우로 인접해 있는 서로 다른 배추들의 모임을 배추 무리라고 할 때,
 * 하나의 배추 무리에는 배추 지렁이가 한마리만 서식하면 되므로 N, M의 땅에 대하여 dfs를 수행하며 배추 무리를 셀 수 있도록 한다.
 *
 * dfs를 수행하는 과정에서 visited 배열에 방문 여부를 체크하기 때문에, 한번 방문한 배추들은 다시 방문할 일이 없으므로
 * dfs를 수행하며 독립된 각 배추 무리를 구분지어 셀 수 있게 된다.
 *
 * -----------------
 * Input 1
 * 2
 * 10 8 17
 * 0 0
 * 1 0
 * 1 1
 * 4 2
 * 4 3
 * 4 5
 * 2 4
 * 3 4
 * 7 4
 * 8 4
 * 9 4
 * 7 5
 * 8 5
 * 9 5
 * 7 6
 * 8 6
 * 9 6
 * 10 10 1
 * 5 5
 *
 * Output 1
 * 5
 * 1
 * -----------------
 * Input 2
 * 1
 * 5 3 6
 * 0 2
 * 1 2
 * 2 2
 * 3 2
 * 4 2
 * 4 0
 *
 * Output 2
 * 2
 * -----------------
 */
public class BOJ1012 {

    static final int[] dirX = {1, -1, 0, 0};
    static final int[] dirY = {0, 0, 1, -1};
    static int N, M, K, T;
    static int[][] map;
    static boolean[][] visited;
    static StringBuilder result = new StringBuilder();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return true;
    }

    public static void dfs(int x, int y) {
        visited[y][x] = true;

        for (int i = 0; i < 4; i++) {
            int nxtX = x + dirX[i];
            int nxtY = y + dirY[i];
            if (!canGo(nxtX, nxtY)) continue;

            boolean exist = map[nxtY][nxtX] == 1;
            boolean notVisited = !visited[nxtY][nxtX];
            if (exist && notVisited) {
                dfs(nxtX, nxtY);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            M = Integer.parseInt(st.nextToken());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            map = new int[N][M];
            visited = new boolean[N][M];

            for (int j = 0; j < K; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                map[y][x] = 1;
            }

            int res = 0;
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < M; k++) {
                    if (map[j][k] == 1 && !visited[j][k]) {
                        dfs(k, j);
                        res++;
                    }
                }
            }
            result.append(res).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}