package string.trie;

import java.io.*;
import java.util.*;


/**
 * 아름다운 이름 - BOJ3080
 * -----------------
 *
 * 각 이름을 Trie에 넣었을 때 각 노드의 (자식의 수)! 한 값을 모두 곱한 것이 답이 된다.
 *
 * why) 한 글자만으로 경우의 수를 생각해 볼 때, 같은 알파벳 순서로 시작 하는 두 이름 사이에는 모두 그 순서로 시작하는 단어가 있어야 하므로
 * A I T T 라는 글자가 있다면, T T는 항상 같이 와야 하므로 T T 를 한 묶음으로 보고 I A T를 순서대로 세우는 경우의 수 (Permutation, 순열)
 * 는 3! = 3 x 2 x 1 = 6가지가 된다.
 * Input 2의 경우 M - A - R에서 R의 자식으로 A I T가 입력되므로 위와 같이 경우의 수가 6가지가 되는 것이다.
 * 그러므로 Input 2의 경우 자식의 수가 1이어서 1!인 경우를 제외하면 M - A - R - T에서 T의 자식의 수가 2 (A, I)이고
 * 위 예시의 M - A - R에서 R의 자식의 수가 3(A, I, T), M - A에서  A의 자식의 수가 2 (R, T)이므로
 * 2! x 3! x 2! = 24가 된다.
 *
 * 주의 할 점은 Input 3의 A, AA, AAA, AAAA는 Trie를 구성하면 각 노드의 자식의 수가 1이므로 1! x 1! x 1! x 1! = 1이 나와버린다.
 * 이 경우를 방지하기 위해 각 이름의 마지막에 영문 대문자가 아닌 단어를 붙여주는데, '.' character 하나를 추가하였다.
 * 그러면 2! x 2! x 2! = 8이 정답이 된다.
 *
 * 해당 문제의 경우 이름 3000개와 최대 이름의 길이 3000 - 1 = 2999가 입력될 수 있어 최대 3000 x 2999개의 단어가 입력되므로
 * 일반적인 배열을 이용한 Trie나 Map을 이용한 Trie를 구축할 경우 메모리 초과가 발생한다.
 * c++의 경우 vector를 이용한 Trie를 구축하면 아슬아슬하게 통과되지만, Java의 경우 vector를 이용하면 노드 탐색 시 선형탐색 (O(N))을 해야 하므로 시간초과가 발생한다.
 *
 * Trie 구축 시 배열이나 Map을 이용하되, Longest Common Prefix (LCP) (최장 공통 접두사)를 이용한다.
 * 두 이름의 접두사가 가장 많이 겹치려면 이름을 모두 정렬한 후 삽입하려는 단어에 대하여 해당 단어의 index - 1, index + 1을 탐색하면 된다.
 * 현재 단어와 index - 1의 단어와의 LCP를 prev, 현재 단어와 index + 1의 단어와의 LCP를 curr라고 한다면
 * 각 단어마다 max(prev, curr) + 1 길이까지 Trie에 넣도록 한다.
 * 즉, max(prev, curr) + 1 = max(LCP(str[i-1], str[i]), LCP(str[i], str[i + 1]) + 1
 *
 * ex) ABCDE, ABCWXYZ, ABCFGH 세 단어를 넣는다면 각 단어의 끝에 .을 더하고 정렬하면
 * [ABCDE., ABCFGH., ABCWXYZ.] 와 같이 된다.
 * LCP인 ABC + 1인 4번째까지 trie에 삽입하면 되는 것이다.
 * (LCP + 1 이후의 부분들은 자식의 수가 모두 1이라 경우의 수에 영향이 없으므로 메모리 절약을 위해 쳐낸다)
 *
 * + 계산 최적화를 위해 Factorial 같은 경우 미리 계산하여 배열에 저장 후 사용한다.
 *
 * -----------------
 * Input 1
 * 3
 * IVO
 * JASNA
 * JOSIPA
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 5
 * MARICA
 * MARTA
 * MATO
 * MARA
 * MARTINA
 *
 * Output 2
 * 24
 * -----------------
 * Input 3
 * 4
 * A
 * AA
 * AAA
 * AAAA
 *
 * Output 3
 * 8
 * -----------------
 * Input 4
 * 3
 * ABCDEFG
 * ABCDEFG
 * ABCDE
 *
 * Output 4
 * 2
 * -----------------
 */
public class BOJ3080 {

    private static class Trie {

        HashMap<Character, Trie> nodes;

        public Trie() {
            nodes = new HashMap<>();
        }

        public void add(String word, int max) {
            Trie current = this;
            max = Math.min(word.length(), max);
            for (int i = 0; i < max; i++) {
                char c = word.charAt(i);
                Trie next = current.nodes.get(c);
                if (next == null) {
                    next = new Trie();
                    current.nodes.put(c, next);
                }
                current = next;
            }
        }
    }

    static final int D = (int) (1e9 + 7);
    static int N;
    static List<String> words = new ArrayList<>();

    static Trie trie = new Trie();
    static long[] factorialArr = new long[28];

    public static void setFactorialArr(long n) {
        factorialArr[0] = factorialArr[1] = 1;
        for (int i = 2; i < n; i++) {
            factorialArr[i] = ((i % D) * (factorialArr[i - 1] % D)) % D;
        }
    }

    public static int compareWord(String word1, String word2) {
        int idx = 0;
        if (word1.length() < word2.length()) {
            String tmp = word1;
            word1 = word2;
            word2 = tmp;
        }
        for (int i = 0; i < word2.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                break;
            }
            idx++;
        }

        return idx;
    }

    public static long getCount(Trie trie) {
        long count = factorialArr[trie.nodes.size()];
        for (Trie t : trie.nodes.values()) {
            count = ((count % D) * getCount(t) % D) % D;
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        setFactorialArr(28);
        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            words.add(br.readLine() + ".");
        }
        Collections.sort(words);

        int prev, curr;
        prev = curr = 0;
        for (int i = 0; i < words.size(); i++) {
            if (i == words.size() - 1) {
                trie.add(words.get(i), prev + 1);
            } else {
                curr = compareWord(words.get(i), words.get(i + 1));
                trie.add(words.get(i), Math.max(prev, curr) + 1);
            }
            prev = curr;
        }

        long result = getCount(trie);
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}