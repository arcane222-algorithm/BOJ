package slidingwindow;

import java.io.*;
import java.util.*;


/**
 * 암호화된 비밀번호 - BOJ9549
 * -----------------
 * category: string (문자열)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 3
 * abcdef
 * ecd
 * cde
 * ecd
 * abcdef
 * fcd
 *
 * Output 1
 * YES
 * YES
 * NO
 * -----------------
 */
public class BOJ9549 {

    static final int ALPHABET_SIZE = 26;
    static int T;

    public static boolean arrCompare(int[] src, int[] dst) {
        for (int i = 0; i < src.length; i++) {
            if (src[i] != dst[i])
                return false;
        }
        return true;
    }

    public static boolean slidingWindow(String target, String src) {
        final int WINDOW_SIZE = src.length();
        int[] srcCnt = new int[ALPHABET_SIZE];
        int[] window = new int[ALPHABET_SIZE];

        for (int i = 0; i < WINDOW_SIZE; i++) {
            srcCnt[src.charAt(i) - 'a']++;
        }

        for (int i = 0; i < WINDOW_SIZE; i++) {
            window[target.charAt(i) - 'a']++;
        }

        if (arrCompare(srcCnt, window))
            return true;

        for (int i = 1; i <= target.length() - WINDOW_SIZE; i++) {
            window[target.charAt(i - 1) - 'a']--;
            window[target.charAt(i + WINDOW_SIZE - 1) - 'a']++;

            if (arrCompare(srcCnt, window))
                return true;
        }

        return false;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            bw.write(slidingWindow(br.readLine(), br.readLine()) ? "YES\n" : "NO\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}