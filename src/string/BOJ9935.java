package string;

import java.io.*;
import java.util.*;


/**
 * 문자열 폭발 - BOJ9935
 * -----------------
 * category: string (문자열),
 *           stack (스택)
 *
 * Time-Complexity: 대상 문자열의 길이 N (1<= N <= 10^6)
 *                  폭탄(패턴) 문자열의 길이 M (1<= M <= 36)
 *                  O(NM)
 * -----------------
 *
 * Stack 자료구조를 이용하여 문자열이 폭발한 후 나머지 문자열을 연결하여 다시 폭발 문자열이 있는지 체크한다.
 * (1) 대상 문자열 (text)의 글자를 하나씩 stack에 삽입한다.
 * (2) 문자열을 삽입하는 과정에서 현재 탐색 중인 글자가 폭탄 문자열 (pattern)의 마지막 글자와 일치하면
 *     stack의 최상단부터 폭탄 문자열의 길이만큼 조사하여 폭탄 문자열과 일치하는지 검사한다.
 *     만약 스택의 최상단 부분 문자열이 폭탄 문자열과 일치하면 폭발하며 사라져야 하므로 스택에서 pop한다.
 *
 * 위 (1), (2)과정을 반복한 후
 * 스택이 비어있다면 모든 문자열이 폭발한 것이므로 FRULA를 출력한다.
 * 스택이 비어있지 않다면 스택 안에 들어있는 문자들을 스택의 가장 하위부터 순서대로 출력한다.
 *
 * -----------------
 * Input 1
 * mirkovC4nizCC44
 * C4
 *
 * Output 1
 * mirkovniz
 * -----------------
 * Input 2
 * 12ab112ab2ab
 * 12ab
 *
 * Output 2
 * FRULA
 * -----------------
 * Input 3
 * a
 * ba
 *
 * Output 3
 * a
 * -----------------
 */
public class BOJ9935 {

    public static boolean compare(Stack<Character> stack, String pattern) {
        final int sSize = stack.size();
        final int pSize = pattern.length();
        if (sSize < pSize) return false;

        for (int i = 0; i < pSize; i++) {
            if (stack.get(sSize - 1 - i) != pattern.charAt(pSize - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static String explosion(String text, String pattern) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < text.length(); i++) {
            stack.add(text.charAt(i));
            if (text.charAt(i) == pattern.charAt(pattern.length() - 1)) {
                if (compare(stack, pattern)) {
                    for (int j = 0; j < pattern.length(); j++) {
                        stack.pop();
                    }
                }
            }
        }

        if (stack.size() == 0) {
            return null;
        } else {
            StringBuilder result = new StringBuilder();
            for (char c : stack) {
                result.append(c);
            }
            return result.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String text = br.readLine();
        String pattern = br.readLine();
        String result = explosion(text, pattern);
        bw.write(result == null ? "FRULA" : result);

        // close the buffer
        br.close();
        bw.close();
    }
}