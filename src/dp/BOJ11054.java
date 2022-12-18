package dp;

import java.io.*;
import java.util.*;


/**
 * 가장 긴 바이토닉 부분 수열 - BOJ11054
 * -----------------
 * category: dp (동적 계획법)
 * -----------------
 * Input 1
 * 10
 * 1 5 2 1 4 3 4 5 2 1
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ11054 {

    static int N;
    static int[] nums;

    public static int[] getLis() {
        int[] lis = new int[N];
        for (int i = 0; i < N; i++) {
            lis[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    lis[i] = Math.max(lis[i], lis[j] + 1);
                }
            }
        }

        return lis;
    }

    public static int[] getLds() {
        int[] lds = new int[N];
        for (int i = N - 1; i >= 0; i--) {
            lds[i] = 1;
            for (int j = N - 1; j > i; j--) {
                if(nums[i] > nums[j]) {
                    lds[i] = Math.max(lds[i], lds[j] + 1);
                }
            }
        }

        return lds;
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

        int[] lis = getLis();
        int[] lds = getLds();

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            max = Math.max(max, lis[i] + lds[i] - 1);
        }
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}
