package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 주식 - BOJ12014
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 3
 * 10 6
 * 100 50 70 90 75 87 105 78 110 60
 * 6 3
 * 100 90 80 70 60 50
 * 10 4
 * 8 11 9 7 4 6 12 13 5 10
 *
 * Output 1
 * Case #1
 * 1
 * Case #2
 * 0
 * Case #3
 * 1
 * -----------------
 */
public class BOJ12014 {

    static int T, N, K;
    static StringBuilder builder = new StringBuilder();

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

    public static int getLisLength(int[] values) {
        final int Size = values.length;
        int[] lis = new int[Size];
        lis[0] = values[0];

        int lisIdx = 0, valueIdx = 1;
        for (; valueIdx < Size; valueIdx++) {
            int nxt = values[valueIdx];

            if (lis[lisIdx] < nxt) {
                lis[++lisIdx] = nxt;
            } else {
                int nxtPos = lowerBound(lis, 0, lisIdx, nxt);
                lis[nxtPos] = nxt;
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
        for (int i = 1; i <= T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());

            int[] values = new int[N];
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                values[j] = Integer.parseInt(st.nextToken());
            }

            int len = getLisLength(values);
            int result = K <= len ? 1 : 0;

            builder.append("Case #").append(i).append('\n');
            builder.append(result).append('\n');
        }
        bw.write(builder.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}