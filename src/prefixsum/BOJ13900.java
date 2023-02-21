package prefixsum;

import java.io.*;
import java.util.*;


/**
 * 순서쌍의 곱의 합 - BOJ13900
 * -----------------
 * category: mathematics (수학)
 *           prefix sum (누적 합)
 * -----------------
 * Input 1
 * 3
 * 2 3 4
 *
 * Output 1
 * 26
 * -----------------
 * Input 2
 * 4
 * 1 2 3 4
 *
 * Output 2
 * 35
 * -----------------
 * Input 3
 * 4
 * 2 3 2 4
 *
 * Output 3
 * 44
 * -----------------
 */
public class BOJ13900 {

    static int N;
    static long[] nums, prefixSum;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        nums = new long[N];
        prefixSum = new long[N];

        st = new StringTokenizer(br.readLine());
        nums[0] = prefixSum[0] = Integer.parseInt(st.nextToken());

        for (int i = 1; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
            prefixSum[i] = prefixSum[i - 1] + nums[i];
        }

        long result = 0;
        for (int i = 0; i < N; i++) {
            long gap = prefixSum[N - 1] - prefixSum[i];
            result += nums[i] * gap;
        }
        bw.write(result + "");

        // close the buffer
        br.close();
        bw.close();
    }
}