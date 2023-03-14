package twopointer;

import java.io.*;
import java.util.*;


/**
 * 부분합 - BOJ1806
 * -----------------
 * category: prefix sum (누적 합)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 10 15
 * 5 1 3 5 10 7 4 9 2 8
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ1806 {

    static int N, S;

    public static int subSubLen(int[] nums) {
        final int maxLen = 100000;
        int begin = 0, end = 0;
        int sum = nums[begin], length = maxLen + 1;

        while (true) {
            if (sum < S) {
                if (N - 1 > end) sum += nums[++end];
                else break;
            } else {
                if (end - begin + 1 < length) length = end - begin + 1;
                if (begin <= end) sum -= nums[begin++];
                else break;
            }
        }

        return length == maxLen + 1 ? 0 : length;
    }

    public static void main(String[] args) throws IOException {
        // open the io stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        // write the result
        bw.write(String.valueOf(subSubLen(nums)));

        // close the io stream
        br.close();
        bw.close();
    }
}