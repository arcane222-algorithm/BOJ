package greedy;

import java.io.*;
import java.util.*;


/**
 * 사탕 배달 - BOJ 17305
 * -----------------
 * Input 1
 * 10 11
 * 3 10
 * 3 20
 * 3 30
 * 3 40
 * 3 50
 * 5 20
 * 5 40
 * 5 60
 * 5 80
 * 5 100
 *
 * Output 1
 * 190
 * -----------------
 */
public class BOJ17305 {

    private static class Candy implements Comparable<Candy> {
        int t, s;   // t: 무게, s: 당도

        public Candy(int t, int s) {
            this.t = t;
            this.s = s;
        }

        @Override
        public int compareTo(Candy c) {
            return Integer.compare(s, c.s);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("t: ");
            sb.append(t);
            sb.append(", s: ");
            sb.append(s);

            return sb.toString();
        }
    }

    static int N, w;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N, w
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        w = Integer.parseInt(st.nextToken());

        // parse candies
        List<Candy> candies3 = new ArrayList<>();
        List<Candy> candies5 = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            if(t == 3) candies3.add(new Candy(t, s));
            else candies5.add(new Candy(t, s));
        }
        Collections.sort(candies3, Collections.reverseOrder());
        Collections.sort(candies5, Collections.reverseOrder());

        // get prefix sum
        long[] candies3PrefixSum = new long[candies3.size() + 1];
        long[] candies5PrefixSum = new long[candies5.size() + 1];
        for(int i = 1; i < candies3PrefixSum.length; i++) {
            if(i == 1)  {
                candies3PrefixSum[i] = candies3.get(i - 1).s;
            } else {
                candies3PrefixSum[i] = candies3PrefixSum[i - 1] + candies3.get(i - 1).s;
            }
        }
        for(int i = 1; i < candies5PrefixSum.length; i++) {
            if(i == 1)  {
                candies5PrefixSum[i] = candies5.get(i - 1).s;
            } else {
                candies5PrefixSum[i] = candies5PrefixSum[i - 1] + candies5.get(i - 1).s;
            }
        }

        long candies3Num = Math.min((w / 3), candies3.size());
        long result = 0;
        while(candies3Num >= 0) {
            long candies5Num = Math.min((w - 3 * candies3Num) / 5, candies5.size());
            if(result < candies3PrefixSum[(int) candies3Num] + candies5PrefixSum[(int) candies5Num]) {
                result = candies3PrefixSum[(int) candies3Num] + candies5PrefixSum[(int) candies5Num];
            }
            candies3Num--;
        }

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}