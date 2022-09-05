package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 시계 사진들 - BOJ10266
 * -----------------
 *
 *
 * 
 *
 * -----------------
 * Input 1
 * 6
 * 1 2 3 4 5 6
 * 7 6 5 4 3 1
 *
 * Output 1
 * impossible
 * -----------------
 * Input 2
 * 2
 * 0 270000
 * 180000 270000
 *
 * Output 2
 * possible
 * -----------------
 * Input 3
 * 7
 * 140 130 110 120 125 100 105
 * 235 205 215 220 225 200 240
 *
 * Output 3
 * impossible
 * -----------------
 */
public class BOJ10266 {

    static final int MAX_SIZE = 360000;
    static byte[] clock1Text = new byte[MAX_SIZE * 2];
    static byte[] clock2Pattern = new byte[MAX_SIZE];
    static int n;

    public static int[] buildPi(byte[] pattern) {
        int[] pi = new int[pattern.length];

        int match = 0, search = 1;
        for (; search < pattern.length; search++) {
            while (match > 0 && pattern[search] != pattern[match]) {
                match = pi[match - 1];
            }

            if (pattern[search] == pattern[match]) {
                pi[search] = ++match;
            }
        }

        return pi;
    }

    public static boolean kmp(byte[] text, byte[] pattern) {
        int[] pi = buildPi(pattern);
        int search = 0, match = 0;

        for (; search < text.length; search++) {
            while (match > 0 && text[search] != pattern[match]) {
                match = pi[match - 1];
            }

            if (text[search] == pattern[match]) {
                if (match == pattern.length - 1) {
                    return true;
                    // result.add(search - match + 1);
                    // match = pi[match];
                } else {
                    match++;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int num = Integer.parseInt(st.nextToken());
            clock1Text[num] = 1;
            clock1Text[num + MAX_SIZE] = 1;
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int num = Integer.parseInt(st.nextToken());
            clock2Pattern[num] = 1;
        }

        bw.write(kmp(clock1Text, clock2Pattern) ? "possible" : "impossible");

        // close the buffer
        br.close();
        bw.close();
    }
}