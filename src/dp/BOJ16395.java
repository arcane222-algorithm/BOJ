package dp;

import java.io.*;
import java.util.*;



public class BOJ16395 {

    static int n, k;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        final int Size = 31;
        long[][] dp = new long[Size][Size];
        dp[0][0] = dp[1][0] = dp[1][1] = 1;

        for (int i = 2; i < Size; i++) {
            for (int j = 0; j < i; j++) {
                if (j == 0 || j == i - 1) dp[i][j] = 1;
                else dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
            }
        }
        System.out.println(dp[n][k - 1]);

        // close the buffer
        br.close();
        bw.close();
    }
}