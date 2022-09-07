package string.kmp;

import java.io.*;
import java.util.*;


/**
 * Oulipo - BOJ9120
 * -----------------
 *
 * 'BOJ1786 - 찾기'와 함께 KMP 알고리즘의 기본문제이다.
 * 각 테스트 케이스 마다 pattern 문자열과 text 문자열이 주어진다.
 * 'BOJ1786 - 찾기'에서는 pattern과 text가 일치할 때 해당 위치를 List에 담아 일치하는 위치의 목록을 반환했다면
 * 이 문제는 단순히 일치 여부가 몇회 나타나는지만 계산하면 된다.
 *
 * -----------------
 * Input 1
 * 3
 * BAPC
 * BAPC
 * AZA
 * AZAZAZA
 * VERDI
 * AVERDXIVYERDIAN
 *
 * Output 1
 * 1
 * 3
 * 0
 * -----------------
 */
public class BOJ9120 {

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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            String pattern = br.readLine();
            String text = br.readLine();
            result.append(KMP.search(text, pattern)).append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}