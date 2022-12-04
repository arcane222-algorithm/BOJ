package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 성격 진단 테스트 - BOJ4305
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 4
 * A B C D E C
 * F C H I J J
 * K B H I F I
 * K C E B J K
 * 0
 *
 * Output 1
 * A
 * B
 * C
 * D
 * E
 * F
 * H
 * I J K
 * -----------------
 * Input 2
 * 4
 * A B C D E C
 * F C H I J J
 * K B H I F I
 * K C E B J K
 * 1
 * A B C D E C
 * 0
 *
 * Output 2
 * A
 * B
 * C
 * D
 * E
 * F
 * H
 * I J K
 *
 * A
 * B
 * C
 * D
 * E
 * -----------------
 */
public class BOJ4305 {

    static final int MAX_CHAR_COUNT = 26;

    static int N;
    static int nodeId;

    static boolean[] finish, exist;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph, sccList;
    static ArrayDeque<Integer> stack;
    static StringBuilder result = new StringBuilder();

    public static void init() {
        nodeId = 0;
        nodeIds = new int[MAX_CHAR_COUNT + 1];
        groupIds = new int[MAX_CHAR_COUNT + 1];
        exist = new boolean[MAX_CHAR_COUNT + 1];
        finish = new boolean[MAX_CHAR_COUNT + 1];

        graph = new ArrayList<>(MAX_CHAR_COUNT + 1);
        sccList = new ArrayList<>();
        stack = new ArrayDeque<>();

        for (int i = 0; i <= MAX_CHAR_COUNT; i++) {
            graph.add(new ArrayList<>());
        }
    }


    public static void getScc() {
        for (int i = 1; i <= MAX_CHAR_COUNT; i++) {
            if (!exist[i]) continue;
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int dfs(int currIdx) {
        nodeIds[currIdx] = ++nodeId;
        stack.addLast(currIdx);

        int parentId = nodeIds[currIdx];
        for (int adjIdx : graph.get(currIdx)) {
            if (nodeIds[adjIdx] == 0) {
                parentId = Math.min(parentId, dfs(adjIdx));
            } else if (!finish[adjIdx]) {
                parentId = Math.min(parentId, nodeIds[adjIdx]);
            }
        }

        if (parentId == nodeIds[currIdx]) {
            int nodeIdx = -1;
            List<Integer> scc = new ArrayList<>();
            while (nodeIdx != currIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = sccList.size();
                scc.add(nodeIdx);
            }
            sccList.add(scc);
        }

        return parentId;
    }

    public static char toChar(int val) {
        return (char) (val + 'A' - 1);
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            N = Integer.parseInt(br.readLine());
            if (N == 0) break;

            init();
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());

                int[] from = new int[5];
                for (int j = 0; j < from.length; j++) {
                    int idx = st.nextToken().charAt(0) - 'A' + 1;
                    from[j] = idx;
                    exist[idx] = true;
                }

                int to = st.nextToken().charAt(0) - 'A' + 1;
                for (int f : from) {
                    if (f != to) {
                        graph.get(f).add(to);
                    }
                }
            }

            getScc();
            for (List<Integer> scc : sccList) {
                scc.sort(Integer::compare);
            }
            sccList.sort(Comparator.comparingInt(o -> o.get(0)));

            for (int i = 0; i < sccList.size(); i++) {
                List<Integer> scc = sccList.get(i);

                for (int j = 0; j < scc.size(); j++) {
                    result.append(toChar(scc.get(j)));
                    if (j < scc.size() - 1)
                        result.append(' ');
                }
                result.append("\n");
            }
            result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}