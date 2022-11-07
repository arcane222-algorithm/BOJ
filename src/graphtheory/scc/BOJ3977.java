package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 축구 전술 - BOJ3977
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 2
 * 4 4
 * 0 1
 * 1 2
 * 2 0
 * 2 3
 *
 * 4 4
 * 0 3
 * 1 0
 * 2 0
 * 2 3
 *
 * Output 1
 * 0
 * 1
 * 2
 *
 * Confused
 * -----------------
 */
public class BOJ3977 {

    static int T, N, M, A, B;
    static int nodeCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds, sccInDegrees;

    static List<List<Integer>> graph, sccList;
    static Stack<Integer> stack;
    static StringBuilder result = new StringBuilder();

    public static int dfs(int begin) {
        nodeIds[begin] = nodeCount++;
        stack.add(begin);

        int parent = nodeIds[begin];
        for (int adj : graph.get(begin)) {
            if (nodeIds[adj] == -1) {
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
                groupIds[nodeId] = sccList.size();
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
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            nodeCount = 0;
            finish = new boolean[N];
            nodeIds = new int[N];
            groupIds = new int[N];
            graph = new ArrayList<>(N);
            sccList = new ArrayList<>();
            stack = new Stack<>();

            Arrays.fill(nodeIds, -1);
            for (int j = 0; j < N; j++) {
                graph.add(new ArrayList<>());
            }

            for (int j = 0; j < M; j++) {
                st = new StringTokenizer(br.readLine());
                A = Integer.parseInt(st.nextToken());
                B = Integer.parseInt(st.nextToken());
                graph.get(A).add(B);
            }

            for (int j = 0; j < N; j++) {
                if (nodeIds[j] == -1) {
                    dfs(j);
                }
            }

            sccInDegrees = new int[sccList.size()];
            for (int j = 0; j < N; j++) {
                for (int adj : graph.get(j)) {
                    if (groupIds[j] != groupIds[adj]) {
                        sccInDegrees[groupIds[adj]]++;
                    }
                }
            }

            int entry = -1;
            for (int j = 0; j < sccInDegrees.length; j++) {
                int inDegree = sccInDegrees[j];
                if (inDegree == 0) {
                    if (entry == -1) entry = j;
                    else {
                        entry = -1;
                        break;
                    }
                }
            }

            if (entry == -1) {
                result.append("Confused");
            } else {
                final int Size = sccList.get(entry).size();
                sccList.get(entry).sort(Comparator.comparingInt(Integer::intValue));
                for (int j = 0; j < Size; j++) {
                    int node = sccList.get(entry).get(j);
                    result.append(node);
                    if (j < Size - 1) result.append('\n');
                }
            }

            if (i < T - 1) {
                result.append("\n\n");
                br.readLine();
            }
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}