package dp;

import java.io.*;
import java.util.*;


/**
 * 평범한 배낭 - BOJ12865
 * -----------------
 * category: dp (동적 계획법)
 *
 * Time-Complexity: 물품의 수 N, 무게 K
 *                  O(NK)
 * -----------------
 * -----------------
 * Input 1
 * 4 7
 * 6 13
 * 4 8
 * 3 6
 * 5 12
 *
 * Output 1
 * 14
 * -----------------
 */
public class BOJ12865 {

    private static class Item implements Comparable<Item> {
        int w, v;

        public Item(int w, int v) {
            this.w = w;
            this.v = v;
        }

        @Override
        public int compareTo(Item i) {
            return Integer.compare(w, i.w);
        }
    }

    static int N, K;
    static int[][] items;
    static int[][] dp = new int[101][100001];


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        items = new int[2][N + 1];

        for (int i = 1; i < N + 1; i++) {
            st = new StringTokenizer(br.readLine());
            int w = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            items[0][i] = w;
            items[1][i] = v;
        }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < K + 1; j++) {
                if (j - items[0][i] >= 0) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - items[0][i]] + items[1][i]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        bw.write(String.valueOf(dp[N][K]));

        // close the buffer
        br.close();
        bw.close();
    }
}
