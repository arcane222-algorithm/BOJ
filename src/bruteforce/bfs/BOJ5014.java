package bruteforce.bfs;

import java.io.*;
import java.util.*;


/**
 * 스타트링크 - BOJ5014
 * -----------------
 * category: graph traversal (그래프 탐색)
 *           bfs (너비우선탐색)
 * -----------------
 * -----------------
 * Input 1
 * 10 1 10 2 1
 *
 * Output 1
 * 6
 * -----------------
 * Input 2
 * 100 2 1 1 0
 *
 * Output 2
 * use the stairs
 * -----------------
 * Input 3
 * 158 9 70 9 16
 *
 * Output 3
 * 29
 * -----------------
 * Input 4
 * 80 45 32 5 19
 *
 * Output 4
 * 7
 * -----------------
 */
public class BOJ5014 {

    private static class Floor {
        int value, dist;

        public Floor(int value, int dist) {
            this.value = value;
            this.dist = dist;
        }
    }

    static int F, S, G, U, D;
    static boolean[] visited;

    public static boolean canGo(int curr, int min, int max) {
        return min <= curr && curr <= max;
    }

    public static int bfs() {
        Queue<Floor> queue = new LinkedList<>();
        queue.add(new Floor(S, 0));
        visited[S] = true;

        int result = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Floor curr = queue.poll();
            if (curr.value == G) {
                result = Math.min(result, curr.dist);
            }

            int nxtU = curr.value + U;
            int nxtD = curr.value - D;
            if (canGo(nxtU, 1, F) && !visited[nxtU]) {
                visited[nxtU] = true;
                queue.add(new Floor(nxtU, curr.dist + 1));
            }

            if (canGo(nxtD, 1, F) && !visited[nxtD]) {
                visited[nxtD] = true;
                queue.add(new Floor(nxtD, curr.dist + 1));
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        F = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        U = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        visited = new boolean[F + 1];

        int result = bfs();
        bw.write(result == Integer.MAX_VALUE ? "use the stairs" : String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}