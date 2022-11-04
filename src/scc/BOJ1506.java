package scc;

import java.io.*;
import java.util.*;
import java.util.stream.*;


/**
 * 경찰서 - BOJ1506
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 5
 * 1 2 3 4 5
 * 00011
 * 10000
 * 00010
 * 00100
 * 01000
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 2
 * 1000000 1000000
 * 01
 * 10
 *
 * Output 2
 * 1000000
 * -----------------
 * Input 3
 * 4
 * 5 3 10 4
 * 0100
 * 0010
 * 0001
 * 1000
 *
 * Output 3
 * 3
 * -----------------
 */
public class BOJ1506 {

    private static class Node {
        int nodeId, cost;

        public Node(int nodeId, int cost) {
            this.nodeId = nodeId;
            this.cost = cost;
        }
    }

    static int N;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] costs;
    static int[] nodeIds, ids;
    static int[][] map;

    static List<List<Integer>> graph;
    static List<List<Node>> scc;
    static Stack<Integer> stack;

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
            int nodeId = -2;
            while (nodeId != begin) {
                nodeId = stack.pop();
                finish[nodeId] = true;
                ids[nodeId] = groupCount;
                scc.get(groupCount).add(new Node(nodeId, costs[nodeId]));
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

        N = Integer.parseInt(br.readLine());
        costs = new int[N];
        map = new int[N][N];
        nodeIds = new int[N];
        ids = new int[N];
        finish = new boolean[N];

        graph = new LinkedList<>();
        scc = new LinkedList<>();
        stack = new Stack<>();

        for (int i = 0; i < N; i++) {
            graph.add(new LinkedList<>());
            scc.add(new LinkedList<>());
        }

        Arrays.fill(nodeIds, -1);
        Arrays.fill(ids, -1);

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            costs[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < N; j++) {
                map[i][j] = line.charAt(j) - '0';
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == 1) {
                    graph.get(i).add(j);
                }
            }
        }

        for (int i = 0; i < N; i++) {
            if (nodeIds[i] == -1) {
                dfs(i);
            }
        }

        Stream<List<Node>> filtered = scc.stream().filter(l -> l.size() > 0);
        int sum = filtered.mapToInt(l -> l.stream().min(Comparator.comparingInt(o -> o.cost)).orElseGet(() -> new Node(0, 0)).cost).sum();
        bw.write(String.valueOf(sum));

        // close the buffer
        br.close();
        bw.close();
    }
}