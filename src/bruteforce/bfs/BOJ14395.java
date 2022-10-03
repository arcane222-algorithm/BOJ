package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 4연산 - BOJ14395
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * -----------------
 * Input 1
 * 7 392
 *
 * Output 1
 * +*+
 * -----------------
 * Input 2
 * 7 256
 *
 * Output 2
 * /+***
 * -----------------
 * Input 3
 * 4 256
 *
 * Output 3
 * **
 * -----------------
 * Input 4
 * 7 7
 *
 * Output 4
 * 0
 * -----------------
 * Input 5
 * 7 9
 *
 * Output 5
 * -1
 * -----------------
 * Input 6
 * 10 1
 *
 * Output 6
 * /
 * -----------------
 */
public class BOJ14395 {

    private static class Node {
        long value;
        String operations;

        public Node(long value, String operations) {
            this.value = value;
            this.operations = operations;
        }
    }

    static final int MAX_VALUE = (int) 1e9;
    static final char[] operations = {'*', '+', '-', '/'};
    static long s, t;
    static HashSet<Long> visited;

    public static long calculation(int type, long value) {
        switch (type) {
            case 0:
                return value * value;
            case 1:
                return value << 1;
            case 2:
                return 0;
            case 3:
                return 1;
            default:
                assert false;
        }
        return 0;
    }

    public static boolean canGo(long value) {
        return 0 <= value && value <= MAX_VALUE;
    }

    public static String bfs() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(s, ""));
        visited.add(s);
        String result = null;

        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            if (curr.value == t) {
                if (result == null) result = curr.operations;
                else result = curr.operations.compareTo(result) < 0 ? curr.operations : result;
            }

            for (int i = 0; i < operations.length; i++) {
                if (i == 3 && curr.value == 0) continue;
                long res = calculation(i, curr.value);

                if (canGo(res) && !visited.contains(res)) {
                    visited.add(res);
                    queue.add(new Node(res, curr.operations + operations[i]));
                }
            }
        }

        if(result == null) {
            result = "-1";
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        s = Long.parseLong(st.nextToken());
        t = Long.parseLong(st.nextToken());
        visited = new HashSet<>();

        if (s == t) bw.write('0');
        else bw.write(bfs());

        // close the buffer
        br.close();
        bw.close();
    }
}