package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 반도체 설계 - BOJ2352
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 6
 * 4 2 6 3 1 5
 *
 * Output 1
 * 3
 * -----------------
 */
public class BOJ2352 {

    static int n;
    static int[] arr, lis;

    public static int lowerBound(int[] values, int begin, int end, int num) {
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (begin + end) >> 1;
            mid = values[pivot];

            if (mid >= num) end = pivot;
            else begin = pivot + 1;
        }

        return begin;
    }

    public static int getLisLength() {
        lis[0] = arr[0];

        int lisIdx = 0, arrIdx = 1;
        for (; arrIdx < n; arrIdx++) {
            if (lis[lisIdx] < arr[arrIdx]) {
                lisIdx++;
                lis[lisIdx] = arr[arrIdx];
            } else {
                int pos = lowerBound(lis, 0, lisIdx, arr[arrIdx]);
                lis[pos] = arr[arrIdx];
            }
        }

        return lisIdx + 1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Integer.parseInt(br.readLine());
        arr = new int[n];
        lis = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        bw.write(String.valueOf(getLisLength()));

        // close the buffer
        br.close();
        bw.close();
    }
}
