package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 수열과 쿼리 37 - BOJ18436
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 6
 * 1 2 3 4 5 6
 * 4
 * 2 2 5
 * 3 1 4
 * 1 5 4
 * 2 1 6
 *
 * Output 1
 * 2
 * 2
 * 4
 * -----------------
 */
public class BOJ18436 {

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
        private boolean isOdd;

        public SegTree(int[] base, boolean isOdd) {
            this.isOdd = isOdd;
            init(base);
        }

        private void init(int[] base) {
            leafCount = 1;
            while (leafCount < base.length)
                leafCount <<= 1;

            nodes = new int[leafCount << 1];
            for (int i = 0; i < base.length; i++) {
                if (isOdd) {
                    if ((base[i] & 1) == 1)
                        nodes[leafCount + i] = 1;
                } else {
                    if ((base[i] & 1) == 0) {
                        nodes[leafCount + i] = 1;
                    }
                }
            }

            for (int i = leafCount - 1; i > 0; i--) {
                int left = i << 1;
                int right = (i << 1) + 1;
                nodes[i] = nodes[left] + nodes[right];
            }
        }

        public int query(int left, int right, int node, int queryLeft, int queryRight) {
            if (queryLeft > right || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                int leftValue = query(left, mid, node << 1, queryLeft, queryRight);
                int rightValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);

                return leftValue + rightValue;
            }
        }

        public void update(int node, int value) {
            int idx = leafCount + node - 1;
            if (isOdd) {
                nodes[idx] = (value & 1) == 1 ? 1 : 0;
            } else {
                nodes[idx] = (value & 1) == 0 ? 1 : 0;
            }

            while (idx > 0) {
                int opponent = (idx & 1) == 0 ? idx + 1 : idx - 1;
                int parent = idx >> 1;
                nodes[parent] = nodes[idx] + nodes[opponent];
                idx = parent;
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

        N = r.nextInt();
        nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
        SegTree oddSegTree = new SegTree(nums, true);
        SegTree evenSegTree = new SegTree(nums, false);

        M = r.nextInt();
        for (int i = 0; i < M; i++) {
            int a = r.nextInt();
            int b = r.nextInt();
            int c = r.nextInt();

            switch (a) {
                case 1:
                    oddSegTree.update(b, c);
                    evenSegTree.update(b, c);
                    break;

                case 2:
                    result.append(evenSegTree.query(1, evenSegTree.leafCount, 1, b, c)).append('\n');
                    break;

                case 3:
                    result.append(oddSegTree.query(1, oddSegTree.leafCount, 1, b, c)).append('\n');
                    break;
            }
        }
        bw.write(result.toString());

        // close the buffer
        bw.close();
    }
}