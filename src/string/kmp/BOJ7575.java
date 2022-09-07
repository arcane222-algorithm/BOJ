package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 바이러스 - BOJ7575
 * -----------------
 *
 * 첫 번째 프로그램 코드에서 K만큼 부분 문자열을 골라 이 값을 pattern으로 사용한다.
 * (즉, 부분 문자열의 시작 위치를 i, 끝 위치를 j라고 한다면, 0 <= i <= j <= N, j - i + 1 == K 이다.)
 *
 * 이후 프로그램 2 ~ N까지의 코드에서 해당 pattern을 이용하여 kmp 알고리즘을 수행한다.
 * 이때, 바이러스 코드의 경우 반대로 나타날 수도 있기 때문에 프로그램 2 ~ N의 코드를 뒤집은 코드도 함께 검사한다.
 * 각 검사과정에서 첫 번째 프로그램 코드에서 K만큼 고른 부분 문자열이 모두 발견된다면 공통되는 바이러스 코드가 존재하는 것이므로 YES를 출력한다.
 * 만약 검사과정에서 하나라도 해당 부분 문자열이 존재하지 않는다면 바이러스 코드가 아닌 것이므로 넘어간다.
 * 첫 번째 프로그램의 코드에서 K인 부분 문자열을 모두 이용하여 검사하였을 때 바이러스 코드인 경우가 나오지 않는다면 NO를 출력한다.
 *
 * -----------------
 * Input 1
 * 3 4
 * 13
 * 10 8 23 93 21 42 52 22 13 1 2 3 4
 * 11
 * 1 3 8 9 21 42 52 22 13 41 42
 * 10
 * 9 21 42 52 13 22 52 42 12 21
 *
 * Output 1
 * YES
 * -----------------
 */
public class BOJ7575 {

    static int N, K;
    static int[] patterns;
    static List<Integer>[] texts;
    static List<Integer>[] reverses;

    public static List<Integer> reverse(List<Integer> list) {
        List<Integer> reverse = new ArrayList<>();
        for (int i = list.size() - 1; i > -1; i--) {
            reverse.add(list.get(i));
        }

        return reverse;
    }

    public static int[] buildPi(int[] patterns, int begin) {
        int[] pi = new int[K];
        int match = 0, search = 1;

        for (; search < K; search++) {
            while (match > 0 && patterns[search + begin] != patterns[match + begin]) {
                match = pi[match - 1];
            }

            if (patterns[search + begin] == patterns[match + begin]) {
                pi[search] = ++match;
            }
        }

        return pi;
    }

    public static boolean kmp(List<Integer> text, int[] patterns, int begin) {
        int[] pi = buildPi(patterns, begin);
        int match = 0, search = 0;

        for (; search < text.size(); search++) {
            while (match > 0 && text.get(search) != patterns[match + begin]) {
                match = pi[match - 1];
            }

            if (text.get(search) == patterns[match + begin]) {
                if (match == K - 1) {
                    return true;
                } else {
                    match++;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        // parse N, K
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        // init patterns
        int patternsLen = Integer.parseInt(br.readLine());
        patterns = new int[patternsLen];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < patternsLen; i++) {
            patterns[i] = Integer.parseInt(st.nextToken());
        }

        // init text
        texts = new ArrayList[N - 1];
        reverses = new ArrayList[N - 1];
        for (int i = 0; i < N - 1; i++) {
            texts[i] = new ArrayList<>();
            int textLen = Integer.parseInt(br.readLine());
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < textLen; j++) {
                texts[i].add(Integer.parseInt(st.nextToken()));
            }
            reverses[i] = reverse(texts[i]);
        }

        boolean isVirus = true;
        for (int i = 0; i < patternsLen - K; i++) {
            isVirus = true;
            for (int j = 0; j < texts.length; j++) {
                isVirus = kmp(texts[j], patterns, i) || kmp(reverses[j], patterns, i);
                if (!isVirus) break;
            }
            if (isVirus) break;
        }

        bw.write(isVirus ? "YES" : "NO");

        // close the buffer
        br.close();
        bw.close();
    }
}