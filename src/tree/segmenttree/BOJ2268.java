package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 수들의 합 7 - BOJ2268
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 3 5
 * 0 1 3
 * 1 1 2
 * 1 2 3
 * 0 2 3
 * 0 1 3
 *
 * Output 1
 * 0
 * 3
 * 5
 * -----------------
 */
public class BOJ2268 {

    private static class Reader {
        private final int BUFFER_SIZE = 1 << 16;
        private final byte[] buffer;
        private final DataInputStream din;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();

            boolean positive = c == '+';
            boolean negative = c == '-';
            if (positive || negative)
                c = read();

            do {
                ret = ret * 10 + (c - '0');
                c = read();
            } while (c >= '0' && c <= '9');

            if (negative) return -ret;
            else return ret;
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
            if (din == null) return;
            din.close();
        }
    }

    private static class SegTree {
        private final long[] nodes;
        private int leafSize;

        public SegTree(int size) {
            leafSize = 1;
            while (leafSize < size)
                leafSize <<= 1;

            nodes = new long[leafSize << 1];
        }

        public void update(int i, long k) {
            int currIdx = leafSize + i - 1;
            nodes[currIdx] = k;

            while (currIdx > 0) {
                int oppositeIdx = (currIdx & 1) == 0 ? currIdx + 1 : currIdx - 1;
                int parentIdx = currIdx >> 1;
                nodes[parentIdx] = nodes[currIdx] + nodes[oppositeIdx];
                currIdx = parentIdx;
            }
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, (node << 1), queryLeft, queryRight);
                long rightValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);
                return leftValue + rightValue;
            }
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
        M = r.nextInt();
        SegTree segTree = new SegTree(N);
        for (int i = 0; i < M; i++) {
            int q = r.nextInt();
            int a = r.nextInt();
            int b = r.nextInt();

            switch (q) {
                case 0:
                    if (a > b) {
                        int tmp = a;
                        a = b;
                        b = tmp;
                    }
                    result.append(segTree.query(1, segTree.leafSize, 1, a, b)).append('\n');
                    break;

                case 1:
                    segTree.update(a, b);
                    break;
            }
        }
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}