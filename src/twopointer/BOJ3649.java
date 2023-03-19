package twopointer;

import java.io.*;
import java.util.*;


/**
 * 로봇 프로젝트 - BOJ3649
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 1
 * 4
 * 9999998
 * 1
 * 2
 * 9999999
 *
 * Output 1
 * yes 1 9999999
 * -----------------
 */
public class BOJ3649 {

    private static final int NANO = 10000000;

    private static int x, n;
    private static int[] nums;

    public static String twoPointerSearch() {
        int lp = 0, rp = n - 1;
        int lr = 0, rr = 0;
        int gapTmp = -1;

        while (lp < rp) {
            int val = nums[lp] + nums[rp];
            if (val > x) {
                rp--;
            } else if (val < x) {
                lp++;
            } else {
                if (gapTmp < nums[rp] - nums[lp]) {
                    gapTmp = nums[rp] - nums[lp];
                    lr = nums[lp];
                    rr = nums[rp];
                }
                rp--;
            }
        }

        if (gapTmp == -1) {
            return "danger";
        } else {
            return "yes " + lr + " " + rr;
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;
            ;
            x = Integer.parseInt(line) * NANO;
            n = Integer.parseInt(br.readLine());

            nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = Integer.parseInt(br.readLine());
            }
            Arrays.sort(nums);
            bw.write(twoPointerSearch() + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}