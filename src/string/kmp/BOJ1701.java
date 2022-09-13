package string.kmp;

import java.io.*;
import java.util.*;


/**
 * Cubeditor - BOJ1701
 * -----------------
 *
 * 어떤 문자열의 부분 문자열들 중 두번 이상 등장하는 문자열의 최대 길이를 찾아야 한다.
 * naive 하게 구한다고 하면 부분 문자열의 종류가 굉장히 많고 KMP알고리즘을 사용하여 선형시간에 탐색한다고 하더라도 너무 많은 경우를 탐색해야 한다.
 * 문제의 조건에서 두번 이상 문자열이 등장해야 한다는 조건에서 주어진 문자열을 S라고 할 때, S의 부분 문자열에서 접두사와 접미사가 최대 일치가 되는 접두사 (혹은 접미사)가
 * S의 부분 문자열에서 위 조건을 만족하는 최대 길이의 문자열이 됨을 알 수 있다.
 * 즉, KMP의 pi table을 이용해 두 번 등장하는 부분 문자열의 길이를 구할 수 있는 것이다.
 * 일반적인 KMP의 pi table의 경우 문자열 S를 왼쪽부터 하나씩 선택한 부분 문자열에 대한 접두사 = 접미사의 최대 길이를 담고 있는 모양을 하고 있다.
 * 이 문제의 경우 모든 경우를 탐색해야 하기 때문에
 * 문자열 S의 전체에 대하여 pi table을 만들고
 * 문자열 S의 왼쪽에서 문자 하나를 제거한 substring에 대하여 pi table을 만들고
 * 문자열 S의 왼쪽에서 문자 하나를 또 제거한 substring에 대하여 pi table을 만들고 ...
 * 이런식으로 substring들에 대하여 pi table을 만들어
 * pi table 들에 있는 값중 가장 큰 값을 찾아내면 된다.
 *
 * -----------------
 * Input 1
 * abcdabcabb
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * abcdefghijklmn
 *
 * Output 2
 * 0
 * -----------------
 * Input 3
 * abcabcabc
 *
 * Output 3
 * 6
 * -----------------
 * Input 4
 * abccdccd
 *
 * Output 4
 * 3
 * -----------------
 */
public class BOJ1701 {

    private static class KMP {

        public static int maxPiValue(String pattern, int begin) {
            int[] pi = new int[pattern.length() - begin];
            int match = 0, search = 1, max = 0;

            for (; search + begin < pattern.length(); search++) {
                while (match > 0 && pattern.charAt(search + begin) != pattern.charAt(match + begin)) {
                    match = pi[match - 1];
                }

                if (pattern.charAt(search + begin) == pattern.charAt(match + begin)) {
                    pi[search] = ++match;
                    max = Math.max(max, pi[search]);
                }
            }

            return max;
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String str = br.readLine();
        int maxLen = 0;
        for (int i = 0; i < str.length(); i++) {
            maxLen = Math.max(KMP.maxPiValue(str, i), maxLen);
        }
        bw.write(String.valueOf(maxLen));

        // close the buffer
        br.close();
        bw.close();
    }
}
