package bitmasking;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 집합 - BOJ11723
 * -----------------
 * category: implementation (구현)
 *           bit-masking (비트마스킹)
 * -----------------
 * Input 1
 * 26
 * add 1
 * add 2
 * check 1
 * check 2
 * check 3
 * remove 2
 * check 1
 * check 2
 * toggle 3
 * check 1
 * check 2
 * check 3
 * check 4
 * all
 * check 10
 * check 20
 * toggle 10
 * remove 20
 * check 10
 * check 20
 * empty
 * check 1
 * toggle 1
 * check 1
 * toggle 1
 * check 1
 *
 * Output 1
 * 1
 * 1
 * 0
 * 1
 * 0
 * 1
 * 0
 * 1
 * 0
 * 1
 * 1
 * 0
 * 0
 * 0
 * 1
 * 0
 * -----------------
 */
public class BOJ11723 {
    private static class BitSet {
        private final int FULL_SET = 0xfffff;   // 0b 1111 1111 1111 1111 1111
        private final int EMPTY_SET = 0x00000;  // 0b 0000 0000 0000 0000 0000

        int elements;

        public void add(int x) {
            int mask = 1 << (x - 1);
            elements = elements | mask;
        }

        public void remove(int x) {
            int mask = ~(1 << (x - 1));
            elements = elements & mask;
        }

        public int check(int x) {
            int mask = 1 << (x - 1);
            return (elements & mask) > 0 ? 1 : 0;
        }

        public void toggle(int x) {
            int mask = 1 << (x - 1);    // 1001 0001  xor (^)  0001 0000
            elements = elements ^ mask;
        }

        public void all() {
            elements = FULL_SET;
        }

        public void empty() {
            elements = EMPTY_SET;
        }

        @Override
        public String toString() {
            return Integer.toBinaryString(elements);
        }
    }

    static int M;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(System.out));
        StringTokenizer st;
        StringBuilder result = new StringBuilder();

        BitSet set = new BitSet();
        M = Integer.parseInt(br.readLine());
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            switch(st.nextToken()) {
                case "add":
                    set.add(Integer.parseInt(st.nextToken()));
                    break;

                case "remove":
                    set.remove(Integer.parseInt(st.nextToken()));
                    break;

                case "check":
                    result.append(set.check(Integer.parseInt(st.nextToken())));
                    result.append('\n');
                    break;

                case "toggle":
                    set.toggle(Integer.parseInt(st.nextToken()));
                    break;

                case "all":
                    set.all();
                    break;

                case "empty":
                    set.empty();
                    break;
            }
        }

        // write the result & close the i/o stream
        bw.write(result.toString());
        br.close();
        bw.close();
    }
}

