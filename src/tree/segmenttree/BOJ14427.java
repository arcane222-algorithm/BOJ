package tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 수열과 쿼리 15 - BOJ14427
 * -----------------
 * category: data structure (자료구조)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 5
 * 5 4 3 2 1
 * 5
 * 2
 * 1 5 3
 * 2
 * 1 4 3
 * 2
 *
 * Output 1
 * 5
 * 4
 * 3
 * -----------------
 */
public class BOJ14427 {

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
        private int[] nodes, idxes;
        private int leafCount;

        public SegTree(int[] base) {
            init(base);
        }

        public void init(int[] base) {
            leafCount = 1;
            while (leafCount < base.length) leafCount <<= 1;

            nodes = new int[leafCount << 1];
            idxes = new int[leafCount << 1];
            Arrays.fill(nodes, Integer.MAX_VALUE);
            Arrays.fill(idxes, Integer.MAX_VALUE);
            for (int i = 0; i < base.length; i++) {
                nodes[leafCount + i] = base[i];
                idxes[leafCount + i] = i + 1;
            }

            for (int i = leafCount - 1; i > 0; i--) {
                int leftChildIdx = i << 1;
                int rightChildIdx = (i << 1) + 1;

                if (nodes[leftChildIdx] < nodes[rightChildIdx]) {
                    nodes[i] = nodes[leftChildIdx];
                    idxes[i] = idxes[leftChildIdx];
                } else if (nodes[leftChildIdx] > nodes[rightChildIdx]) {
                    nodes[i] = nodes[rightChildIdx];
                    idxes[i] = idxes[rightChildIdx];
                } else {
                    nodes[i] = nodes[leftChildIdx];
                    idxes[i] = Math.min(idxes[leftChildIdx], idxes[rightChildIdx]);
                }
            }
        }

        public void update(int node, int value) {
            int curr = leafCount + node - 1;
            nodes[curr] = value;

            while (curr > 0) {
                int opponent = (curr & 1) == 0 ? curr + 1 : curr - 1;
                int parent = curr >> 1;

                if (nodes[curr] < nodes[opponent]) {
                    nodes[parent] = nodes[curr];
                    idxes[parent] = idxes[curr];
                } else if (nodes[curr] > nodes[opponent]) {
                    nodes[parent] = nodes[opponent];
                    idxes[parent] = idxes[opponent];
                } else {
                    nodes[parent] = nodes[curr];
                    idxes[parent] = Math.min(idxes[curr], idxes[opponent]);
                }
                curr = parent;
            }
        }
    }

    static int N, M;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = r.nextInt();
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
        SegTree segTree = new SegTree(nums);

        M = r.nextInt();
        for (int i = 0; i < M; i++) {
            int q = r.nextInt();

            if (q == 1) {
                int a = r.nextInt();
                int b = r.nextInt();
                segTree.update(a, b);
            } else {
                result.append(segTree.idxes[1]);
                if (i < M - 1) result.append('\n');
            }
        }
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}