package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 세진 바이러스 - BOJ15783
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 10 5
 * 0 5
 * 0 4
 * 2 3
 * 5 9
 * 8 6
 *
 * Output 1
 * 5
 * -----------------
 * Input 2
 * 5 3
 * 0 1
 * 1 2
 * 2 0
 *
 * Output 2
 * 3
 * -----------------
 */
public class BOJ15783 {

    static class Reader {
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
    static ArrayDeque<Integer> arrayDeque;

    public static void init(Reader r) throws IOException {
        StringTokenizer st;

        finish = new boolean[N];
        nodeIds = new int[N];
        groupIds = new int[N];
        Arrays.fill(nodeIds, -1);
        Arrays.fill(groupIds, -1);

        graph = new ArrayList<>(N);
        arrayDeque = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int u = r.nextInt();
            int v = r.nextInt();
            graph.get(u).add(v);
        }
    }

    public static void getScc() {
        for (int i = 0; i < N; i++) {
            if (nodeIds[i] == -1) {
                dfs(i);
            }
        }
    }

    public static int dfs(int currIdx) {
        nodeIds[currIdx] = nodeCount++;
        arrayDeque.offerLast(currIdx);

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

            while (nodeIdx != currIdx) {
                nodeIdx = arrayDeque.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = groupCount;
            }
            groupCount++;
        }

        return parentId;
    }

    public static int[] getSccInDegrees() {
        int[] inDegrees = new int[groupCount];
        for (int i = 0; i < N; i++) {
            for (int adj : graph.get(i)) {
                if (groupIds[i] != groupIds[adj]) {
                    inDegrees[groupIds[adj]]++;
                }
            }
        }

        return inDegrees;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        M = r.nextInt();

        // init & get scc
        init(r);
        getScc();

        // get scc in-degrees
        int[] inDegrees = getSccInDegrees();
        int count = 0;
        for (int inDegree : inDegrees) {
            if (inDegree == 0) count++;
        }
        bw.write(String.valueOf(count));

        // close the buffer
        bw.close();
    }
}