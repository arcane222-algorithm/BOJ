package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * ACM Craft - BOJ1005
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 *           dynamic programming (다이나믹 프로그래밍)
 * -----------------
 * Input 12
 * 4 4
 * 10 1 100 10
 * 1 2
 * 1 3
 * 2 4
 * 3 4
 * 4
 * 8 8
 * 10 20 1 5 8 7 1 43
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 3 6
 * 5 7
 * 6 7
 * 7 8
 * 7
 *
 *
 * Output 1
 * 120
 * 39
 * -----------------
 * Input 2
 * 5
 * 3 2
 * 1 2 3
 * 3 2
 * 2 1
 * 1
 * 4 3
 * 5 5 5 5
 * 1 2
 * 1 3
 * 2 3
 * 4
 * 5 10
 * 100000 99999 99997 99994 99990
 * 4 5
 * 3 5
 * 3 4
 * 2 5
 * 2 4
 * 2 3
 * 1 5
 * 1 4
 * 1 3
 * 1 2
 * 4
 * 4 3
 * 1 1 1 1
 * 1 2
 * 3 2
 * 1 4
 * 4
 * 7 8
 * 0 0 0 0 0 0 0
 * 1 2
 * 1 3
 * 2 4
 * 3 4
 * 4 5
 * 4 6
 * 5 7
 * 6 7
 * 7
 *
 * Output 2
 * 6
 * 5
 * 399990
 * 2
 * 0
 * -----------------
 */
public class BOJ1005 {

    private static class Building implements Comparable<Building> {
        int idx, time, inDegree;
        ArrayList<Integer> children = new ArrayList<>();

        public Building(int idx, int time) {
            this.idx = idx;
            this.time = time;
        }

        @Override
        public int compareTo(Building b) {
            return Integer.compare(time, b.time);
        }
    }

    static int T, N, K, W;
    static StringBuilder result = new StringBuilder();

    public static int topologicalSort(List<Building> graph) {
        int[] dp = new int[graph.size()];
        Queue<Building> queue = new LinkedList<>();

        // find the node that value of inDegree is 0
        for(int i = 0; i < graph.size(); i++) {
            dp[i] = graph.get(i).time;
            if(graph.get(i).inDegree == 0) {
                queue.add(graph.get(i));
            }
        }

        while(!queue.isEmpty()) {
            Building node = queue.poll();

            for(int i = 0; i < node.children.size(); i++) {
                int child = node.children.get(i);
                graph.get(child).inDegree -= 1;
                dp[child] = Math.max(dp[node.idx] + graph.get(child).time, dp[child]);

                if(graph.get(child).inDegree == 0) {
                    queue.add(graph.get(child));
                }
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            // Init N, K
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());

            // Init buildings
            List<Building> graph = new ArrayList<>();
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                int time = Integer.parseInt(st.nextToken());
                graph.add(new Building(j, time));
            }

            // Init process
            for(int j = 0; j < K; j++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken()) - 1;
                int b = Integer.parseInt(st.nextToken()) - 1;
                graph.get(a).children.add(b);
                graph.get(b).inDegree += 1;
            }
            W = Integer.parseInt(br.readLine()) - 1;
            result.append(topologicalSort(graph));
            result.append('\n');
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}