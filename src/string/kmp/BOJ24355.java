package string.kmp;

import java.io.*;
import java.util.*;


/**
 * ПАЛИНДРОМ - BOJ24355
 * -----------------
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
