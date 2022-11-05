package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 선수과목 (Prerequisite) - BOJ14567
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 * -----------------
 * Input 1
 * 3 2
 * 2 3
 * 1 2
 *
 * Output 1
 * 1 2 3
 * -----------------
 * Input 2
 * 6 4
 * 1 2
 * 1 3
 * 2 5
 * 4 5
 *
 * Output 2
 * 1 2 2 1 3 1
 * -----------------
 */
public class BOJ14567 {

    static int N, M;
    static List<List<Integer>> graph;
    static int[] inDegrees, semesters;
    static StringBuilder result = new StringBuilder();

    public static void topologicalSort() {
        List<Integer> result = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 1; i <= N; i++) {
            if (inDegrees[i] == 0) {
                queue.offer(i);
            }
        }

        int semester = 1;
        while (!queue.isEmpty()) {
            final int Size = queue.size();
            for (int i = 0; i < Size; i++) {
                int node = queue.poll();
                for (int adj : graph.get(node)) {
                    if (--inDegrees[adj] == 0) {
                        queue.add(adj);
                    }
                }
                result.add(node);
                semesters[node] = semester;
            }
            semester++;
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

        inDegrees = new int[N + 1];
        semesters = new int[N + 1];

        graph = new LinkedList<>();
        for (int i = 0; i <= N; i++) {
            graph.add(new LinkedList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph.get(u).add(v);    // u --> v
            inDegrees[v]++;
        }

        topologicalSort();
        for(int i = 1; i <= N; i++) {
            result.append(semesters[i]);
            if(i < N) result.append(' ');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
