package graphtheory.scc.twoset;

import java.util.*;
import java.io.*;


/**
 * 하우스 M.D. - BOJ1217
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 *           2-sat (2-satisfiability)
 * -----------------
 * Input 1
 * 3 3
 * 1 -2
 * 2 3
 * -3 -1
 * 4 2
 * 1 2
 * 1 -2
 * -1 2
 * -1 -2
 * 0 0
 *
 * Output 1
 * 1
 * 0
 * -----------------
 */
public class BOJ1217 {

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

    private static class SimpleArrayList {
        int size;
        short[] elements;

        public SimpleArrayList(int initCapacity) {
            elements = new short[initCapacity];
        }

        private void resize() {
            int newCapacity = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, newCapacity);
        }

        public void addLast(short e) {
            if (elements.length == size) resize();
            elements[size++] = e;
        }

        public short removeLast() {
            short e = elements[--size];
            elements[size] = 0;
            return e;
        }

        public short get(int idx) {
            if (idx < 0 || idx > size - 1) throw new IndexOutOfBoundsException();
            return elements[idx];
        }

        public void clear() {
            size = 0;
        }
    }

    static final short EMPTY = Short.MIN_VALUE;
    static int N, M;
    static short nodeCount, groupCount;

    static boolean[] finish;
    static short[] nodeIds, groupIds;

    static SimpleArrayList[] graph;
    static SimpleArrayList stack;

    public static int positive(int x) {
        return (x << 1) - 1;
    }

    public static int negative(int x) {
        return -x << 1;
    }

    public static int cvtToIdx(int x) {
        return x > 0 ? positive(x) : negative(x);
    }

    public static short cvtToOrigin(int x) {
        return (short) ((x & 1) == 0 ? -(x >> 1) : (x + 1) >> 1);
    }

    public static int notIdx(int x) {
        return (x & 1) == 0 ? x - 1 : x + 1;
    }

    // beginIdx: 1 ~ 2M (1 ~ 40000)
    // beginOrigin : -M ~ M (-20000 ~ 20000)
    // short의 경우 -32768 ~ 32767 이므로 32768 ~ 40000을 넣으면 오버플로우가 발생하므로 변환해주는 것
    public static int dfs(int beginIdx) {
        short beginOrigin = cvtToOrigin(beginIdx);
        nodeIds[beginIdx] = nodeCount++;
        stack.addLast(beginOrigin);

        int parent = nodeIds[beginIdx];
        for (short adj : graph[beginIdx].elements) {
            int adjIdx = cvtToIdx(adj);

            if (nodeIds[adjIdx] == EMPTY) {
                parent = Math.min(parent, dfs(adjIdx));
            } else if (!finish[adjIdx]) {
                parent = Math.min(parent, nodeIds[adjIdx]);
            }
        }

        if (parent == nodeIds[beginIdx]) {
            int nodeIdx = -1;

            while (nodeIdx != beginIdx) {
                nodeIdx = cvtToIdx(stack.removeLast());
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = groupCount;
            }
            groupCount++;
        }

        return parent;
    }

    public static void main(String[] args) throws Exception {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        final int MaxSize = 40001;
        finish = new boolean[MaxSize];
        groupIds = new short[MaxSize];
        nodeIds = new short[MaxSize];
        graph = new SimpleArrayList[MaxSize];
        stack = new SimpleArrayList(MaxSize);

        while (true) {
            //st = new StringTokenizer(br.readLine());
            N = r.nextInt();
            M = r.nextInt();
            if (N == 0 && M == 0) break;

            final int CurrSize = 2 * M + 1;
            nodeCount = EMPTY;
            groupCount = EMPTY;
            Arrays.fill(finish, false);
            Arrays.fill(nodeIds, EMPTY);
            Arrays.fill(groupIds, EMPTY);
            stack.clear();

            for (int i = 0; i < CurrSize; i++) {
                graph[i] = new SimpleArrayList(10);
            }

            for (int i = 0; i < N; i++) {
                //st = new StringTokenizer(br.readLine());
                int xi = r.nextInt();
                int xj = r.nextInt();
                int nxiIdx = notIdx(cvtToIdx(xi));
                int nxjIdx = notIdx(cvtToIdx(xj));

                graph[nxiIdx].addLast((short) xj);
                graph[nxjIdx].addLast((short) xi);
            }

            for (int i = 1; i < CurrSize; i++) {
                if (nodeIds[i] == EMPTY) {
                    dfs(i);
                }
            }

            boolean canCnfTrue = true;
            for (int i = 1; i < CurrSize; i += 2) {
                if (groupIds[i] == groupIds[notIdx(i)]) {
                    canCnfTrue = false;
                    break;
                }
            }
            bw.write(canCnfTrue ? "1\n" : "0\n");
        }

        bw.close();
    }
}