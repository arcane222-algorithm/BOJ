package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 이분 그래프 - BOJ1707
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bipartite graph (이분그래프)
 *           dfs (깊이 우선 탐색)
 * -----------------
 * Input 1
 * 2
 * 3 2
 * 1 3
 * 2 3
 * 4 4
 * 1 2
 * 2 3
 * 3 4
 * 4 2
 *
 * Output 1
 * YES
 * NO
 * -----------------
 */
public class BOJ1707 {

    static int K, V, E;
    static boolean[] visited;
    static int[] type;
    static List<List<Integer>> graph;
    static StringBuilder result = new StringBuilder();

    public static void dfs(int idx) {
        if (!visited[idx]) type[idx] = 1;

        for (int adj : graph.get(idx)) {
            if (!visited[adj]) {
                type[adj] = type[idx] == 1 ? 2 : 1;
                visited[adj] = true;
                dfs(adj);
            }
        }
    }

    public static boolean isBipartiteGraph() {
        for(int i = 1; i <= V; i++) {
            for(int adj : graph.get(i)) {
                if(type[i] == type[adj]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        K = Integer.parseInt(br.readLine());
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            V = Integer.parseInt(st.nextToken());
            E = Integer.parseInt(st.nextToken());

            visited = new boolean[V + 1];
            type = new int[V + 1];
            graph = new ArrayList<>();
            for (int j = 0; j <= V; j++) {
                graph.add(new ArrayList<>());
            }

            for (int j = 0; j < E; j++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                graph.get(u).add(v);
                graph.get(v).add(u);
            }

            for (int j = 1; j <= V; j++) {
                if (!visited[j]) {
                    dfs(j);
                }
            }

            result.append(isBipartiteGraph() ? "YES" : "NO");
            if (i < K - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}