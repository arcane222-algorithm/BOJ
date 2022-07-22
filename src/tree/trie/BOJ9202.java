package tree.trie;

import java.io.*;
import java.util.*;


/**
 * Boggle - BOJ9202
 * -----------------
 *
 * 사전에 등록된 각 단어를 대문자 Trie에 저장함.
 * 4x4 Boggle 주사위를 DFS를 이용하여 8방향을 탐색하며 해당 단어가 Trie에서 등록되어 있는지 여부를 체크하며
 * 만약 Trie에 등록되어 있지 않다면 해당 dfs를 종료하여 back-tracking 방식으로 탐색을 한다.
 * 중복 제거를 위해 HashSet을 사용하고 사전 순 정렬을 위해 ArrayList에 HashSet 결과를 넣어 정렬한다.
 *
 * -----------------
 * Input 1
 * 5
 * ICPC
 * ACM
 * CONTEST
 * GCPC
 * PROGRAMM
 *
 * 3
 * ACMA
 * APCA
 * TOGI
 * NEST
 *
 * PCMM
 * RXAI
 * ORCN
 * GPCG
 *
 * ICPC
 * GCPC
 * ICPC
 * GCPC
 *
 * Output 1
 * 8 CONTEST 4
 * 14 PROGRAMM 4
 * 2 GCPC 2
 * -----------------
 * Input 2
 * 3
 * ABC
 * AB
 * A
 *
 * 1
 * ABCX
 * XXXX
 * XXXX
 * XXXX
 *
 * Output 2
 * 1 ABC 3
 * -----------------
 */
public class BOJ9202 {

    private static class Trie {
        private static final int MAX_LENGTH = 26;
        private static final char BASE_CHAR = 'A';

        Trie[] children;
        boolean isEnd;
        char c;

        public Trie() {
            children = new Trie[MAX_LENGTH];
        }

        public void add(String word) {
            Trie current = this;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - BASE_CHAR;
                if (current.children[idx] == null) {
                    current.children[idx] = new Trie();
                    current.children[idx].c = c;
                }
                current = current.children[idx];
            }
            current.isEnd = true;
        }

        public Trie next(char c) {
            int idx = c - BASE_CHAR;
            return children[idx];
        }

        public boolean hasNext(char c) {
            int idx = c - BASE_CHAR;
            return children[idx] != null;
        }
    }

    static final int BOARD_SIZE = 4;
    static final int[] dirX = {1, 0, -1, 0, -1, -1, 1, 1};
    static final int[] dirY = {0, 1, 0, -1, -1, 1, -1, 1};
    static final int[] scores = {0, 0, 0, 1, 1, 2, 3, 5, 11};

    static int w, b;
    static char[][] board;
    static boolean[][] visited;
    static Trie trie;
    static HashSet<String> found = new HashSet<>();
    static StringBuilder result = new StringBuilder();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > BOARD_SIZE - 1) return false;
        if (y < 0 || y > BOARD_SIZE - 1) return false;
        return true;
    }

    public static void dfs(Trie trie, String word, int x, int y, int depth) {
        if (depth > 7) return;

        Trie current = trie.next(board[y][x]);

        if (current != null) {
            word += board[y][x];
            visited[y][x] = true;

            if (current.isEnd) {
                found.add(word);
            }

            // 8-direction 으로 dfs 수행
            for (int i = 0; i < dirX.length; i++) {
                int nxtX = x + dirX[i];
                int nxtY = y + dirY[i];
                if (canGo(nxtX, nxtY) && !visited[nxtY][nxtX]) {
                    dfs(current, word, nxtX, nxtY, depth + 1);
                }
            }
        } else {
            visited[y][x] = false;
            return;
        }

        // visited 복구
        visited[y][x] = false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse w
        w = Integer.parseInt(br.readLine());
        trie = new Trie();
        for (int i = 0; i < w; i++) {
            String word = br.readLine();
            trie.add(word);
        }

        // read blank
        br.readLine();

        // parse b
        b = Integer.parseInt(br.readLine());

        // parse boards
        for (int i = 0; i < b; i++) {
            // parse board
            board = new char[BOARD_SIZE][BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                String line = br.readLine();
                for (int k = 0; k < BOARD_SIZE; k++) {
                    board[j][k] = line.charAt(k);
                }
            }

            // depth-first-search
            found.clear();
            visited = new boolean[BOARD_SIZE][BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int k = 0; k < BOARD_SIZE; k++) {
                    dfs(trie, "", k, j, 0);
                }
            }

            List<String> words = new ArrayList<>(found);
            Collections.sort(words);

            int score = 0;
            String find = "";
            for (String s : words) {
                score += scores[s.length()];
                if (find.length() < s.length()) {
                    find = s;
                }
            }
            result.append(score);
            result.append(' ');
            result.append(find);
            result.append(' ');
            result.append(found.size());
            result.append('\n');

            // read blank
            br.readLine();
        }

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}