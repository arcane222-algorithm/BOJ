package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 속타는 저녁 메뉴 - BOJ11585
 * -----------------
 *
 * 'BOJ10266 - 시계 사진들'과 유사한 문제이다.
 * 입력으로 주어지는 두 문자열 중 첫 번째 문자열은 저녁으로 고기를 먹게 될 돌림판의 경우의 수 (pattern)이 주어진다.
 * 두 번째 문자열은 돌림판의 현재 상태 (text) 가 주어진다.
 *
 * 'BOJ10266 - 시계 사진들'처럼 문자열이 순환하며 나타내는 것을 표현하기 위해 text 문자열 두개를 더하여 표현한다.
 * 주의할 점은 룰렛이 한바퀴 돌아 제자리에 도착하였을 때의 경우의 수는 현재 상태와 같으므로 해당 상태를 제외해야 한다.
 * 즉, text 문자열을 같은 길이로 두개 이어붙이는 것이 아미라 text + (text - 1)을 이어 붙여야 (text - 1)에서 현재 상태로 다시 돌아오는 경우의 수가 빠지게 된다.
 * 해당 text + (text - 1) 문자열에 대하여 pattern 문자열을 이용하여 kmp 알고리즘을 수행한다.
 * kmp 알고리즘을 수행하며 경우의 수를 counting 하고, 전체 경우의 수 (룰렛판의 칸의 수) N에 대하여
 * 분모 N이 분자 counting으로 나누어 진다면 1/(N / counting)을 출력하고 나누어지지 않는다면 counting/N을 출력한다.
 *
 * -----------------
 * Input 1
 * 9
 * I W A N T M E A T
 * E A T I W A N T M
 *
 * Output 1
 * 1/9
 * -----------------
 * Input 2
 * 6
 * A A B A A B
 * B A A B A A
 *
 * Output 2
 * 1/3
 * -----------------
 * Input 3
 * 5
 * A B C A B
 * A B C A B
 *
 * Output 3
 * 1/5
 * -----------------
 */
public class BOJ11585 {

    static int N;
    static String text, pattern;

    public static int[] buildPi(String pattern) {
        int[] pi = new int[pattern.length()];
        int match = 0, search = 1;

        for (; search < pattern.length(); search++) {
            while (match > 0 && pattern.charAt(search) != pattern.charAt(match)) {
                match = pi[match - 1];
            }

            if (pattern.charAt(search) == pattern.charAt(match)) {
                pi[search] = ++match;
            }
        }

        return pi;
    }

    public static int kmp(String text, String pattern) {
        int[] pi = buildPi(pattern);
        int match = 0, search = 0, count = 0;

        for (; search < text.length(); search++) {
            while (match > 0 && text.charAt(search) != pattern.charAt(match)) {
                match = pi[match - 1];
            }

            if (text.charAt(search) == pattern.charAt(match)) {
                if (match == pattern.length() - 1) {
                    count++;
                    match = pi[match];
                } else {
                    match++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        StringBuilder patternBuilder = new StringBuilder();
        for (int i = 0; i < N; i++) {
            patternBuilder.append(st.nextToken());
        }
        pattern = patternBuilder.toString();

        st = new StringTokenizer(br.readLine());
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < N; i++) {
            textBuilder.append(st.nextToken());
        }
        textBuilder.append(textBuilder.substring(0, textBuilder.length() - 1));
        text = textBuilder.toString();

        int numerator = kmp(text, pattern);
        int denominator = N;
        if (denominator % numerator == 0) {
            bw.write(1 + "/" + denominator / numerator);
        } else {
            bw.write(numerator + " / " + denominator);
        }

        // close the buffer
        br.close();
        bw.close();
    }
}