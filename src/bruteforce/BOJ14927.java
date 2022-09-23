package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 전구 끄기 - BOJ14927
 * -----------------
 * category: brute force (브루트포스 알고리즘)
 *           greedy (그리디 알고리즘)
 *           bit masking (비트마스킹)
 * -----------------
 * -----------------
 * Input 1
 * 2
 * 1 1
 * 1 1
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 3
 * 0 0 0
 * 0 0 0
 * 0 0 1
 *
 * Output 2
 * -----------------
 * Input 3
 * 5
 * 0 0 0 1 0
 * 1 1 0 1 1
 * 1 1 1 0 1
 * 1 1 0 0 0
 * 0 0 0 0 1
 *
 * Output 3
 * -1
 * -----------------
 */
public class BOJ14927 {

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static int Size;
    static int MaxCount;
    static boolean[][] mapOrigin;

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > Size - 1) return false;
        if (y < 0 || y > Size - 1) return false;
        return true;
    }

    public static void toggle(boolean[][] map, int x, int y) {
        map[y][x] = !map[y][x];
        for (int i = 0; i < dirX.length; i++) {
            int nxtX = x + dirX[i];
            int nxtY = y + dirY[i];
            if (canGo(nxtX, nxtY)) {
                map[nxtY][nxtX] = !map[nxtY][nxtX];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        Size = Integer.parseInt(br.readLine());
        MaxCount = 1 << Size;
        mapOrigin = new boolean[Size][Size];
        for (int i = 0; i < Size; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < Size; j++) {
                mapOrigin[i][j] = Integer.parseInt(st.nextToken()) == 1;
            }
        }

        int count = Integer.MAX_VALUE;
        for (int bit = 0; bit < MaxCount; bit++) {
            int temp = 0;
            boolean[][] mapCopy = new boolean[Size][Size];
            for (int y = 0; y < Size; y++) {
                mapCopy[y] = Arrays.copyOf(mapOrigin[y], Size);
            }

            for (int x = 0; x < Size; x++) {
                int mask = 1 << x;
                if ((bit & mask) > 0) {
                    toggle(mapCopy, x, 0);
                    temp++;
                }
            }

            for (int y = 0; y < Size - 1; y++) {
                for (int x = 0; x < Size; x++) {
                    if (mapCopy[y][x]) {
                        toggle(mapCopy, x, y + 1);
                        temp++;
                    }
                }
            }

            boolean isDone = true;
            for (int x = 0; x < Size; x++) {
                if (mapCopy[Size - 1][x]) {
                    isDone = false;
                    break;
                }
            }

            if (isDone) {
                count = Math.min(temp, count);
            }
        }
        bw.write(String.valueOf(count == Integer.MAX_VALUE ? -1 : count));

        // close the buffer
        br.close();
        bw.close();
    }
}
