package dp;

import java.io.*;
import java.util.*;


/**
 * 가장 큰 증가 부분 수열 - BOJ11055
 * -----------------
 * category: dp (동적 계획법)
 * -----------------
 * Input 1
 * 10
 * 1 100 2 50 60 3 5 6 7 8
 *
 * Output 1
 * 113
 * -----------------
 * Input 2
 * 5
 * 1 8 2 3 9
 *
 * Output 2
 * 18
 * -----------------
 */
public class BOJ11055 {

    static int N;
    static int[] nums, dp;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        nums = new int[N + 1];
        dp = new int[N + 1];

        for (int i = 1; i <= N; i++) {
            nums[i] = dp[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + nums[i]);
                }
            }
        }

        int max = Integer.MIN_VALUE;
        for (int i = 1; i <= N; i++) {
            max = Math.max(max, dp[i]);
        }
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}
