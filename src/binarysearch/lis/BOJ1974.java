package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * Jump Jump Championship - BOJ1974
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 2
 * 4
 * 3 1 2 4
 * 5
 * 3 2 4 2 5
 *
 * Output 1
 * 3
 * 2 3 4
 * 3
 * 1 3 5
 * -----------------
 */
public class BOJ1974 {

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

    public static void getLIS(int[] values, int[] lis, int[] idxes) {
        lis[0] = values[0];

        int lisIdx = 0, valueIdx = 1;
        for (; valueIdx < N; valueIdx++) {
            int nxt = values[valueIdx];

            if (lis[lisIdx] < nxt) {
                lis[++lisIdx] = nxt;
                idxes[valueIdx] = lisIdx;
            } else {
                int nxtIdx = lowerBound(lis, 0, lisIdx, nxt);
                lis[nxtIdx] = nxt;
                idxes[valueIdx] = nxtIdx;
            }
        }

        Stack<Integer> stack = new Stack<>();
        for (int i = N - 1; i >= 0; i--) {
            int idx = idxes[i];
            if (lisIdx == idx) {
                stack.add(i);
                lisIdx--;
            }
        }

        result.append(stack.size()).append('\n');
        while (!stack.isEmpty()) {
            result.append(stack.pop() + 1).append(' ');
        }

        result.delete(result.length() - 1, result.length());
        result.append('\n');
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
            int[] idxes = new int[N];

            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                values[j] = Integer.parseInt(st.nextToken());
            }
            getLIS(values, lis, idxes);
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}