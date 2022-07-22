package tree.trie;
import java.io.*;
import java.util.*;


/**
 * Secret Message - BOJ6073
 * -----------------
 *
 * 소들이 주고 받는 codeword(암호)중 농부 John이 b_i개의 비트를 가로챘다. (intercepted bits)
 * m개의 intercepted bits가 주어질 때, codeword가 intercepted bits 중 몇개와 앞부분이 일치하는지 세는 문제이다.
 * 즉, 문제에 "(codeword j의 경우 intercepted bits와 codeword가 동일한 초기 비트를 갖는 횟수). 당신의 임무는 이 숫자를 계산하는 것입니다." 라고 나와있다.
 * intercepted bits가 codeword의 접두사 (prefix)가 되거나, 반대로 codeword가 intercepted bits의 접두사가 되는 경우를 찾아주면 된다.
 *
 * 예를 들어, intercepted bits의 경우 1, 110이 있고 codeword의 경우 11이 있다면
 * intercepted bits 1은 codeword 11의 접두사 이므로 경우의 수가 된다.
 * codeword 11은 intercepted bits 110의 접두사 이므로 경우의 수가 된다. (총 2가지)
 *
 * 이러한 계산을 위해 0과 1로 이루어진 BinaryTrie를 구축한다.
 * BinaryTrie의 각 node에는 해당 node에 bits가 몇번 할당되는지 나타내는 count와 단어의 끝을 나타내는 end를 가지고 있도록 한다.
 * 보통 단어의 끝은 boolean을 이용하여 표현하지만, 해당 문제에는 중복된 단어가 들어올 수 있으므로 end를 boolean이 아닌 int로 선언하여
 * 단어의 끝이 여러 번 올때마다 값을 증가시키도록 구현하여야 한다.
 *
 * intercepted bits를 BinaryTrie에 넣어 구축 한 후 codeword를 Trie에서 탐색하며 결과를 도출한다.
 * 1) intercepted bits가 codeword의 접두사 (prefix)가 되는 경우
 * codeword를 탐색하며 각 자리의 end 값을 결과에 더해준다. (중간에 end가 0보다 크다는 것은 intercepted bits를 접두사로 가지는 codeword가 존재한다는 것이다.)
 *
 * 2) codeword가 intercepted bits의 접두사가 되는 경우
 * codeword를 탐색하며 전부 탐색했을 때, 마지막 탐색 노드에 자식이 있다는 것은 codeword를 접두사로 하는 intercepted bits가 있다는 뜻이므로 자식의 count 값을 더해준다.
 * 주의할 점은, codeword를 탐색 시 codeword길이만큼 모두 탐색하지 못한 경우 (즉, 중간에 없는 글자를 만나 node가 null이 된 경우)
 * 해당 codeword를 접두사로 하는 intercepted bits가 있는 것이 아니므로 마지막 탐색 노드가 null이 아닌 경우에만 자식노드의 count 값을 더해준다.
 *
 * -----------------
 * Input 1
 * 4 5
 * 3 0 1 0
 * 1 1
 * 3 1 0 0
 * 3 1 1 0
 * 1 0
 * 1 1
 * 2 0 1
 * 5 0 1 0 0 1
 * 2 1 1
 *
 * Output 1
 * 1
 * 3
 * 1
 * 1
 * 2
 * -----------------
 * Input 2
 * 4 5
 * 3 0 1 0
 * 1 1
 * 3 1 0 0
 * 3 1 1 0
 * 1 0
 * 1 1
 * 3 1 0 1
 * 5 0 1 0 0 1
 * 2 1 1
 *
 * Output 2
 * 1
 * 3
 * 1
 * 1
 * 2
 * -----------------
 */
public class BOJ6073 {

    private static class BinaryTrie {

        BinaryTrie[] nodes;
        int count, end;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public boolean hasChildren() {
            return nodes[0] != null || nodes[1] != null;
        }

        public void add(int length, StringTokenizer st) {
            BinaryTrie current = this;

            for (int i = 0; i < length; i++) {
                int idx = st.nextToken().charAt(0) - '0';
                if (current.nodes[idx] == null) {
                    current.nodes[idx] = new BinaryTrie();
                }
                current = current.nodes[idx];
                current.count++;
            }

            current.end++;
        }

        public int match(int length, StringTokenizer st) {
            BinaryTrie current = this;
            int result = 0, i;

            for (i = 0; i < length; i++) {
                int idx = st.nextToken().charAt(0) - '0';
                if (current.nodes[idx] == null) {
                    current = null;
                    break;
                } else {
                    if (current.nodes[idx].end > 0) {
                        result += current.nodes[idx].end;
                    }
                }
                current = current.nodes[idx];
            }

            if (current != null) {
                if (current.nodes[0] != null)
                    result += current.nodes[0].count;

                if (current.nodes[1] != null)
                    result += current.nodes[1].count;
            }

            return result;
        }
    }

    static int M, N;
    static BinaryTrie trie = new BinaryTrie();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int length = Integer.parseInt(st.nextToken());
            trie.add(length, st);
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int length = Integer.parseInt(st.nextToken());
            result.append(trie.match(length, st)).append('\n');
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
