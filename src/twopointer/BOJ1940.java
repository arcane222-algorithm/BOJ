package twopointer;

import java.io.*;
import java.util.*;


/**
 * 주몽 - BOJ1940
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 6
 * 9
 * 2 7 4 1 5 3
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ1940 {

    static int N, M;
    static int[] nums;

    public static int twoPointerSearch() {
        int lp = 0, rp = nums.length - 1;
        int count = 0;

        while (lp < rp) {
            int tmp = nums[lp] + nums[rp];

            if (tmp < M)
                lp++;
            else if (tmp > M)
                rp--;
            else {
                count++;
                lp++;
                rp--;
            }
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        M = Integer.parseInt(br.readLine());
        nums = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }
        Arrays.sort(nums);
        bw.write(twoPointerSearch() + "");

        // close the buffer
        br.close();
        bw.close();
    }
}