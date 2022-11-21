package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 수열과 쿼리 17 - BOJ14438
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5
 * 5 4 3 2 1
 * 6
 * 2 1 3
 * 2 1 4
 * 1 5 3
 * 2 3 5
 * 1 4 3
 * 2 3 5
 *
 * Output 1
 * 3
 * 2
 * 2
 * 3
 * -----------------
 */
public class BOJ14438 {

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

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg) return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null) return;
            din.close();
        }
    }

    private static class SegTree {
        private long[] nodes;
        private int leafCount;

        public SegTree(long[] base) {
            init(base);
        }

        public void init(long[] base) {
            leafCount = 1;
            while (leafCount < base.length) leafCount <<= 1;

            nodes = new long[leafCount << 1];
            Arrays.fill(nodes, Integer.MAX_VALUE);
            System.arraycopy(base, 0, nodes, leafCount, base.length);

            for (int i = leafCount - 1; i > 0; i--) {
                int leftChild = i << 1;
                int rightChild = (i << 1) + 1;
                nodes[i] = Math.min(nodes[leftChild], nodes[rightChild]);
            }
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return Integer.MAX_VALUE;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, (node << 1), queryLeft, queryRight);
                long rightValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);
                return Math.min(leftValue, rightValue);
            }
        }

        public void update(int node, long value) {
            int idx = leafCount + node - 1;
            nodes[idx] = value;

            while (idx > 0) {
                int opponent = (idx & 1) == 0 ? idx + 1 : idx - 1;
                int parent = idx >> 1;
                nodes[parent] = Math.min(nodes[idx], nodes[opponent]);
                idx = parent;
            }
        }

        public long getLeafValue(int idx) {
            return nodes[leafCount + idx - 1];
        }
    }

    static int N, M;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        long[] nums = new long[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        SegTree segTree = new SegTree(nums);
        M = r.nextInt();
        for (int i = 0; i < M; i++) {
            int q = r.nextInt();
            int a = r.nextInt();
            int b = r.nextInt();

            if (q == 1) segTree.update(a, b);
            else {
                result.append(segTree.query(1, segTree.leafCount, 1, a, b));
                if (i < M - 1) result.append('\n');
            }
        }
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}