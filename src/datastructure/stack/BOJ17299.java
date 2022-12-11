package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 오등큰수 - BOJ17299
 * -----------------
 * category: data structure (자료 구조)
 *           stack (스택)
 * -----------------
 * Input 1
 * 7
 * 1 1 2 3 4 2 1
 *
 * Output 1
 * -1 -1 1 2 2 1 -1
 * -----------------
 */
public class BOJ17299 {

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

    static final int MAX_RANGE = (int) 1e6;
    static int N;
    static int[] nums, counts;
    static Stack<Integer> monotoneStack, resultStack;
    static StringBuilder result;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        nums = new int[N];
        counts = new int[MAX_RANGE + 1];
        monotoneStack = new Stack<>();
        resultStack = new Stack<>();
        result = new StringBuilder();

        for (int i = 0; i < N; i++) {
            int value = r.nextInt();
            nums[i] = value;
            counts[value]++;
        }
    }

    public static void getNGF() {
        for (int i = N - 1; i >= 0; i--) {
            int value = nums[i];
            while (!monotoneStack.isEmpty()) {
                int last = monotoneStack.peek();
                if (counts[last] <= counts[value])
                    monotoneStack.pop();
                else break;
            }

            resultStack.add(monotoneStack.isEmpty() ? -1 : monotoneStack.peek());
            monotoneStack.add(value);
        }

        while (!resultStack.isEmpty()) {
            result.append(resultStack.pop()).append(' ');
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(r);
        getNGF();
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}