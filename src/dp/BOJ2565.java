package dp;

import java.io.*;
import java.util.*;


/**
 * 전깃줄 - BOJ2565
 * -----------------
 * category: dp (동적 계획법)
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
 * -----------------
 * Input 2
 * 10
 * 1 6
 * 2 8
 * 3 2
 * 4 9
 * 5 5
 * 6 10
 * 7 4
 * 8 1
 * 9 7
 * 10 3
 *
 * Output 2
 * 6
 * -----------------
 */
public class BOJ2565 {

    private static class Pair {
        int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static int N;
    static int[] dp;
    static Pair[] telephonePole;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        telephonePole = new Pair[N];
        dp = new int[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            telephonePole[i] = new Pair(x, y);
        }
        Arrays.sort(telephonePole, Comparator.comparingInt(o -> o.x));

        for (int i = 0; i < N; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (telephonePole[i].y > telephonePole[j].y) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        OptionalInt opMax = Arrays.stream(dp).max();
        assert opMax.isPresent();
        int maxCount = opMax.getAsInt();
        bw.write(String.valueOf(N - maxCount));

        // close the buffer
        br.close();
        bw.close();
    }
}