package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 작업 - BOJ2056
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 *           dynamic programming (다이나믹 프로그래밍)
 * -----------------
 * Input 1
 * 7
 * 5 0
 * 1 1 1
 * 3 1 2
 * 6 1 1
 * 1 2 2 4
 * 8 2 2 4
 * 4 3 3 5 6
 *
 * Output 1
 * 23
 * -----------------
 * Input 2
 * 5
 * 6 0
 * 3 0
 * 3 2 1 2
 * 1 1 1
 * 1 2 3 4
 *
 * Output 2
 * 10
 * -----------------
 */
public class BOJ2056 {

    static int N;
    static int[] times, inDegrees;
    static List<List<Integer>> graph;

    public static int topologicalSort() {
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 1; i <= N; i++) {
            if (inDegrees[i] == 0) {
                queue.add(i);
            }
        }

        int[] dp = Arrays.copyOf(times, N + 1);
        while (!queue.isEmpty()) {
            final int Size = queue.size();
            for (int i = 0; i < Size; i++) {
                int curr = queue.poll();

                for (int adj : graph.get(curr)) {
                    dp[adj] = Math.max(dp[adj], dp[curr] + times[adj]);
                    if (--inDegrees[adj] == 0) {
                        queue.add(adj);
                    }
                }
            }
        }

        int result = 0;
        for (int i = 1; i <= N; i++) {
            result = Math.max(result, dp[i]);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        times = new int[N + 1];
        inDegrees = new int[N + 1];
        graph = new ArrayList<>(N + 1);
        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int time = Integer.parseInt(st.nextToken());
            int preCount = Integer.parseInt(st.nextToken());

            times[i] = time;
            for (int j = 0; j < preCount; j++) {
                int pre = Integer.parseInt(st.nextToken());
                graph.get(i).add(pre);
                inDegrees[pre]++;
            }
        }
        bw.write(String.valueOf(topologicalSort()));


        // close the buffer
        br.close();
        bw.close();
    }
}