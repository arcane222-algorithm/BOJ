package datastructure.tree.segmenttree;

import java.io.*;
import java.util.*;


/**
 * 버블 소트 - BOJ1517
 * -----------------
 * category: data structure (자료구조)
 *           sorting (정렬)
 *           segment tree (세그먼트 트리)
 * -----------------
 * Input 1
 * 3
 * 3 2 1
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 4
 * 2 2 2 1
 * Output 2
 * 3
 * -----------------
 */
public class BOJ1517 {

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
        private final long[] nodes;
        private int leafCounts;

        public SegmentTree() {
            leafCounts = 1;
            while (leafCounts < N)
                leafCounts <<= 1;

            nodes = new long[leafCounts << 1];
        }

        public long query(int left, int right, int node, int queryLeft, int queryRight) {
            if (right < queryLeft || queryRight < left) {
                return 0;
            } else if (queryLeft <= left && right <= queryRight) {
                return nodes[node];
            } else {
                int mid = (left + right) >> 1;
                long lValue = query(left, mid, (node << 1), queryLeft, queryRight);
                long rValue = query(mid + 1, right, (node << 1) + 1, queryLeft, queryRight);
                return lValue + rValue;
            }
        }

        public void update(int target, long value) {
            int idx = leafCounts + target - 1;
            nodes[idx] = value;
            while (idx > 0) {
                int opponent = (idx & 1) == 0 ? idx + 1 : idx - 1;
                int parent = idx >> 1;
                nodes[parent] = nodes[idx] + nodes[opponent];
                idx = parent;
            }
        }

        public long getLeafValue(int idx) {
            return nodes[leafCounts + idx - 1];
        }
    }

    static int N;
    static int[] sortedNums, idxes;

    public static long getSwapCount(SegmentTree segmentTree) {
        long result = 0;
        for (int i = 0; i < N; i++) {
            result += segmentTree.query(1, segmentTree.leafCounts, 1, idxes[i], N);
            segmentTree.update(idxes[i], segmentTree.getLeafValue(idxes[i]) + 1);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        sortedNums = new int[N];
        idxes = new int[N];
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int val = r.nextInt();
            if (map.containsKey(val)) {
                map.get(val).add(i + 1);
            } else {
                List<Integer> list = new LinkedList<>();
                list.add(i + 1);
                map.put(val, list);
            }
            sortedNums[i] = val;
        }
        Arrays.sort(sortedNums);

        for (int i = 0; i < N; i++) {
            idxes[i] = map.get(sortedNums[i]).remove(0);
        }

        SegmentTree segmentTree = new SegmentTree();
        bw.write(getSwapCount(segmentTree) + "");

        // close the buffer
        r.close();
        bw.close();
    }
}