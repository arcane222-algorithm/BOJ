package dp;

import java.io.*;
import java.util.*;


/**
 * 2×n 타일링 - BOJ11726
 * -----------------
 * category: dp (동적 계획법)
 * -----------------
 * Input 1
 * 2
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 9
 *
 * Output 2
 * 55
 * -----------------
 */
public class BOJ11726 {

    static final int MOD = (int) 1e4 + 7;

    static int n;
    static int[] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Integer.parseInt(br.readLine());
        dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2]) % MOD;
        }

        System.out.println(dp[n]);

        // close the buffer
        br.close();
        bw.close();
    }
}