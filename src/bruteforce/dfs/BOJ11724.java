package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 연결 요소의 개수 - BOJ11724
 * -----------------
 *
 * 문제에서 무향 그래프가 주어지므로 인접리스트를 이용해 그래프를 표현한다.
 * List<Integer>[] graph; 와 같이 선언하여 각 노드를 List<Integer> 배열로 선언하고 각 List안에는 인접한 노드들의 값이 들어가도록 한다.
 * 노드 삽입 시 노드 u와 노드 v가 연결되어 있다면
 *             graph[u].add(v);
 *             graph[v].add(u);
 * 와 같이 양방향으로 연결되도록 삽입한다.
 *
 * 이제 dfs를 이용하여 연결 요수의 개수를 세어주면 된다.
 * 모든 그래프가 연결되어 있다면 연결 요소의 개수는 1개 일 것이고,
 * 서로 연결되어 있지 않은 그룹을 세어주면 된다.
 * dfs를 돌면서 visited 배열에 방문을 표시할 것이고, 한 그룹에 속한 노드들을 모두 방문했다면 visited가 전부 true가 될 것이다.
 * 이 상황에서 visited가 false인 노드를 탐색하게 되면 다른 그룹을 찾은 것이므로 연결 요소의 개수를 1개 증가시켜주며 이 그룹에 대해서도 dfs를 수행한다.
 *
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