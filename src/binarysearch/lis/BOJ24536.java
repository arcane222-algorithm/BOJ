package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 정원장어 - BOJ24536
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 6
 * LLRLRL
 * 2 1 4 8 5 7
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 6
 * LRLLRL
 * 2 3 5 7 9 10
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 5
 * LLLLL
 * 4 1 2 5 3
 *
 * Output 3
 * 2
 * -----------------
 * Input 4
 * 3
 * RLL
 * 7 4 5
 *
 * Output 4
 * 1
 * -----------------
 */
public class BOJ24536 {

    static int N;
    static int[] values;
    static String dir;
    static List<Integer> left, right;

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

    public static int[] getLisLengths(List<Integer> values) {
        final int Size = values.size();
        int[] lis = new int[Size];
        int[] lengths = new int[Size];

        lis[0] = values.get(0);
        lengths[0] = 1;

        int lisIdx = 0, valueIdx = 1;
        for (; valueIdx < Size; valueIdx++) {
            int nxt = values.get(valueIdx);

            if (lis[lisIdx] < nxt) {
                lis[++lisIdx] = nxt;
            } else {
                int nxtIdx = lowerBound(lis, 0, lisIdx, nxt);
                lis[nxtIdx] = nxt;
            }
            lengths[valueIdx] = lisIdx + 1;
        }

        return lengths;
    }

    public static void reverse(List<Integer> list) {
        final int Size = list.size();

        for (int i = 0; i < Size >> 1; i++) {
            int left = list.get(i);
            int right = list.get(Size - i - 1);

            list.set(i, right);
            list.set(Size - i - 1, left);
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        left = new ArrayList<>();
        right = new ArrayList<>();

        N = Integer.parseInt(br.readLine());
        values = new int[N];
        dir = br.readLine();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int val = Integer.parseInt(st.nextToken());

            if (dir.charAt(i) == 'L') left.add(val);
            else right.add(val);
        }
        reverse(right);

        int[] lisLeftLens = left.size() > 0 ? getLisLengths(left) : null;
        int[] lisRightLens = right.size() > 0 ? getLisLengths(right) : null;

        int leftCnt = 0, rightCnt = 0, max = 0;
        for (int i = 0; i < N; i++) {
            int leftSurviveCnt, rightSurviveCnt;
            if (dir.charAt(i) == 'L') {
                leftCnt++;
                leftSurviveCnt = leftCnt > 0 && lisLeftLens != null ? lisLeftLens[leftCnt - 1] : 0;
                rightSurviveCnt = rightCnt < right.size() && lisRightLens != null ? lisRightLens[right.size() - rightCnt - 1] : 0;
            } else {
                leftSurviveCnt = leftCnt > 0 && lisLeftLens != null ? lisLeftLens[leftCnt - 1] : 0;
                rightSurviveCnt = rightCnt < right.size() && lisRightLens != null ? lisRightLens[right.size() - rightCnt - 1] : 0;
                rightCnt++;
            }

            max = Math.max(max, leftSurviveCnt + rightSurviveCnt);
        }
        bw.write(String.valueOf(N - max));

        // close the buffer
        br.close();
        bw.close();
    }
}