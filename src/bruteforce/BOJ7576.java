package bruteforce;

import javafx.util.Pair;

import java.io.*;
import java.util.*;


/**
 * 토마토 - BOJ7576
 * -----------------
 * Input 1
 * 6 4
 * 0 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 1
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 6 4
 * 0 -1 0 0 0 0
 * -1 0 0 0 0 0
 * 0 0 0 0 0 0
 * 0 0 0 0 0 1
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 6 4
 * 1 -1 0 0 0 0
 * 0 -1 0 0 0 0
 * 0 0 0 0 -1 0
 * 0 0 0 0 -1 1
 *
 * Output 3
 * 6
 * -----------------
 * Input 4
 * 5 5
 * -1 1 0 0 0
 * 0 -1 -1 -1 0
 * 0 -1 -1 -1 0
 * 0 -1 -1 -1 0
 * 0 0 0 0 0
 *
 * Output 4
 * 14
 * -----------------
 * Input 5
 * 2 2
 * 1 -1
 * -1 1
 *
 * Output 5
 * 0
 * -----------------
 */
public class BOJ7576 {

    static final int[] dirX = {1, -1, 0, 0};
    static final int[] dirY = {0, 0, 1, -1};

    static int M, N, count, totalCount, time;
    static int[][] box;
    static Queue<Pair<Integer, Integer>> queue = new LinkedList<>();

    public static boolean canGo(int x, int y) {
        if (x < 0 || x > M - 1) return false;
        if (y < 0 || y > N - 1) return false;
        if (box[y][x] != 0) return false;
        return true;
    }

    public static int bfs() {
        int result = 0;

        while (!queue.isEmpty()) {
            final int size = queue.size();
            for (int i = 0; i < size; i++) {
                Pair<Integer, Integer> node = queue.poll();
                int x = node.getValue();
                int y = node.getKey();

                for (int j = 0; j < dirX.length; j++) {
                    int nxtX = x + dirX[j];
                    int nxtY = y + dirY[j];
                    if (canGo(nxtX, nxtY)) {
                        box[nxtY][nxtX] = 1;
                        count++;
                        queue.add(new Pair<>(nxtY, nxtX));
                    }
                }

                if (queue.isEmpty()) break;
            }
            result++;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        box = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int num = Integer.parseInt(st.nextToken());
                box[i][j] = num;
                if (num > -1) {
                    if (num == 1) {
                        queue.add(new Pair<>(i, j));
                        count++;
                    }
                    totalCount++;
                }
            }
        }

        int result = bfs();
        bw.write(String.valueOf(count == totalCount ? result - 1 : -1));


        // close the buffer
        br.close();
        bw.close();
    }
}