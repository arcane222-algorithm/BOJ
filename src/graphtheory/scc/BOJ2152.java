package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 여행 계획 세우기 - BOJ2152
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           topological sorting (위상 정렬)
 *           dynamic programming (다이나믹 프로그래밍)
 * -----------------
 * Input 1
 * 3 4 1 1
 * 1 2
 * 2 3
 * 3 2
 * 2 1
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 8 9 2 7
 * 1 2
 * 1 3
 * 3 4
 * 4 5
 * 5 3
 * 2 6
 * 3 6
 * 6 7
 * 2 8
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * 4 2 4 1
 * 2 1
 * 4 3
 *
 * Output 3
 * 0
 * -----------------
 */
public class BOJ2152 {

    private static class Reader {
        private static final int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;

        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) {
                fillBuffer();
            }
            return buffer[bufferPointer++];
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private void close() throws IOException {
            din.close();
        }

        public int nextInt() throws IOException {
            int result = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }

            boolean positive = c == '+';
            boolean negative = c == '-';
            if (positive || negative) {
                c = read();
            }

            do {
                result = 10 * result + (c - '0');
                c = read();
            } while (c >= '0' && c <= '9');

            if (negative) return -result;
            else return result;
        }
    }

    static int N, M, S, T;
    static int nodeId, groupId;

    static boolean[] finish;
    static int[] nodeIds, groupIds, sccInDegrees;

    static List<List<Integer>> graph, sccList, sccGraph;
    static ArrayDeque<Integer> stack;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        M = r.nextInt();
        S = r.nextInt();
        T = r.nextInt();

        finish = new boolean[N + 1];
        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];

        graph = new ArrayList<>(N + 1);
        sccList = new ArrayList<>();
        sccGraph = new ArrayList<>();
        stack = new ArrayDeque<>();

        for (int i = 0; i < N + 1; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int u = r.nextInt();
            int v = r.nextInt();
            graph.get(u).add(v);
        }
    }

    public static void getScc() {
        for (int i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }

        for (int i = 0; i < sccList.size(); i++) {
            sccGraph.add(new ArrayList<>());
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
                groupIds[nodeIdx] = sccList.size();
                finish[nodeIdx] = true;
                scc.add(nodeIdx);
            }
            sccList.add(scc);
        }

        return parentId;
    }

    public static void getSccInDegrees() {
        sccInDegrees = new int[sccList.size()];

        for (int i = 1; i <= N; i++) {
            int currGroupId = groupIds[i];
            for (int adjIdx : graph.get(i)) {
                int adjGroupId = groupIds[adjIdx];
                if (currGroupId != adjGroupId) {
                    sccInDegrees[adjGroupId]++;
                    sccGraph.get(currGroupId).add(adjGroupId);
                }
            }
        }
    }

    public static int topologicalSort() {
        int beginGroupId = groupIds[S];
        int endGroupId = groupIds[T];
        if (beginGroupId == endGroupId) {
            return sccList.get(beginGroupId).size();
        }

        final int Size = sccList.size();
        int[] dp = new int[Size];
        boolean[] canGo = new boolean[Size];
        for (int i = 0; i < Size; i++) {
            dp[i] = sccList.get(i).size();
        }
        canGo[beginGroupId] = true;

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < Size; i++) {
            if (sccInDegrees[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int adj : sccGraph.get(curr)) {
                if (canGo[curr]) {
                    canGo[adj] = true;
                    dp[adj] = Math.max(dp[adj], dp[curr] + sccList.get(adj).size());
                }

                if (--sccInDegrees[adj] == 0) {
                    queue.add(adj);
                }
            }
        }

        return canGo[endGroupId] ? dp[endGroupId] : 0;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(r);
        getScc();
        getSccInDegrees();
        bw.write(topologicalSort() + "");

        // close the buffer
        r.close();
        bw.close();
    }
}