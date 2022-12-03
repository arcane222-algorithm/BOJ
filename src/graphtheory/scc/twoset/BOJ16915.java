package graphtheory.scc.twoset;

import java.io.*;
import java.util.*;


/**
 * 호텔 관리 - BOJ16915
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 3 3
 * 1 0 1
 * 2 1 3
 * 2 1 2
 * 2 2 3
 *
 * Output 1
 * 0
 * -----------------
 * Input 2
 * 3 3
 * 1 0 1
 * 3 1 2 3
 * 1 2
 * 2 1 3
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 3 3
 * 1 0 1
 * 3 1 2 3
 * 2 1 2
 * 1 3
 *
 * Output 3
 * 0
 * -----------------
 */
public class BOJ16915 {
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

    static int N, M;
    static int nodeId, groupId;

    static boolean[] finish;
    static int[] nodeIds, groupIds, rooms;
    static int[][] switches;

    static List<List<Integer>> graph;
    static ArrayDeque<Integer> stack;

    public static int positive(int x) {
        return (x << 1) - 1;
    }

    public static int negative(int x) {
        return -x << 1;
    }

    public static int not(int x) {
        return (x & 1) == 0 ? x - 1 : x + 1;
    }

    public static int getIdx(int x) {
        return x > 0 ? positive(x) : negative(x);
    }

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        M = r.nextInt();
        rooms = new int[N + 1];
        switches = new int[N + 1][2];
        for (int i = 1; i <= N; i++) {
            rooms[i] = r.nextInt();
        }

        for (int i = 1; i <= M; i++) {
            int K = r.nextInt();
            for (int j = 0; j < K; j++) {
                int roomNum = r.nextInt();
                if (switches[roomNum][0] == 0)
                    switches[roomNum][0] = i;
                else
                    switches[roomNum][1] = i;
            }
        }

        final int Size = 2 * M + 1;
        finish = new boolean[Size];
        nodeIds = new int[Size];
        groupIds = new int[Size];

        graph = new ArrayList<>(Size);
        stack = new ArrayDeque<>();

        for (int i = 0; i < Size; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 1; i <= N; i++) {
            int xi = getIdx(switches[i][0]);
            int xj = getIdx(switches[i][1]);
            int nxi = not(xi);
            int nxj = not(xj);

            if (rooms[i] == 0) {
                graph.get(xi).add(nxj);     // xi -> ~xj (xj -> ~xi)
                graph.get(xj).add(nxi);
                graph.get(nxj).add(xi);     // ~xj -> xi (~xi -> xj)
                graph.get(nxi).add(xj);
            } else {
                graph.get(xi).add(xj);      // xi -> xj (~xj -> ~xi)
                graph.get(nxj).add(nxi);
                graph.get(xj).add(xi);      // xj -> xi (~xi -> ~xj)
                graph.get(nxi).add(nxj);
            }
        }
    }

    public static void getScc() {
        for (int i = 1; i < nodeIds.length; i++) {
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
            while (nodeIdx != currIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = groupId;
            }
            groupId++;
        }

        return parentId;
    }

    public static int isCnfTrue() {
        for (int i = 1; i < nodeIds.length; i += 2) {
            if (groupIds[i] == groupIds[not(i)]) {
                return 0;
            }
        }
        return 1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(r);
        getScc();
        bw.write(isCnfTrue() + "");

        // close the buffer
        r.close();
        bw.close();
    }
}