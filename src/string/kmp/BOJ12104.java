package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 순환 순열 - BOJ12104
 * -----------------
 *
 * 'BOJ10266 - 시계 사진들', 'BOJ11585 - 속타는 저녁메뉴'와 유사한 문제이다.
 * 순열 P = P0, P1, ..., PN-1에 대하여 P를 k번 순환 이동 시키면, Pi -> P(i+k) mod N 로 표현된다.
 * 이 말은 즉, 순열 P에 대하여 왼쪽에서 k만큼 뽑아 오른쪽에 다시 순서대로 넣어준 것과 같다.
 * 위 문제들과 유사하게 해당 순열에 대하여 순환하는 모양을 나타내기 위해 두 번째 입력 문자열 B를 두번 이어 붙인 문자열을 text로 사용한다.
 *
 * 이때, 순열의 마지막 문자 PN-1에 대하여 i = N - 1, k = N 일 때, (i + k) mod n = (N - 1 + N) mod N = N - 1 이므로, 같은 마지막 문자를 나타낸다.
 * 즉 k가 N이라면 끝 문자가 다시 끝 문자를 나타내고 이것은 첫 번째와 같은 상태를 나타내므로 이 경우를 빼주어야 한다.
 * 즉, B + (B - 1)을 text로 설정하고 첫 번째 입력 문자열 A를 pattern 으로 설정한다.
 * 문제애서 A와 XOR 하여 0이 되는 B + (B - 1)의 부분 문자열의 개수를 찾고 있는데, A와 XOR 하여 0이 되려면 A와 같은 값을 가져야 하고 이것은 A pattern을
 * B + (B - 1)에서 찾는 것이므로 kmp 알고리즘을 이용하여 선형 탐색 하며 경우의 수를 계산해주면 된다.
 *
 * -----------------
 * Input 1
 * 101
 * 101
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 111
 * 111
 *
 * Output 2
 * 3
 * -----------------
 */
public class BOJ12104 {

    private static class KMP {
        private static int[] buildPI(String pattern) {
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
            int[] pi = buildPI(pattern);
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String pattern = br.readLine();
        StringBuilder textBuilder = new StringBuilder(br.readLine());
        textBuilder.append(textBuilder.subSequence(0, textBuilder.length() - 1));
        String text = textBuilder.toString();

        int cnt = KMP.search(text, pattern);
        bw.write(String.valueOf(cnt));

        // close the buffer
        br.close();
        bw.close();
    }
}
