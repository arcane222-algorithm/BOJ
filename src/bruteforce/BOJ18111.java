package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 마인크래프트 - BOJ18111
 * -----------------
 *
 * 현재 최소 높이 (minHeight)와 최대 높이 (maxHeight)를 N x M 안에 있는 블록들의 최소, 최대 높이라고 할때,
 * 최소, 최대 높이 사이의 각각 해당 층을 만들기 위해 블록을 부수는 경우와 채우는 경우를 계산한다.
 * (부술 경우 2초가 걸리고 채울 경우 1초가 걸리는데, 인벤토리에 블럭이 없을 경우 채울 수 없다)
 *
 * 최소 높이부터 N X M을 조사하며 해당 층에 블록이 없을 경우 블록을 채워주고 블록이 있다면 넘치는 블록만큼 부숴준다.
 * 해당 층에서 invSize (현재 가지고 있는 블록) 이 >= 0인 경우만 답인지 비교하는데, < 0 이라면 해당 층을 채우기 위해 소지한 블록 + 부숴서 얻은 블록
 * 보다 더 많은 블록이 필요하므로 만들 수 없는 층이기 때문에 넘어간다.
 *
 * 만약 작업 시간이 같을 경우 더 높은 층을 출력해야 하는데, 아래에서부터 조사하므로 현재 저장된 작업 시간보다 새로 비교할 작업 시간이 더 짧은지만 비교하면 된다.
 *
 * -----------------
 * Input 1
 * 3 4 99
 * 0 0 0 0
 * 0 0 0 0
 * 0 0 0 1
 *
 * Output 1
 * 2 0
 * -----------------
 * Input 2
 * 3 4 1
 * 64 64 64 64
 * 64 64 64 64
 * 64 64 64 63
 *
 * Output 2
 * 1 64
 * -----------------
 */
public class BOJ18111 {

    static int N, M, B, minHeight, maxHeight;
    static int[][] map;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        minHeight = Integer.MAX_VALUE;
        maxHeight = 0;

        map = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int height = Integer.parseInt(st.nextToken());
                if (minHeight > height) minHeight = height;
                if (maxHeight < height) maxHeight = height;
                map[i][j] = height;
            }
        }

        int ansTime, ansHeight;
        ansTime = ansHeight = Integer.MAX_VALUE;
        for (int i = minHeight; i < maxHeight + 1; i++) {
            int gap = 0;
            int workTime = 0;
            int invSize = B;

            for(int j = 0; j < N; j++) {
                for(int k = 0; k < M; k++) {
                    gap = map[j][k] - i;
                    workTime += gap > 0 ? Math.abs(gap) * 2 : gap < 0 ? Math.abs(gap) : 0;
                    invSize += gap;
                }
            }

            if(invSize >= 0) {
                if(workTime <= ansTime) {
                    ansTime = workTime;
                    ansHeight = i;
                }
            }
        }

        result.append(ansTime).append(' ').append(ansHeight);
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}