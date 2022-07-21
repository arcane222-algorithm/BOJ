package dp;

import java.io.*;
import java.util.*;



/**
 * 트리와 쿼리 - BOJ15681
 * -----------------
 *
 * 트리의 경우 사이클이 없는 무향 그래프 이므로 인접리스트를 이용하여 그래프를 구축하듯 트리를 만들면 된다.
 * List<Integer>[] tree = new ArrayList[N];
 * ... List 배열 초기화
 * ...
 * tree[U].add(V);
 * tree[V].add(U); (두 정점 U, V 사이에 간선이 있다는 것을  추가함)
 *
 * 이후 서브트리의 수를 찾기 위해 dfs를 이용하여 본인을 root로 하는 서브트리의 합 = 자식의 서브 트리의 합 + 1을 dp 배열에 누적하여 구해준다.
 * 이렇게 되면 특정 정점 U를 루트로 하는 서브트리의 합을 구하는 쿼리에 대하여 dp[U]로 O(1)에 찾을 수 있게 된다.
 *
 *
 * -----------------
 * Input 1
 * 9 5 3
 * 1 3
 * 4 3
 * 5 4
 * 5 6
 * 6 7
 * 2 3
 * 9 6
 * 6 8
 * 5
 * 4
 * 8
 *
 * Output 1
 * 9
 * 4
 * 1
 * -----------------
 */
public class BOJ15681 {

    static int N, R, Q;
    static int[] dp;
    static boolean[] visited;
    static List<Integer>[] tree;
    static StringBuilder result = new StringBuilder();

    public static void dfs(int current) {
        List<Integer> node = tree[current];
        visited[current] = true;
        for (int i = 0; i < node.size(); i++) {
            int child = node.get(i);
            if (!visited[child]) {
                dfs(child);
                dp[current] += dp[child];
            }
        }

        dp[current] += 1;
        visited[current] = false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        dp = new int[N + 1];
        visited = new boolean[N + 1];
        tree = new ArrayList[N + 1];
        for (int i = 0; i < N + 1; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int U = Integer.parseInt(st.nextToken());
            int V = Integer.parseInt(st.nextToken());
            tree[U].add(V);
            tree[V].add(U);
        }
        dfs(R);

        for (int i = 0; i < Q; i++) {
            result.append(dp[Integer.parseInt(br.readLine())]).append('\n');
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}