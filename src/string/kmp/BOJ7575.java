package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 바이러스 - BOJ7575
 * -----------------
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