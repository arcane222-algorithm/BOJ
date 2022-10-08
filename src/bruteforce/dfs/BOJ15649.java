package bruteforce.dfs;

import java.io.*;
import java.util.*;

/**
 * N과 M (1) - BOJ15649
 * -----------------
 * category: back tracking (백트레킹)
 *           dfs (깊이 우선 탐색)
 * -----------------
 * -----------------
 * Input 1
 * 3 1
 *
 * Output 1
 * 1
 * 2
 * 3
 * -----------------
 * Input 2
 * 4 2
 *
 * Output 2
 * 1 2
 * 1 3
 * 1 4
 * 2 1
 * 2 3
 * 2 4
 * 3 1
 * 3 2
 * 3 4
 * 4 1
 * 4 2
 * 4 3
 * -----------------
 * Input 3
 * 4 4
 *
 * Output 3
 * 1 2 3 4
 * 1 2 4 3
 * 1 3 2 4
 * 1 3 4 2
 * 1 4 2 3
 * 1 4 3 2
 * 2 1 3 4
 * 2 1 4 3
 * 2 3 1 4
 * 2 3 4 1
 * 2 4 1 3
 * 2 4 3 1
 * 3 1 2 4
 * 3 1 4 2
 * 3 2 1 4
 * 3 2 4 1
 * 3 4 1 2
 * 3 4 2 1
 * 4 1 2 3
 * 4 1 3 2
 * 4 2 1 3
 * 4 2 3 1
 * 4 3 1 2
 * 4 3 2 1
 * -----------------
 */
public class BOJ15649 {

    static int N, M;
    static boolean[] visited;
    static StringBuilder result = new StringBuilder();

    public static void dfs(int depth, String str) {
        if (depth == M) {
            result.append(str).append('\n');
        } else {
            for (int i = 1; i < N + 1; i++) {
                if (!visited[i]) {
                    visited[i] = true;
                    dfs(depth + 1, depth + 1 == M ? str + i : str + i + ' ');
                    visited[i] = false;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        visited = new boolean[N + 1];

        dfs(0, "");
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}