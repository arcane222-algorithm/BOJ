package dp;

import java.io.*;
import java.util.*;


/**
 * 가장 긴 증가하는 부분 수열 - BOJ11053
 * -----------------
 * category: dp (동적 계획법)
 *
 * Time-Complexity: 수열 A의 길이 - N
 *                  O(N^2)
 * -----------------
 * -----------------
 * Input 1
 * 6
 * 10 20 10 30 20 50
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ11053 {

    static int N;
    static int[] A;
    static int[] dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        A = new int[N];
        dp = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            A[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (A[i] > A[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                }
            }
        }

        int max = 0;
        for(int i = 0; i < N; i++) {
            max = Math.max(dp[i], max);
        }
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}
