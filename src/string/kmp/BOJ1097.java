package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 마법의 문자열 - BOJ1097
 * -----------------
 *
 * 문제에 주어진 문자열 T(i)에 대하여
 * "L개의 문자로 이루어진 문자열 T가 있다. T(i)는 T를 i (0 ≤ i < L)번째 문자부터 시작하게 부터 시작하게 원형 이동한 것이고,
 * 길이는 T의 길이와 같다. 즉, 0 ≤ j < L을 만족하는 모든 j에 대해서, T(i)의 j번째 문자는 T의 (i+j)%L 번째 문자와 같다. "라고 정의하고 있다.
 * 또한 T(i)와 T가 같은 문자열이 되는 i가 정확히 K개가 있다면 T를 마법의 문자열이라고 한다.
 *
 * N개의 문자열 S1, S2, ..., SN이 주어진다. 크기가 N인 모든 순열 p = (p0, p1, ..., pN-1) 마다 새로운 문자열을 Sp0 + Sp1 + ... + SpN-1을 정의할 때
 * 마법의 문자열이 되는 모든 경우의 수를 따져야 한다.
 * N이 최대 8이므로 따져야 하는 순열의 경우의 수는 8 x 7 x ... x 1 = 8! = 40320이 되고, brute-force하게 모든 경우를 탐색해도 시간안에 해결이 가능하다.
 * 위의 정의에 따라 주어진 S1, S2, S3... SN의 문자열을 합쳐 SpN을 정의하고
 * 마법의 문자열 판정을 위해 SpN + SpN-1을 한 새로운 문자열을 SpN을 이용하여 KMP 알고리즘을 통해 패턴 매칭을 하면 된다.
 * ex) 첫번째 테스트 케이스의 경우 S1, S2, S3은 각 AB, RAAB, RA이고 한 종류로
 *     AB + RAAB + RA = ABRAABRA가 된다. 문자열을 원형 이동시킨 것처럼 하기 위해 ABRAABRA + ABRAABR = ABRAABRAABRAABR로 만들고 ABRAABRA로 패턴매칭을 하면 된다.
 *    
 * K개의 패턴 매칭이 된다면 이 SpN은 마법의 문자열인 것이다.
 * 이러한 식으로 모든 경우의 수를 세어주면 된다.
 *
 * -----------------
 * Input 1
 * 3
 * AB
 * RAAB
 * RA
 * 2
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 3
 * CAD
 * ABRA
 * ABRA
 * 1
 *
 * Output 2
 * 6
 * -----------------
 * Input 3
 * 4
 * AA
 * AA
 * AAA
 * A
 * 1
 *
 * Output 3
 * 0
 * -----------------
 * Input 4
 * 6
 * AA
 * AA
 * AAA
 * A
 * AAA
 * AAAA
 * 15
 *
 * Output 4
 * 720
 * -----------------
 * Input 5
 * 4
 * ABC
 * AB
 * ABC
 * CA
 * 3
 *
 * output 5
 * 0
 * -----------------
 * 6
 * A
 * B
 * C
 * A
 * B
 * C
 * 1
 *
 * Output 6
 * 672
 * -----------------
 * Input 7
 * 8
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAA
 * AAAAAAAAAAAAAAAAAAAB
 * 1
 *
 * Output 7
 * 40320
 * -----------------
 */
public class BOJ1097 {

    private static class KMP {
        private static int[] buildPi(String pattern) {
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

        public static int search(String text, String pattern) {
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
    }

    static int N, K;
    static String[] words;
    static boolean[] visited;

    public static int dfs(int depth, String word) {
        if (depth == N) {
            String text = word + word.substring(0, word.length() - 1);
            int matchCnt = KMP.search(text, word);
            if (matchCnt == K) return 1;
        }

        int count = 0;
        for (int i = 0; i < words.length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                count += dfs(depth + 1, word + words[i]);
                visited[i] = false;
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
        words = new String[N];
        visited = new boolean[N];
        for (int i = 0; i < N; i++) {
            words[i] = br.readLine();
        }
        K = Integer.parseInt(br.readLine());

        int cnt = dfs(0, "");
        bw.write(String.valueOf(cnt));

        // close the buffer
        br.close();
        bw.close();
    }
}
