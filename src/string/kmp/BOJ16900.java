package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 이름 정하기 - BOJ16900
 * -----------------
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