package mathematics;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;


/**
 * Ax+Bsin(x)=C ② - BOJ14786
 *
 * 세 수 A, B, C가 주어졌을 때, 방정식 Ax + Bsin(x) = C의 해를 찾는 문제이다.
 * 뉴턴-랩슨법을 이용하여
 * X_n = X_n-1 - (F(X_n-1) / F'(X_n-1)) 의 식을 이용한다 (F'(X)는 F(X)의 도함수이다)
 * C를 이항하면 Ax + Bsin(x) - C = 0이 되고 이것은
 * F(x) = Ax + Bsin(x) - C 가 0인 지점을 찾는 것과 같다.
 * F'(x) = Bcos(x) + A이고
 * 이것을 뉴턴법에 이용하면
 * x에 F(x) / F'(x) = (Ax + Bsin(x) - C) /  (Bcos(x) + A)을 빼주며 근사치를 구하면 된다.
 * 이때 문제에서 소수점 10^-9까지 오차를 허용하므로, 해당 범위까지 오차가 좁혀질때까지 근사치를 계산한다.
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
public class BOJ14786 {

    static final BigDecimal gap = new BigDecimal("0.00000000001");
    static BigDecimal A, B, C;

    public static BigDecimal term(BigDecimal x) {
        BigDecimal part1 = A.multiply(x);
        BigDecimal part2 = B.multiply(new BigDecimal(Math.sin(x.doubleValue())));
        return part1.add(part2).subtract(C);
    }

    public static BigDecimal term2(BigDecimal x) {
        return B.multiply(new BigDecimal(Math.cos(x.doubleValue()))).add(A);
    }

    public static BigDecimal getTermResult(BigDecimal x) {
        BigDecimal res = term(x);

        while(res.abs().compareTo(gap) == 1) {
            BigDecimal val1 = term(x);
            BigDecimal val2 = term2(x);
            x = x.subtract(val1.divide(val2, 19, BigDecimal.ROUND_CEILING));
            res = term(x);
        }

        return x;
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

        // write the result
        bw.write(String.valueOf(String.format("%.19f", getTermResult(BigDecimal.ZERO))));

        // close the buffer
        br.close();
        bw.close();
    }
}