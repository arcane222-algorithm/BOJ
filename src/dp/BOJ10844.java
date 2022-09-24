package dp;

import java.io.*;
import java.util.*;


/**
 * 쉬운 계단 수 - BOJ10844
 * -----------------
 * category: dp (동적 계획법)
 *
 * -----------------
 * -----------------
 * Input 1
 * 1
 *
 * Output 1
 * 9
 * -----------------
 * Input 2
 * 2
 *
 * Output 2
 * 17
 * -----------------
 */
public class BOJ10844 {

    static final int MOD = (int) 1e9;
    static int N;
    static int[][] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        dp = new int[N + 1][10];

        for (int i = 1; i < 10; i++) {
            dp[1][i] = 1;
        }

        for (int i = 2; i < N + 1; i++) {
            for (int j = 0; j < 10; j++) {
                if (j == 0) {
                    dp[i][j] = dp[i - 1][j + 1] % MOD;
                } else if (j == 9) {
                    dp[i][j] = dp[i - 1][j - 1] % MOD;
                } else {
                    dp[i][j] = (dp[i - 1][j - 1] % MOD + dp[i - 1][j + 1] % MOD) % MOD;
                }
            }
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum = (sum % MOD + dp[N][i] % MOD) % MOD;
        }
        bw.write(String.valueOf(sum));

        // close the buffer
        br.close();
        bw.close();
    }
}
