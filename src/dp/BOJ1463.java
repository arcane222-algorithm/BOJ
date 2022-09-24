package dp;

import java.io.*;
import java.util.*;

/**
 * 1로 만들기 - BOJ1463
 * -----------------
 * category: dp (동적 계획법)
 *
 * -----------------
 * -----------------
 * Input 1
 * 2
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 10
 *
 * Output 2
 * 3
 * -----------------
 */
public class BOJ1463 {

    static int[] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        int num = Integer.parseInt(br.readLine());
        dp = new int[num + 1];
        for (int i = 2; i < num + 1; i++) {
            dp[i] = dp[i - 1] + 1;
            if (i % 3 == 0) dp[i] = Math.min(dp[i / 3] + 1, dp[i]);
            if (i % 2 == 0) dp[i] = Math.min(dp[i / 2] + 1, dp[i]);
        }
        bw.write(String.valueOf(dp[num]));

        // close the buffer
        br.close();
        bw.close();
    }
}
