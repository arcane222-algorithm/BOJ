package dp;

import java.io.*;
import java.util.*;


/**
 * RGB거리 - BOJ1149
 * -----------------
 * category: dp (동적 계획법)
 * -----------------
 * Input 1
 * 3
 * 26 40 83
 * 49 60 57
 * 13 89 99
 *
 * Output 1
 * 96
 * -----------------
 * Input 2
 * 3
 * 1 100 100
 * 100 1 100
 * 100 100 1
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * 3
 * 1 100 100
 * 100 100 100
 * 1 100 100
 *
 * Output 3
 * 102
 * -----------------
 * Input 4
 * 6
 * 30 19 5
 * 64 77 64
 * 15 19 97
 * 4 71 57
 * 90 86 84
 * 93 32 91
 *
 * Output 4
 * 208
 * -----------------
 * Input 5
 * 8
 * 71 39 44
 * 32 83 55
 * 51 37 63
 * 89 29 100
 * 83 58 11
 * 65 13 15
 * 47 25 29
 * 60 66 19
 *
 * Output 5
 * 253
 * -----------------
 */
public class BOJ1149 {
    static final int TYPE_CNT = 3;
    static int N;
    static int[][] cost, dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        cost = new int[N][TYPE_CNT];
        dp = new int[N][TYPE_CNT];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < TYPE_CNT; j++) {
                cost[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        System.arraycopy(cost[0], 0, dp[0], 0, TYPE_CNT);

        for (int i = 1; i < N; i++) {
            for (int color1 = 0; color1 < TYPE_CNT; color1++) {
                int color2 = (color1 + 1) % TYPE_CNT;
                int color3 = (color1 + 2) % TYPE_CNT;
                dp[i][color1] = Math.min(dp[i - 1][color2], dp[i - 1][color3]) + cost[i][color1];
            }
        }
        bw.write(String.valueOf(Arrays.stream(dp[N - 1]).min().getAsInt()));

        // close the buffer
        br.close();
        bw.close();
    }
}