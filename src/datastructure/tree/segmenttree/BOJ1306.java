package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 달려라 홍준 - BOJ1275
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 5 2
 * 1 1 1 3 2
 *
 * Output 1
 * 1 3 3
 * -----------------
 */
public class BOJ1306 {

    private static class Reader {
        private static final int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;

        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
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
            din.close();
        }

        public int nextInt() throws IOException {
            int result = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }

            boolean positive = c == '+';
            boolean negative = c == '-';
            if (positive || negative) {
                c = read();
            }

            do {
                result = 10 * result + (c - '0');
                c = read();
            } while (c >= '0' && c <= '9');

            if (negative) return -result;
            else return result;
        }
    }

    private static class SegmentTree {
        private final int[] nodes;
        private int leafCount;

        public SegmentTree(int[] base) {
            leafCount = 1;
            while (leafCount < base.length)
                leafCount <<= 1;

            nodes = new int[leafCount << 1];
            init(base);
        }

        private void init(int[] base) {
            System.arraycopy(base, 0, nodes, leafCount, base.length);
            for (int i = leafCount - 1; i > 0; i--) {
                int lChild = nodes[i << 1];
                int rChild = nodes[(i << 1) + 1];
                nodes[i] = Math.max(lChild, rChild);
            }
        }

        public int query(int left, int right, int node, int queryLeft, int queryRight) {
            if (right < queryLeft || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                int lVal = query(left, mid, node << 1, queryLeft, queryRight);
                int rVal = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);
                return Math.max(lVal, rVal);
            }
        }
    }

    static int N, M;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        M = r.nextInt();
        int[] lights = new int[N];
        for (int i = 0; i < N; i++) {
            lights[i] = r.nextInt();
        }

        SegmentTree segmentTree = new SegmentTree(lights);
        for (int i = M; i <= (N - M + 1); i++) {
            int qLeft = i - M + 1;
            int qRight = i + M - 1;
            bw.write(String.valueOf(segmentTree.query(1, segmentTree.leafCount, 1, qLeft, qRight)));
            if(i < N - M + 1) bw.write('\n');
        }

        // close the buffer
        r.close();
        bw.close();
    }
}