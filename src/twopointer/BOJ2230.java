package twopointer;

import java.io.*;
import java.util.*;


/**
 * 수 고르기 - BOJ2230
 * -----------------
 * category: sorting (정렬)
 *           two-pointer (투 포인터)
 * -----------------
 * Input 1
 * 3 3
 * 1
 * 5
 * 3
 *
 * Output 1
 * 4
 * -----------------
 */
public class BOJ2230 {

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

    static int N, M;
    static int[] nums;

    public static int twoPointerSearch() {
        int lp = 0, rp = 1;
        int result = Integer.MAX_VALUE;

        while(lp < nums.length && rp < nums.length) {
            int gap = nums[rp] - nums[lp];

            if(gap >= M) {
                lp++;
                result = Math.min(result, gap);
            } else {
                rp++;
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = r.nextInt();
        M = r.nextInt();
        nums = new int[N];

        for (int i = 0; i < N; i++) {
            nums[i] = r.nextInt();
        }
        Arrays.sort(nums);
        bw.write(twoPointerSearch() + "");

        // close the buffer
        r.close();
        bw.close();
    }
}