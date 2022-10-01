package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 뱀과 사다리 게임 - BOJ16928
 * -----------------
 * category: bfs (너비우선탐색)
 * -----------------
 * -----------------
 * Input 1
 * 3 7
 * 32 62
 * 42 68
 * 12 98
 * 95 13
 * 97 25
 * 93 37
 * 79 27
 * 75 19
 * 49 47
 * 67 17
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 4 9
 * 8 52
 * 6 80
 * 26 42
 * 2 72
 * 51 19
 * 39 11
 * 37 29
 * 81 3
 * 59 5
 * 79 23
 * 53 7
 * 43 33
 * 77 21
 *
 * Output 2
 * 5
 * -----------------
 */
public class BOJ16928 {

    private static class Block {
        int idx, dist;

        public Block(int idx, int dist) {
            this.idx = idx;
            this.dist = dist;
        }
    }

    static int N, M;
    static boolean[] visited;
    static int[] trap;

    public static boolean canGo(int curr) {
        return 1 <= curr && curr <= 100;
    }

    public static int bfs() {
        Queue<Block> queue = new LinkedList<>();
        queue.add(new Block(1, 0));
        visited[1] = true;

        int count = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Block curr = queue.poll();

            if (curr.idx == 100) {
                count = Math.min(count, curr.dist);
            }

            for (int i = 1; i <= 6; i++) {
                int nxt = curr.idx + i;
                if (canGo(nxt)) {
                    if (trap[nxt] != 0) nxt = trap[nxt];
                    if(!visited[nxt]) {
                        queue.add(new Block(nxt, curr.dist + 1));
                        visited[nxt] = true;
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        visited = new boolean[101];
        trap = new int[101];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            trap[from] = to;
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            trap[from] = to;
        }

        int result = bfs();
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}