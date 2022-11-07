package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 도미노 - BOJ4196
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 1
 * 3 2
 * 1 2
 * 2 3
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 1
 * 12 14
 * 1 2
 * 2 3
 * 3 4
 * 4 5
 * 5 3
 * 5 11
 * 11  12
 * 12 11
 * 6 7
 * 7 8
 * 8 9
 * 8 11
 * 9 10
 * 10 7
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 1
 * 6 7
 * 1 2
 * 2 3
 * 3 1
 * 4 5
 * 5 6
 * 6 4
 * 6 3
 *
 * Output 3
 * 1
 * -----------------
 */
public class BOJ4196 {

    static int T, N, M;
    static int nodeCount;
    static boolean[] finish;
    static int[] nodeIds, groupIds;
    static List<List<Integer>> graph, sccList;
    static Stack<Integer> stack;
    static StringBuilder result = new StringBuilder();

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
            List<Integer> scc = new ArrayList<>();
            while (nodeId != begin) {
                nodeId = stack.pop();
                finish[nodeId] = true;
                groupIds[nodeId] = sccList.size() + 1;
                scc.add(nodeId);
            }
            sccList.add(scc);
        }

        return parent;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            nodeCount = 0;
            nodeIds = new int[N + 1];
            groupIds = new int[N + 1];
            finish = new boolean[N + 1];
            graph = new ArrayList<>(N + 1);
            sccList = new ArrayList<>();
            stack = new Stack<>();

            for (int j = 0; j <= N; j++) {
                graph.add(new ArrayList<>());
            }

            for (int j = 0; j < M; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                graph.get(x).add(y);
            }

            for (int j = 1; j <= N; j++) {
                if (nodeIds[j] == 0) {
                    dfs(j);
                }
            }

            int[] sccInDegrees = new int[sccList.size()];
            for (int j = 1; j <= N; j++) {
                for (int adj : graph.get(j)) {
                    if (groupIds[j] != groupIds[adj]) {
                        sccInDegrees[groupIds[adj]]++;
                    }
                }
            }

            int count = 0;
            for (int inDegree : sccInDegrees) {
                if (inDegree == 0) count++;
            }
            result.append(count);
            if(i < T - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
