package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 문자열 제곱 - BOJ4354
 * -----------------
 *
 * 문자열 S와 어떤 문자열 a에 대하여 대하여 S = a^n = a + a + ... n개 ... + a을 만족하는 가장 큰 n을 찾는 문제이다.
 * naive 하게 생각해본다면 S의 길이를 소인수 분해 해서 나오는 값을 길이로 가지는 접두사만 a가 될 수 있다.
 * 즉, S의 길이가 6이라고 하면 a의 길이는 1, 2, 3, 6이 될 수 있을 것이다. (예를 들어, 2의 경우 3번 반복(a+a+a), 3의 경우 2번 반복의 패턴을 나타낼 수 있다.(a+a))
 * 만약 a의 길이가 4, 5 라면 a^n꼴로 나타낼 수 없다.
 * kmp를 이용하여 이 문제를 해결할 수 있는데, kmp를 이용한 패턴매칭을 하는 것이 아니라 kmp 알고리즘 과정에서 생성하는 pi(or lcs) table (or failure function)을 이용한다.
 * pi table의 경우 문자열 S에 대하여 각 길이의 접두사 부분 문자열에 대한 접두사 = 접미사의 최대 길이를 저장해놓는 table이다.
 * pi table의 마지막 값은 문자열 S의 최대 길이 부분 문자열에 대한 즉, 문자열 S 자체에 대한 접두사 = 접미사의 최대 길이를 담고 있다.
 * 문자열 S의 길이에서 이 값을 뺀다면 (|S| - pi[|S| - 1]) 이 값은 문자열에서 반복되는 부분 문자열의 최소의 길이이다.
 * 따라서 가장 큰 n을 찾기 위해서
 * 이때 |S|가 (|S| - pi[|S| - 1])로 나누어 떨어진다면 S는 위 길이의 부분 문자열의 반복으로 이루어져 있는 것이고, n의 값은 |S| / (|S| - pi[|S| - 1]) 가 되며
 * 나누어 떨어지지 않는다면 1이 될 것이다.
 *
 * -----------------
 * Input 1
 * abcd
 * aaaa
 * ababab
 * .
 *
 * Output 1
 * 1
 * 4
 * 3
 * -----------------
 * Input 2
 * abcdabcdabcdabc
 * bcaabca
 * bbabbab
 * .
 *
 * Output 2
 * 1
 * 1
 * 1
 * -----------------
 */
public class BOJ4354 {

    public static int[] buildPi(String pattern) {
        int[] pi = new int[pattern.length()];
        int search = 1, match = 0;
        for (; search < pattern.length(); search++) {
            while (match > 0 && pattern.charAt(search) != pattern.charAt(match)) {
                match = pi[match - 1];
            }

            if (pattern.charAt(search) == pattern.charAt(match)) {
                pi[search] = ++match;
            }
        }

        return pi;
    }

    public static List<Integer> kmp(String text, String pattern) {
        int[] pi = buildPi(pattern);
        List<Integer> result = new ArrayList<>();

        int search = 0, match = 0;
        for (; search < text.length(); search++) {
            while (match > 0 && text.charAt(search) != pattern.charAt(match)) {
                match = pi[match - 1];
            }

            if (text.charAt(search) == pattern.charAt(match)) {
                if (match == pattern.length() - 1) {
                    result.add(search - match + 1);
                    match = pi[match];
                } else {
                    match++;
                }
            }
        }

        return result;
    }

    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            String text = br.readLine();
            if (text.charAt(0) == '.') break;
            int[] pi = buildPi(text);
            int last = pi[pi.length - 1];
            int size = text.length();
            int res = size / (size - last);
            if (size % (size - last) == 0) {
                result.append(res);
            } else {
                result.append("1");
            }
            result.append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
