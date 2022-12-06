package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 즉흥 여행 (Easy) - BOJ26146
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 4 5
 * 1 2
 * 2 3
 * 3 1
 * 1 4
 * 4 1
 *
 * Output 1
 * Yes
 * -----------------
 * Input 2
 * 4 4
 * 1 2
 * 2 3
 * 3 4
 * 4 2
 *
 * Output 2
 * No
 * -----------------
 * Input 3
 * 4 4
 * 1 2
 * 2 1
 * 3 4
 * 4 3
 *
 * Output 3
 * No
 * -----------------
 */
public class BOJ26146 {

    static class Reader {
        private final int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;
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
            boolean neg = (c == '-');
            if (neg)
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
            din.close();
        }
    }

    static int N, M, T;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph;
    static ArrayDeque<Integer> stack;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        M = r.nextInt();

        finish = new boolean[N + 1];
        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];

        graph = new ArrayList<>(N + 1);
        stack = new ArrayDeque<>();

        for (int i = 0; i <= N; i++) {
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(r);
        getScc();
        bw.write(groupCount == 1 ? "Yes" : "No");

        // close the buffer
        r.close();
        bw.close();
    }
}
