package datastructure.priorityqueue;

import java.io.*;
import java.util.*;

/**
 * 카드 정렬하기 - BOJ1715
 * -----------------
 * -----------------
 * Input 1
 * 3
 * 10
 * 20
 * 40
 *
 * Output 1
 * 100
 * -----------------
 */
public class BOJ1715 {

    static int N;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Integer.parseInt(br.readLine());
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for(int i = 0; i < N; i++) {
            pq.add(Long.parseLong(br.readLine()));
        }

        long ans = 0;
        while(pq.size() > 1)  {
            long n1 = pq.poll();
            long n2 = pq.poll();
            ans += (n1 + n2);
            pq.add(n1 + n2);
        }

        bw.write(String.valueOf(ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
