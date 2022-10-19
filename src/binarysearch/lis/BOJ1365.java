package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 꼬인 전깃줄 - BOJ1365
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 4
 * 2 3 4 1
 *
 * Output 1
 * 1
 * -----------------
 */
public class BOJ1365 {

    static int N;
    static int[] nums, lis;

    public static int getLisLength() {
        lis[0] = nums[0];

        int lisIdx = 0, numsIdx = 1;
        for (; numsIdx < N; numsIdx++) {
            if (lis[lisIdx] < nums[numsIdx]) {
                lisIdx++;
                lis[lisIdx] = nums[numsIdx];
            } else {
                int pos = lowerBound(lis, 0, lisIdx, nums[numsIdx]);
                lis[pos] = nums[numsIdx];
            }
        }

        return lisIdx + 1;
    }

    public static int lowerBound(int[] values, int begin, int end, int target) {
        int pivot = 0, mid = 0;

        while (begin < end) {
            pivot = (begin + end) >> 1;
            mid = values[pivot];

            if (mid >= target) end = pivot;
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
        nums = new int[N];
        lis = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        int lisLen = getLisLength();
        bw.write(String.valueOf(N - lisLen));

        // close the buffer
        br.close();
        bw.close();
    }
}
