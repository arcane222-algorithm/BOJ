package prefixsum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;




/**
 * 수열 (Easy) - BOJ23827
 * -----------------
 * category: mathematics (수학)
 *           prefix sum (누적 합)
 * -----------------
 * Input 1
 * 3
 * 1 2 3
 *
 * Output 1
 * 11
 * -----------------
 */
public class BOJ23827 {

    static final int MOD = (int) 1e9 + 7;

    static int N;
    static long[] values, prefixSum;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());

        values = new long[N];
        prefixSum = new long[N];

        values[0] = prefixSum[0] = Integer.parseInt(st.nextToken());
        for (int i = 1; i < N; i++) {
            values[i] = Integer.parseInt(st.nextToken());
            prefixSum[i] = (prefixSum[i - 1] + values[i]) % MOD;
        }

        long result = 0;
        for (int i = 1; i < N; i++) {
            long term = (values[i - 1] % MOD * (prefixSum[N - 1] % MOD - prefixSum[i - 1] % MOD + MOD) % MOD) % MOD;
            result = (result + term) % MOD;
        }
        bw.write(result + "");

        // close the buffer
        br.close();
        bw.close();
    }
}