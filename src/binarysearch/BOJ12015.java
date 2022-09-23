package binarysearch;

import java.io.*;
import java.util.*;


/**
 * 가장 긴 증가하는 부분 수열 2 - BOJ12015
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 *
 * Time-Complexity: 수열의 길이 - N
 *                  O(NlogN)
 * -----------------
 * -----------------
 * Input 1
 * 6
 * 10 20 10 30 20 50
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 7
 * 10 20 30 15 10 40 25
 *
 * Output 2
 * 4
 * -----------------
 */
public class BOJ12015 {

    static int N;
    static int[] A;
    static int[] lis;

    public static int lowerBound(int[] values, int begin, int end, int value) {
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid >= value) end = pivot;
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
        A = new int[N];
        lis = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            A[i] = Integer.parseInt(st.nextToken());
        }

        lis[0] = A[0];
        int aIdx = 1, lisIdx = 0;
        for (; aIdx < N; aIdx++) {
            if (lis[lisIdx] < A[aIdx]) {
                lis[++lisIdx] = A[aIdx];
            } else {
                int nxt = lowerBound(lis, 0, lisIdx, A[aIdx]);
                lis[nxt] = A[aIdx];
            }
        }
        bw.write(String.valueOf(lisIdx + 1));

        // close the buffer
        br.close();
        bw.close();
    }
}