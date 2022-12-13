package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 크게 만들기 - BOJ2812
 * -----------------
 * category: data structure (자료 구조)
 *           stack (스택)
 * -----------------
 * Input 1
 * 4 2
 * 1924
 *
 * Output 1
 * 94
 * -----------------
 * Input 2
 * 7 3
 * 1231234
 *
 * Output 2
 * 3234
 * -----------------
 * Input 3
 * 10 4
 * 4177252841
 *
 * Output 3
 * 775841
 * -----------------
 * Input 4
 * 10 4
 * 7898111101
 *
 * Output 4
 * 981111
 * -----------------
 */
public class BOJ2812 {

    static int N, K;
    static StringBuilder result = new StringBuilder();

    public static int toDigit(char c) {
        return c >= '0' && c <= '9' ? c - '0' : 0;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        String num = br.readLine();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < num.length(); i++) {
            int value = toDigit(num.charAt(i));
            while (!stack.isEmpty() && K > 0) {
                int last = stack.peek();
                if (last < value) {
                    stack.pop();
                    K--;
                } else break;
            }
            stack.add(value);
        }

        while (!stack.isEmpty() && K > 0) {
            stack.pop();
            K--;
        }

        for (int val : stack) {
            result.append(val);
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}