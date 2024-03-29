package string.hashing;

import java.io.*;
import java.util.*;


/**
 * 좋은 부분 문자열의 개수 - BOJ13507
 * -----------------
 * category: string (문자열)
 *           hashing (해싱)
 * -----------------
 * Input 1
 * ababab
 * 01000000000000000000000000
 * 1
 *
 * Output 1
 * 5
 * -----------------
 * Input 2
 * acbacbacaa
 * 00000000000000000000000000
 * 2
 *
 * Output 2
 * 8
 * -----------------
 */
public class BOJ13507 {

    static final int P = 31;
    static final int ALPHABET_SIZE = 26;

    static String S, T;
    static int K;
    static long[] pows;

    public static long hash(String s, int size) {
        long hash = 0;
        for (int i = 0; i < size; i++) {
            hash = P * hash + s.charAt(i);
        }

        return hash;
    }

    public static int count(String s, boolean[] isBad, int size) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            int idx = s.charAt(i) - 'a';
            if (isBad[idx]) count++;
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        S = br.readLine();
        T = br.readLine();
        K = Integer.parseInt(br.readLine());

        boolean[] isBad = new boolean[ALPHABET_SIZE];
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            isBad[i] = T.charAt(i) == '0';
        }

        final int Size = S.length();
        pows = new long[Size];
        pows[0] = 1;
        for (int i = 1; i < Size; i++) {
            pows[i] = P * pows[i - 1];
        }

        int result = 0;
        for (int ws = 1; ws <= Size; ws++) {
            long hash = 0;
            int count = 0;
            Set<Long> set = new HashSet<>();

            for (int i = 0; i < Size - ws + 1; i++) {
                if (i == 0) {
                    hash = hash(S, ws);
                    count = count(S, isBad, ws);
                } else {
                    char oldChar = S.charAt(i - 1);
                    char newChar = S.charAt(i + ws - 1);
                    hash = P * (hash - oldChar * pows[ws - 1]) + newChar;

                    if (isBad[S.charAt(i - 1) - 'a']) count--;
                    if (isBad[S.charAt(i + ws - 1) - 'a']) count++;
                }

                if (!set.contains(hash) && count <= K) {
                    set.add(hash);
                }
            }

            result += set.size();
        }
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}
