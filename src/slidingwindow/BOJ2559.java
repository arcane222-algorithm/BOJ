package slidingwindow;

import java.io.*;
import java.util.*;


/**
 * 수열 - BOJ2559
 * -----------------
 * category: prefix sum (누적 합)
 *           two-pointer (두 포인터)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 10 2
 * 3 -2 -4 -9 0 3 7 13 8 -3
 *
 * Output 1
 * 21
 * -----------------
 * Input 2
 * 10 5
 * 3 -2 -4 -9 0 3 7 13 8 -3
 *
 * Output 2
 * 31
 * -----------------
 */
public class BOJ2559 {

    static int N, K;
    static int[] nums;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        int max = 0, curr = 0;
        for (int i = 0; i < K; i++) {
            curr += nums[i];
        }
        max = curr;

        for (int i = 1; i <= N - K; i++) {
            curr -= nums[i - 1];
            curr += nums[K + i - 1];
            max = Math.max(max, curr);
        }
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}