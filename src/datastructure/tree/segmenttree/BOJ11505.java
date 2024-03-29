package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 구간 곱 구하기 - BOJ11505
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5 2 2
 * 1
 * 2
 * 3
 * 4
 * 5
 * 1 3 6
 * 2 2 5
 * 1 5 2
 * 2 3 5
 *
 * Output 1
 * 240
 * 48
 * -----------------
 * Input 2
 * 5 2 2
 * 1
 * 2
 * 3
 * 4
 * 5
 * 1 3 0
 * 2 2 5
 * 1 3 6
 * 2 2 5
 *
 * Output 2
 * 0
 * 240
 * -----------------
 * Input 3
 * 4 1 2
 * 1000000
 * 1000000
 * 1000000
 * 1000000
 * 2 1 4
 * 1 2 1
 * 2 1 4
 *
 * Output 3
 * 49000000
 * 49
 * -----------------
 */
public class BOJ11505 {

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

    private static class SegTree {
        private long[] nodes;
        private int leafCount;

        public SegTree(long[] base) {
            init(base);
        }

        private void init(long[] base) {
            leafCount = 1;
            while (leafCount < base.length)
                leafCount <<= 1;

            nodes = new long[leafCount << 1];
            Arrays.fill(nodes, 1);
            System.arraycopy(base, 0, nodes, leafCount, base.length);

            for (int i = leafCount - 1; i > 0; i--) {
                int left = i << 1;
                int right = (i << 1) + 1;
                nodes[i] = (nodes[left] % MOD * nodes[right] % MOD) % MOD;
            }
        }

        public void update(int node, long value) {
            int idx = leafCount + node - 1;
            nodes[idx] = value;

            while (idx > 0) {
                int opponent = (idx & 1) == 0 ? idx + 1 : idx - 1;
                int parent = idx >> 1;
                nodes[parent] = (nodes[idx] % MOD) * (nodes[opponent] % MOD) % MOD;
                idx = parent;
            }
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return 1;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node] % MOD;
            } else {
                int mid = (left + right) >> 1;
                long leftValue = query(left, mid, node << 1, queryLeft, queryRight);
                long rightValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);

                return (leftValue % MOD) * (rightValue % MOD) % MOD;
            }
        }
    }

    static final int MOD = (int) (1e9 + 7);
    static int N, M, K;
    static long[] nums;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        M = r.nextInt();
        K = r.nextInt();

        nums = new long[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        SegTree segTree = new SegTree(nums);
        for (int i = 0; i < M + K; i++) {
            int a = r.nextInt();
            int b = r.nextInt();
            int c = r.nextInt();

            switch (a) {
                case 1:
                    segTree.update(b, c);
                    break;

                case 2:
                    bw.write(String.valueOf(segTree.query(1, segTree.leafCount, 1, b, c)));
                    bw.write('\n');
                    break;
            }
        }


        // close the buffer
        br.close();
        bw.close();
    }
}