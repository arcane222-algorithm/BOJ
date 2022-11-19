package graphtheory.scc.twoset;

import java.io.*;
import java.util.*;


/**
 * 완벽한 선거! - BOJ3747
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 3 3  +1 +2  -1 +2  -1 -3
 * 2 3  -1 +2  -1 -2  +1 -2
 * 2 4  -1 +2  -1 -2  +1 -2  +1 +2
 * 2 8  +1 +2  +2 +1  +1 -2  +1 -2  -2 +1  -1 +1  -2 -2  +1 -1
 *
 * Output 1
 * 1
 * 1
 * 0
 * 1
 * -----------------
 */
public class BOJ3747 {

    private static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n')
                    break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            if (c == -1)
                return -1;

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

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

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

            if (bytesRead == 1 && buffer[0] <= ' ')
                return -1;

            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null)
                return;
            din.close();
        }
    }

    static int N, M;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph;
    static ArrayDeque<Integer> stack;
    static StringBuilder result = new StringBuilder();

    public static int positive(int x) {
        return (x << 1) - 1;
    }

    public static int negative(int x) {
        return -x << 1;
    }

    public static int setIdx(int x) {
        return x > 0 ? positive(x) : negative(x);
    }

    public static int not(int x) {
        return (x & 1) == 0 ? x - 1 : x + 1;
    }

    public static void init(Reader r) throws IOException {
        final int Size = 2 * N + 1;
        nodeCount = groupCount = 0;

        finish = new boolean[Size];
        nodeIds = new int[Size];
        groupIds = new int[Size];
        graph = new ArrayList<>(Size);
        stack = new ArrayDeque<>();

        for (int i = 0; i < Size; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int xi = r.nextInt();
            int xj = r.nextInt();
            xi = setIdx(xi);
            xj = setIdx(xj);

            graph.get(not(xi)).add(xj);
            graph.get(not(xj)).add(xi);
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

    public static boolean isCnfTrue() {
        for (int i = 1; i < nodeIds.length; i += 2) {
            if (groupIds[i] == groupIds[not(i)]) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            try {
                N = r.nextInt();
                if (N == -1) break;
                M = r.nextInt();
                init(r);
                getScc();
                result.append(isCnfTrue() ? "1\n" : "0\n");
            } catch (Exception e) {
                break;
            }
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
