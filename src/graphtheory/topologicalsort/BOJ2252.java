package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 줄 세우기 - BOJ2252
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 * -----------------
 * Input 1
 * 3 2
 * 1 3
 * 2 3
 *
 * Output 1
 * 1 2 3
 * -----------------
 * Input 2
 * 4 2
 * 4 2
 * 3 1
 *
 * Output 2
 * 4 3 2 1
 * -----------------
 */
public class BOJ2252 {

    static int N, M;
    static StringBuilder result = new StringBuilder();

    public static List<Integer> topologicalSort(List<List<Integer>> graph, int[] inDegree) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        for(int i = 0; i < inDegree.length; i++) {
            if(inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while(!queue.isEmpty()) {
            int node = queue.poll();
            for(int i = 0; i < graph.get(node).size(); i++) {
                if(--inDegree[graph.get(node).get(i)] == 0) {
                    queue.add(graph.get(node).get(i));
                }
            }
            result.add(node);
        }

        return result;
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[N];
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken()) - 1;
            inDegree[b] += 1;
            graph.get(a).add(b);
        }

        List<Integer> sortedList = topologicalSort(graph, inDegree);
        for(int i = 0; i < sortedList.size(); i++) {
            result.append(sortedList.get(i) + 1);
            if(i < sortedList.size() - 1)
                result.append(' ');
        }

        // write the result
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}