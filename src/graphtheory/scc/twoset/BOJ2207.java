package graphtheory.scc.twoset;

import java.io.*;
import java.util.*;


/**
 * 가위바위보 - BOJ2207
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 2 1
 * 1 1
 * -1 -1
 *
 * Output 1
 * OTL
 * -----------------
 * Input 2
 * 2 2
 * 2 1
 * -1 -2
 *
 * Output 2
 * ^_^
 * -----------------
 */
public class BOJ2207 {

    static int N, M;
    static int nodeCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph, sccList;
    static ArrayDeque<Integer> arrayDeque;
    static StringBuilder result = new StringBuilder();

    public static int positive(int x) {
        return (x << 1) - 1;
    }

    public static int negative(int x) {
        return -x << 1;
    }

    public static int cvtToIdx(int x) {
        return x > 0 ? positive(x) : negative(x);
    }

    public static int not(int x) {
        return (x & 1) == 0 ? x - 1 : x + 1;
    }

    public static int dfs(int begin) {
        nodeIds[begin] = ++nodeCount;
        arrayDeque.offerLast(begin);

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
                nodeId = arrayDeque.removeLast();
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

        final int Size = 2 * M + 1;
        finish = new boolean[Size];
        nodeIds = new int[Size];
        groupIds = new int[Size];

        graph = new ArrayList<>(Size);
        sccList = new ArrayList<>();
        arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < Size; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int xi = Integer.parseInt(st.nextToken());
            int xj = Integer.parseInt(st.nextToken());
            xi = cvtToIdx(xi);
            xj = cvtToIdx(xj);

            int nxi = not(xi);
            int nxj = not(xj);

            graph.get(nxi).add(xj);
            graph.get(nxj).add(xi);
        }

        for (int i = 1; i < Size; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        boolean canCnfTrue = true;
        for (int i = 1; i < Size; i += 1) {
            if (groupIds[i] == groupIds[not(i)]) {
                canCnfTrue = false;
                break;
            }
        }
        bw.write(canCnfTrue ? "^_^" : "OTL");

        // close the buffer
        br.close();
        bw.close();
    }
}