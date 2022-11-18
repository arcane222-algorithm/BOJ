package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 그래프의 싱크 - BOJ6543
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 3 3
 * 1 3 2 3 3 1
 * 2 1
 * 1 2
 * 0
 *
 * Output 1
 * 1 3
 * 2
 * -----------------
 * Input 2
 * 2 1
 * 2 1
 * 2 0
 *
 * 5 5
 * 1 2 2 3 3 1 5 4 4 3
 * 5 5
 * 1 2 2 3 3 1 3 4 4 5
 * 5 1
 * 5 1
 * 5 6
 * 1 2 2 3 3 1 3 4 4 5 5 3
 * 0
 *
 * Output 2
 * 1
 * 1 2
 * 1 2 3
 * 5
 * 1 2 3 4
 * 1 2 3 4 5
 * -----------------
 */
public class BOJ6543 {

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
        private static final int DEFAULT_CAPACITY = 10;

        int size;
        short[] elements;

        public SimpleArrayList() {
            elements = new short[DEFAULT_CAPACITY];
        }

        public SimpleArrayList(int initCapacity) {
            elements = new short[initCapacity];
        }

        private void resize() {
            int newCapacity = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, newCapacity);
        }

        public void addAll(SimpleArrayList list) {
            for(int i = 0; i < list.size; i++) {
                addLast(list.get(i));
            }
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

        public boolean isEmpty() {
            return size == 0;
        }
    }

    static int N, M;
    static short nodeCount;

    static boolean[] finish;
    static short[] nodeIds, groupIds;

    static SimpleArrayList[] graph;
    static List<SimpleArrayList> sccList;
    static SimpleArrayList stack;

    public static void init(Reader r) throws IOException {
        nodeCount = 0;
        finish = new boolean[N + 1];
        nodeIds = new short[N + 1];
        groupIds = new short[N + 1];

        graph = new SimpleArrayList[N + 1];
        sccList = new ArrayList<>();
        stack = new SimpleArrayList();

        for (int i = 0; i <= N; i++) {
            graph[i] = new SimpleArrayList();
        }

        if (M == 0) r.readLine();
        else {
            for (int i = 0; i < M; i++) {
                short v = (short) r.nextInt();
                short w = (short) r.nextInt();
                graph[v].addLast(w);
            }
        }
    }

    public static void getScc() {
        for (short i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int dfs(short currIdx) {
        nodeIds[currIdx] = ++nodeCount;
        stack.addLast(currIdx);

        short parentId = nodeIds[currIdx];
        for (int i = 0; i < graph[currIdx].size; i++) {
            short adj = graph[currIdx].get(i);
            if (nodeIds[adj] == 0) {
                parentId = (short) Math.min(parentId, dfs(adj));
            } else if (!finish[adj]) {
                parentId = (short) Math.min(parentId, nodeIds[adj]);
            }
        }

        if (parentId == nodeIds[currIdx]) {
            short nodeIdx = -1;
            SimpleArrayList scc = new SimpleArrayList();

            while (nodeIdx != currIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = (short) sccList.size();
                scc.addLast(nodeIdx);
            }
            sccList.add(scc);
        }

        return parentId;
    }

    public static int[] getSccOutDegrees() {
        int[] outDegrees = new int[sccList.size()];

        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < graph[i].size; j++) {
                short adj = graph[i].get(j);
                if (groupIds[i] != groupIds[adj]) {
                    outDegrees[groupIds[i]]++;
                }
            }
        }

        return outDegrees;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            N = r.nextInt();
            if (N == 0) break;
            M = r.nextInt();
            init(r);
            getScc();

            int[] outDegrees = getSccOutDegrees();
            SimpleArrayList nodes = new SimpleArrayList();

            for (int i = 0; i < outDegrees.length; i++) {
                int od = outDegrees[i];
                if (od == 0) {
                    nodes.addAll(sccList.get(i));
                }
            }

            Arrays.sort(nodes.elements, 0, nodes.size);
            for(int i = 0; i < nodes.size; i++) {
                bw.write(String.valueOf(nodes.get(i)));
                bw.write(' ');
            }
            bw.write('\n');
        }

        // close the buffer
        bw.close();
    }
}