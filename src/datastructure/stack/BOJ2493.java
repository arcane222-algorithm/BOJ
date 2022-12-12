package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 탑 - BOJ2493
 * -----------------
 * category: data structure (자료 구조)
 *           stack (스택)
 * -----------------
 * Input 1
 * 5
 * 6 9 5 7 4
 *
 * Output 1
 * 0 0 2 2 4
 * -----------------
 */
public class BOJ2493 {

    private static class Reader {
        private static final int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return idx == pair.idx && value == pair.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(idx, value);
        }
    }

    static int N;
    static int[] nums, idxes;
    static Stack<Pair> monotoneStack;
    static StringBuilder result;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        nums = new int[N];
        idxes = new int[N];
        monotoneStack = new Stack<>();
        result = new StringBuilder();

        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
    }

    public static void getResult() {
        for (int i = N - 1; i >= 0; i--) {
            int value = nums[i];
            while (!monotoneStack.isEmpty()) {
                Pair last = monotoneStack.peek();
                if (value >= last.value) {
                    monotoneStack.pop();
                    idxes[last.idx] = i + 1;
                } else break;
            }
            monotoneStack.add(new Pair(i, value));
        }

        for(int idx : idxes) {
            result.append(idx).append(' ');
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        init(r);
        getResult();
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}
