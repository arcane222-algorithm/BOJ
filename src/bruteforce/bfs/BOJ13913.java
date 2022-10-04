package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 숨바꼭질 4 - BOJ13913
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * -----------------
 * Input 1
 * 5 17
 *
 * Output 1
 * 4
 * 5 4 8 16 17
 * -----------------
 */
public class BOJ13913 {

    private static class Node {
        int pos;
        Node prev;

        public Node(int pos, Node prev) {
            this.pos = pos;
            this.prev = prev;
        }
    }

    static final int MAX_SIZE = (int) 1e5 + 1;

    static int N, K;
    static boolean[] visited;
    static StringBuilder result = new StringBuilder();

    public static boolean canGo(int pos) {
        return 0 <= pos && pos <= MAX_SIZE - 1;
    }

    public static Node next(int idx, Node curr) {
        int nxt = curr.pos;
        switch (idx) {
            case 0:
                nxt += 1;
                break;
            case 1:
                nxt -= 1;
                break;
            case 2:
                nxt <<= 1;
                break;
            default:
                assert false;
        }

        if (canGo(nxt) && !visited[nxt]) return new Node(nxt, curr);
        return null;
    }

    public static Stack<Node> bfs() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(N, null));
        visited[N] = true;

        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            if (curr.pos == K) {
                Stack<Node> stack = new Stack<>();
                while(curr != null) {
                    stack.add(curr);
                    curr = curr.prev;
                }
                return stack;
            }

            for (int i = 0; i < 3; i++) {
                Node nxt = next(i, curr);
                if (nxt != null) {
                    queue.add(nxt);
                    visited[nxt.pos] = true;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        visited = new boolean[MAX_SIZE];

        Stack<Node> stack = bfs();
        assert stack != null;

        result.append(stack.size() - 1).append('\n');
        while(!stack.isEmpty()) {
            result.append(stack.pop().pos).append(' ');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
