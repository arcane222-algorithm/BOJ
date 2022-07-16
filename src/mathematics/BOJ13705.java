package mathematics;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


/**
 * Ax+Bsin(x)=C - BOJ13705
 *
 * Ax+Bsin(x)=C ② (BOJ14786) 의 상위 난이도 버전이다.
 * 뉴턴법인 F_n+1 = X_n + F_n / F'_n 의 식을 이용하거나 (F'_n은 F_n의 도함수이다) 이분탐색을 이용하는데,
 * 해당 문제의 경우 이분탐색을 이용하여 해결하였다.
 *
 * Ax + Bsin(x) = C식을 정리하면
 * x = (Bsin(x) - C) / A가 되고 sin(x)는 결과값으로 -1 ~ 1의 범위를 가지므로
 * x의 범위는 (Bsin(x) - C) / A <= x <= (Bsin(x) + C) / A 가 된다.
 * 즉, 이 범위 내에서 이분탐색을 이용하여 x의 근사치를 구하면 되는 것이다.
 * Left 값을 범위 최솟값 -1, Right값을 범위 최댓값 +1로 잡고 중간값 = (left + Right) / 2를 하며 이분탐색을 한다.
 *
 * Ax+Bsin(x)=C ② (BOJ14786) 문제처럼
 * F(x) = Ax + Bsin(x) - C = 0인 x 값을 위의 범위 내에서 찾으면 되는 것인데,
 * 해당 문제의 경우 소수점 이하 20 ~ 30자리까지의 정확도를 요구하므로, sin함수를 직접 구현한다.
 * sin(x) 함수의 경우 테일러 급수를 통해 표현할 수 있고 sin(x) 테일러 급수의 경우
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + x^9 / 9! ... 으로 정의된다.
 * 정확도를 위해 최소 30항 이상 계산해준다.
 * 분모의 팩토리얼의 경우 DP 방식으로 배열에 저장하여 처리한다.
 *
 * 주의할 점은, x값이 점점 큰 값이 들어올 수 있는데, 이 경우 BigDecimal 을 사용하더라도 오차가 발생할 가능성이 커진다.
 * 삼각함수의 성질 중 sin(x) = sin(x + 2PI) = sin(x + 4PI) = ... 인 성질을 이용하여
 * sin(x + kPI)의 값이 들어올 경우 k 가 짝수라면 x에서 kPI만큼 빼주고, k가 홀수라면, (k-1)PI만큼 빼주어 x 값을 작게하여 계산한다.
 * (위 계산을 재귀로 매번 2PI 만큼 빼주도록 구현할 경우 큰 값 C에 대하여 StackOverflow가 발생할 수 있으므로 나눗셈을 통해 k값을 구한다)
 * 만약 x 값에 음수가 들어올 경우 sin(-x) = -sin(x)의 삼각함수의 성질을 이용하여 x를 양수로 바꿔준다.
 *
 * -----------------
 * Input 1
 * 1 1 20
 *
 * Output 1
 * 19.441787
 * -----------------
 * Input 2
 * 1 1 100000
 *
 * Output 1
 * 99999.433481
 * -----------------
 * Input 3
 * 97084 82977 68488
 *
 * Output 3
 * 0.384700
 * -----------------
 * Input 4
 * 27020 1897 56128
 *
 * Output 4
 * 2.013848
 * -----------------
 */
public class BOJ13705 {

    static final int MAX_CALC_CNT = 50;
    static final BigDecimal DECIMAL_MINUS_ONE = new BigDecimal(-1);
    static final BigDecimal DECIMAL_TWO = new BigDecimal(2);

    static final BigDecimal DECIMAL_PI = new BigDecimal("3.14159265358979323846264338327950288419716939937510");
    static final BigDecimal ERROR_GAP = new BigDecimal("0.00000000000000000000000000000000000000000000000001");
    static final BigDecimal[] factorial = new BigDecimal[MAX_CALC_CNT];

    static BigDecimal A, B, C;
    static BigDecimal Left, Right, X, Res;

    public static void calFactorial() {
        for (int i = 0; i < MAX_CALC_CNT; i++) {
            factorial[i] = new BigDecimal(4 * i * i + 10 * i + 6);
        }
    }

    public static BigDecimal bigSin(BigDecimal X) {
        BigDecimal x = new BigDecimal(String.valueOf(X));

        if (x.equals(BigDecimal.ZERO))
            return BigDecimal.ZERO;   // sin(x) = 0 (if x = 0)

        if (x.compareTo(BigDecimal.ZERO) == -1)
            return bigSin(x.multiply(DECIMAL_MINUS_ONE)).multiply(DECIMAL_MINUS_ONE);  // sin(-x) = -sin(x) (if x > 0)

        if (x.compareTo(DECIMAL_PI) == 1) {
            int gap = x.divide(DECIMAL_PI, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP).intValue();
            if (gap % 2 == 1) gap--;
            if (gap > 0) {
                return bigSin(x.subtract(DECIMAL_PI.multiply(new BigDecimal(gap))));
            }
        }

        BigDecimal xPow2 = x.multiply(x);
        BigDecimal fact = BigDecimal.ONE;
        BigDecimal result = new BigDecimal(String.valueOf(x));

        for (int i = 0; i < MAX_CALC_CNT; i++) {
            fact = fact.multiply(factorial[i]);
            fact = fact.multiply(DECIMAL_MINUS_ONE);
            x = x.multiply(xPow2);
            result = result.add(x.divide(fact, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP));
        }

        return result;
    }

    public static BigDecimal term(BigDecimal x) {
        BigDecimal part1 = A.multiply(x);
        BigDecimal part2 = B.multiply(bigSin(x));

        return part1.add(part2).subtract(C);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // parse A, B, C
        A = new BigDecimal(st.nextToken());
        B = new BigDecimal(st.nextToken());
        C = new BigDecimal(st.nextToken());
        calFactorial();

        Left = C.subtract(B).divide(A, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE);
        Right = C.add(B).divide(A, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP).add(BigDecimal.ONE);
        X = Left.add(Right).divide(DECIMAL_TWO, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP);
        Res = term(X);

        while(Right.subtract(Left).compareTo(ERROR_GAP) == 1) {
            if (Res.compareTo(BigDecimal.ZERO) == 1) Right = X;
            else if (Res.compareTo(BigDecimal.ZERO) == -1) Left = X;
            else break;

            X = Left.add(Right).divide(DECIMAL_TWO, MAX_CALC_CNT, BigDecimal.ROUND_HALF_UP);
            Res = term(X);
        }

        // write the result
        bw.write(String.format("%.6f", X));

        // close the buffer
        br.close();
        bw.close();
    }
}