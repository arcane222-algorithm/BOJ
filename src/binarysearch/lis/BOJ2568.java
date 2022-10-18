package binarysearch.lis;

import java.io.*;
import java.util.*;


/**
 * 전깃줄 - 2 - BOJ2568
 * -----------------
 * category: binary search (이분탐색)
 *           LIS in O(nlogn) (가장 긴 증가하는 부분 수열: O(nlogn)
 * -----------------
 * Input 1
 * 8
 * 1 8
 * 3 9
 * 2 2
 * 4 1
 * 6 4
 * 10 10
 * 9 7
 * 7 6
 *
 * Output 1
 * 3
 * 1
 * 3
 * 4
 * -----------------
 */
public class BOJ2568 {

    private static class Pair {
        int x, y;
        boolean removed;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int N;
    static Pair[] nums;
    static int[] lis, idxes;
    static StringBuilder result = new StringBuilder();

    public static int lowerBound(int[] values, int begin, int end, int num) {
        int pivot, mid;

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

        N = Integer.parseInt(br.readLine());
        nums = new Pair[N];
        lis = new int[N];
        idxes = new int[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            nums[i] = new Pair(a, b);
        }
        Arrays.sort(nums, (Comparator.comparingInt(o -> o.x)));

        lis[0] = nums[0].y;
        int lisIdx = 0, numsIdx = 1;
        for (; numsIdx < N; numsIdx++) {
            int curr = nums[numsIdx].y;
            if (lis[lisIdx] < curr) {
                lis[++lisIdx] = curr;
                idxes[numsIdx] = lisIdx;
            } else {
                int pos = lowerBound(lis, 0, lisIdx, curr);
                lis[pos] = curr;
                idxes[numsIdx] = pos;
            }
        }

        int count = 0;
        for (int i = N - 1; i >= 0; i--) {
            if (idxes[i] != lisIdx) {
                nums[i].removed = true;
                count++;
            } else {
                lisIdx--;
            }
        }


        result.append(count).append('\n');
        for (Pair p : nums) {
            if (p.removed) {
                result.append(p.x).append('\n');
            }
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}