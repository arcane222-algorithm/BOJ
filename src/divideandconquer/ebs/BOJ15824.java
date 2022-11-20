package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 너 봄에는 캡사이신이 맛있단다 - BO15824
 * -----------------
 * category: mathematics (수학)
 *           sorting (정렬)
 *           combinatorics (조합론)
 *           exponentiation by squaring (분할 정복을 이용한 거듭제곱)
 * -----------------
 * Input 1
 * 3
 * 5 2 8
 *
 * Output 1
 * 18
 * -----------------
 * Input 2
 * 6
 * 1 4 5 5 6 10
 *
 * Output 2
 * 307
 * -----------------
 */
public class BOJ15824 {

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

    static final int MOD = (int) (1e9 + 7);
    static int N;

    public static long fastPow(long a, long n) {
        long result = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                result = result * a % MOD;
            }
            a = a * a % MOD;
            n >>= 1;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        long[] nums = new long[N];
        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
        Arrays.sort(nums);

        long result = 0;
        for (int i = 0; i < N; i++) {
            result += (nums[i] % MOD) * ((fastPow(2, i) - fastPow(2, N - i - 1) + MOD) % MOD);
            result %= MOD;
        }
        bw.write(String.valueOf(result));

        // close the buffer
        bw.close();
    }
}