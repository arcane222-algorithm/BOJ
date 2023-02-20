package prefixsum;

import java.io.*;
import java.util.*;


/**
 * 2차원 배열의 합 - BOJ2167
 * -----------------
 * category: implementation (구현)
 *           prefix sum (누적 합)
 * -----------------
 * Input 1
 * 2 3
 * 1 2 4
 * 8 16 32
 * 3
 * 1 1 2 3
 * 1 2 1 2
 * 1 3 2 3
 *
 * Output 1
 * 63
 * 2
 * 36
 * -----------------
 */
public class BOJ2167 {

    static int N, M, K;
    static int[][] prefixSum;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        prefixSum = new int[N + 1][M + 1];
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= M; j++) {
                int curr = Integer.parseInt(st.nextToken());
                prefixSum[i][j] = prefixSum[i - 1][j] + prefixSum[i][j - 1] - prefixSum[i - 1][j - 1] + curr;
            }
        }

        K = Integer.parseInt(br.readLine());
        for (int k = 0; k < K; k++) {
            st = new StringTokenizer(br.readLine());
            int i = Integer.parseInt(st.nextToken());
            int j = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());

            int res = prefixSum[y][x] - prefixSum[y][j - 1] - prefixSum[i - 1][x] + prefixSum[i - 1][j - 1];
            bw.write(res + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}