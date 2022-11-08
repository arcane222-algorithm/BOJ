package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * ATM - BOJ4013
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           topological sorting (위상 정렬)
 *           dynamic programming (다이나믹 프로그래밍)
 * -----------------
 * Input 1
 * 6 7
 * 1 2
 * 2 3
 * 3 5
 * 2 4
 * 4 1
 * 2 6
 * 6 5
 * 10
 * 12
 * 8
 * 16
 * 1
 * 5
 * 1 4
 * 4 3 5 6
 *
 * Output 1
 * 47
 * -----------------
 * Input 2
 * 5 4
 * 1 2
 * 1 3
 * 3 4
 * 3 5
 * 1
 * 100
 * 10
 * 5
 * 10
 * 3 5
 * 1 2 3 4 5
 *
 * Output 2
 * 20
 * -----------------
 * Input 3
 * 8 8
 * 1 2
 * 2 3
 * 3 4
 * 3 7
 * 7 8
 * 1 5
 * 5 6
 * 6 8
 * 1
 * 1
 * 1
 * 10
 * 1
 * 100
 * 1
 * 1
 * 2 8
 * 1 2 3 4 5 6 7 8
 *
 * Output 3
 * 12
 * -----------------
 */
public class BOJ4013 {

    static int N, M, S, P;
    static int nodeCount;

    static boolean[] finish, isRestaurant;
    static int[] nodeIds, groupIds, cashInfos;

    static List<List<Integer>> graph, sccList;
    static Stack<Integer> stack;

    public static void init() {
        // init data structures
        finish = new boolean[N + 1];
        isRestaurant = new boolean[N + 1];

        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];
        cashInfos = new int[N + 1];

        graph = new ArrayList<>(N + 1);
        sccList = new ArrayList<>();
        stack = new Stack<>();

        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public static void read(BufferedReader br) throws Exception {
        StringTokenizer st;

        // parse graph
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph.get(u).add(v);
        }

        // parse cost
        for (int i = 1; i <= N; i++) {
            int cash = Integer.parseInt(br.readLine());
            cashInfos[i] = cash;
        }

        // parse S, P
        st = new StringTokenizer(br.readLine());
        S = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());

        // parse restaurant position
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < P; i++) {
            int rest = Integer.parseInt(st.nextToken());
            isRestaurant[rest] = true;
        }
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

    public static int topologicalSort(List<List<Integer>> graph, int[] inDegrees, int[] cashSums) {
        final int Size = inDegrees.length;
        Queue<Integer> queue = new LinkedList<>();

        boolean[] isSccRestaurant = new boolean[Size];
        boolean[] canGo = new boolean[Size];
        for (int i = 1; i <= N; i++) {
            if (isRestaurant[i]) {
                isSccRestaurant[groupIds[i]] = true;
            }
        }
        canGo[groupIds[S]] = true;

        for (int i = 1; i < Size; i++) {
            if (inDegrees[i] == 0) {
                queue.offer(i);
            }
        }

        int[] dp = Arrays.copyOf(cashSums, Size);
        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (int adj : graph.get(curr)) {
                if (canGo[curr]) {
                    canGo[adj] = true;
                    dp[adj] = Math.max(dp[adj], dp[curr] + cashSums[adj]);
                }

                if (--inDegrees[adj] == 0) {
                    queue.add(adj);
                }
            }
        }

        int max = 0;
        for (int i = 1; i < Size; i++) {
            if (canGo[i] && isSccRestaurant[i]) {
                max = Math.max(max, dp[i]);
            }
        }

        return max;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // init data structures
        init();

        // read data
        read(br);

        // get scc
        for (int i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        final int SccCount = sccList.size();
        int[] sccInDegrees = new int[SccCount + 1];
        int[] cashSums = new int[SccCount + 1];
        List<List<Integer>> sccGraph = new ArrayList<>(SccCount + 1);
        for (int i = 0; i <= SccCount; i++) {
            sccGraph.add(new ArrayList<>());
        }

        for (int i = 1; i <= N; i++) {
            int groupId = groupIds[i];
            cashSums[groupId] += cashInfos[i];

            for (int adj : graph.get(i)) {
                int adjGroupId = groupIds[adj];

                if (groupId != adjGroupId) {
                    sccGraph.get(groupId).add(adjGroupId);
                    sccInDegrees[adjGroupId]++;
                }
            }
        }

        int max = topologicalSort(sccGraph, sccInDegrees, cashSums);
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}
