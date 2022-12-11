package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 공장 - BOJ7578
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5
 * 132 392 311 351 231
 * 392 351 132 311 231
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 5
 * 569483 680496 157971 484174 256131
 * 680496 157971 569483 484174 256131
 *
 * Output 2
 * 2
 * -----------------
 */
public class BOJ7578 {

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

    private static class SegTree {
        private long[] nodes;
        private int leafCount;

        public SegTree(int size) {
            init(size);
        }

        private void init(int size) {
            leafCount = 1;
            while (leafCount < size)
                leafCount <<= 1;

            nodes = new long[leafCount << 1];
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, node * 2, queryLeft, queryRight);
                long rightValue = query(mid + 1, right, node * 2 + 1, queryLeft, queryRight);

                return leftValue + rightValue;
            }
        }

        public void update(int left, int right, int node, int target, long diff) {
            if (left > target || right < target) {
                return;
            } else {
                nodes[node] += diff;
                if (left != right) {
                    int mid = (left + right) >> 1;
                    update(left, mid, (node << 1), target, diff);
                    update(mid + 1, right, (node << 1) + 1, target, diff);
                }
            }
        }
    }

    static int N;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        int[] nums = new int[N];
        HashMap<Integer, Integer> map = new HashMap<>();
        SegTree segTree = new SegTree(N);

        for (int i = 0; i < N; i++) {
            map.put(r.nextInt(), i);
        }

        for (int i = 0; i < N; i++) {
            nums[i] = map.get(r.nextInt());
        }

        long count = 0;
        for (int i = 0; i < N; i++) {
            count += segTree.query(1, segTree.leafCount, 1, nums[i] + 1, N);
            segTree.update(1, segTree.leafCount, 1, nums[i], 1);
        }
        bw.write(String.valueOf(count));

        // close the buffer
        bw.close();
    }
}