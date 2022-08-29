package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 핸들 뭘로 하지 - BOJ25498
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 8
 * dfcjsasb
 * 8 4
 * 6 3
 * 1 3
 * 2 1
 * 4 1
 * 4 7
 * 2 5
 *
 * Output 1
 * djs
 * -----------------
 * Input 2
 * 7
 * aaaaaaa
 * 1 2
 * 1 3
 * 3 4
 * 3 5
 * 5 6
 * 5 7
 *
 * Output 2
 * aaaa
 * -----------------
 * Input 3
 * 5
 * aaacb
 * 1 2
 * 1 3
 * 2 4
 * 3 5
 *
 * Output 3
 * aac
 * -----------------
 * Input 4
 * 5
 * aaabc
 * 1 2
 * 1 3
 * 2 4
 * 3 5
 *
 * Output 4
 * aac
 * -----------------
 */
public class BOJ25498 {

    static int N;
    static String word;
    static boolean[] visited;
    static ArrayList<Integer>[] tree;

    public static String bfs() {
        StringBuilder builder = new StringBuilder();
        Queue<Integer> queue = new LinkedList<>();
        builder.append(word.charAt(0));
        queue.add(1);

        List<Integer> tmpList = new ArrayList<>();
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            visited[curr] = true;

            for (int child : tree[curr]) {
                if (!visited[child]) {
                    if (tmpList.isEmpty()) {
                        tmpList.add(child);
                    } else {
                        if (word.charAt(child - 1) > word.charAt(tmpList.get(0) - 1)) {
                            tmpList.clear();
                            tmpList.add(child);
                        } else if (word.charAt(child - 1) == word.charAt(tmpList.get(0) - 1)) {
                            tmpList.add(child);
                        }
                    }
                }
            }

            if (queue.isEmpty()) {
                if (tmpList.size() > 0) {
                    queue.addAll(tmpList);
                    builder.append(word.charAt(tmpList.get(0) - 1));
                    tmpList.clear();
                }
            }
        }

        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        word = br.readLine();
        visited = new boolean[N + 1];
        tree = new ArrayList[N + 1];
        for (int i = 0; i < N + 1; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            tree[u].add(v);
            tree[v].add(u);
        }

        bw.write(bfs());

        // close the buffer
        br.close();
        bw.close();
    }
}