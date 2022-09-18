package divideandconquer.ebs;

import java.io.*;
import java.util.*;

/**
 * 이항 계수와 쿼리 - BOJ13977
 * -----------------
 * category: numbertheory (정수론), modular multiplicative inverse (모듈로 곱셈 역원),
 *           fermat's little theorem (페르마의 소정리), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 * -----------------
 * Input 1
 * 5
 * 5 2
 * 5 3
 * 10 5
 * 20 10
 * 10 0
 *
 * Output 1
 * 10
 * 10
 * 252
 * 184756
 * 1
 * -----------------
 */
public class BOJ13977 {

    static final int MOD = (int) (1e9 + 7);
    static int N, M, K;
    static long[] factorial = new long[4000001];
    static StringBuilder result = new StringBuilder();

    public static long factorial(long x) {
        long result = 1;

        while (x > 0) {
            result = (result * x) % MOD;
            x--;
        }

        return result;
    }

    public static long fastPow(long a, long n) {
        long result = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                result = result * a % MOD;
            }
            a = a * a % MOD;
            n >>= 1;
        }

        return result;
    }

    public static long getBinomialCoefficient3() {
        return (factorial[N] * fastPow((factorial[N - K] * factorial[K]) % MOD, MOD - 2)) % MOD;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        factorial[0] = factorial[1] = 1;
        for (int i = 2; i < factorial.length; i++) {
            factorial[i] = factorial[i - 1] * i % MOD;
        }

        M = Integer.parseInt(br.readLine());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            result.append(getBinomialCoefficient3()).append('\n');
        }
        bw.write(result.toString());

        br.close();
        bw.close();
    }
}