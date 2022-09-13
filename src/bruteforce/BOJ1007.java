package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 벡터 매칭 - BOJ1017
 * -----------------
 *
 * 평면에 점이 N개가 있고 이를 집합 P 라고 할때, 점의 집합 P의 벡터 매칭은 벡터의 집합이다.
 * 즉, 집합 P의 벡터 매칭은 각 N개의 점을 벡터의 시작점과 끝점으로 이용하여 만든 벡터들의 모임이다.
 * 이때 N개의 점은 각 한번씩만 쓰여야 하므로 벡터 매칭에는 총 N / 2개의 벡터가 존재할 것이다.
 * 집합 P의 벡터 매칭에 있는 벡터의 합의 길이의 최솟값을 찾아야 한다.
 *
 * 집합 P에 점 v1, v2, v3, v4가 있고 벡터 매칭의 한 종류를 따져보면 v2-v1, v3-v4가 될 것이다. (즉, (v2.x - v1.x, v2.y - v1.y), (v3.x - v4.x, v3.y - v4.y))
 * 이 벡터들의 합은 x성분끼리 더하고 y성분끼리 더하는 결과이고 (v2.x - v1.x + v3.x - v4.x, v2.y - v1.y + v3.y - v4.y)가 될 것이다.
 * 여기서 알 수 있는 점은 벡터를 이루는 시작점은 좌표 값에 -를 하고 끝점은 +를 해야 한다는 것이다.
 * 즉, 벡터의 시작점 N/2개를 결정하고 나면 끝점 N/2는 자동으로 결정되며 이 때 벡터들의 합은 시작점 N/2개의 (각 x좌표의 합, y좌표의 합) 에서
 * 끝점 N/2개의 (각 x좌표의 합, y좌표의 합) 을 빼주는 것과 같다.
 * 이러한 방식으로 N = 최대 20 일때, N/2인 10개의 점을 선택한다고 하면
 * 20C10 = 184,756의 경우의 수가 나와 brute-force하게 탐색이 가능해진다.
 *
 * naive하게 생각하여 N = 최대 20개의 점에 대하여 단순히 하나씩 뽑아 두 쌍을 이어주게 되면 20 x 19 x 18 x ... x 10이 되어 brute-force하게 탐색하더라도 시간초과가 발생한다.
 * dfs 재귀를 통해 brute-force 한 search시
 *
 * i = 0으로 초기화 하여 탐색하면 모든 노드를 전부 탐색하는 결과가 나오므로 시간초과가 발생한다.
 *
 *         for (int i = 0; i < N; i++) {
 *             if (!visited[i]) {
 *                 visited[i] = true;
 *                 dfs(depth + 1, i, x + xArr[i], y + yArr[i]);
 *                 visited[i] = false;
 *             }
 *         }
 * i = begin으로 초기화 하여 위의 설명과 같이 탐색의 범위를 조합의 경우처럼 좁힐 수 있도록 한다.
 *
 *         for (int i = begin; i < N; i++) {
 *             if (!visited[i]) {
 *                 visited[i] = true;
 *                 dfs(depth + 1, i, x + xArr[i], y + yArr[i]);
 *                 visited[i] = false;
 *             }
 *         }
 * ex) 수열 (2 4 6 8 10) 에서 순서에 상관없이 3개를 뽑는 경우를 생각해보자. (5C3)
 *     (1 2 3) (1 2 4) (1 2 5) (1 3 4) (1 3 5) (1 4 5) (2 3 4) (2 3 5) (2 4 5) (3 4 5) 총 10가지가 있으며 (수열의 값이 아니라 자리 index로 표현함)
 *     탐색과정에서 각 자리를 (n1, n2, n3)라고 할 때 (각 자리의 ni값은 위 설명과 같이 각 수의 idx)
 *     ni, nj에 대하여 ni가 결정된 상황에서 다음 경우인 nj (즉, i < j) 를 탐색할 때 ni < nj를 만족한다.
 *     즉, 앞서 결정한 자리의 다음 자리에 올 값은 이전 자리보다 뒤에 위치하는 숫자들의 경우의 수만 탐색하는 것이다.
 *     그렇기에 위와 같이 for문에서 dfs를 수행할 때, 다음 dfs 함수를 재귀적으로 호출하는 과정에서 현재 dfs 함수의 탐색의 결정된 위치 값부터
 *     다음 dfs 함수가 탐색하도록 begin값을 넘겨 다음 dfs 함수에서는 i = 0이 아닌 i = begin부터 탐색하는 것이다.
 *
 * -----------------
 * Input 1
 * 2
 * 4
 * 5 5
 * 5 -5
 * -5 5
 * -5 -5
 * 2
 * -100000 -100000
 * 100000 100000
 *
 * Output 1
 * 0.000000000000
 * 282842.712474619038
 * -----------------
 * Input 2
 * 1
 * 10
 * 26 -76
 * 65 -83
 * 78 38
 * 92 22
 * -60 -42
 * -27 85
 * 42 46
 * -86 98
 * 92 -47
 * -41 38
 *
 * Output 2
 * 13.341664064126334
 * -----------------
 */
public class BOJ1007 {

    static int T, N;
    static long ANS;
    static long[] xArr, yArr;
    static boolean[] visited;
    static StringBuilder result = new StringBuilder();

    public static void dfs(int depth, int begin, long x, long y) {
        if (depth == N >> 1) {
            for (int i = 0; i < N; i++) {
                if (!visited[i]) {
                    x -= xArr[i];
                    y -= yArr[i];
                }
            }
            ANS = Math.min(ANS, x * x + y * y);
        }

        for (int i = begin; i < N; i++) {
            if (!visited[i]) {
                visited[i] = true;
                dfs(depth + 1, i, x + xArr[i], y + yArr[i]);
                visited[i] = false;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            xArr = new long[N];
            yArr = new long[N];
            visited = new boolean[N];
            for (int j = 0; j < N; j++) {
                st = new StringTokenizer(br.readLine());
                xArr[j] = Long.parseLong(st.nextToken());
                yArr[j] = Long.parseLong(st.nextToken());
            }
            ANS = Long.MAX_VALUE;
            dfs(0, 0, 0, 0);
            result.append(Math.sqrt(ANS)).append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
