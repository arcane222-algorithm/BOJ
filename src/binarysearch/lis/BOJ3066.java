package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 브리징 시그널 - BOJ3066
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 3
 * 6
 * 4
 * 2
 * 6
 * 3
 * 1
 * 5
 * 10
 * 2
 * 3
 * 4
 * 5
 * 6
 * 7
 * 8
 * 9
 * 10
 * 1
 * 9
 * 5
 * 8
 * 9
 * 2
 * 3
 * 1
 * 7
 * 4
 * 6
 *
 * Output 1
 * 3
 * 9
 * 4
 * -----------------
 */
public class BOJ3066 {

    static int T, N;
    static StringBuilder result = new StringBuilder();

    public static int lowerBound(int[] values, int begin, int end, int value) {
        int pivot, mid;

        while (begin < end) {
            pivot = (begin + end) >> 1;
            mid = values[pivot];

            if (mid >= value) end = pivot;
            else begin = pivot + 1;
        }

        return begin;
    }

    public static int getLisLen(int[] values, int[] lis) {
        final int Size = values.length;
        int lisIdx = 0, valueIdx = 1;

        lis[0] = values[0];
        for (; valueIdx < Size; valueIdx++) {
            int nxt = values[valueIdx];

            if (lis[lisIdx] < nxt) {
                lis[++lisIdx] = nxt;
            } else {
                int nxtIdx = lowerBound(lis, 0, lisIdx, nxt);
                lis[nxtIdx] = nxt;
            }
        }

        return lisIdx + 1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            int[] values = new int[N];
            int[] lis = new int[N];

            for (int j = 0; j < N; j++) {
                values[j] = Integer.parseInt(br.readLine());
            }

            int len = getLisLen(values, lis);
            result.append(len);
            if (i < T - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}