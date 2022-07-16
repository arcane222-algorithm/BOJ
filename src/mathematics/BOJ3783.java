package mathematics;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


/**
 * 세제곱근 - BOJ3783
 *
 * 어떤 양수 N에 대하여 이것의 세제곱근을 구해야 한다.
 * 세제곱근을 x라고 한다면 x^3 = N이라는 식이 성립한다.
 * 이것은 x^3 - N = 0이고, F(x) = x^3 - N = 0인 x 값을 찾는 문제로 생각해볼 수 있다.
 * 뉴턴-랩슨법을 이용하여
 * F(x) = x^3 - N,
 * F'(x) = 3x^2 이므로
 * X_n = X_n-1 - (F(x) / F'(x))
 *     = X_n-1 - ((x^3 - N) / 3x^2) 을 계산해주면 된다.
 * (즉, F(x) = 0이 되는 x 값에 F(X) / F'(x)값을 빼주며 근사치를 찾아간다.)
 *
 * 이때 시작 x 값을 0으로 잡으면 분모가 0이 되어 DivideByZeroException 이 발생할 수 있으므로 1로 잡고 시작한다.
 * 해당 문제의 경우 소수점 아래 101번째 자리까지 정밀도를 요구하므로 BigDecimal 계산 시 소수점 아래 101번째 자리까지 커버할 수 있도록 한다.
 *
 * -----------------
 * Input 1
 * 1 1 20
 *
 * Output 1
 * 19.4417867100477812106
 * -----------------
 * Input 2
 * 1 1 99995
 *
 * Output 1
 * 99995.8596261857837833920
 * -----------------
 * Input 3
 * 1 1 99969
 *
 * Output 3
 * 99969.9773407515887475656
 * -----------------
 */
public class BOJ3783 {

    static final int MAX_CALC_CNT = 101;
    static final BigDecimal DECIMAL_THREE = new BigDecimal(3);
    static BigDecimal ERROR_GAP;

    static int T;
    static BigDecimal N;
    static StringBuilder result = new StringBuilder();

    public static BigDecimal func(BigDecimal x) {
        return x.multiply(x).multiply(x).subtract(N);
    }

    public static BigDecimal derivative(BigDecimal x) {
        return x.multiply(x).multiply(DECIMAL_THREE);
    }

    public static BigDecimal getTermResult(BigDecimal x) {
        BigDecimal res = func(x).divide(derivative(x), MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP);

        while (res.abs().compareTo(ERROR_GAP) == 1) {
            x = x.subtract(res);
            res = func(x).divide(derivative(x), MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP);
        }

        return x;
    }

    public static void getErrorGap() {
        StringBuilder sb = new StringBuilder();
        sb.append("0.");
        for (int i = 0; i < MAX_CALC_CNT - 1; i++) {
            sb.append('0');
        }
        sb.append('1');

        ERROR_GAP = new BigDecimal(sb.toString());
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        getErrorGap();

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = new BigDecimal(br.readLine());
            BigDecimal res = getTermResult(BigDecimal.ONE);
            res = res.divide(BigDecimal.ONE, 10, BigDecimal.ROUND_DOWN);
            result.append(res).append('\n');
        }

        // write the result\
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}