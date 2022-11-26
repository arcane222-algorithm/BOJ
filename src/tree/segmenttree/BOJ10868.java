package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 최솟값 - BOJ10868
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 10 4
 * 75
 * 30
 * 100
 * 38
 * 50
 * 51
 * 52
 * 20
 * 81
 * 5
 * 1 10
 * 3 5
 * 6 9
 * 8 10
 *
 * Output 1
 * 5
 * 38
 * 20
 * 5
 * -----------------
 */
public class BOJ10868 {

    private static class Reader {
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
            din.close();
        }
    }

    private static class SegmentTree {
        private int[] nodes;
        private int leafCount;

        public SegmentTree(int[] base) {
            init(base);
        }

        public void init(int[] base) {
            leafCount = 1;
            while (leafCount < base.length) leafCount <<= 1;

            nodes = new int[leafCount << 1];
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
    }

    static int N, M;

    public static SegmentTree init(Reader r) throws IOException {
        N = r.nextInt();
        M = r.nextInt();
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        return new SegmentTree(nums);
    }

    public static String queryAll(Reader r, SegmentTree segTree) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < M; i++) {
            int a = r.nextInt();
            int b = r.nextInt();
            builder.append(segTree.query(1, segTree.leafCount, 1, a, b)).append('\n');
        }

        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        SegmentTree segTree = init(r);
        bw.write(queryAll(r, segTree));

        // close the buffer
        r.close();
        bw.close();
    }
}