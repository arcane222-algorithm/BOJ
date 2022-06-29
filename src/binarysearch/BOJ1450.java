package binarysearch;

import java.io.*;
import java.util.*;


/**
 * 냅색 문제 - BOJ 1450
 * -----------------
 * Input 1
 * 2 1
 * 1 1
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 1 1
 * 1
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 2 2
 * 1 1
 *
 * Output 3
 * 4
 * -----------------
 * Input 4
 * 30 30
 * 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
 *
 * Output 4
 * 1073741824
 * -----------------
 */
public class BOJ1450 {

    static int N, C;
    static long[] items;

    public static long upperBound(List<Long> values, long num) {
        int begin = 0, end = values.size(), pivot = 0;
        long mid = 0;

        while(begin < end) {
            pivot = (end + begin) >> 1;
            mid = values.get(pivot);

            if(mid > num) end = pivot;
            else begin = pivot + 1;
        }

        return end;
    }

    public static void dfs(int begin, int end, long sum, List<Long> group) {
        if(begin <= end) {
            dfs(begin + 1, end, sum, group);
            dfs(begin + 1, end, sum + items[begin], group);
        } else {
            group.add(sum);
        }
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        items = new long[N + 1];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            items[i] = Long.parseLong(st.nextToken());
        }

        List<Long> leftGroup = new ArrayList<>();
        List<Long> rightGroup = new ArrayList<>();

        dfs(0, (N >> 1) - 1, 0, leftGroup);
        dfs(N >> 1, N - 1, 0, rightGroup);
        rightGroup.sort(Long::compare);

        long result = 0;
        for(int i = 0; i < leftGroup.size(); i++) {
            result += upperBound(rightGroup, C - leftGroup.get(i));
        }

        // write the result
        bw.write(String.valueOf(result));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}