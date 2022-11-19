package bruteforce;

import java.io.*;
import java.util.*;


/**
 * A와 B 2 - BOJ12919
 * -----------------
 * category: implementation (구현)
 *           string (문자열)
 *           brute force (브루트포스 알고리즘)
 *           recursion (재귀)
 * -----------------
 * -----------------
 * Input 1
 * A
 * BABA
 *
 * Output 1
 * 1
 * -----------------
 * BAAAAABAA
 * BAABAAAAAB
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * A
 * ABBA
 *
 * Output 3
 * 0
 * -----------------
 */
public class BOJ12919 {
    static boolean canMake;

    public static void dfs(long target, int targetSize, long curr, int currSize) {
        if (canMake) return;

        if (targetSize == currSize) {
            if (target == curr) {
                canMake = true;
            }
        } else {
            if ((curr & 1L) == 1) {
                dfs(target, targetSize, curr >> 1, currSize - 1);
            }
            if ((curr & (1L << (currSize - 1))) == 0) {
                long rev = reverse(curr, currSize);
                dfs(target, targetSize, rev >> 1, currSize - 1);
            }
        }
    }

    public static long toLong(String str) {
        long num = 0;
        long curr = 1L << (str.length() - 1);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 'A') num |= curr;
            curr >>= 1;
        }

        return num;
    }

    public static long reverse(long num, int size) {
        long rev = 0;
        long left = 1L << (size - 1);
        long right = 1L;

        for (int i = 0; i < size; i++) {
            if ((num & right) > 0) {
                rev |= left;
            }
            left >>= 1;
            right <<= 1;
        }

        return rev;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String S = br.readLine();
        String T = br.readLine();

        long sToLong = toLong(S);
        long tToLong = toLong(T);

        dfs(sToLong, S.length(), tToLong, T.length());

        bw.write(canMake ? "1" : "0");

        // close the buffer
        br.close();
        bw.close();
    }
}