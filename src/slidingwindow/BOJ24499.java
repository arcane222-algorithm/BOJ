package slidingwindow;

import java.io.*;
import java.util.StringTokenizer;


/**
 * blobyum - BOJ24499
 * -----------------
 * category: prefix sum (누적 합)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 4 2
 * 5 2 3 4
 *
 * Output 1
 * 9
 * -----------------
 */
public class BOJ24499 {

    public static int N, K;
    public static int[] applePies;

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        applePies = new int[N];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            applePies[i] = Integer.parseInt(st.nextToken());
        }

        int tmp = 0, ans = 0;
        for(int i = 0; i < K; i++) {
            tmp += applePies[i];
        }
        ans = tmp;

        for(int i = 1; i < N; i++) {
            int left = i - 1;
            int right = i + K - 1 > N - 1 ? i + K - 1 - N : i + K - 1;
            tmp -= applePies[left];
            tmp += applePies[right];
            if(tmp > ans) {
                ans = tmp;
            }
        }

        // write the result
        bw.write(String.valueOf(ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}