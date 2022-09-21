package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * N-Queen - BOJ9663
 * -----------------
 * category: brute force (브루트포스 알고리즘)
 *           back tracking (백트래킹)
 *
 * Time-Complexity: O(N!)
 * -----------------
 *
 * N x N 의 체스판 위에 퀸 N개를 서로 공격 할 수 없도록 놓는 경우의 수를 구해야 한다.
 * N x N 보드 배열을 만들어 계산하여도 되지만 효율적인 구현을 위해 1차원 배열을 사용한다.
 * 맨 윗줄부터 아래 줄까지 각 줄의 깊이 값을 depth 라고 하고, 가로에 대한 좌표를 x좌표라고 하면
 * board[depth] = x의 형태를 띄게 된다.
 * (즉, 1차원 배열의 index는 depth 값 (y좌표), 배열의 각 원소의 값은 퀸 말의 x좌표가 된다.)
 *
 * 맨 윗줄부터 말을 하나씩 놓으며 dfs 탐색을 실시한다.
 * 현재 depth에서 말을 놓기 위한 조건은
 * 0 ~ depth - 1 까지 놓여진 말들에 대하여
 *         for (int i = 0; i < depth; i++) {
 *             if (board[i] == currX) return false;
 *             if (Math.abs(board[i] - currX) == depth - i) return false;
 *         }
 * 위와 같이 처리해주면 된다.
 * (i) if (board[i] == currX) return false;
 *     현재 놓여진 말과 i번째 말이 같은 세로축 상에 놓여진 상태이다. (즉, x좌표가 같은 상태)
 *
 * (ii) if (Math.abs(board[i] - currX) == depth - i) return false;
 *      현재 놓여진 말과 i번째 말이 대각선의 위치에 놓여진 상태이다.
 *      즉, 두 말의 x좌표의 절댓값 차이가 depth - i 만큼 차이나는 상태이다.
 *      
 * 만약 중간에 말을 놓을 수 없는 경우의 수가 생기면 탐색을 종료하며 가지치기를 수행한다. (백트래킹)
 *
 * -----------------
 * Input 1
 * 8
 *
 * Output 1
 * 92
 * -----------------
 */
public class BOJ9663 {

    static int N;
    static int count;
    static int[] board;

    public static boolean canGo(int depth, int currX) {
        for (int i = 0; i < depth; i++) {
            if (board[i] == currX) return false;
            if (Math.abs(board[i] - currX) == depth - i) return false;
        }
        return true;
    }

    public static void dfs(int depth) {
        if (depth == N) {
            count++;
        } else {
            for (int i = 0; i < N; i++) {
                if (canGo(depth, i)) {
                    board[depth] = i;
                    dfs(depth + 1);
                    board[depth] = -1;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        board = new int[N];
        for (int i = 0; i < N; i++) {
            board[i] = -1;
        }

        dfs(0);
        bw.write(String.valueOf(count));

        // close the buffer
        br.close();
        bw.close();
    }
}