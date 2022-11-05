package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 최종 순위 - BOJ3665
 * -----------------
 * Input 1
 * 3
 * 5
 * 5 4 3 2 1
 * 2
 * 2 4
 * 3 4
 * 3
 * 2 3 1
 * 0
 * 4
 * 1 2 3 4
 * 3
 * 1 2
 * 3 4
 * 2 3
 *
 * Output 1
 *
 * 5 3 2 4 1
 * 2 3 1
 * IMPOSSIBLE
 * -----------------
 */
public class BOJ3665 {

    static int T, N, M;
    static StringBuilder result = new StringBuilder();

    public static String topologicalSort(List<List<Integer>> graph, int[] inDegree) {
        StringBuilder result = new StringBuilder();
        List<Integer> sortedList = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        for(int i = 0; i < inDegree.length; i++) {
            if(inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while(!queue.isEmpty()) {
            if(queue.size() > 1) {
                result.append("?");
                break;
            }

            int node = queue.poll();
            for(int i = 0; i < graph.get(node).size(); i++) {
                if(--inDegree[graph.get(node).get(i)] == 0) {
                    queue.add(graph.get(node).get(i));
                }
            }
            sortedList.add(node);
        }

        if(result.length() == 0 && sortedList.size() != inDegree.length) {
            result.append("IMPOSSIBLE");
        } else {
            for(int i = 0; i < sortedList.size(); i++) {
                result.append(sortedList.get(i) + 1);
                if(i < sortedList.size() - 1)
                    result.append(' ');
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            // Init N
            N = Integer.parseInt(br.readLine());
            List<List<Integer>> graph = new ArrayList<>();
            int[] inDegree = new int[N];
            for(int j = 0; j < N; j++) {
                graph.add(new ArrayList<>());
            }

            // Init teams ranks
            int[] teams = new int[N];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                teams[j] = Integer.parseInt(st.nextToken()) - 1;
            }

            // Init graph
            for(int j = 0; j < N - 1; j++) {
                for(int k = j + 1; k < N; k++) {
                    graph.get(teams[j]).add(teams[k]);
                    inDegree[teams[k]] += 1;
                }
            }

            // init M
            M = Integer.parseInt(br.readLine());
            for(int j = 0; j < M; j++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken()) - 1;
                int b = Integer.parseInt(st.nextToken()) - 1;

                // swap rank
                int idx;
                if(graph.get(a).contains(b)) {
                    idx = graph.get(a).indexOf(b);
                    graph.get(a).remove(idx);
                    inDegree[b] -= 1;
                    graph.get(b).add(a);
                    inDegree[a] += 1;
                } else {
                    idx = graph.get(b).indexOf(a);
                    graph.get(b).remove(idx);
                    inDegree[a] -= 1;
                    graph.get(a).add(b);
                    inDegree[b] += 1;
                }
            }

            result.append(topologicalSort(graph, inDegree));
            result.append('\n');
        }

        // write the result
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}