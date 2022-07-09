package trie;

import java.io.*;
import java.util.*;


/**
 * 디스크 트리 - BOJ7432
 * -----------------
 *
 * 개미굴 (BOJ14725)와 유사한 문제이다.
 * Trie를 구성할 때 Key 값을 file 이름, Value 값을 Trie 노드로 구성하여 Trie를 구축한다.
 * 깊이우선탐색 (DFS)를 하여 탐색 시, 파일 명에 맞게 정렬 후 StringBuilder에 depth에 맞게 공백을 추가하며 Trie속 파일을 전부 탐색하면 정답을 구할 수 있다.
 *
 * -----------------
 * Input 1
 * 7
 * WINNT\SYSTEM32\CONFIG
 * GAMES
 * WINNT\DRIVERS
 * HOME
 * WIN\SOFT
 * GAMES\DRIVERS
 * WINNT\SYSTEM32\CERTSRV\CERTCO~1\X86
 *
 * Output 1
 * GAMES
 *  DRIVERS
 * HOME
 * WIN
 *  SOFT
 * WINNT
 *  DRIVERS
 *  SYSTEM32
 *   CERTSRV
 *    CERTCO~1
 *     X86
 *   CONFIG
 * -----------------
 */
public class BOJ7432 {

    private static class Trie {

        private HashMap<String, Trie> children;
        private String file;

        public Trie() {
            children = new HashMap<>();
        }

        public void add(List<String> files) {
            Trie current = this;

            for(int i = 0; i < files.size(); i++) {
                String file = files.get(i);
                if(!current.children.containsKey(file)) {
                    Trie child = new Trie();
                    child.file = file;
                    current.children.put(file, child);
                }
                current = current.children.get(file);
            }
        }
    }

    static int N, K;
    static StringBuilder result = new StringBuilder();

    public static void dfs(Trie trie, int depth) {
        List<Map.Entry<String, Trie>> children = new ArrayList<>(trie.children.entrySet());
        children.sort(Comparator.comparing(Map.Entry::getKey));

        for(int i = 0; i < children.size(); i++) {
            Trie child = children.get(i).getValue();
            for(int j = 0; j < depth; j++) {
                result.append(" ");
            }
            result.append(child.file);
            result.append('\n');
            dfs(child, depth + 1);
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;
        Trie trie = new Trie();

        // parse N
        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            //parse K
            List<String> files = new ArrayList<>();
            st = new StringTokenizer(br.readLine(), "\\");
            while(st.hasMoreTokens()) {
                files.add(st.nextToken());
            }
            trie.add(files);
        }

        dfs(trie, 0);

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}