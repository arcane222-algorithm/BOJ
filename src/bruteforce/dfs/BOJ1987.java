package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 알파벳 - BOJ1987
 * -----------------
 * category: brute force (브루트포스 알고리즘)
 *           back tracking (백트래킹)
 * -----------------
 * -----------------
 * Input 1
 * 2 4
 * CAAB
 * ADCB
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 3 6
 * HFDFFB
 * AJHGDH
 * DGAGEH
 *
 * Output 2
 * 6
 * -----------------
 * Input 3
 * 5 5
 * IEFCJ
 * FHFKC
 * FFALF
 * HFGCF
 * HMCHH
 *
 * Output 3
 * 10
 * -----------------
 */
public class BOJ1987 {

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static int R, C;
    static char[][] map;
    static int check, count;


    public static boolean canGo(int x, int y) {
        if (x < 0 || x > C - 1) return false;
        if (y < 0 || y > R - 1) return false;
        return true;
    }

    public static void dfs(int depth, int x, int y) {
        int idx = map[y][x] - 'A';
        int mask = (1 << idx);
        check |= mask;

        for (int i = 0; i < dirX.length; i++) {
            int nxtX = x + dirX[i];
            int nxtY = y + dirY[i];
            if (!canGo(nxtX, nxtY)) continue;

            idx = map[nxtY][nxtX] - 'A';
            mask = 1 << idx;
            if ((check & mask) == 0) {
                dfs(depth + 1, nxtX, nxtY);
                check &= ~mask;
            }
        }

        count = Math.max(count, depth);
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

        for (int i = 0; i < R; i++) {
            String line = br.readLine();
            for (int j = 0; j < C; j++) {
                map[i][j] = line.charAt(j);
            }
        }
        dfs(0, 0, 0);
        bw.write(String.valueOf(count + 1));

        // close the buffer
        br.close();
        bw.close();
    }
}
