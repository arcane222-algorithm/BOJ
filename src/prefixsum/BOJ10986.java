package prefixsum;

import java.io.*;
import java.util.*;

/**
 * 나머지 합 - BOJ10986
 * (prefixSum[i] - prefixSum[j]) % M = 0
 * prefixSum[i] % M = prefixSum[j] % M
 *
 * -----------------
 * Input 1
 * 5 3
 * 1 2 3 1 2
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ10986 {

    static int N, M;
    static int[] nums;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse inputs
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        nums = new int[N];

        long prefixSum = 0;
        long[] remains = new long[M];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            prefixSum += Integer.parseInt(st.nextToken());
            prefixSum %= M;
            remains[(int) prefixSum]++; // remains[i], 나머지가 i인 것의 개수
        }

        // write the answer
        long answer = remains[0];
        for (int i = 0; i < M; i++) {
            answer += (remains[i] * (remains[i] - 1)) >> 1;
        }
        bw.write(String.valueOf(answer));

        // close the buffer
        br.close();
        bw.close();
    }
}
