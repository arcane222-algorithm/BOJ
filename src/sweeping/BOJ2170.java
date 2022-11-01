package sweeping;

import java.io.*;
import java.util.*;


/**
 * 선 긋기 - BOJ2170
 * -----------------
 * category: sorting (정렬)
 *           sweeping (스위핑)
 * -----------------
 * Input 1
 * 4
 * 1 3
 * 2 5
 * 3 5
 * 6 7
 *
 * Output 1
 * 5
 * -----------------
 */
public class BOJ2170 {

    private static class Pair {
        int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int N;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        List<Pair> points = new ArrayList<>(N);

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points.add(new Pair(x, y));
        }

        points.sort((o1, o2) -> {
            if (o1.x == o2.x) return Integer.compare(o1.y, o2.y);
            return Integer.compare(o1.x, o2.x);
        });

        long begin = points.get(0).x;
        long end = points.get(0).y;
        long gap = end - begin;

        for (int i = 1; i < N; i++) {
            long nextBegin = points.get(i).x;
            long nextEnd = points.get(i).y;

            if (begin <= nextBegin && nextEnd <= end) {
                continue;
            } else if (nextBegin < end) {
                gap += nextEnd - end;
            } else {
                gap += nextEnd - nextBegin;
            }

            begin = points.get(i).x;
            end = points.get(i).y;
        }
        bw.write(String.valueOf(gap));

        // close the buffer
        br.close();
        bw.close();
    }
}