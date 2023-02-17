package prefixsum;

import java.io.*;
import java.util.*;


/**
 * Predictable Queue - BOJ16510
 * -----------------
 * category: prefix sum (누적 합)
 *           binary search (이분 탐색)
 * -----------------
 * Input 1
 * 7 4
 * 1 2 3 1 2 3 1
 * 1
 * 8
 * 11
 * 14
 *
 * Output 1
 * 1
 * 4
 * 5
 * 7
 * -----------------
 * Input 2
 * 7 4
 * 1 2 3 1 2 3 1
 * 3
 * 8
 * 11
 * 14
 *
 * Output 2
 * 2
 * 4
 * 5
 * 7
 * -----------------
 */
public class BOJ16510 {

    static int N, M;
    static int[] values;

    public static int lowerBound(int num) {
        int begin = 0, end = values.length;
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid >= num) end = pivot;
            else begin = pivot + 1;
        }

        return begin;
    }

    public static int upperBound(int num) {
        int begin = 0, end = values.length;
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid > num) end = pivot;
            else begin = pivot + 1;
        }

        return end;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        values = new int[N];
        values[0] = Integer.parseInt(st.nextToken());
        for (int i = 1; i < N; i++) {
            values[i] = values[i - 1] + Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < M; i++) {
            int q = Integer.parseInt(br.readLine());
            int idx = upperBound(q);
            bw.write(idx + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}