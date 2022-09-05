package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 속타는 저녁 메뉴 - BOJ11585
 * -----------------
 * -----------------
 * Input 1
 * 9
 * I W A N T M E A T
 * E A T I W A N T M
 *
 * Output 1
 * 1/9
 * -----------------
 * Input 2
 * 6
 * A A B A A B
 * B A A B A A
 *
 * Output 2
 * 1/3
 * -----------------
 * Input 3
 * 5
 * A B C A B
 * A B C A B
 *
 * Output 3
 * 1/5
 * -----------------
 */
public class BOJ11585 {

    static int N;
    static String text, pattern;

    public static int[] buildPi(String pattern) {
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

    public static int kmp(String text, String pattern) {
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        StringBuilder patternBuilder = new StringBuilder();
        for (int i = 0; i < N; i++) {
            patternBuilder.append(st.nextToken());
        }
        pattern = patternBuilder.toString();

        st = new StringTokenizer(br.readLine());
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < N; i++) {
            textBuilder.append(st.nextToken());
        }
        textBuilder.append(textBuilder.substring(0, textBuilder.length() - 1));
        text = textBuilder.toString();

        int numerator = kmp(text, pattern);
        int denominator = N;
        if (denominator % numerator == 0) {
            bw.write(1 + "/" + denominator / numerator);
        } else {
            bw.write(numerator + " / " + denominator);
        }

        // close the buffer
        br.close();
        bw.close();
    }
}