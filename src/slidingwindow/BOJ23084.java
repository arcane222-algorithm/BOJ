package slidingwindow;

import java.io.*;
import java.util.*;



/**
 * IUPC와 비밀번호 - BOJ23084
 * -----------------
 * category: string (문자열)
 *           case work (많은 조건 분기)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * abc
 * 4
 * defabcghi
 * bda
 * cba
 * ac
 *
 * Output 1
 * YES
 * YES
 * NO
 * NO
 * -----------------
 * Input 2
 * 7 5
 * 1 1 1 1 1 5 1
 *
 * Output 2
 * 9
 * 2
 * -----------------
 * Input 3
 * adc
 * 3
 * adecf
 * decf
 * defad
 *
 * Output 3
 * YES
 * YES
 * YES
 * -----------------
 */
public class BOJ23084 {

    static final int MAX_ALPHABET_COUNT = 26;

    static int N;
    static StringBuilder result = new StringBuilder();

    public static int compare(int[] cntArr1, int[] cntArr2, int windowSize) {
        int gap = 0;
        for (int i = 0; i < MAX_ALPHABET_COUNT; i++) {
            gap += Math.abs(cntArr1[i] - cntArr2[i]);
        }

        return gap >> 1;
    }

    public static int[] getCnt(String str, int size) {
        int[] cnt = new int[MAX_ALPHABET_COUNT];
        for (int i = 0; i < size; i++) {
            int idx = str.charAt(i) - 'a';
            cnt[idx]++;
        }
        return cnt;
    }

    public static boolean isValid(String window, String target) {
        if (window.length() > target.length()) return false;

        int[] baseCntArr = getCnt(window, window.length());
        int[] strCntArr = getCnt(target, window.length());
        if (window.length() == target.length()) {
            return compare(baseCntArr, strCntArr, window.length()) == 1;
        } else {
            // base.length() < str.length()
            int idx = 0;
            while (true) {
                int res = compare(baseCntArr, strCntArr, window.length());
                if (res == 0 || res == 1) return true;

                idx++;
                if (idx > target.length() - window.length()) break;

                int prevIdx = idx - 1;
                int nxtIdx = idx + window.length() - 1;
                strCntArr[target.charAt(prevIdx) - 'a']--;
                strCntArr[target.charAt(nxtIdx) - 'a']++;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String S = br.readLine();
        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            result.append(isValid(S, br.readLine()) ? "YES\n" : "NO\n");
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
