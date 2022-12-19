package dp;

import java.io.*;
import java.util.*;


/**
 * LCS - BOJ9251
 * -----------------
 * category: string (문자열)
 *           dp (동적 계획법)
 * -----------------
 * Input 1
 * ACAYKP
 * CAPCAK
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ9251 {

    static int[][] dp;

    public static int getLcsLen(String str1, String str2) {
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String str1 = br.readLine();
        String str2 = br.readLine();
        dp = new int[str1.length() + 1][str2.length() + 1];
        bw.write(String.valueOf(getLcsLen(str1, str2)));

        // close the buffer
        br.close();
        bw.close();
    }
}
