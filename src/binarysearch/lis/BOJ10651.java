package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * Cow Jog - BOJ10651
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 5 3
 * 0 1
 * 1 2
 * 2 3
 * 3 2
 * 6 1
 *
 * Output 1
 * 3
 * -----------------
 */
public class BOJ10651 {
    static int N, T;

    public static int upperBound(long[] values, int begin, int end, long value) {
        int pivot;
        long mid;

        while (begin < end) {
            pivot = (begin + end) >> 1;
            mid = values[pivot];

            if (mid > value) end = pivot;
            else begin = pivot + 1;
        }

        return end;
    }

    public static int getLisLen(long[] values) {
        final int Size = values.length;

        long[] lis = new long[Size];
        lis[0] = values[Size - 1];

        int lisIdx = 0, valueIdx = Size - 2;
        for (; valueIdx >= 0; valueIdx--) {
            long nxt = values[valueIdx];

            if (lis[lisIdx] < nxt) {
                lis[++lisIdx] = nxt;
            } else {
                int nxtIdx = upperBound(lis, 0, lisIdx + 1, nxt);
                lis[nxtIdx] = nxt;
                if (lisIdx + 1 == nxtIdx) lisIdx++;
            }
        }

        return lisIdx + 1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        long[] dest = new long[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken());
            int spd = Integer.parseInt(st.nextToken());
            long dist = idx + (long) spd * T;
            dest[i] = dist;
        }
        System.out.println(getLisLen(dest));

        // close the buffer
        br.close();
        bw.close();
    }
}