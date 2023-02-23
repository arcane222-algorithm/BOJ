package prefixsum;

import java.io.*;
import java.util.*;


/**
 * 인간-컴퓨터 상호작용 - BOJ16139
 * -----------------
 * category: prefix sum (누적 합)
 * -----------------
 * Input 1
 * seungjaehwang
 * 4
 * a 0 5
 * a 0 6
 * a 6 10
 * a 7 10
 *
 * Output 1
 * 0
 * 1
 * 2
 * 1
 * -----------------
 */
public class BOJ16139 {

    static final int CHAR_SIZE = 26;

    static String S;
    static int q;
    static int[][] prefixSum;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        S = br.readLine();
        prefixSum = new int[S.length() + 1][CHAR_SIZE];

        for (int i = 1; i <= S.length(); i++) {
            char c = S.charAt(i - 1);
            int idx = c - 'a';
            for (int j = 0; j < CHAR_SIZE; j++) {
                prefixSum[i][j] = prefixSum[i - 1][j];
                if (j == idx) prefixSum[i][j]++;
            }
        }

        q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            char c = st.nextToken().charAt(0);
            int idx = c - 'a';
            int l = Integer.parseInt(st.nextToken()) + 1;
            int r = Integer.parseInt(st.nextToken()) + 1;
            bw.write((prefixSum[r][idx] - prefixSum[l - 1][idx]) + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}