package graphtheory.scc;


/**
 * Strongly Connected Component - BOJ2150
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 7 9
 * 1 4
 * 4 5
 * 5 1
 * 1 6
 * 6 7
 * 2 7
 * 7 3
 * 3 7
 * 7 2
 *
 * Output 1
 * 3
 * 1 4 5 -1
 * 2 3 7 -1
 * 6 -1
 * -----------------
 */
import java.io.*;
import java.util.*;


public class BOJ2150 {

    static int V, E;
    static List<List<Integer>> graph, result;
    static boolean[] finish;
    static int[] nodeIds, ids;
    static int count = 1, groupCount;
    static Stack<Integer> stack;
    static StringBuilder builder = new StringBuilder();

    public static int dfs(int begin) {
        nodeIds[begin] = count++;
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
                result.get(groupCount).add(nodeId);
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

        st = new StringTokenizer(br.readLine());
        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());

        finish = new boolean[V + 1];
        nodeIds = new int[V + 1];
        ids = new int[V + 1];

        graph = new ArrayList<>();
        result = new ArrayList<>();
        for (int i = 0; i <= V; i++) {
            graph.add(new ArrayList<>());
            result.add(new ArrayList<>());
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());   // u --> v
            graph.get(u).add(v);
        }

        stack = new Stack<>();
        for (int i = 1; i <= V; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        builder.append(groupCount).append('\n');
        for(int i = 0; i < groupCount; i++) {
            result.get(i).sort(Integer::compare);
        }

        List<List<Integer>> list = new ArrayList<>();
        for(int i = 0; i <= V; i++) {
            if(result.get(i).size() > 0) {
                list.add(result.get(i));
            }
        }
        list.sort(Comparator.comparingInt(o -> o.get(0)));

        for(List<Integer> list2 : list) {
            for(int node : list2) {
                builder.append(node).append(' ');
            }
            builder.append(-1).append('\n');
        }

        bw.write(builder.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}