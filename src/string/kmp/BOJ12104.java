package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 순환 순열 - BOJ12104
 * -----------------
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
