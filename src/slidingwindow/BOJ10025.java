package slidingwindow;

import java.util.*;
import java.io.*;



/**
 * 게으른 백곰 - BOJ10025
 * -----------------
 * category: sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 4 3
 * 4 7
 * 10 15
 * 2 2
 * 5 1
 *
 * Output 1
 * 11
 * -----------------
 */
public class BOJ10025 {

    static final int SIZE = (int) 1e6 + 1;

    static int N, K;
    static int gi, xi;
    static int[] arr;

    public static long slidingWindow() {
        int ws = Math.min(2 * K + 1, SIZE);

        long tmp = 0, max = 0;
        for(int i = 0; i < ws; i++) {
            tmp += arr[i];
        }
        max = tmp;

        for(int i = 1; i <= SIZE - ws; i++) {
            tmp -= arr[i - 1];
            tmp += arr[i + ws - 1];
            max = Math.max(tmp, max);
        }

        return max;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        arr = new int[SIZE];

        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            gi = Integer.parseInt(st.nextToken());
            xi = Integer.parseInt(st.nextToken());
            arr[xi] = gi;
        }

        bw.write(slidingWindow() + "");
        bw.flush();

        br.close();
        bw.close();
    }
}