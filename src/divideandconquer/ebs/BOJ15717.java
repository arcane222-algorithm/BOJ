package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 떡파이어 - BOJ15717
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * 문제의 조건에 따라 떡파이어가 N세가 되는 경우의 수를 나열하면 다음과 같다.
 *
 * N = 0일 때 0 - 1가지
 * N = 1일 때, 1 0 - 1가지
 * N = 2일 때, 2 0, 1 1 0 - 2가지
 * N = 3일 때, 3 0, 2 1 0, 1 2 0, 1 1 1 0 - 4가지
 * . . .
 *
 * 즉, 이 문제는 a1 + a2 + a3 + ... + an = N이라는 방정식의 해의 경우의 수와 같다.
 * 중복조합을 이용하여 구하게 되면
 * N = k 일때, 경우의 수는 2^k-1이 되는데, 0일 때도 1가지가 존재하므로 예외처리를 해주어야 한다.
 * N이 최대 10^12 까지 입력되므로 거듭제곱으로 빠르게 계산하기 위해 분할정복을 이용한 거듭제곱으로 구현한다.
 *
 * -----------------
 * Input 1
 * 3
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 0
 *
 * Output 2
 * 1
 * -----------------
 */
public class BOJ15717 {

    static final int MOD = (int) 1e9 + 7;
    static long N;

    public static long fastPow(long value, long n) {
        if (n == 0) return 1;
        else if (n == 1) return value % MOD;
        else {
            long temp = fastPow(value, n >> 1);
            if ((n & 1) == 0) {
                return temp * temp % MOD;
            } else {
                return (temp * temp % MOD) * (value % MOD) % MOD;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Long.parseLong(br.readLine());
        bw.write(String.valueOf(N == 0 ? 1 : fastPow(2, N - 1)));

        // close the buffer
        br.close();
        bw.close();
    }
}