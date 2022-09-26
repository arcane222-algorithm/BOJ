package string.trie;

import java.io.*;
import java.util.*;


/**
 * 용량 부족- BOJ5446
 * -----------------
 *
 * Trie를 구성할 때, 영어 대/소문자, ., 숫자 총 63개의 문자를 배열을 써서 저장해도 되지만 (Trie[] children = new Trie[63])
 * HashMap<Character, Trie>을 사용하면 좀 더 편리하게 구현할 수 있다.
 *
 * 기본적으로 삭제해야 될 파일들을 Trie에 먼저 삽입 시 canDelete를 true로 설정해주고 파일명의 마지막 부분인 isEnd도 체크해준다.
 * 그 다음 삭제하지 말아야 할 파일들을 Trie에서 체크하는데, 각 노드들의 canDelete를 false로 바꿔준다.
 *
 * 결과 탐색 시 DFS를 이용하여 해당 파일의 각 노드를 탐색하는데, 이 때 canDelete값이 true인 노드를 만나면 해당 노드 뒤에 부터
 * 와일드카드 (*)을 붙여 한꺼번에 삭제할 수 있으므로 count를 ++해주고 dfs 함수를 종료하여 해당 case는 1번으로 묶어서 카운팅 해준다. (back-tracking)
 *
 * 만약 탐색 중인 Trie 가지의 노드의 canDelete가 모두 false인 상황에서 마지막 isEnd를 만난다면
 * 와일드카드 (*) 없이 파일명 만으로 삭제해야 하는 경우이므로 count를 ++해준다.
 *
 * 주의할 점은,
 * 1
 * 2
 * A
 * B
 * 0
 * > Test case와 같이 삭제하지 말아야 할 파일명이 없는 경우 와일드카드 (*) 한 글자로 모든 파일을 지울 수 있으므로
 * > 처음 삽입 시 Root 노드까지 canDelete를 true로 잡아주고, 삭제하지 말아야 할 파일을 mark 경우 Root노드부터 canDelete를 false로 해야 한다.
 *   (add와 mark 작업 시, root 노드를 제외한 글자가 있는 children 노드들만 canDelete를 조작했더니 값이 잘못되어 나옴)
 *
 * -----------------
 * Input 1
 * 1
 * 11
 * BAPC.in
 * BAPC.out
 * BAPC.tex
 * filter
 * filename
 * filenames
 * clean
 * cleanup.IN1
 * cleanup.IN2
 * cleanup.out
 * problem.tex
 * 5
 * BAPC
 * files
 * cleanup
 * cleanup.IN
 * cleaning
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 1
 * 2
 * A
 * B
 * 0
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 1
 * 2
 * A
 * AA
 * 0
 *
 * Output 3
 * 1
 * -----------------
 */
public class BOJ5446 {

    private static class Trie {
        private HashMap<Character, Trie> children;

        boolean isEnd;
        boolean canDelete;

        public Trie() {
            children = new HashMap<>();
            canDelete = true;
        }

        public void add(String fileName) {
            Trie current = this;

            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (current.children.get(c) == null) {
                    current.children.put(c, new Trie());
                }
                current = current.children.get(c);
            }
            current.isEnd = true;
        }

        public void mark(String fileName) {
            Trie current = this;
            current.canDelete = false;

            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (current.children.get(c) == null) {
                    break;
                }
                current = current.children.get(c);
                current.canDelete = false;
            }
        }

        public int count() {
            if (canDelete) return 1;

            int count = 0;
            if (isEnd) {
                count++;
            }

            for (Trie t : children.values()) {
                count += t.count();
            }
            return count;
        }
    }

    static int T, N1, N2;
    static Trie trie;
    static StringBuilder result = new StringBuilder();

    static int res;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            trie = new Trie();
            N1 = Integer.parseInt(br.readLine());
            for (int j = 0; j < N1; j++) {
                trie.add(br.readLine());
            }

            N2 = Integer.parseInt(br.readLine());
            for (int j = 0; j < N2; j++) {
                trie.mark(br.readLine());
            }

            result.append(trie.count());
            if(i < T - 1) result.append('\n');
        }

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
