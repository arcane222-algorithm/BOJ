package trie;

import java.io.*;
import java.util.*;

/**
 * 개미굴 - BOJ14725
 * -----------------
 *
 * Trie를 구성할 때 Key 값을 개미굴의 음식, Value 값을 Trie 노드로 구성하여 Trie를 구축한다.
 * 깊이우선탐색 (DFS)를 이용하여 StringBuilder에 depth에 맞게 --표시를 추가하며 개미굴을 탐색하면 정답을 구할 수 있다.
 *
 * -----------------
 * Input 1
 * 3
 * 2 B A
 * 4 A B C D
 * 2 A C
 *
 * Output 1
 * A
 * --B
 * ----C
 * ------D
 * --C
 * B
 * --A
 * -----------------
 * Input 2
 * 4
 * 2 KIWI BANANA
 * 2 KIWI APPLE
 * 2 APPLE APPLE
 * 3 APPLE BANANA KIWI
 *
 * Output 2
 * APPLE
 * --APPLE
 * --BANANA
 * ----KIWI
 * KIWI
 * --APPLE
 * --BANANA
 * -----------------
 */
public class BOJ14725 {

    private static class Trie {

        private HashMap<String, Trie> children;
        private String food;

        public Trie() {
            children = new HashMap<>();
        }

        public void add(List<String> foods) {
            Trie current = this;

            for(int i = 0; i < foods.size(); i++) {
                String food = foods.get(i);
                if(!current.children.containsKey(food)) {
                    Trie child = new Trie();
                    child.food = food;
                    current.children.put(food, child);
                }
                current = current.children.get(food);
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
                result.append("--");
            }
            result.append(child.food);
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
            st = new StringTokenizer(br.readLine());
            K = Integer.parseInt(st.nextToken());

            List<String> foods = new ArrayList<>();
            for (int j = 0; j < K; j++) {
                foods.add(st.nextToken());
            }
            trie.add(foods);
        }
        dfs(trie, 0);

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}