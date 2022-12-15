package dp;

import java.io.*;
import java.util.*;


/**
 * 1로 만들기 - BOJ1463
 * -----------------
 * category: dp (동적 계획법)
 * -----------------
 * Input 1
 * 10
 * 10 -4 3 1 5 6 -35 12 21 -1
 *
 * Output 1
 * 33
 * -----------------
 * Input 2
 * 10
 * 2 1 -4 3 4 -4 6 5 -5 1
 *
 * Output 2
 * 14
 * -----------------
 * Input 3
 * 5
 * -1 -2 -3 -4 -5
 *
 * Output 3
 * -1
 * -----------------
 */
public class BOJ1912 {

    static int N;
    static int[] nums, dp;

    public static int getMaxValue() {
        int max = Integer.MIN_VALUE;
        for(int val : dp) {
            max = Math.max(max, val);
        }
        return max;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        nums = new int[N];
        dp = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        dp[0] = nums[0];
        for(int i = 1; i < N; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
        }
        bw.write(String.valueOf(getMaxValue()));

        // close the buffer
        br.close();
        bw.close();
    }
}