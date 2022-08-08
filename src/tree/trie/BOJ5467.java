package tree.trie;

import java.io.*;
import java.util.*;


/**
 * Type Printer - BOJ5467
 * -----------------
 *
 * 프린터 operation에는 단어글자 삽입, 삭제, 출력 3가지 기능을 지원하는데, 최소한의 operation count로 모든 글자를 출력하기 위해서는 삽입과 삭제의 횟수가 최소가 되어야 한다.
 * 이 때, 긴 글자를 먼저 출력하고 짧은 글자를 나중에 출력하면 짧은 글자를 먼저 출력할 때보다 삭제연산의 횟수가 늘어나기 때문에 각 단어를 출력할 때
 * 단어의 길이 = maxDepth 값이 작은 node 부터 선택해서 출력하도록 한다.
 * 각 단어를 Trie에 삽입 할 때, 각 Trie Node마다 maxDepth 값을 기록하여 저장하는데,
 * 현재 해당 Node의 자식 노드중 가장 깊은 값을 나타내도록 설정하면 된다.
 * 그렇게 하기 위해서, Trie에 단어들을 모두 삽입 후, 삽입의 마지막 node부터 parent 값을 이용하여 거슬러 올라가며 현재 node의 maxDepth 값이 단어삽입 시
 * 계산한 depth 보다 작으면 갱신해주는 식으로 하면 된다.
 * 위와 같이 계산하여 삽입 시
 * print, poem 두 단어에 대하여
 * print 각 글자의 maxDepth 5가 되고, peom의 p는 5, oem의 maxDepth는 4가 된다.
 *
 * 출력형식에서 글자 글자 글자 P(출력) -(삭제) -(삭제) 이러한 형식을 띄고 있기 때문에
 * void recursive() {
 *      if(단어가 완성됨) {
 *          (단어출력, 'P')
 *      }
 *
 *     (글자삽입)
 *     recursive();
 *     (삭제, '-')
 * }
 * 위와 같이 재귀를 구성하면 글자 삽입이 모두 이루어 지고 필요에 따라 삭제 연산이 삽입되어 조건을 만족하게 출력할 수 있다.
 *
 * -----------------
 * Input 1
 * 3
 * print
 * the
 * poem
 *
 * Output 1
 * 20
 * t
 * h
 * e
 * P
 * -
 * -
 * -
 * p
 * o
 * e
 * m
 * P
 * -
 * -
 * -
 * r
 * i
 * n
 * t
 * P
 * -----------------
 * Input 2
 * 4
 * print
 * the
 * poem
 * pop
 *
 * Output 2
 * 23
 * t
 * h
 * e
 * P
 * -
 * -
 * -
 * p
 * o
 * p
 * P
 * -
 * e
 * m
 * P
 * -
 * -
 * -
 * r
 * i
 * n
 * t
 * P
 * -----------------
 */
public class BOJ5467 {

    private static class Trie {

        HashMap<Character, Trie> nodes;
        Trie parent;
        boolean isEnd;
        int maxDepth;
        char c;

        public Trie() {
            nodes = new HashMap<>();
        }

        public void add(String word) {
            Trie current = this;
            int depth = 0;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                Trie next = current.nodes.get(c);
                if (next == null) {
                    next = new Trie();
                    next.c = c;
                    next.parent = current;
                    current.nodes.put(c, next);
                }
                current = next;
                depth++;
            }
            current.isEnd = true;

            while (current.parent != null) {
                if (current.maxDepth < depth) {
                    current.maxDepth = depth;
                }
                current = current.parent;
            }
        }

        public void printAll(String word) {
            for (char c : nodes.keySet()) {
                Trie next = nodes.get(c);
                if (next.isEnd) {
                    System.out.println(word + c);
                } else {
                    next.printAll(word + c);
                }
            }
        }
    }

    static int N, printedWordCount, operationCount;
    static Trie root = new Trie();
    static StringBuilder result = new StringBuilder();

    public static void printWords(Trie trie) {
        if (trie.isEnd) {
            printedWordCount++;
            result.append('P').append('\n');
            operationCount++;
        }

        List<Trie> sortedNodes = new ArrayList<>(trie.nodes.values());
        Collections.sort(sortedNodes, Comparator.comparingInt(o -> o.maxDepth));
        for (Trie node : sortedNodes) {
            result.append(node.c).append('\n');
            operationCount++;
            printWords(node);
        }
        if (printedWordCount < N) {
            result.append('-').append('\n');
            operationCount++;
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            String word = br.readLine();
            root.add(word);
        }

        // print words to StringBuilder
        printWords(root);

        // print operationCount to StringBuilder
        result.insert(0, operationCount + "\n");

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
