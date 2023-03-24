package twopointer;

import java.io.*;
import java.util.*;


/**
 * 두 수의 합 - BOJ9024
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 4
 * 10 8
 * -7 9 2 -4 12 1 5 -3 -2 0
 * 10 4
 * -7 9 2 -4 12 1 5 -3 -2 0
 * 4 20
 * 1 7 3 5
 * 5 10
 * 3 9 7 1 5
 *
 * Output 1
 * 1
 * 5
 * 1
 * 2
 * -----------------
 */
public class BOJ9024 {

    static int T, N, K;
    static int[] nums;

    public static int twoPointerSearch(int target) {
        int lp = 0, rp = N - 1;
        int tmp = Integer.MAX_VALUE;
        int result = 0;

        while (lp < rp) {
            int val = nums[lp] + nums[rp];
            if (Math.abs(val - target) < Math.abs(tmp - target)) {
                tmp = val;
                result = 1;
            } else if (Math.abs(val - target) == Math.abs(tmp - target)) {
                result++;
            }

            if (val > target) rp--;
            else lp++;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            nums = new int[N];

            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                nums[j] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(nums);
            bw.write(twoPointerSearch(K) + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}