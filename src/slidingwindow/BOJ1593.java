package slidingwindow;

import java.io.*;
import java.util.*;


/**
 * 문자 해독 - BOJ1593
 * -----------------
 * category: string (문자열)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 4 11
 * cAda
 * AbrAcadAbRa
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ1593 {

    static final int MAX_CHAR_SIZE = 52;
    static int w, s;

    public static int toIdx(char c) {
        // 'a' ~ 'z': 0 ~ 25,  'A' ~ 'Z': 26 ~ 51
        return (c >= 'a' && c <= 'z') ? c - 'a' : c - 'A' + 26;
    }

    public static int[] initArr(String str, int size) {
        int[] arr = new int[MAX_CHAR_SIZE];
        for (int i = 0; i < size; i++) {
            int idx = toIdx(str.charAt(i));
            arr[idx]++;
        }
        return arr;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        w = Integer.parseInt(st.nextToken());
        s = Integer.parseInt(st.nextToken());

        String wStr = br.readLine();
        String sStr = br.readLine();
        int[] base = initArr(wStr, w);
        int[] window = initArr(sStr, w);

        int count = Arrays.equals(base, window) ? 1 : 0;
        for (int i = 1; i <= sStr.length() - w; i++) {
            int prevIdx = toIdx(sStr.charAt(i - 1));
            int nextIdx = toIdx(sStr.charAt(i + w - 1));
            window[prevIdx]--;
            window[nextIdx]++;

            if (Arrays.equals(base, window))
                count++;
        }
        bw.write(String.valueOf(count));

        // close the buffer
        br.close();
        bw.close();
    }
}