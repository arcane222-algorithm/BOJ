package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 문제집 - BOJ1766
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 *           priority queue (우선순위 큐)
 * -----------------
 * Input 1
 * 4 2
 * 4 2
 * 3 1
 *
 * Output 1
 * 3 1 4 2
 * -----------------
 */
public class BOJ1766 {

    static int N, M;
    static StringBuilder result = new StringBuilder();

    public static List<Integer> topologicalSort(List<List<Integer>> graph, int[] inDegree) {
        List<Integer> sortedList = new ArrayList<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for(int i = 0; i < inDegree.length; i++) {
            if(inDegree[i] == 0) {
                pq.add(i);
            }
        }

        while(!pq.isEmpty()) {
            int node = pq.poll();
            List<Integer> children = graph.get(node);
            for(int i = 0; i < children.size(); i++) {
                inDegree[children.get(i)] -= 1;

                if(inDegree[children.get(i)] == 0) {
                    pq.add(children.get(i));
                }
            }
            sortedList.add(node);
        }

        return sortedList;
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int[] inDegree = new int[N];
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken()) - 1;
            graph.get(a).add(b);
            inDegree[b] += 1;
        }

        List<Integer> sortedList = topologicalSort(graph, inDegree);

        for(int i : sortedList) {
            result.append(i + 1);
            result.append(' ');
        }
        result.delete(result.length() - 1, result.length());

        // write the result
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}