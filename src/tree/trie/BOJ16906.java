package tree.trie;

import java.io.*;
import java.util.*;


/**
 * 욱제어 - BOJ16906
 * -----------------
 *
 * 욱제어로 되어 있는 각 단어 L1, L2, L3, ... , Ln은 한 단어가 다른 단어의 접두어가 되는 경우가 없다.
 * 즉, 접두어가 되지 않도록 각 단어를 배치하기 위해 각 단어의 길이를 오름차순 (혹은 내림차순) 으로 정렬한다.
 * 이후 각 단어의 길이를 이용하여 이진 트라이에 각 단어를 삽입한다.
 * 트라이에 해당 단어를 삽입할 때, dfs를 이용하여 삽입하려는 단어가 현재 트라이에 존재하는 단어와 접두사 관계가 되지 않도록 해야 한다.
 *
 * depth마다 현재 탐색하는 노드의 자식노드들 (nodes[0], nodes[1]) 이 null 이라면 해당 자식 노드들을 생성해준다.
 * 이후 left child (nodes[0]) 에 대하여 left child의 isEnd값이 false라면 left child를 다음 탐색 노드로 하여 dfs를 진행한다.
 * 만약 left child에 대한 dfs 탐색의 결과가 null이라면 현재 탐색 노드 기준 왼쪽 서브트리를 이용하여 조건을 만족하는 문자열을 만들 수 없으므로
 * right child (nodes[1])에 대하여 right child의 isEnd 값이 false 라면 right child를 다음 탐색노드로 하여 dfs를 진행한다.
 * depth 값이 만족하는 문자열의 길이가 될 때 (depth == length), 현재 탐색하는 노드의 isEnd 값이 false 라면 isEnd를 true로 설정해주고 지금까지 누적한 이진 문자열을 return 한다.
 *
 * (+ isEnd 값은 해당 문자열이 트라이에 삽입되어 있는지를 나타내는 것이고, 만약 트라이 탐색 과정에서 isEnd 노드를 부모노드 (or 조상노드)로 하여 트라이를 구성할 경우
 *    삽입되어 있는 문자열과 현재 만들려고 하는 문자열간에 접두어 관계가 생성되므로 isEnd가 아닌 자식노드만 탐색하는 것이다.)
 *
 * -----------------
 * Input 1
 * 3
 * 1 2 3
 *
 * Output 1
 * 1
 * 0
 * 10
 * 110
 * -----------------
 * Input 2
 * 3
 * 1 1 1
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 4
 * 2 2 2 2
 *
 * Output 3
 * 00
 * 01
 * 10
 * 11
 * -----------------
 * Input 4
 * 8
 * 3 3 3 3 3 3 3 3
 *
 * Output 4
 * 1
 * 000
 * 001
 * 010
 * 011
 * 100
 * 101
 * 110
 * 111
 * -----------------
 */
public class BOJ16906 {

    private static class Word {
        int idx, length;
        String binaryStr;

        public Word(int idx, int length) {
            this.idx = idx;
            this.length = length;
        }
    }

    private static class BinaryTrie {

        BinaryTrie[] nodes;
        boolean isEnd;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public String add(int depth, int length, String binaryStr) {
            if (depth == length && !isEnd) {
                isEnd = true;
                return binaryStr;
            }

            if (nodes[0] == null) nodes[0] = new BinaryTrie();
            if (nodes[1] == null) nodes[1] = new BinaryTrie();

            String str = null;
            if (!nodes[0].isEnd) {
                str = nodes[0].add(depth + 1, length, binaryStr + '0');
            }
            if (!nodes[1].isEnd && str == null) {
                str = nodes[1].add(depth + 1, length, binaryStr + '1');
            }

            return str;
        }
    }

    static int N;
    static Word[] words;
    static BinaryTrie root = new BinaryTrie();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        words = new Word[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            words[i] = new Word(i, Integer.parseInt(st.nextToken()));
        }
        Arrays.sort(words, Comparator.comparing(w -> w.length));

        for (Word w : words) {
            String binaryStr = root.add(0, w.length, "");
            if (binaryStr == null) {
                result.append("-1");
                break;
            } else {
                w.binaryStr = binaryStr;
            }
        }

        if (result.length() == 0) {
            Arrays.sort(words, Comparator.comparingInt(w -> w.idx));
            result.append('1').append('\n');
            for (Word w : words) {
                result.append(w.binaryStr).append('\n');
            }
        }
        bw.write(result.toString());


        // close the buffer
        br.close();
        bw.close();
    }
}