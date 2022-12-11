package datastructure.deque;

import java.io.*;
import java.util.*;


/**
 * 수 고르기 - BOJ3988
 * -----------------
 * category: data structure (자료 구조)
 *           deque (덱)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 5 2
 * -3 -2 3 8 6
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 6 2
 * -5 8 10 1 13 -1
 *
 * Output 2
 * 13
 * -----------------
 * Input 3
 * 6 3
 * 10 2 8 17 2 17
 *
 * Output 3
 * 6
 * -----------------
 * Input 4
 * 7 3
 * 1 5 10 15 16 17 19
 *
 * Output 4
 * 5
 * -----------------
 */
public class BOJ3988 {

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

    private static class Pair {
        int idx, value;

        public Pair(int idx, int value) {
            this.idx = idx;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair p = (Pair) o;
                return idx == p.idx && value == p.value;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * idx + value;
        }
    }

    static int N, K;
    static int[] nums;
    static Deque<Pair> monotoneQueue;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        K = r.nextInt();
        nums = new int[N];
        monotoneQueue = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
        Arrays.sort(nums);
    }

    public static int slidingWindow() {
        final int windowSize = N - K;
        int m, M;

        for (int i = 0; i < windowSize; i++) {
            int value = nums[i + 1] - nums[i];

            while (!monotoneQueue.isEmpty()) {
                Pair last = monotoneQueue.peekLast();
                if (last.value >= value)
                    monotoneQueue.removeLast();
                else break;
            }
            monotoneQueue.add(new Pair(i, value));
        }

        int min = Integer.MAX_VALUE;
        if (!monotoneQueue.isEmpty()) {
            m = monotoneQueue.peekFirst().value;
            M = nums[windowSize - 1] - nums[0];
            min = Math.min(min, m + M);
        }

        for (int i = 1; i <= N - windowSize; i++) {
            int value = nums[i + windowSize - 1] - nums[i + windowSize - 2];

            while (!monotoneQueue.isEmpty()) {
                Pair last = monotoneQueue.peekLast();
                if (last.value >= value)
                    monotoneQueue.removeLast();
                else break;
            }
            monotoneQueue.add(new Pair(i + windowSize - 2, value));

            while (!monotoneQueue.isEmpty()) {
                Pair first = monotoneQueue.peekFirst();
                if (first.idx < i || first.idx > i + windowSize - 1)
                    monotoneQueue.removeFirst();
                else break;
            }

            if (!monotoneQueue.isEmpty()) {
                m = monotoneQueue.peekFirst().value;
                M = nums[i + windowSize - 1] - nums[i];
                min = Math.min(min, m + M);
            }
        }

        return min;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        init(r);
        int min = slidingWindow();
        bw.write(String.valueOf(min));

        // close the buffer
        r.close();
        bw.close();
    }
}
