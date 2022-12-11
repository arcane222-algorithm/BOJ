package datastructure.stack;

import java.io.*;
import java.util.*;


/**
 * 오큰수 - BOJ17298
 * -----------------
 * category: data structure (자료 구조)
 *           stack (스택)
 * -----------------
 * Input 1
 * 4
 * 3 5 2 7
 *
 * Output 1
 * 5 7 7 -1
 * -----------------
 * Input 2
 * 4
 * 9 5 4 8
 *
 * Output 2
 * -1 8 8 -1
 * -----------------
 */
public class BOJ17298 {

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

    static int N;
    static int[] nums;
    static Stack<Integer> monotoneStack, resultStack;
    static StringBuilder result;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        nums = new int[N];
        monotoneStack = new Stack<>();
        resultStack = new Stack<>();
        result = new StringBuilder();

        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        for (int i = N - 1; i >= 0; i--) {
            int value = nums[i];
            while (!monotoneStack.isEmpty()) {
                int last = monotoneStack.peek();
                if (last <= value)
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
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}
