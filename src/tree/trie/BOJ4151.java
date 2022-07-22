package tree.trie;

import java.io.*;
import java.util.*;

/**
 * Compound Words - BOJ4151
 * -----------------
 *
 * HashSet을 이용해 단어의 중복을 없애고 소문자 alphabet을 가지는 Trie에 삽입한다.
 * 이후 Trie에 저장된 단어들을 이용하여 각각의 단어가 Compound word 인지 조사한다.
 * 예를들어, 만약 alien이라는 단어와 al, ien이라는 단어가 Trie에 저장되어 있다면
 * alien 단어 탐색 시 두번 째 글자인 l에서 isEnd가 true 일 것이고 이 위치부터의 substring인 ien을 Trie에 있는지 조사하여 있다면
 * 해당 단어는 compound word 이므로 최종 결과에 넣는다.
 *
 * -----------------
 * Input 1
 * a
 * alien
 * born
 * less
 * lien
 * never
 * nevertheless
 * new
 * newborn
 * the
 * zebra
 *
 * Output 1
 * alien
 * newborn
 * -----------------
 * Input 2
 * al
 * ien
 * alien
 * alien
 *
 * alien
 *
 * Output 2
 * alien
 * -----------------
 * Input 3
 * al
 * ien
 * alien
 * alien
 * alienn
 * n
 *
 * Output 3
 * alien
 * alienn
 * -----------------
 * Input 4
 * al
 * ien
 * ie
 * alien
 * alie
 *
 * Output 4
 * alie
 * alien
 * -----------------
 */
public class BOJ4151 {

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
    }

    public static boolean isCompoundWord(Trie trie, String word, boolean isSecond) {
        if (word == null || word.isEmpty())
            return false;

        Trie.Node current = trie.root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int idx = c - 'a';
            if (current.children[idx] == null) {
                return false;
            }
            if (current.isEnd) {
                if (!isSecond) {
                    if(isCompoundWord(trie, word.substring(i), true)) {
                        return true;
                    }
                }
            }
            current = current.children[idx];
        }

        return current.isEnd && isSecond;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        Trie trie = new Trie();
        HashSet<String> wordSet = new HashSet<>();
        String line = "";
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            trie.add(line);
            wordSet.add(line);
        }

        StringBuilder result = new StringBuilder();
        List<String> resultList = new ArrayList<>();
        for (String s : wordSet) {
            if (isCompoundWord(trie, s, false)) {
                resultList.add(s);
            }
        }
        Collections.sort(resultList);   // sort to alphabetical order.

        for(int i = 0; i < resultList.size(); i++) {
            result.append(resultList.get(i));
            result.append('\n');
        }

        // write the result
        if (result.length() > 0) result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}