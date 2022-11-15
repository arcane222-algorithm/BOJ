package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 宣伝 (Advertisement) - BOJ24131
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 5 5
 * 1 2
 * 2 3
 * 3 1
 * 3 4
 * 5 4
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ24131 {

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
        int[] elements;

        public SimpleArrayList() {
            elements = new int[DEFAULT_CAPACITY];
        }

        public SimpleArrayList(int initCapacity) {
            elements = new int[initCapacity];
        }

        private void resize() {
            int newCapacity = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, newCapacity);
        }

        public void addLast(int e) {
            if (elements.length == size) resize();
            elements[size++] = e;
        }

        public int removeLast() {
            int e = elements[--size];
            elements[size] = 0;
            return e;
        }

        public int get(int idx) {
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
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static SimpleArrayList[] graph;
    static SimpleArrayList stack;

    public static int dfs(int begin) {
        nodeIds[begin] = ++nodeCount;
        stack.addLast(begin);

        int parent = nodeIds[begin];
        for (int i = 0; i < graph[begin].size; i++) {
            int adj = graph[begin].get(i);
            if (nodeIds[adj] == 0) {
                parent = Math.min(parent, dfs(adj));
            } else if (!finish[adj]) {
                parent = Math.min(parent, nodeIds[adj]);
            }
        }

        if (parent == nodeIds[begin]) {
            int nodeId = -1;
            while (nodeId != begin) {
                nodeId = stack.removeLast();
                finish[nodeId] = true;
                groupIds[nodeId] = groupCount + 1;
            }
            groupCount++;
        }

        return parent;
    }

    public static void getScc() {
        for (int i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int[] getInDegrees() {
        int[] inDegrees = new int[groupCount + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < graph[i].size; j++) {
                int adj = graph[i].get(j);
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
        //ufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        //st = new StringTokenizer(br.readLine());
        N = r.nextInt();
        M = r.nextInt();

        finish = new boolean[N + 1];
        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];
        graph = new SimpleArrayList[N + 1];
        stack = new SimpleArrayList();

        for (int i = 0; i <= N; i++) {
            graph[i] = new SimpleArrayList();
        }

        for (int i = 0; i < M; i++) {
            //st = new StringTokenizer(br.readLine());
            int u = r.nextInt();
            int v = r.nextInt();
            graph[u].addLast(v);
        }
        getScc();

        int[] inDegrees = getInDegrees();
        int result = 0;
        for (int i = 1; i < inDegrees.length; i++) {
            if (inDegrees[i] == 0) result++;
        }
        bw.write(String.valueOf(result));

        // close the buffer
        //br.close();
        bw.close();
    }
}