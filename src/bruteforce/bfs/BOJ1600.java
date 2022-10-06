package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 말이 되고픈 원숭이- BOJ1600
 * -----------------
 * -----------------
 * Input 1
 * 1
 * 4 4
 * 0 0 0 0
 * 1 0 0 0
 * 0 0 1 0
 * 0 1 0 0
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 2
 * 5 2
 * 0 0 1 1 0
 * 0 0 1 1 0
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 1
 * 4 4
 * 0 1 1 1
 * 0 0 1 1
 * 1 0 1 1
 * 1 1 1 0
 *
 * Output 3
 * 4
 * -----------------
 */
public class BOJ1600 {

    private static class Node {
        int x, y, dist, jump;

        public Node(int x, int y, int dist, int jump) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.jump = jump;
        }
    }

    static int[] dirHorseX = {-2, -1, 1, 2, -2, -1, 1, 2};
    static int[] dirHorseY = {-1, -2, -2, -1, 1, 2, 2, 1};
    static int[] dirMonkeyX = {0, 0, -1, 1};
    static int[] dirMonkeyY = {-1, 1, 0, 0};

    static int K, W, H;
    static int[][] map;
    static boolean[][][] visited;

    public static boolean canGo(int x, int y) {
        if(x < 0 || x > W - 1) return false;
        if(y < 0 || y > H - 1) return false;
        return map[y][x] != 1;
    }

    public static int bfs() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(0, 0, 0, 0));
        visited[0][0][0] = true;

        int dist = Integer.MAX_VALUE;
        while(!queue.isEmpty()) {
            Node curr = queue.poll();

            if(curr.x == W - 1 && curr.y == H - 1) {
                dist = Math.min(dist, curr.dist);
            }

            int nxtX, nxtY;
            nxtX = nxtY = 0;
            if(curr.jump < K) {
                for(int i = 0; i < dirHorseX.length; i++) {
                    nxtX = curr.x + dirHorseX[i];
                    nxtY = curr.y + dirHorseY[i];
                    if(canGo(nxtX, nxtY) && !visited[nxtY][nxtX][curr.jump + 1]) {
                        queue.add(new Node(nxtX, nxtY, curr.dist + 1, curr.jump + 1));
                        visited[nxtY][nxtX][curr.jump + 1] = true;
                    }
                }
            }

            for(int i = 0; i < dirMonkeyX.length; i++) {
                nxtX = curr.x + dirMonkeyX[i];
                nxtY = curr.y + dirMonkeyY[i];
                if(canGo(nxtX, nxtY) && !visited[nxtY][nxtX][curr.jump]) {
                    queue.add(new Node(nxtX, nxtY, curr.dist + 1, curr.jump));
                    visited[nxtY][nxtX][curr.jump] = true;
                }
            }
        }

        return dist == Integer.MAX_VALUE ? -1 : dist;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        K = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        W = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        map = new int[H][W];
        visited = new boolean[H][W][K + 1];
        for(int i = 0; i < H; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < W; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        bw.write(String.valueOf(bfs()));

        // close the buffer
        br.close();
        bw.close();
    }
}