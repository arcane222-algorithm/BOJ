package slidingwindow;

import java.io.*;
import java.util.*;


/**
 * 블로그 - BOJ21921
 * -----------------
 * category: prefix sum (누적합)
 *           sliding window (슬라이딩 윈도우)
 * -----------------
 * Input 1
 * 5 2
 * 1 4 2 5 1
 *
 * Output 1
 * 7
 * 1
 * -----------------
 * Input 2
 * 7 5
 * 1 1 1 1 1 5 1
 *
 * Output 2
 * 9
 * 2
 * -----------------
 * Input 3
 * 5 3
 * 0 0 0 0 0
 *
 * Output 3
 * SAD
 * -----------------
 */
public class BOJ21921 {

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

    static int N, X;
    static int[] nums;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        X = r.nextInt();
        nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }

        int max, sum, count;
        max = sum = 0;
        count = 1;

        for (int i = 0; i < X; i++) {
            sum += nums[i];
        }
        max = sum;

        for (int i = 1; i <= N - X; i++) {
            sum -= nums[i - 1];
            sum += nums[i + X - 1];

            if (sum > max) {
                max = sum;
                count = 1;
            } else if (sum == max) {
                count++;
            }
        }

        if (max == 0) {
            bw.write("SAD");
        } else {
            bw.write(max + "\n");
            bw.write(count + "");
        }

        // close the buffer
        r.close();
        bw.close();
    }
}