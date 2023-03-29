package twopointer;

import java.io.*;
import java.util.*;


/**
 * 용액  - BOJ2467
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 5
 * -99 -2 -1 4 98
 *
 * Output 1
 * -99 98
 * -----------------
 * Input 2
 * 4
 * -100 -2 -1 103
 *
 * Output 1
 * -2 -1
 * -----------------
 */
public class BOJ2467 {

    static final int BUFFER_SIZE = 1 << 20;

    static int N;
    static int[] nums;

    public static int abs(int num) {
        return num >= 0 ? num : -num;
    }

    public static String twoPointerSearch(int target) {
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

            if (val < target) {
                lp++;
            } else if (val > target) {
                rp--;
            } else {
                return nums[lp] + " " + nums[rp];
            }
        }

        return nums[lr] + " " + nums[rr];
    }

    public static int nextInt(DataInputStream din) throws IOException {
        int result = 0;
        byte b = din.readByte();
        while (b <= ' ') {
            b = din.readByte();
        }

        boolean positive = b == '+';
        boolean negative = b == '-';
        if (positive || negative) {
            b = din.readByte();
        }

        do {
            result = 10 * result + (b - '0');
            b = din.readByte();
        } while (b >= '0' && b <= '9');

        return negative ? -result : result;
    }

    public static void main(String[] args) throws IOException {
        DataInputStream din = new DataInputStream(new BufferedInputStream(System.in, BUFFER_SIZE));
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(System.out, BUFFER_SIZE));

        N = nextInt(din);
        nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = nextInt(din);
        }
        Arrays.sort(nums);
        dos.writeBytes(twoPointerSearch(0));

        din.close();
        dos.close();
    }
}
