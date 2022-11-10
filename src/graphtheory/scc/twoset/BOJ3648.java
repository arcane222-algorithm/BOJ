package graphtheory.scc.twoset;

import java.io.*;
import java.util.*;


/**
 * 아이돌 - BOJ3648
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 4 3
 * 1 2
 * -2 -3
 * 2 4
 * 2 4
 * 1 2
 * 1 -2
 * -1 2
 * -1 -2
 *
 * Output 1
 * yes
 * no
 * -----------------
 */
public class BOJ3648 {

    static int N, M;
    static int nodeCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph, sccList;
    static ArrayDeque<Integer> arrayDeque;
    static StringBuilder result = new StringBuilder();

    public static int cvtToIdx(int x) {
        if (x > 0) return (x - 1) << 1;
        else return (-x << 1) - 1;
    }

    public static int not(int x) {
        return x ^ 1;
    }

    public static int dfs(int begin) {
        nodeIds[begin] = nodeCount++;
        arrayDeque.offerLast(begin);

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
                nodeId = arrayDeque.removeLast();
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

        String line = "";
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            st = new StringTokenizer(line);
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            nodeCount = 0;
            final int Size = 2 * N;
            finish = new boolean[Size];
            nodeIds = new int[Size];
            groupIds = new int[Size];
            Arrays.fill(nodeIds, -1);

            graph = new ArrayList<>(Size);
            sccList = new ArrayList<>();
            arrayDeque = new ArrayDeque<>();

            for (int i = 0; i < Size; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                int xi = cvtToIdx(Integer.parseInt(st.nextToken()));
                int xj = cvtToIdx(Integer.parseInt(st.nextToken()));
                int nxi = not(xi);
                int nxj = not(xj);

                graph.get(nxi).add(xj);     // ~xi -> xj
                graph.get(nxj).add(xi);     // ~xj -> xi
            }

            int begin = cvtToIdx(1);
            graph.get(not(begin)).add(begin);   // ~1 -> 1 => (xi v xj ) ^ ... ^ (1 v 1)

            for (int i = 0; i < Size; i++) {
                if (nodeIds[i] == -1) {
                    dfs(i);
                }
            }

            boolean isCnfTrue = true;
            for (int i = 0; i < Size; i += 2) {
                if (groupIds[i] == groupIds[not(i)]) {
                    isCnfTrue = false;
                    break;
                }
            }
            result.append(isCnfTrue ? "yes\n" : "no\n");
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}