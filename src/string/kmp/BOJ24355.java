package string.kmp;

import java.io.*;
import java.util.*;


/**
 * ПАЛИНДРОМ - BOJ24355
 * -----------------
 *
 * 문제의 경우 현재 주어진 문자열의 왼쪽에 라틴 문자를 추가하여 회문 (palindrome)을 만들 때, 그 길이 중 가장 짧은 길이를 출력해야 한다.
 * 이 문제의 경우 KMP 알고리즘을 이용해야 하지만 직접 문자열을 탐색하는 것이 아니라 실패함수 (failure function or PI table) 를 이용하는 문제이다.
 *
 * 어떤 문자열 S를 회문으로 만들기 위해서는 기본적으로 S를 뒤집어서 (S_reverse) 왼쪽에 이어 붙이면 회문이 된다.
 * 예제 입력 babba에 대하여 이를 수행하면 aabab + babaa = aababbabaa가 되어 회문이 된다.
 * 이 때, S와 S_reverse를 더해줄 때, S의 우측 접미사와 S_reverse의 좌측 접두사의 공통 부분이 있다면 이 부분을 합쳐 회문을 만들 수 있다.
 * 위 예제의 경우 bab가 공통되므로 aa + (bab, 공통) + aa가 되어 최소 길이 회문은 aababaa가 된다.
 *
 * 즉, 이 말은 S + S_reverse를 한 문자열에 대하여 해당 문자열의 최대 길이의 접두사 == 접미사인 길이를 구해
 * 2 * S - (최대 접두사 == 접미사인 길이) 를 해주면 되는 것이다.
 *
 * -----------------
 * Input 1
 * babaa
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * abcde
 *
 * Output 2
 * 9
 * -----------------
 */
public class BOJ24355 {

    private static class KMP {
        private static int[] buildPi(String pattern) {
            int[] pi = new int[pattern.length()];
            int match = 0, search = 1;
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

        public static int search(String text, String pattern) {
            int[] pi = buildPi(pattern);
            int match = 0, search = 0, count = 0;
            for (; search < text.length(); search++) {
                while (match > 0 && text.charAt(search) != pattern.charAt(match)) {
                    match = pi[match - 1];
                }

                if (text.charAt(search) == pattern.charAt(match)) {
                    if (match == pattern.length() - 1) {
                        count++;
                        match = pi[match];
                    } else {
                        match++;
                    }
                }
            }

            return count;
        }
    }

    static int N;
    static StringBuilder result = new StringBuilder();

    public static String reverse(String str) {
        StringBuilder builder = new StringBuilder();
        for (int i = str.length() - 1; i > -1; i--) {
            builder.append(str.charAt(i));
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String text = br.readLine();
        int[] pi = KMP.buildPi(text + reverse(text));
        int duplicate = pi[pi.length - 1];
        bw.write(String.valueOf(text.length() * 2 - duplicate));

        // close the buffer
        br.close();
        bw.close();
    }
}
