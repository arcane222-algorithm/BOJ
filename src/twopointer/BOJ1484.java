package twopointer;

import java.io.*;
import java.util.*;


/**
 * 다이어트 - BOJ1484
 * -----------------
 * category: mathematics (수학)
 *           number theory (정수론)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 15
 *
 * Output 1
 * 4
 * 8
 * -----------------
 * Input 2
 * 99999
 *
 * Output 2
 * 320
 * 468
 * 1240
 * 5560
 * 16668
 * 50000
 * -----------------
 */
public class BOJ1484 {

    static int G;

    public static int pow2(int num) {
        return num * num;
    }

    public static List<Integer> twoPointerSearch() {
        int curr = 2, prev = 1;
        List<Integer> result = new LinkedList<>();

        while(prev < curr) {
            int val = pow2(curr) - pow2(prev);

            if(val > G) {
                prev++;
            } else if(val < G) {
                curr++;
            } else {
                result.add(curr);
                prev++;
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        G = Integer.parseInt(br.readLine());

        List<Integer> result = twoPointerSearch();
        if(result.size() == 0) {
            bw.write("-1");
        } else{
            for(int val : result)
                bw.write(val + "\n");
        }

        // close the buffer
        br.close();
        bw.close();
    }
}