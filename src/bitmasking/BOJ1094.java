package bitmasking;

import java.io.*;
import java.util.*;


/**
 * 막대기 - BOJ1094
 * -----------------
 * category: mathematics (수학)
 *           bit-masking (비트마스킹)
 * -----------------
 * Input 1
 * 23
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 32
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 64
 *
 * Output 3
 * 1
 * -----------------
 * Input 4
 * 48
 *
 * Output 4
 * 2
 * -----------------
 */
public class BOJ1094 {

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        int x = Integer.parseInt(br.readLine());
        int count = 0;
        while (x > 0) {
            if ((x & 1) == 1) {
                count++;
            }
            x >>= 1;
        }
        bw.write(String.valueOf(count));

        // close the buffer
        br.close();
        bw.close();
    }
}
