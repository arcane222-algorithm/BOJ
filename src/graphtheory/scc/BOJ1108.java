package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 검색 엔진 - BOJ1108
 * -----------------
 * category: graph theory (그래프이론)
 *           set / map by hashing (해시를 사용한 집합과 맵)
 *           topological sorting (위상 정렬)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 3
 * A 3 B C D
 * B 2 C D
 * C 1 D
 * A
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 1
 * C 2 A B
 * C
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * 1
 * A 0
 * A
 *
 * Output 3
 * 1
 * -----------------
 * Input 4
 * 2
 * A 1 B
 * B 1 A
 * A
 *
 * Output 4
 * 1
 * -----------------
 * Input 5
 * 4
 * A 5 B C D E F
 * B 1 A
 * C 1 B
 * D 1 B
 * A
 *
 * Output 5
 * 3
 * -----------------
 * Input 6
 * 1
 * MYSITE 3 OTHERSITE ANOTHERSITE THIRDSITE
 * MYSITE
 *
 * Output 6
 * 4
 * -----------------
 * Input 7
 * 4
 * A 2 B C
 * C 1 D
 * D 1 A
 * E 2 C D
 * C
 *
 * Output 7
 * 1
 * -----------------
 */
public class BOJ1108 {

    static int N;
    static int nodeId, nodeCount;

    static int[] nodeIds, groupIds;
    static boolean[] finish;

    static List<List<Integer>> graph, sccGraph, sccList;
    static ArrayDeque<Integer> stack;
    static HashMap<String, Integer> indexMap;

    public static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        graph = new ArrayList<>();
        sccGraph = new ArrayList<>();
        sccList = new ArrayList<>();
        stack = new ArrayDeque<>();
        indexMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String dest = st.nextToken();
            if (!indexMap.containsKey(dest)) {
                indexMap.put(dest, nodeCount++);
                graph.add(new ArrayList<>());
            }

            int destIdx = indexMap.get(dest);
            int size = Integer.parseInt(st.nextToken());
            for (int j = 0; j < size; j++) {
                String src = st.nextToken();
                if (!indexMap.containsKey(src)) {
                    indexMap.put(src, nodeCount++);
                    graph.add(new ArrayList<>());
                }

                int srcIdx = indexMap.get(src);
                graph.get(srcIdx).add(destIdx);
            }
        }

        nodeIds = new int[nodeCount];
        groupIds = new int[nodeCount];
        finish = new boolean[nodeCount];
        Arrays.fill(nodeIds, -1);
        Arrays.fill(groupIds, -1);
    }

    public static void getScc() {
        for (int i = 0; i < nodeCount; i++) {
            if (nodeIds[i] == -1) {
                dfs(i);
            }
        }
    }

    public static int dfs(int currIdx) {
        nodeIds[currIdx] = nodeId++;
        stack.addLast(currIdx);

        int parentId = nodeIds[currIdx];
        for (int adjIdx : graph.get(currIdx)) {
            if (nodeIds[adjIdx] == -1) {
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
                groupIds[nodeIdx] = sccList.size();
                finish[nodeIdx] = true;
                scc.add(nodeIdx);
            }
            sccList.add(scc);
        }

        return parentId;
    }

    public static int[] initSccGraph() {
        int[] inDegrees = new int[sccList.size()];
        for (int i = 0; i < sccList.size(); i++) {
            sccGraph.add(new ArrayList<>());
        }

        for (int i = 0; i < nodeCount; i++) {
            for (int adjIdx : graph.get(i)) {
                if (groupIds[i] != groupIds[adjIdx]) {
                    inDegrees[groupIds[adjIdx]]++;
                    sccGraph.get(groupIds[i]).add(groupIds[adjIdx]);
                }
            }
        }
        return inDegrees;
    }

    public static long getScore(int[] inDegrees, String target) {
        int targetIdx = indexMap.get(target);
        long[] scores = new long[nodeCount];
        Arrays.fill(scores, 1);

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < sccList.size(); i++) {
            if (inDegrees[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int member : sccList.get(curr)) {
                for (int adj : graph.get(member)) {
                    if (groupIds[member] != groupIds[adj]) {
                        scores[adj] += scores[member];
                    }
                }
            }

            for (int adj : sccGraph.get(curr)) {
                if (--inDegrees[adj] == 0) {
                    queue.add(adj);
                }
            }
        }

        return scores[targetIdx];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(br);
        getScc();

        String target = br.readLine();
        long score = getScore(initSccGraph(), target);
        bw.write(score + "");

        // close the buffer
        br.close();
        bw.close();
    }
}