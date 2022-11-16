package graphtheory.scc;

import java.io.*;
import java.util.*;


/**
 * 夏合宿の朝は早い - BOJ22399
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 2
 * 0.60 1 2
 * 0.60 0
 * 2
 * 0.60 1 2
 * 0.60 1 1
 * 5
 * 0.10 1 2
 * 0.20 1 3
 * 0.30 1 4
 * 0.40 1 5
 * 0.50 1 1
 * 5
 * 0.10 0
 * 0.20 1 1
 * 0.30 1 1
 * 0.40 1 1
 * 0.50 1 1
 * 5
 * 0.10 4 2 3 4 5
 * 0.20 0
 * 0.30 0
 * 0.40 0
 * 0.50 0
 * 4
 * 0.10 1 2
 * 0.20 0
 * 0.30 1 4
 * 0.40 1 3
 * 5
 * 0.10 0
 * 0.20 0
 * 0.30 0
 * 0.40 0
 * 0.50 0
 * 0
 *
 * Output 1
 * 0.400000000
 * 0.640000000
 * 0.998800000
 * 0.168000000
 * 0.900000000
 * 0.792000000
 * 0.151200000
 * -----------------
 */
public class BOJ22399 {

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
        byte[] elements;

        public SimpleArrayList() {
            elements = new byte[DEFAULT_CAPACITY];
        }

        public SimpleArrayList(int initCapacity) {
            elements = new byte[initCapacity];
        }

        private void resize() {
            int newCapacity = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, newCapacity);
        }

        public void addLast(byte e) {
            if (elements.length == size) resize();
            elements[size++] = e;
        }

        public byte removeLast() {
            byte e = elements[--size];
            elements[size] = 0;
            return e;
        }

        public byte get(int idx) {
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

    static int N;
    static byte nodeCount;

    static boolean[] finish;
    static byte[] nodeIds, groupIds;
    static double[] probabilities;

    static SimpleArrayList[] graph;
    static ArrayList<SimpleArrayList> sccList;
    static SimpleArrayList stack;

    public static void init(Reader r) throws IOException {
        nodeCount = 0;
        finish = new boolean[N + 1];
        nodeIds = new byte[N + 1];
        groupIds = new byte[N + 1];
        probabilities = new double[N + 1];
        graph = new SimpleArrayList[N + 1];
        sccList = new ArrayList<>();
        stack = new SimpleArrayList();

        //graph[0] = new SimpleArrayList();
        for (byte i = 1; i <= N; i++) {
            graph[i] = new SimpleArrayList();
            double pi = r.nextDouble();
            byte mi = (byte) r.nextInt();

            probabilities[i] = pi;
            for (byte j = 0; j < mi; j++) {
                byte v = (byte) r.nextInt();
                graph[i].addLast(v);
            }
        }
    }

    public static void getScc() {
        for (byte i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int dfs(byte beginIdx) {
        nodeIds[beginIdx] = ++nodeCount;
        stack.addLast(beginIdx);

        int parentId = nodeIds[beginIdx];
        for (int i = 0; i < graph[beginIdx].size; i++) {
            byte adj = graph[beginIdx].get(i);
            if (nodeIds[adj] == 0) {
                parentId = Math.min(parentId, dfs(adj));
            } else if (!finish[adj]) {
                parentId = Math.min(parentId, nodeIds[adj]);
            }
        }

        if (parentId == nodeIds[beginIdx]) {
            byte nodeIdx = -1;
            SimpleArrayList scc = new SimpleArrayList();

            while (nodeIdx != beginIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = (byte) sccList.size();
                scc.addLast(nodeIdx);
            }
            sccList.add(scc);
        }

        return parentId;
    }

    public static int[] getInDegrees() {
        int[] inDegrees = new int[sccList.size()];

        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < graph[i].size; j++) {
                byte adj = graph[i].get(j);
                if (groupIds[i] != groupIds[adj]) {
                    inDegrees[groupIds[adj]]++;
                }
            }
        }

        return inDegrees;
    }

    public static double getProbability(SimpleArrayList scc) {
        final int Size = scc.size;
        double sum = 0;
        for (int i = 0; i < Size; i++) {
            int currIdx = scc.get(i);
            double currProbability = 1 - probabilities[currIdx];

            for (int j = 0; j < i; j++) {
                int prevIdx = scc.get(j);
                currProbability *= probabilities[prevIdx];
            }

            sum += currProbability;
        }

        return sum;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        while (true) {
            N = r.nextInt();
            if (N == 0) break;

            init(r);
            getScc();

            int[] sccInDegrees = getInDegrees();
            double result = 1;
            for (int i = 0; i < sccInDegrees.length; i++) {
                if (sccInDegrees[i] == 0) {
                    result *= getProbability(sccList.get(i));
                }
            }
            bw.write(String.valueOf(result));
            bw.write('\n');
        }

        // close the buffer
        br.close();
        bw.close();
    }
}