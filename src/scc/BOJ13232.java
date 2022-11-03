package scc;

import java.io.*;
import java.util.*;


/**
 * Domain clusters - BOJ13232
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 5
 * 7
 * 1 2
 * 1 4
 * 2 3
 * 3 4
 * 3 2
 * 3 5
 * 5 2
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 4
 * 4
 * 1 2
 * 4 1
 * 3 4
 * 2 3
 *
 * Output 2
 * 4
 * -----------------
 */
public class BOJ13232 {

    static int D, L;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, ids;

    static List<List<Integer>> graph, scc;

    static Stack<Integer> stack;

    public static int dfs(int begin) {
        nodeIds[begin] = ++nodeCount;
        stack.add(begin);

        int parent = nodeIds[begin];
        for (int adj : graph.get(begin)) {
            if (nodeIds[adj] == 0) {
                parent = Math.min(parent, dfs(adj));
            } else if (!finish[adj]) {
                parent = Math.min(parent, nodeIds[adj]);
            }
        }

        if (parent == nodeIds[begin]) {
            int nodeId = -1;
            while (nodeId != begin) {
                nodeId = stack.pop();
                finish[nodeId] = true;
                ids[nodeId] = groupCount;
                scc.get(groupCount).add(nodeId);
            }
            groupCount++;
        }

        return parent;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        D = Integer.parseInt(br.readLine());
        L = Integer.parseInt(br.readLine());

        finish = new boolean[D + 1];
        nodeIds = new int[D + 1];
        ids = new int[D + 1];

        graph = new LinkedList<>();
        scc = new LinkedList<>();
        for (int i = 0; i <= D; i++) {
            graph.add(new LinkedList<>());
            scc.add(new LinkedList<>());
        }
        stack = new Stack<>();

        for (int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph.get(u).add(v);    // u --> v
        }

        for (int i = 1; i <= D; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        for (Iterator<List<Integer>> iter = scc.listIterator(); iter.hasNext(); ) {
            List<Integer> element = iter.next();
            if (element.size() == 0) {
                iter.remove();
            }
        }
        scc.sort(Comparator.comparingInt(List::size));
        bw.write(String.valueOf(scc.get(scc.size() - 1).size()));

        // close the buffer
        br.close();
        bw.close();
    }
}