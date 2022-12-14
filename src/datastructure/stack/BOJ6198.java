package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 옥상 정원 꾸미기 - BOJ6198
 * -----------------
 * category: data structure (자료 구조)
 *           stack (스택)
 * -----------------
 * Input 1
 * 6
 * 10
 * 3
 * 7
 * 4
 * 12
 * 2
 *
 * Output 1
 * 5
 * -----------------
 */
public class BOJ6198 {

    static int N;
    static int[] nums;
    static Stack<Integer> stack;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        nums = new int[N];
        stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(br.readLine());
        }

        long count = 0;
        for (int i = 0; i < N; i++) {
            int value = nums[i];
            while(!stack.isEmpty()) {
                int last = stack.peek();
                if(last <= value) {
                    stack.pop();
                } else break;
            }

            count += stack.size();
            stack.add(value);
        }
        bw.write(String.valueOf(count));

        // close the buffer
        br.close();
        bw.close();
    }
}