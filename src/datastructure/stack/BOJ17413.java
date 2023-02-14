package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 단어 뒤집기 2 - BOJ17413
 * -----------------
 * category: implementation (구현)
 *           data-structure (자료구조)
 *           string (문자열)
 *           stack (스택)
 * -----------------
 * Input 1
 * baekjoon online judge
 *
 * Output 1
 * noojkeab enilno egduj
 * -----------------
 * Input 2
 * <open>tag<close>
 *
 * Output 2
 * <open>gat<close>
 * -----------------
 * Input 3
 * <ab cd>ef gh<ij kl>
 *
 * Output 3
 * <ab cd>fe hg<ij kl>
 * -----------------
 * Input 4
 * one1 two2 three3 4fourr 5five 6six
 *
 * Output 4
 * 1eno 2owt 3eerht rruof4 evif5 xis6
 * -----------------
 * Input 5
 * <int><max>2147483647<long long><max>9223372036854775807
 *
 * Output 5
 * <int><max>7463847412<long long><max>7085774586302733229
 * -----------------
 */
public class BOJ17413 {

    static StringBuilder builder = new StringBuilder();
    static Stack<Character> stack = new Stack<>();

    public static void flushStack() {
        while (!stack.isEmpty()) {
            builder.append(stack.pop().charValue());
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String line = br.readLine();

        int idx = 0;
        boolean addToStack = true;
        while (idx < line.length()) {
            char c = line.charAt(idx);

            switch (c) {
                case '<':
                    addToStack = false;
                    if (!stack.isEmpty())
                        flushStack();
                    builder.append(c);
                    break;

                case '>':
                    addToStack = true;
                    builder.append(c);
                    break;

                case ' ':
                    flushStack();
                    builder.append(c);
                    break;

                default:
                    if (addToStack)
                        stack.add(c);
                    else
                        builder.append(c);
            }
            idx++;
        }

        if (!stack.isEmpty())
            flushStack();

        bw.write(builder.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}