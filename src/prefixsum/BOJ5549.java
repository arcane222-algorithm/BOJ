package prefixsum;

import java.io.*;
import java.util.*;


/**
 * 행성 탐사 - BOJ5549
 * -----------------
 * category: prefix sum (누적 합)
 * -----------------
 * Input 1
 * 4 7
 * 4
 * JIOJOIJ
 * IOJOIJO
 * JOIJOOI
 * OOJJIJO
 * 3 5 4 7
 * 2 2 3 6
 * 2 2 2 2
 * 1 1 4 7
 *
 * Output 1
 * 1 3 2
 * 3 5 2
 * 0 1 0
 * 10 11 7
 * -----------------
 */
public class BOJ5549 {

    static final char[] type = {'J', 'O', 'I'};
    static final int TYPE_COUNT = 3;
    static int N, M, K;
    static String[] map;

    static int[][][] prefixSum;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(br.readLine());

        map = new String[N + 1];
        prefixSum = new int[N + 1][M + 1][TYPE_COUNT];

        for (int i = 1; i <= N; i++) {
            map[i - 1] = br.readLine();
            for (int j = 1; j <= M; j++) {
                for (int k = 0; k < TYPE_COUNT; k++) {
                    prefixSum[i][j][k] = prefixSum[i - 1][j][k] + prefixSum[i][j - 1][k] - prefixSum[i - 1][j - 1][k];
                    if (type[k] == map[i - 1].charAt(j - 1)) {
                        prefixSum[i][j][k] += 1;
                    }
                }
            }
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            for (int j = 0; j < TYPE_COUNT; j++) {
                int val = prefixSum[c][d][j] - prefixSum[c][b - 1][j] - prefixSum[a - 1][d][j] + prefixSum[a - 1][b - 1][j];
                bw.write(val + "");
                if (j < TYPE_COUNT - 1)
                    bw.write(" ");
            }
            bw.write("\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}
