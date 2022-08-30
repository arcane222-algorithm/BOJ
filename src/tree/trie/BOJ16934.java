package tree.trie;

import java.io.*;
import java.util.*;


/**
 * 게임 닉네임 - BOJ16934
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 5
 * baekjoon
 * startlink
 * bakejoon
 * beakjoon
 * baekjoon
 *
 * Output 1
 * b
 * s
 * bak
 * be
 * baekjoon2
 * -----------------
 * Input 2
 * 7
 * codeplus
 * startlink
 * beakjoon
 * baek
 * baekjoon
 * baek
 * codingwiki
 *
 * Output 2
 * c
 * s
 * b
 * ba
 * baekj
 * baek2
 * codi
 * -----------------
 * Input 3
 * 6
 * abcd
 * ab
 * ab
 * a
 * a
 * ab
 *
 * Output 3
 * a
 * ab
 * ab2
 * a
 * a2
 * ab3
 * -----------------
 */
public class BOJ16934 {

    private static class Trie {
        private static final int SIZE = 26;

        Trie[] nodes;

        public Trie() {
            nodes = new Trie[SIZE];
        }

        public String add(String word) {
            StringBuilder builder = new StringBuilder();
            boolean flag = false;
            Trie curr = this;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - 'a';
                if (!flag) builder.append(c);
                if (curr.nodes[idx] == null) {
                    curr.nodes[idx] = new Trie();
                    flag = true;
                }
                curr = curr.nodes[idx];
            }

            return builder.toString();
        }
    }

    static int N;
    static Trie root = new Trie();
    static HashMap<String, Integer> wordMap = new HashMap<>();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            String word = br.readLine();
            String nickname = root.add(word);
            if (wordMap.containsKey(word)) {
                wordMap.put(word, wordMap.get(word) + 1);
                result.append(nickname).append(wordMap.get(word)).append('\n');
            } else {
                result.append(nickname).append('\n');
                wordMap.put(word, 1);
            }
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}