package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 마인크래프트 - BOJ18111
 * -----------------
 * Input 1
 * 3 4 99
 * 0 0 0 0
 * 0 0 0 0
 * 0 0 0 1
 *
 * Output 1
 * 2 0
 * -----------------
 * Input 2
 * 3 4 1
 * 64 64 64 64
 * 64 64 64 64
 * 64 64 64 63
 *
 * Output 2
 * 1 64
 * -----------------
 */
public class BOJ18111 {

    static int N, M, B, minHeight, maxHeight;
    static int[][] map;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        minHeight = Integer.MAX_VALUE;
        maxHeight = 0;

        map = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int height = Integer.parseInt(st.nextToken());
                if (minHeight > height) minHeight = height;
                if (maxHeight < height) maxHeight = height;
                map[i][j] = height;
            }
        }

        int ansTime, ansHeight;
        ansTime = ansHeight = Integer.MAX_VALUE;
        for (int i = minHeight; i < maxHeight + 1; i++) {
            int gap = 0;
            int workTime = 0;
            int invSize = B;

            for(int j = 0; j < N; j++) {
                for(int k = 0; k < M; k++) {
                    gap = map[j][k] - i;
                    workTime += gap > 0 ? Math.abs(gap) * 2 : gap < 0 ? Math.abs(gap) : 0;
                    invSize += gap;
                }
            }

            if(invSize >= 0) {
                if(workTime <= ansTime) {
                    ansTime = workTime;
                    ansHeight = i;
                }
            }
        }

        result.append(ansTime).append(' ').append(ansHeight);
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}