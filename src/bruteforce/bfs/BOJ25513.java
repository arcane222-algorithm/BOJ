package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 빠른 오름차순 숫자 탐색 - BOJ25513
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비 우선 탐색)
 * -----------------
 * Input 1
 * 0 0 1 0 0
 * 0 0 2 0 0
 * 0 0 3 0 0
 * 0 0 4 0 0
 * 0 0 5 6 -1
 * 0 1
 *
 * Output 1
 * 6
 * -----------------
 * Input 2
 * 0 0 1 0 0
 * 0 0 2 0 0
 * 0 0 3 0 0
 * 0 0 4 6 0
 * 0 0 5 -1 0
 * 0 1
 *
 * Output 2
 * 7
 * -----------------
 * Input 3
 * 0 0 -1 1 0
 * 0 0 -1 2 0
 * 0 0 -1 3 0
 * 0 0 -1 4 0
 * 0 0 -1 5 6
 * 0 1
 *
 * Output 3
 * -1
 * -----------------
 * Input 4
 * 0 0 1 0 0
 * 0 -1 2 0 0
 * 0 -1 3 0 0
 * 0 -1 4 -1 0
 * 0 -1 5 -1 6
 * 0 1
 *
 * Output 4
 * 11
 * -----------------
 */
public class BOJ25513 {

    private static class Node {
        int x, y, dist;

        public Node(int y, int x, int dist) {
            this.y = y;
            this.x = x;
            this.dist = dist;
        }
    }

    static final int W = 5, H = 5, DEST_NUM = 6;
    static final int[] dirX = {-1, 1, 0, 0};
    static final int[] dirY = {0, 0, -1, 1};

    static int[][] map = new int[H][W];
    static boolean[][][] visited = new boolean[DEST_NUM][H][W];
    static int[][] dest = new int[DEST_NUM][2];
    static int R, C;

    public static boolean canGo(int y, int x) {
        if (y < 0 || y > H - 1) return false;
        if (x < 0 || x > W - 1) return false;
        return map[y][x] != -1;
    }

    public static int bfs() {
        Queue<Node> queue = new LinkedList<>();
        int distSum = 0;

        for (int floor = 0; floor < DEST_NUM; floor++) {
            queue.clear();
            queue.add(new Node(R, C, 0));
            visited[floor][R][C] = true;

            boolean findPath = false;
            while (!queue.isEmpty()) {
                Node curr = queue.poll();

                if (curr.y == dest[floor][0] && curr.x == dest[floor][1]) {
                    distSum += curr.dist;
                    R = curr.y;
                    C = curr.x;
                    findPath = true;
                    break;
                }

                for (int i = 0; i < dirX.length; i++) {
                    int nxtX = curr.x + dirX[i];
                    int nxtY = curr.y + dirY[i];
                    if (!canGo(nxtY, nxtX)) continue;

                    if (!visited[floor][nxtY][nxtX]) {
                        Node nxt = new Node(nxtY, nxtX, curr.dist + 1);
                        queue.add(nxt);
                        visited[floor][nxtY][nxtX] = true;
                    }
                }
            }

            if (!findPath) return -1;
        }

        return distSum;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        for (int i = 0; i < H; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < W; j++) {
                int val = Integer.parseInt(st.nextToken());
                if (val > 0) {
                    dest[val - 1][0] = i;   // y
                    dest[val - 1][1] = j;   // x
                }
                map[i][j] = val;
            }
        }
        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        bw.write(String.valueOf(bfs()));

        // close the buffer
        br.close();
        bw.close();
    }
}