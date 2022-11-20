package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 최솟값과 최댓값 - BOJ2357
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
 * 5 100
 * 38 100
 * 20 81
 * 5 81
 * -----------------
 */
public class BOJ2357 {

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
        private int[] nodes;
        private int leafCount;
        private boolean isMin;

        public SegTree(int[] base, boolean isMin) {
            this.isMin = isMin;
            init(base);
        }

        private void init(int[] base) {
            leafCount = 1;
            while (leafCount < base.length)
                leafCount <<= 1;

            nodes = new int[leafCount << 1];
            Arrays.fill(nodes, isMin ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            System.arraycopy(base, 0, nodes, leafCount, base.length);

            for (int i = leafCount - 1; i > 0; i--) {
                int left = i << 1;
                int right = (i << 1) + 1;
                nodes[i] = isMin ? Math.min(nodes[left], nodes[right]) : Math.max(nodes[left], nodes[right]);
            }

            //System.out.println(Arrays.toString(nodes));
        }

        public int query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return isMin ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                int leftValue = query(left, mid, node << 1, queryLeft, queryRight);
                int rightValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);

                return isMin ? Math.min(leftValue, rightValue) : Math.max(leftValue, rightValue);
            }
        }
    }

    static int N, M;
    static int[] nums;
    static StringBuilder result = new StringBuilder();


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        M = r.nextInt();
        nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        SegTree minSegTree = new SegTree(nums, true);
        SegTree maxSegTree = new SegTree(nums, false);

        for (int i = 0; i < M; i++) {
            int a = r.nextInt();
            int b = r.nextInt();
            int min = minSegTree.query(1, minSegTree.leafCount, 1, a, b);
            int max = maxSegTree.query(1, maxSegTree.leafCount, 1, a, b);
            result.append(min).append(' ').append(max).append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        bw.close();
    }
}