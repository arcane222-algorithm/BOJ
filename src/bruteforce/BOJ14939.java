package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 불 끄기 - BOJ14939
 * -----------------
 * category: brute force (브루트포스 알고리즘)
 *           greedy (그리디 알고리즘)
 *           bit masking (비트마스킹)
 * -----------------
 * -----------------
 * Input 1
 * #O########
 * OOO#######
 * #O########
 * ####OO####
 * ###O##O###
 * ####OO####
 * ##########
 * ########O#
 * #######OOO
 * ########O#
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ14939 {

    static final int[] dirX = {0, 0, -1, 1};
    static final int[] dirY = {-1, 1, 0, 0};

    static final int Size = 10;
    static final int MAX_COUNT = 1 << Size;
    static boolean[][] mapOrigin = new boolean[Size][Size];

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

        for (int i = 0; i < Size; i++) {
            String line = br.readLine();
            for (int j = 0; j < Size; j++) {
                mapOrigin[i][j] = line.charAt(j) == 'O';
            }
        }

        int count = Integer.MAX_VALUE;
        for (int bit = 0; bit < MAX_COUNT; bit++) {
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
                if (mapCopy[9][x]) {
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