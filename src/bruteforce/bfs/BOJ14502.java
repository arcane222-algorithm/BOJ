package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 연구소 - BOJ14502
 * -----------------
 * category: brute-force (브루트포스 알고리즘),
 *           bfs (너비우선탐색)
 *
 * Time-Complexity: 바이러스의 수 V, 맵의 크기 N x M,
 *                  dfs -> (N x M - V)C3  ( (N x M - V)개에서 3개를 뽑는 조합 )
 *                  bfs -> 2 * N * M (map copy + bfs)
 *                  O(2NM * ((N x M - V)C3) => O(NM * ((N x M - V)C3)
 * -----------------
 *
 * dfs(백트래킹)을 이용하여 depth == 3일 때 (맵에 벽을 3개 추가로 설치했을 때)의 경우의 수를 모두 따지며 bfs를 실행한다.
 * bfs를 실행하여 바이러스를 맵에 퍼뜨린 후 맵에 0인 지역은 안전 지역이므로 이것의 개수를 세어주면 된다.
 * 벽을 한번 설치한 후 해당 경우의 수가 끝나면 다시 철거해야 하므로 visited 배열을 사용할 때와 비슷하게
 *
 * map[y][x] = '1';
 * dfs(depth + 1);
 * map[u][x] = '0'; >> 다시 벽을 철거해야 한다.
 *
 * 이러한 방식으로 depth == 3인 경우의 수를 모두 따져 안전 지역의 수가 최대인 경우를 출력하면 된다.
 * dfs의 depth == 3인 경우 마다 bfs를 하며 map을 조작해야 하기 때문에 mapCopy 배열을 선언하여 현재 벽이 3개 쳐진 상태의 원본 map을 복사한 후
 * 바이러스가 bfs를 통해 퍼지는 시뮬레이션을 구현한다.
 *
 * -----------------
 * Input 1
 * 7 7
 * 2 0 0 0 1 1 0
 * 0 0 1 0 1 2 0
 * 0 1 1 0 1 0 0
 * 0 1 0 0 0 0 0
 * 0 0 0 0 0 1 1
 * 0 1 0 0 0 0 0
 * 0 1 0 0 0 0 0
 *
 * Output 1
 * 27
 * -----------------
 * Input 2
 * 4 6
 * 0 0 0 0 0 0
 * 1 0 0 0 0 2
 * 1 1 1 0 0 2
 * 0 0 0 0 0 2
 *
 * Output 2
 * 9
 * -----------------
 * Input 3
 * 8 8
 * 2 0 0 0 0 0 0 2
 * 2 0 0 0 0 0 0 2
 * 2 0 0 0 0 0 0 2
 * 2 0 0 0 0 0 0 2
 * 2 0 0 0 0 0 0 2
 * 0 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0 0
 *
 * Output 3
 * 3
 * -----------------
 */
public class BOJ14502 {

    private static class Vec2 {
        int x, y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static final int[] dirX = {1, -1, 0, 0};
    static final int[] dirY = {0, 0, 1, -1};
    static int N, M;
    static int maxSafeZoneSize;
    static char[][] map;
    static List<Vec2> virus = new ArrayList<>();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return true;
    }

    public static int bfs() {
        char[][] mapCopy = new char[N][M];
        for (int i = 0; i < N; i++) {
            mapCopy[i] = Arrays.copyOf(map[i], M);
        }

        Queue<Vec2> queue = new LinkedList<>(virus);
        while (!queue.isEmpty()) {
            final int len = queue.size();

            for (int i = 0; i < len; i++) {
                Vec2 pos = queue.poll();

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = pos.x + dirX[j];
                    int nxtY = pos.y + dirY[j];
                    if (canGo(nxtX, nxtY) && mapCopy[nxtY][nxtX] == '0') {
                        mapCopy[nxtY][nxtX] = '2';
                        queue.add(new Vec2(nxtX, nxtY));
                    }
                }
            }
        }

        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mapCopy[i][j] == '0') count++;
            }
        }

        return count;
    }

    public static void dfs(int depth) {
        if (depth == 3) {
            maxSafeZoneSize = Math.max(maxSafeZoneSize, bfs());
        } else {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (map[i][j] == '0') {
                        map[i][j] = '1';
                        dfs(depth + 1);
                        map[i][j] = '0';
                    }
                }
            }
        }
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

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                char block = st.nextToken().charAt(0);
                if (block == '2') virus.add(new Vec2(j, i));
                map[i][j] = block;
            }
        }
        dfs(0);
        bw.write(String.valueOf(maxSafeZoneSize));

        // close the buffer
        br.close();
        bw.close();
    }
}