package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 적록색약 - BOJ10026
 * -----------------
 * category: dfs (깊이 우선 탐색)
 *           bfs (너비 우선 탐색)
 *
 * Time-Complexity: 맵의 크기 N x N
 *                  O(2N^2) => O(N^2)
 * -----------------
 *
 * N x N의 그림을 map 배열로 선언하여 R, G, B에 대하여 각각 dfs 혹은 bfs를 수행하여 몇 개의 영역으로 나눠지는지 계산한다.
 * 적록색약의 경우 R와 G를 구분하지 못하기 때문에 입력을 받을 때 적록색약의 경우를 계산하기 위한 map2 배열을 선언하여 G일 때 R의 값을 저장하도록 한다.
 * 각 지역을 구분하는 방법은
 * dfs 혹은 bfs를 수행하며 탐색하려는 색상을 제외한 색상은 벽으로 처리하여 탐색한다.
 * 탐색 과정에서 방문한 위치를 visited 배열로 관리하기 때문에, 한 지역에 대한 탐색 과정에서 더 이상 방문할 곳이 없다면 탐색이 종료된다.
 * 이후 map 에 대하여 탐색하려는 색상 위치에 대해 visited 값이 false 라면 해당 지역은 아직 탐색되지 않은 것이므로 다시 dfs 혹은 bfs를 수행하며
 * 지역의 종류를 셀 수 있게 된다.
 *
 * -----------------
 * Input 1
 * 5
 * RRRBB
 * GGBBB
 * BBBRR
 * BBRRR
 * RRRRR
 *
 * Output 1
 * 4 3
 * -----------------
 */
public class BOJ10026 {

    private static class Vec2 {
        int x, y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static final int[] dirX = {1, -1, 0, 0};
    static final int[] dirY = {0, 0, 1, -1};

    static int N;
    static char[][] map;
    static char[][] map2;
    static boolean[][] visited;

    public static boolean canGo(int x, int y, char[][] map, char color) {
        if (x < 0 || x > N - 1) return false;
        if (y < 0 || y > N - 1) return false;
        return map[y][x] == color;
    }

    public static void bfs(int x, int y, char[][] map, char color) {
        Queue<Vec2> queue = new LinkedList<>();
        queue.add(new Vec2(x, y));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Vec2 curr = queue.poll();
            for (int i = 0; i < dirX.length; i++) {
                int nxtX = curr.x + dirX[i];
                int nxtY = curr.y + dirY[i];
                if (canGo(nxtX, nxtY, map, color) && !visited[nxtY][nxtX]) {
                    visited[nxtY][nxtX] = true;
                    queue.add(new Vec2(nxtX, nxtY));
                }
            }
        }
    }

    public static int check(char[][] map, char color) {
        visited = new boolean[N][N];
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == color && !visited[i][j]) {
                    count++;
                    bfs(j, i, map, color);
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        map = new char[N][N];
        map2 = new char[N][N];
        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < N; j++) {
                char block = line.charAt(j);
                map[i][j] = block;
                map2[i][j] = block == 'G' ? 'R' : block;
            }
        }

        int areaSize1 = 0, areaSize2 = 0;
        areaSize1 += check(map, 'R');
        areaSize1 += check(map, 'G');
        areaSize1 += check(map, 'B');
        areaSize2 += check(map2, 'R');
        areaSize2 += check(map2, 'B');
        bw.write(areaSize1 + " " + areaSize2);

        // close the buffer
        br.close();
        bw.close();
    }
}