package twopointer;

import java.io.*;
import java.util.*;


/**
 * 용액 합성하기 - BOJ14921
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 5
 * -101 -3 -1 5 93
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 2
 * -100000 -99999
 *
 * Output 2
 * -199999
 * -----------------
 * Input 3
 * 7
 * -698 -332 -123 54 531 535 699
 *
 * Output 3
 * 1
 * -----------------
 */
public class BOJ14921 {

    static final int TARGET = 0;

    static int N;
    static int[] nums;

    public static int twoPointerSearch() {
        int lp = 0, rp = N - 1;
        int lr = 0, rr = N - 1, tmp = Math.abs(nums[lr] + nums[rr]);

        while (lp < rp) {
            int val = nums[lp] + nums[rp];
            int abs = Math.abs(val);
            if (tmp > abs) {
                tmp = abs;
                lr = lp;
                rr = rp;
            }

            if (val < TARGET) {
                lp++;
            } else if (val > TARGET) {
                rp--;
            } else {
                return nums[lp] + nums[rp];
            }
        }

        return nums[lr] + nums[rr];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
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