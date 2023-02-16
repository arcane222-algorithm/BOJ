package prefixsum;

import java.io.*;
import java.util.*;


/**
 * 피아노 체조 - BOJ21318
 * -----------------
 * category: prefix sum (누적 합)
 * -----------------
 * Input 1
 * 9
 * 1 2 3 3 4 1 10 8 1
 * 5
 * 1 3
 * 2 5
 * 4 7
 * 9 9
 * 5 9
 *
 * Output 1
 * 0
 * 0
 * 1
 * 0
 * 3
 * -----------------
 * Input 2
 * 6
 * 573 33283 5572 346 906 567
 * 5
 * 5 6
 * 1 3
 * 2 2
 * 1 6
 * 3 6
 *
 * Output 2
 * 1
 * 1
 * 0
 * 3
 * 2
 * -----------------
 */
public class BOJ21318 {

    static int N, Q;
    static int[] prefixSum;

    public static int query(int x, int y) {
        return prefixSum[y] - prefixSum[x];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        prefixSum = new int[N];
        st = new StringTokenizer(br.readLine());

        int curr = Integer.parseInt(st.nextToken());
        for (int i = 1; i < N; i++) {
            int nxt = Integer.parseInt(st.nextToken());
            if (curr > nxt)
                prefixSum[i] = prefixSum[i - 1] + 1;
            else
                prefixSum[i] = prefixSum[i - 1];
            curr = nxt;
        }

        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            bw.write(query(x - 1, y - 1) + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}
