package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 치킨 배달 - BOJ15686
 * -----------------
 * category: implementation (구현),
 *           brute-force (브루트포스 알고리즘),
 *           back-tracking (백 트래킹)
 *
 * Time-Complexity: 전체 치킨 집의 수 X, 전체 집의 수 Y, 살아남은 치킨 집의 수 M
 *                  O(X * Y * XCM) (XCM -> X개에서 M개를 뽑는 조합)
 * -----------------
 *
 * 맵을 초기화하는 과정에서 1의 경우 houseList, 2의 경우 chickenList에 넣어준다.
 * 전체 치킨집 중에서 M개만 선택해야 하므로 백트래킹을 이용한다.
 * dfs(depth) 함수를 재귀적으로 호출하며 depth == M 일때, dfs를 중단하고 남은 치킨집과 집들과의 치킨 거리를 계산한다.
 * 이러한 방법으로 모든 depth == M일 때 (즉, 전체 치킨 집의 수 X에서 M개를 뽑을 때)를 모두 탐색하며 치킨 거리를 계산하고
 * 치킨 거리의 최솟값을 출력해주면 된다.
 *
 * -----------------
 * Input 1
 * 5 3
 * 0 0 1 0 0
 * 0 0 2 0 1
 * 0 1 2 0 0
 * 0 0 1 0 0
 * 0 0 0 0 2
 *
 * Output 1
 * 5
 * -----------------
 * Input 2
 * 5 2
 * 0 2 0 1 0
 * 1 0 1 0 0
 * 0 0 0 0 0
 * 2 0 0 1 1
 * 2 2 0 1 2
 *
 * Output 2
 * 10
 * -----------------
 * Input 3
 * 5 1
 * 1 2 0 0 0
 * 1 2 0 0 0
 * 1 2 0 0 0
 * 1 2 0 0 0
 * 1 2 0 0 0
 *
 * Output 3
 * 11
 * -----------------
 * Input 4
 * 5 1
 * 1 2 0 2 1
 * 1 2 0 2 1
 * 1 2 0 2 1
 * 1 2 0 2 1
 * 1 2 0 2 1
 *
 * Output 4
 * 32
 * -----------------
 */
public class BOJ15686 {

    private static class Vec2 {
        int x, y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vec2) {
                Vec2 v = (Vec2) o;
                return x == v.x && y == v.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Vec2={");
            builder.append("x=").append(x);
            builder.append(", y=").append(y).append('}');

            return builder.toString();
        }
    }

    static int N, M;
    static boolean[] visited;
    static List<Vec2> houseList = new ArrayList<>();
    static List<Vec2> chickenList = new ArrayList<>();

    public static int dist(Vec2 v1, Vec2 v2) {
        return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
    }

    public static int dfs(int depth, int parent) {
        if (depth == M) {
            return getDistSum();
        }

        int min = Integer.MAX_VALUE;
        for (int i = parent; i < chickenList.size(); i++) {
            if (!visited[i]) {
                visited[i] = true;
                min = Math.min(min, dfs(depth + 1, i));
                visited[i] = false;
            }
        }

        return min;
    }

    public static int getDistSum() {
        int result = 0;
        for (int i = 0; i < houseList.size(); i++) {
            int min = Integer.MAX_VALUE;
            Vec2 h = houseList.get(i);

            for (int j = 0; j < chickenList.size(); j++) {
                if (!visited[j]) continue;
                Vec2 c = chickenList.get(j);
                min = Math.min(min, dist(h, c));
            }
            result += min;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int i = 1; i < N + 1; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j < N + 1; j++) {
                char c = st.nextToken().charAt(0);
                if (c == '1') houseList.add(new Vec2(j, i));
                if (c == '2') chickenList.add(new Vec2(j, i));
            }
        }
        visited = new boolean[chickenList.size()];
        int result = dfs(0, 0);
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}