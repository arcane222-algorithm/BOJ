package string.trie;
import java.io.*;
import java.util.*;


/**
 * 접두사 찾기 - BOJ14426
 * -----------------
 *
 * Trie의 개념을 이해할 수 있는 문제이다.
 * 각 Trie의 노드마다 알파벳 소문자 26개의 자식을 가지고 있도록 구현하면 된다.
 * 기존 알파벳에 대한 Trie는 find 메소드의 경우 마지막에 return current.isEnd;를 하여 해당 단어가 Trie에 있는지 확인하는 작업을 수행한다.
 * 이번 문제에서는 해당 위치에서 return true;를 수행하여 검사하려는 문자열이 Trie의 부분문자열로 존재할 경우 무조건 true를 리턴하도록 수정하면 된다.
 * (검사하려는 문자열이 Trie안에 부분문자열로 존재한다는 것은 Trie 내부 문자열들에 대해 접두사 (Prefix)라는 뜻이다.)
 *
 * -----------------
 * Input 1
 * 5 10
 * baekjoononlinejudge
 * startlink
 * codeplus
 * sundaycoding
 * codingsh
 * baekjoon
 * star
 * start
 * code
 * sunday
 * coding
 * cod
 * online
 * judge
 * plus
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ14426 {

    private static class Trie {
        private static final int MAX_LENGTH = 26;

        private static class Node {
            Node[] children;
            boolean isEnd;

            public Node() {
                children = new Node[MAX_LENGTH];
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        public void add(String word) {
            Node current = root;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - 'a';
                if (current.children[idx] == null) {
                    current.children[idx] = new Node();
                }
                current = current.children[idx];
            }
            current.isEnd = true;
        }

        public boolean find(String word) {
            Node current = root;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - 'a';
                if (current.children[idx] == null) {
                    return false;
                }
                current = current.children[idx];
            }

            // if you want to check whether the word exist in trie,
            // change return statement to return current.isEnd;
            return true;
        }
    }

    static int N, M;
    static String[] prefixes;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // parse N, M
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // parse words
        Trie trie = new Trie();
        for (int i = 0; i < N; i++) {
            String word = br.readLine();
            trie.add(word);
        }

        // parse prefixes
        int result = 0;
        prefixes = new String[M];
        for (int i = 0; i < M; i++) {
            String prefix = br.readLine();
            if (trie.find(prefix)) {
                result++;
            }
        }

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}
