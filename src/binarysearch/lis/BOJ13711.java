package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * LCS 4 - BOJ13711
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * -----------------
 * Input 1
 * 2
 * 1 2
 * 2 1
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 3
 * 1 2 3
 * 1 3 2
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 6
 * 1 3 5 4 2 6
 * 2 5 1 6 3 4
 *
 * Output 3
 * 3
 * -----------------
 */
public class BOJ13711 {

    static int N;

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
        lis[0] = values[0];

        int lisIdx = 0, valueIdx = 1;
        for (; valueIdx < N; valueIdx++) {
            int nxt = values[valueIdx];

            if (nxt > lis[lisIdx]) {
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

        N = Integer.parseInt(br.readLine());
        int[] valuesA = new int[N];
        int[] valuesB = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int aVal = Integer.parseInt(st.nextToken()) - 1;
            valuesA[i] = aVal;
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int bVal = Integer.parseInt(st.nextToken()) - 1;
            valuesB[bVal] = i;
        }

        int[] temp = new int[N];
        int[] lis = new int[N];
        for (int i = 0; i < N; i++) {
            int aVal = valuesA[i];
            temp[i] = valuesB[aVal];
        }
        bw.write(String.valueOf(getLisLen(temp, lis)));

        // close the buffer
        br.close();
        bw.close();
    }
}