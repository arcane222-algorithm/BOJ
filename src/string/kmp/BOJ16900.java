package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 이름 정하기 - BOJ16900
 * -----------------
 *
 * 욱제가 컴퓨터에 이름을 붙이기 위해 이름에 가장 좋아하는 문자열 S가 최소 K 번 등장해야 한다고 하면
 * (i) K = 1 일 때, 이름에 S가 등장해야 하므로 이름은 S가 된다.
 * (ii) K > 1 일 때, 이름에 S가 두 번 이상 등장해야 하는데, S를 여러 번 이어 붙일 때
 *                  만약 S의 접두사 = 접미사인 최대 길이의 부분이 있다면 그 부분을 이어붙여 문자열을 만든다면 그냥 이어 붙이는 것보다 짧은 문자열을 만들 수 있다.
 *                  즉, 입력 예제 ada에 대하여 접두사 a, 접미사 a 가 같으므로 K = 2라면, adaada가 아닌 ad(a)da로 길이가 5인 문자열을 만들 수 있다.
 *
 * 즉, K > 1에 대하여 문자열 S의 접두사 = 접미사의 최대 길이를 p라고 한다면 S - p만큼 계속해서 이어붙일 수 있는 것이다.
 * 이 길이를 나타내면 K에 대하여 S + (S - p) + ... + (S - p) = KS - (K-1)p가 된다. (K >= 1)
 *
 * -----------------
 * Input 1
 * ada 3
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * abc 2
 *
 * Output 2
 * 6
 * -----------------
 * Input 3
 * r 7
 *
 * 7
 * -----------------
 * Input 4
 * rr 5
 *
 * Output 4
 * 6
 * -----------------
 * Input 5
 * abbababbbbababababba 2
 *
 * Output 5
 * 36
 * -----------------
 * Input 6
 * abcabcabca 3
 *
 * Output 6
 * 16
 * -----------------
 */
public class BOJ16900 {

    private static class KMP {
        public static int[] buildPI(String pattern) {
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

    static String S;
    static long K;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        S = st.nextToken();
        K = Integer.parseInt(st.nextToken());

        int[] pi = KMP.buildPI(S);
        long len = K * S.length() - (K - 1) * pi[pi.length - 1];
        bw.write(String.valueOf(len));

        // close the buffer
        br.close();
        bw.close();
    }
}