package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 오름세 - BOJ3745
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 6
 * 5 2 1 4 5 3
 * 3
 * 1 1 1
 * 4
 * 4 3 2 1
 *
 * Output 1
 * 3
 * 1
 * 1
 * -----------------
 */
public class BOJ3745 {

    static int N;
    static int[] nums, lis;

    public static int lowerBound(int[] values, int begin, int end, int num) {
        int pivot, mid;
        pivot = mid = 0;

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

        String line = null;
        while (true) {
            line = br.readLine();
            if (line == null) break;
            if (line.isEmpty()) break;

            line = line.trim();
            N = Integer.parseInt(line);
            nums = new int[N];
            lis = new int[N];

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) {
                nums[i] = Integer.parseInt(st.nextToken());
            }

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

            bw.write(String.valueOf(lisIdx + 1));
            bw.write('\n');
        }


        // close the buffer
        br.close();
        bw.close();
    }
}