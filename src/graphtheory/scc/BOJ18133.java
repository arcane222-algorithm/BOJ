package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 가톨릭대학교에 워터 슬라이드를?? - BOJ18133
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 4 3
 * 1 2
 * 2 1
 * 3 1
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 10 4
 * 6 1
 * 9 7
 * 9 7
 * 4 3
 *
 * Output 2
 * 7
 * -----------------
 */
public class BOJ18133 {

    private static class Reader {
        private static final int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }


        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();

            boolean pos = (c == '+');
            boolean neg = (c == '-');
            if (pos || neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();

            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null) return;
            din.close();
        }
    }

    static int N, M;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph;
    static ArrayDeque<Integer> stack;

    public static void init(Reader r) throws IOException {
        nodeCount = groupCount = 0;
        finish = new boolean[N + 1];
        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];

        graph = new ArrayList<>(N + 1);
        stack = new ArrayDeque<>();
        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int x = r.nextInt();
            int y = r.nextInt();
            graph.get(x).add(y);
        }
    }


    public static void getScc() {
        for (int i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int dfs(int currIdx) {
        nodeIds[currIdx] = ++nodeCount;
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
            while (nodeIdx != currIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = groupCount;
            }
            groupCount++;
        }

        return parentId;
    }

    public static int[] getInDegrees() {
        int[] inDegrees = new int[groupCount];
        for (int i = 1; i <= N; i++) {
            for (int adjIdx : graph.get(i)) {
                if (groupIds[i] != groupIds[adjIdx]) {
                    inDegrees[groupIds[adjIdx]]++;
                }
            }
        }

        return inDegrees;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = r.nextInt();
        M = r.nextInt();
        init(r);

        getScc();
        int[] inDegrees = getInDegrees();

        int count = 0;
        for (int deg : inDegrees) {
            if (deg == 0) count++;
        }
        bw.write(String.valueOf(count));

        // close the buffer
        r.close();
        bw.close();
    }
}