package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 연결 요소의 개수 - BOJ11724
 * -----------------
 * Input 1
 * 6 5
 * 1 2
 * 2 5
 * 5 1
 * 3 4
 * 4 6
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 6 8
 * 1 2
 * 2 5
 * 5 1
 * 3 4
 * 4 6
 * 5 4
 * 2 4
 * 2 3
 *
 * Output 2
 * 1
 * -----------------
 */
public class BOJ11724 {

    static List<Integer>[] graph;
    static boolean[] visited;
    static int N, M;
    static int result;

    public static void dfs(int node) {
        visited[node] = true;

        for (int adj : graph[node]) {
            if (!visited[adj]) {
                dfs(adj);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        graph = new ArrayList[N + 1];
        visited = new boolean[N + 1];
        for (int i = 0; i < N + 1; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph[u].add(v);
            graph[v].add(u);
        }

        for (int i = 1; i < N + 1; i++) {
            if (!visited[i]) {
                result++;
                dfs(i);
            }
        }

        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}