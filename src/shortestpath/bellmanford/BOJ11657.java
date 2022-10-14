package shortestpath.bellmanford;

import java.io.*;
import java.util.*;



/**
 * 타임머신  - BOJ11657
 * -----------------
 * category: graph theory (그래프 이론)
 *           bellman-ford (벨만-포드)
 * -----------------
 * Input 1
 * 3 4
 * 1 2 4
 * 1 3 3
 * 2 3 -1
 * 3 1 -2
 *
 * Output 1
 * 4
 * 3
 * -----------------
 * Input 2
 * 3 4
 * 1 2 4
 * 1 3 3
 * 2 3 -4
 * 3 1 -2
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 3 2
 * 1 2 4
 * 1 2 3
 *
 * Output 3
 * 3
 * -1
 * -----------------
 */
public class BOJ11657 {

    private static class Edge {
        int depart, dest, w;

        public Edge(int depart, int dest, int w) {
            this.depart = depart;
            this.dest = dest;
            this.w = w;
        }
    }

    static int N, M;
    static ArrayList<Edge> edges;

    public static String bellmanFord(int depart) {
        long[] dist = new long[N + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[depart] = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 1; i < N + 1; i++) {
            for (Edge e : edges) {
                if (dist[e.depart] == Long.MAX_VALUE) continue;

                if (dist[e.dest] > dist[e.depart] + e.w) {
                    if (i == N) return "-1";
                    dist[e.dest] = dist[e.depart] + e.w;
                }
            }
        }

        for (int i = 2; i < N + 1; i++) {
            result.append(dist[i] == Long.MAX_VALUE ? -1 : dist[i]);
            if (i < N) result.append('\n');
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        edges = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());
            edges.add(new Edge(A, B, C));
        }
        bw.write(bellmanFord(1));

        // close the buffer
        br.close();
        bw.close();
    }
}