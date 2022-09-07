package string.kmp;

import java.io.*;
import java.util.*;


/**
 * Oulipo - BOJ9120
 * -----------------
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