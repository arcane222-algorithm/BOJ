package implementation;

import java.io.*;
import java.util.*;

/**
 * 미세먼지 안녕! - BOJ 17144
 * --------------------
 * Input 1
 * 7 8 3
 * 0 0 0 0 0 0 0 9
 * 0 0 0 0 3 0 0 8
 * -1 0 5 0 0 0 22 0
 * -1 8 0 0 0 0 0 0
 * 0 0 0 0 0 10 43 0
 * 0 0 5 0 15 0 0 0
 * 0 0 40 0 0 0 20 0
 *
 * Output 1
 * 186
 * --------------------
 * Input 2
 * 7 8 50
 * 0 0 0 0 0 0 0 9
 * 0 0 0 0 3 0 0 8
 * -1 0 5 0 0 0 22 0
 * -1 8 0 0 0 0 0 0
 * 0 0 0 0 0 10 43 0
 * 0 0 5 0 15 0 0 0
 * 0 0 40 0 0 0 20 0
 *
 * Output 2
 * 46
 * --------------------
 */
public class BOJ17144 {

    static final int[] dirX = {1, 0, -1, 0};
    static final int[] dirY = {0, 1, 0, -1};
    static int R, C, T;
    static int AirCleanerHeadX = -1, AirCleanerHeadY = -1;
    static int AirCleanerBodyX = -1, AirCleanerBodyY = -1;
    static int[][] room;

    public static boolean canGo(int x, int y) {
        if(x < 0 || x > C - 1) return false;
        if(y < 0 || y > R - 1) return false;
        return true;
    }

    public static int[][] diffuse(int[][] src) {
        int[][] dst = new int[R][C];

        for(int i = 0; i < R; i++) {
            for(int j = 0; j < C; j++) {
                int diffuseCnt = 0;
                int diffuseAmount = src[i][j] / 5;
                for(int k = 0; k < 4; k++) {
                    int nextX = j + dirX[k];
                    int nextY = i + dirY[k];
                    if(canGo(nextX, nextY) && src[nextY][nextX] != -1) {
                        dst[nextY][nextX] += diffuseAmount;
                        diffuseCnt++;
                    }
                }
                dst[i][j] += src[i][j] - diffuseCnt * diffuseAmount;
            }
        }

        return dst;
    }

    public static void rotate(int[][] src, int sx, int sy, int ex, int ey, boolean ccw) {
        int temp;
        int gapX = Math.abs(ex - sx);
        int gapY = Math.abs(ey - sy);

        if(ccw) {
            temp = src[ey][sx];

            // left
            for(int i = 0; i < gapY; i++) {
                src[ey - i][sx] = src[ey - 1 - i][sx];
            }
            // top
            for(int i = 0; i < gapX; i++) {
                src[sy][sx + i] = src[sy][sx + 1 + i];
            }
            // right
            for(int i = 0; i < gapY; i++) {
                src[sy + i][ex] = src[sy + 1 + i][ex];
            }
            // bottom
            for(int i = 0; i < gapX; i++) {
                src[ey][ex - i] = src[ey][ex - 1 - i];
            }
            src[ey][sx + 1] =  temp;

        } else {
            temp = src[sy][sx];

            // left
            for(int i = 0; i < gapY; i++) {
                src[sy + i][sx] = src[sy + 1 + i][sx];
            }
            // bottom
            for(int i = 0; i < gapX; i++) {
                src[ey][sx + i] = src[ey][sx + 1 + i];
            }
            // right
            for(int i = 0; i < gapY; i++) {
                src[ey - i][ex] = src[ey - 1 - i][ex];
            }
            // top
            for(int i = 0; i < gapX; i++) {
                src[sy][ex - i] = src[sy][ex - 1 - i];
            }
            src[sy][sx + 1] = temp;
        }
    }

    public static void cleanUp(int[][] src) {
        src[AirCleanerHeadY][AirCleanerHeadX] = src[AirCleanerBodyY][AirCleanerBodyX] = 0;
        rotate(src, 0, 0, C - 1, AirCleanerHeadY, true);
        rotate(src, AirCleanerBodyX, AirCleanerBodyY, C - 1, R - 1, false);
        src[AirCleanerHeadY][AirCleanerHeadX] = src[AirCleanerBodyY][AirCleanerBodyX] = -1;
    }

    public static int sum(int[][] src) {
        int sum = 0;
        for(int i = 0; i < R; i++) {
            for(int j = 0; j < C; j++) {
                sum += src[i][j];
            }
        }

        return sum;
    }

    public static void dumpRoom(int[][] src) {
        for(int i = 0; i < R; i++) {
            System.out.println(Arrays.toString(src[i]));
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse first line
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        room = new int[R][C];

        // parse room information
        for(int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < C; j++) {
                int dust = Integer.parseInt(st.nextToken());
                room[i][j] = dust;
                if(dust == -1 && AirCleanerHeadX == -1) {
                    AirCleanerHeadY = i;
                    AirCleanerBodyY = i + 1;
                    AirCleanerHeadX = AirCleanerBodyX = j;
                }
            }
        }

        // calculate the dust
        for(int i = 0; i < T; i++) {
            room = diffuse(room);
            cleanUp(room);
        }

        // write the answer
        int answer = sum(room) + 2;
        bw.write(String.valueOf(answer));

        // close the buffer
        br.close();
        bw.close();
    }
}
