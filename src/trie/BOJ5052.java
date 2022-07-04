package trie;

import java.io.*;
import java.util.*;


/**
 * 전화번호 목록 - BOJ5052
 * -----------------
 *
 * 전화번호를 길이 순으로 정렬한 후 전화번호길이가 긴 전화번호부터 Trie에 삽입함.
 * 삽입한 전화번호보다 이전에 위치한 (index - 1) 전화번호의 경우 해당 Trie에 삽입되어 있는 전화번호들의 접두사 (prefix)가 되는지 체크
 * Trie를 구성할 때 알파벳이 아닌 전화번호를 위한 숫자 (0 ~ 9) 로 구성함
 *
 * -----------------
 * Input 1
 * 2
 * 3
 * 911
 * 97625999
 * 91125426
 * 5
 * 113
 * 12340
 * 123440
 * 12345
 * 98346
 *
 * Output 1
 * NO
 * YES
 * -----------------
 * Input 2
 * 1
 * 3
 * 12345
 * 15678
 * 123456
 *
 * Output 2
 * NO
 * -----------------
 */
public class BOJ5052 {

    private static class Trie {
        private static final int MAX_LENGTH = 10;

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
                int idx = c - '0';
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
                int idx = c - '0';
                if (current.children[idx] == null) {
                    return false;
                }
                current = current.children[idx];
            }

            return true;
        }
    }

    static int t, n;
    static List<String> phoneNumbers;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse t
        t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int n = Integer.parseInt(br.readLine());
            phoneNumbers = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                String phoneNumber = br.readLine();
                phoneNumbers.add(phoneNumber);
            }
            Collections.sort(phoneNumbers, Comparator.comparingInt(String::length));

            Trie trie = new Trie();
            boolean check = true;
            for (int j = n - 1; j > 0; j--) {
                trie.add(phoneNumbers.get(j));
                if (trie.find(phoneNumbers.get(j - 1))) {
                    check = false;
                    break;
                }
            }
            result.append(check ? "YES\n" : "NO\n");
        }


        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}