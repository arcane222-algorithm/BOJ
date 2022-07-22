package tree.trie;

import java.io.*;
import java.util.*;

/**
 * 휴대폰 자판 - BOJ5670
 * -----------------
 *
 * 영문자 소문자로 구성된 트라이를 구축 할 때 각 트라이의 노드마다 childCount를 설정하여 자식 수를 저장함. (add 연산 시 업데이트)
 * 노드 탐색 시 Root에서 시작하여 문자열의 마지막 이전 글자까지 탐색함 (즉, 다음에 입력할 글자를 (다음 글자를 버튼을 누를지, 자동완성을 받을지)
 * 부모 노드가 가지고 있는 childCount를 이용하여 계산, 결정함)
 * 이때 해당 글자의 자식 수가 1이고 마지막 글자가 아니라는 것 (isEnd == false) 은 다음 입력 글자에 대한 선택지가 1개이므로 자동완성을 받을 수 있으므로 count 증가 x
 * 반대로 해당 문자의 자식 수가 1이 아니거나 (or) (즉, 0이거나 2 이상) 단어의 끝이라면 선택지가 여러 개 이므로 휴대폰 자판을 직접 눌러야함 (count를 증가시킴)
 * (자식 수가 0일 때도 선택지는 1개이지만, 자동완성을 받지 못한 마지막 글자의 경우 직접 눌러야 하므로 count를 증가시켜 준다.)
 * 
 * 이때 루트에서부터 탐색하며 단어의 마지막 글자를 제외한 모든 노드를 탐색하게 되는데, 마지막 글자를 제외하였는데도 탐색하려는 글자가 마지막 글자 (isEnd == ture)
 * 인 경우, 현재 탐색하고 있는 글자가 Trie에 포함된 단어의 접두사가 될 수 있는 경우이고, 이 경우 자식 수가 여러 개인 것처럼 선택지가 여러 개가 되므로 휴대폰 자판을 직접 눌러야 한다.
 * (다시 말해서, hell은 hello의 접두사(prefix)가 될 수 있고 hello 탐색 시 4번 째 노드 l에서 isEnd가 true이므로,
 * l에서 그 다음 단어를 이어갈지 (hello), 단어를 끝마칠지 (hell) 선택지가 여려 개이므로 위에서 설명한 것과 같이 count를 증가시켜준다.)
 *
 * -----------------
 * Input 1
 * 4
 * hello
 * hell
 * heaven
 * goodbye
 * 3
 * hi
 * he
 * h
 * 7
 * structure
 * structures
 * ride
 * riders
 * stress
 * solstice
 * ridiculous
 *
 * Output 1
 * 2.00
 * 1.67
 * 2.71
 * -----------------
 */
public class BOJ5670 {

    private static class Trie {
        private static final int MAX_LENGTH = 26;

        private static class Node {
            Node[] children;
            boolean isEnd;
            int childCount;

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
                    current.childCount++;
                }
                current = current.children[idx];
            }
            current.isEnd = true;
        }

        public int clickCount(String word) {
            //  pre-process the root character
            int cnt = 1, idx = getIdx(word.charAt(0));
            Node current = root.children[idx];

            for(int i = 1; i < word.length(); i++) {
                idx = getIdx(word.charAt(i));

                if(current.childCount != 1 || current.isEnd) {
                    cnt++;
                }
                current = current.children[idx];
            }

            return cnt;
        }

        public int getIdx(char c) {
            return c - 'a';
        }
    }

    static int N;
    static String[] words;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N
        String line = "";
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            N = Integer.parseInt(line);
            words = new String[N];
            Trie trie = new Trie();

            for (int i = 0; i < N; i++) {
                words[i] = br.readLine();
                trie.add(words[i]);
            }

            int count = 0;
            for (int i = 0; i < N; i++) {
                int cnt = trie.clickCount(words[i]);
                count += cnt;
            }
            result.append(String.format("%.2f\n", count / (double) N));
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
