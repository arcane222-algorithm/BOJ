package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 비숍 - BOJ1799
 * -----------------
 * category: brute force (브루트포스 알고리즘)
 *           back tracking (백트래킹)
 *
 * Time-Complexity: O(2 * 2^((N/2)^2)) => O(2^((N/2)^2))
 * -----------------
 *
 * -----------------
 * Input 1
 * 5
 * 1 1 0 1 1
 * 0 1 0 0 0
 * 1 0 1 0 1
 * 1 0 0 0 0
 * 1 0 1 1 1
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ1799 {
    private static class Vec2 {
        int x, y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static final int BLACK = 0, WHITE = 1;

    static int N;
    static int[][][] board;
    static int[] count;
    static LinkedList<Vec2>[] boards;
    static int[] xStack, yStack;

    public static boolean canGo(Vec2 v, int depth) {
        for (int i = 0; i < depth; i++) {
            if (Math.abs(v.x - xStack[i]) == Math.abs(v.y - yStack[i])) return false;
        }
        return true;
    }

    public static void dfs(LinkedList<Vec2> board, int type, int depth, int parent) {
        count[type] = Math.max(count[type], depth);

        for (int i = parent; i < board.size(); i++) {
            Vec2 v = board.get(i);
            if (canGo(v, depth)) {
                xStack[depth] = v.x;
                yStack[depth] = v.y;
                dfs(board, type, depth + 1, i);
                xStack[depth] = -1;
                yStack[depth] = -1;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        boards = new LinkedList[2];
        count = new int[2];
        xStack = new int[100];
        yStack = new int[100];
        for (int i = 0; i < 2; i++) {
            boards[i] = new LinkedList<>();
        }
        for (int i = 0; i < 100; i++) {
            xStack[i] = yStack[i] = -1;
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int type = (i + j) & 1;
                if (st.nextToken().charAt(0) == '1') {
                    boards[type].add(new Vec2(j, i));
                }
            }
        }

        dfs(boards[BLACK], BLACK, 0, 0);
        dfs(boards[WHITE], WHITE, 0, 0);
        bw.write(String.valueOf(count[BLACK] + count[WHITE]));

        // close the buffer
        br.close();
        bw.close();
    }
}
