package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 책정리 - BOJ1818
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 5
 * 2 1 4 5 3
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ1818 {

    static int N;
    static int[] nums, lis;

    public static int getLisLength() {
        lis[0] = nums[0];

        int lisIdx = 0, numsIdx = 1;
        for (; numsIdx < N; numsIdx++) {
            if (lis[lisIdx] < nums[numsIdx]) {
                lis[++lisIdx] = nums[numsIdx];
            } else {
                int pos = lowerBound(lis, 0, lisIdx, nums[numsIdx]);
                lis[pos] = nums[numsIdx];
            }
        }

        return lisIdx + 1;
    }

    public static int lowerBound(int[] values, int begin, int end, int num) {
        int mid, pivot;

        while (begin < end) {
            pivot = (begin + end) >> 1;
            mid = values[pivot];

            if (mid >= num) end = pivot;
            else begin = pivot + 1;
        }

        return begin;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        lis = new int[N];
        nums = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        int len = getLisLength();
        bw.write(String.valueOf(N - len));

        // close the buffer
        br.close();
        bw.close();
    }
}