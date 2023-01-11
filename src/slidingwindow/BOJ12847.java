package slidingwindow;

import java.io.*;
import java.util.*;


/**
 * 꿀 아르바이트 - BOJ12847
 * -----------------
 * category: prefix sum (누적 합)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 5 3
 * 10 20 30 20 10
 *
 * Output 1
 * 70
 * -----------------
 */
public class BOJ12847 {

    public static long slidingWindow(final int ws, long[] nums) {
        long max, curr;
        max = curr = 0;
        for(int i = 0; i < ws; i++) {
            curr += nums[i];
        }
        max = curr;

        for(int i = 1; i <= nums.length - ws; i++) {
            curr -= nums[i - 1];
            curr += nums[i + ws - 1];
            max = Math.max(max, curr);
        }

        return max;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int T = Integer.parseInt(st.nextToken());

        long[] nums = new long[N];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        bw.write(slidingWindow(T, nums) + "");
        bw.flush();

        br.close();
        bw.close();
    }
}
