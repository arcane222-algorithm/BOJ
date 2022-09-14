package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * xor 게임 - BOJ15710
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * 현재 a라는 수에서 0 ~ 2^31 - 1 범위의 어떤 수 u를 뽑아 a xor u를 해야 한다. a는 a xor u로 바뀌게 되고
 * 위 과정을 n회 수행하였을 때 a의 값이 b가 되어야 한다.
 * 이때 가능한 과정의 경우의 수를 모두 구해 10^9 + 7로 나누어야 한다.
 * 수 a를 각 자리의 비트 별로 본다고 할 때, 어떤 자리의 비트가 0 일 때, 다음에 0이 들어오면 xor 시 0이 나오고 1이 들어오면 xor 시 1이 들어온다.
 * 반대로 어떤 자리의 비트가 1일 때, 다음에 0이 들어오면 xor 시 1이 나오고 1이 들어오면 xor 시 0이 들어온다.
 *
 * 즉, 어떤 자리의 비트에 대하여 xor하러 들어오는 수 u에 대하여 2가지 경우의 수가 생기는 것이다.
 * 마지막 n회째 xor 시 숫자 b가 되어야 하기 때문에 2가지 경우의 수가 아니라 무조건 b가 될 수 있는 1가지 경우의 수가 나오도록 비트를 맞춰서 xor 해야한다.
 * 즉, 1 ~ N - 1회 까지는 xor시 각 자리마다 2개의 경우의 수가 생기게 되고 마지막 N회에는 1번의 경우의 수만 있는 것이다.
 * 수의 범위가 32bit 정수 에서 음이 아닌 값이므로 31개의 비트를 쓰게 되고
 * 1회 수행 시 각 자리 비트마다 2개의 경우의 수가 나온다고 했으므로 2 x 2 x 2 x ... x 2 = 2^31의 경우의 수가 생기게 된다.
 * 게임을 N회 수행한다고 하면 마지막 N번째 시기는 무조건 1회 이므로 2^(31 * N - 1)의 경우의 수가 생기는 것이다.
 *
 * N이 최대 10억까지 들어오므로 선형계산으로 거듭제곱을 계산하면 당연히 시간초과가 발생한다.
 * 분할정복을 이용한 거듭제곱 방식을 이용하여 log(31 * (N - 1))의 연산을 수행하여 빠르게 계산할  수 있도록 한다.
 *
 * -----------------
 * Input 1
 * 2 3 1
 *
 * Output 1
 * 1
 * -----------------
 */
public class BOJ15710 {

    static final int MOD = (int) 1e9 + 7;
    static int a, b, n;

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

        st = new StringTokenizer(br.readLine());
        a = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());

        long exponent = 31L * (n - 1);
        long result = fastPow(2, exponent);
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}