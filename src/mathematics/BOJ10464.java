package mathematics;

import java.io.*;
import java.util.StringTokenizer;


/**
 * XOR - BOJ10464
 * -----------------
 *
 * 수열의 i ~ j (i <= j) 까지의 XOR 합 을 XOR[i, j] 라고 할 때,
 * XOR[i, j] = XOR[1, i - 1] ^ XOR[1, j] 로 표현 가능
 * why) (n1 ^ n2 ^ n3 ... ^ n(i - 1)) ^ (n1 ^ n2 ^ n3 ... ^ n(j))
 *     = {(n1 ^ n1) ^ (n2 ^ n2) ^ (n3 ^ n3) ... ^ (n(i - 1) ^ n(i - 1))} ^ n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j) (XOR 연산은 교환법칙과 결합법칙이 성립하므로 ..)
 *     = 0 ^ 0 ^ 0 ^ ... ^ 0 ^ n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j) (XOR 연산의 0은 항등원(identity) 이므로 결과값에 영향 X)
 *     = n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j)
 *     = XOR[i, j]
 *
 * 이 점을 활용하여 S, F 사이의 XOR 합을 XOR[1, S - 1] ^ XOR[1, F]로 변환하여 해결한다.
 * 입력 값이 10억까지 들어올 수 있고, 여전히 이 값이 매우 크므로 XOR 합의 재미있는 성질을 이용한다.
 * XOR 합의 경우 1 ~ X 까지의 XOR합 XOR[1, X]에 대하여 X % 4 == 3 (3으로 나누어 떨어짐) 일 경우 XOR[1, X] = 0이다.
 * 이 점을 이용하여 각 XOR[1, S - 1], XOR[1, F]를 구할 때, XOR이 0인 지점 이후 부터 XOR 합을 계산하여 구한다.
 *
 * [기억해야 될 XOR 성질]
 * (1) 부분 수열의 XOR 합은 XOR 누적 합 두개를 XOR 하는 것으로 쪼갤 수 있다. XOR[i, j] = XOR[1, i - 1] ^ XOR[1, j] (i <= j)
 * (2) A(i) = i 인 수열에 대하여 XOR 누적 합은 i % 4 == 3이라면 0이 된다.
 * (3) XOR 연산에 대해 항등원 (identity) 0이고, 역원 (inverse)은 자기 자신이다.
 *     => a ^ 0 = a
 *     => a ^ a = 0
 * (4) XOR 연산의 경우 교환 법칙이 성립하므로, 합을 구할 때 순서를 신경 쓸 필요가 없다.
 *    => a ^ b = b ^ a
 *
 * -----------------
 * Input 1
 * 5
 * 3 10
 * 5 5
 * 13 42
 * 666 1337
 * 1234567 89101112
 *
 * Output 1
 * 8
 * 5
 * 39
 * 0
 * 89998783
 * -----------------
 */
public class BOJ10464 {

    static int T, S, F;
    static StringBuilder result = new StringBuilder();

    // 1 ~ end 까지의 XOR 합을 return
    public static int XORSum(int end) {
        if (end % 4 == 3) {
            return 0;
        } else {
            int result = (end >> 2) << 2;
            for (int i = result + 1; i < end + 1; i++) {
                result ^= i;
            }
            return result;
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        // parse T
        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            S = Integer.parseInt(st.nextToken());
            F = Integer.parseInt(st.nextToken());
            result.append(XORSum(S - 1) ^ XORSum(F));
            result.append('\n');
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}