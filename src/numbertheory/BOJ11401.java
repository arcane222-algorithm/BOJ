package numbertheory;

import java.io.*;
import java.util.StringTokenizer;

/**
 * 이항 계수 3 - BOJ 11401
 * -----------------
 * category: numbertheory (정수론), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 *
 *
 * -----------------
 * Input 1
 * 5 2
 *
 * Output 1
 * 3
 * -----------------
 */
public class BOJ11401 {

    final static int P = (int)(1e9 + 7);
    static int N, K;

    public static long factorial(long x) {
        long result = 1;

        while(x > 0) {
            result = (result * x) % P;
            x--;
        }

        return result;
    }

    public static long fastPow(long x, long y) {
        if(y == 0) {
            return 1;
        } else if(y == 1) {
            return x % P;
        } else {
            long tmp = fastPow(x, y >> 1);

            if(y % 2 == 0) {
                return (tmp * tmp) % P;
            } else {
                return ((tmp * tmp % P) * (x % P)) % P;
            }
        }
    }

    public static long getBinomialCoefficient3() {
        return (factorial(N) * fastPow((factorial(N - K) * factorial(K)) % P, P - 2)) % P;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        bw.write(String.valueOf(getBinomialCoefficient3()));
        br.close();
        bw.close();
    }
}