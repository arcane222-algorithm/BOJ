package dp;

import java.io.*;
import java.util.*;


/**
 * 파도반 수열 - BOJ9461
 * -----------------
 * category: mathematics (수학)
 *           dp (동적 계획법)
 * -----------------
 * Input 1
 * 2
 * 6
 * 12
 *
 * Output 1
 * 3
 * 16
 * -----------------
 * Input 2
 * 1
 * 100
 *
 * Output 2
 * 888855064897
 * -----------------
 */
public class BOJ9461 {
    static final int MAX_LEN = 100;

    static int T, N;
    static long[] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        dp = new long[MAX_LEN + 1];
        for (int i = 1; i <= 3; i++) {
            dp[i] = 1;
        }
        for (int i = 4; i <= MAX_LEN; i++) {
            dp[i] = dp[i - 2] + dp[i - 3];
        }

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            bw.write(dp[N] + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}