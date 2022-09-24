package dp;

import java.io.*;
import java.util.*;


/**
 * 포도주 시식 - BOJ2156
 * -----------------
 * category: dp (동적 계획법)
 *
 * -----------------
 * -----------------
 * Input 1
 * 6
 * 6
 * 10
 * 13
 * 9
 * 8
 * 1
 *
 * Output 1
 * 33
 * -----------------
 */
public class BOJ2156 {

    static int n;
    static int[] values;
    static int[] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Integer.parseInt(br.readLine());
        values = new int[n + 1];
        dp = new int[n + 1];

        for (int i = 1; i < n + 1; i++) {
            values[i] = Integer.parseInt(br.readLine());
        }
        dp[1] = values[1];
        if (n > 1) {
            dp[2] = dp[1] + values[2];
        }
        for (int i = 3; i < n + 1; i++) {
            dp[i] = Math.max(dp[i - 2] + values[i], dp[i - 3] + values[i - 1] + values[i]);
            dp[i] = Math.max(dp[i - 1], dp[i]);
        }

        int max = 0;
        for(int i = 0; i < n + 1; i++) {
            max = Math.max(dp[i], max);
        }
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}