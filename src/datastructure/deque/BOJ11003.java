package datastructure.deque;

import java.io.*;
import java.util.*;


/**
 * 최솟값 찾기 - BOJ11003
 * -----------------
 * category: data structure (자료 구조)
 *           deque (덱)
 * -----------------
 * Input 1
 * 12 3
 * 1 5 2 3 6 2 3 7 3 5 2 6
 *
 * Output 1
 * 1 1 1 2 2 2 2 2 3 3 2 2
 * -----------------
 */
public class BOJ11003 {

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

    static int N, L;
    static StringBuilder result = new StringBuilder();


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = r.nextInt();
        L = r.nextInt();
        Deque<Pair> deque = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            int value = r.nextInt();

            // 덱의 맨 뒤부터 조사하여 현재 넣어야 하는 값보다 같거나 더 큰 값이 있으면 전부 제거
            while (!deque.isEmpty()) {
                Pair last = deque.peekLast();
                if (last.value >= value)
                    deque.removeLast();
                else break;
            }
            deque.add(new Pair(i, value));

            // 덱의 맨 앞부터 조사하여 현재 찾으려는 구간 [i - L + 1, i] 외의 값이 있다면 전부 제거
            while (!deque.isEmpty()) {
                Pair first = deque.peekFirst();
                if (first.idx < i - L + 1 || first.idx > i)
                    deque.removeFirst();
                else break;
            }

            // 위 과정을 거치면 모노톤 큐 (monotone queue)의 맨 앞의 값이 구간에서의 가장 작은 값이 됨
            if (!deque.isEmpty())
                result.append(deque.peekFirst().value).append(' ');
        }
        bw.write(result.toString());

        // close the buffer
        r.close();
        bw.close();
    }
}