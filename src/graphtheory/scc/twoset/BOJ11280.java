package graphtheory.scc.twoset;

import java.io.*;
import java.util.*;


/**
 * 2-SAT - 3 - BOJ11280
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 3 4
 * -1 2
 * -2 3
 * 1 3
 * 3 2
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 1 2
 * 1 1
 * -1 -1
 *
 * Output 2
 * 0
 * -----------------
 */
public class BOJ11280 {

    static int N, M;
    static int nodeCount;

    static int[] nodeIds, groupIds;
    static boolean[] finish;

    static List<List<Integer>> graph, sccList;
    static Stack<Integer> stack;

    public static int positive(int x) {
        return 2 * x - 1;
    }

    public static int negative(int x) {
        return -2 * x;
    }

    public static int opponent(int x) {
        return (x & 1) == 0 ? x - 1 : x + 1;
    }

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
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        final int Size = 2 * N + 1;
        nodeIds = new int[Size];
        groupIds = new int[Size];
        finish = new boolean[Size];

        graph = new ArrayList<>(Size);
        sccList = new ArrayList<>();
        stack = new Stack<>();

        for (int i = 0; i < Size; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int xi = Integer.parseInt(st.nextToken());
            int xj = Integer.parseInt(st.nextToken());
            xi = xi < 0 ? negative(xi) : positive(xi);      // +1,+2,+3,+4,+5...    => 1,3,5,7,9...
            xj = xj < 0 ? negative(xj) : positive(xj);      // -1,-2,-3,-4,-5...    => 2,4,6,8,10...

            int nxi = opponent(xi);
            int nxj = opponent(xj);

            graph.get(nxi).add(xj);     // ~xi -> xj
            graph.get(nxj).add(xi);     // ~xj -> xi
        }

        for (int i = 1; i < Size; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        boolean isCnfTrue = true;
        for (int i = 1; i < Size; i += 2) {
            if (groupIds[i] == groupIds[opponent(i)]) {
                isCnfTrue = false;
                break;
            }
        }
        bw.write(isCnfTrue ? "1" : "0");

        // close the buffer
        br.close();
        bw.close();
    }
}