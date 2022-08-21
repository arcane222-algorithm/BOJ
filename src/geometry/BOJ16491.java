package geometry;

import java.io.*;
import java.util.*;


/**
 * 대피소 찾기 - BOJ16491
 * -----------------
 *
 * 각 로봇과 대피소를 하나의 선분으로 연결 했을 때 N개의 선분이 서로 교차하지 않도록 하는 경우를 구해야 한다.
 * N이 최대 10이므로, brute-force하게 모든 경우의 수를 탐색해 볼 수 있다.
 * 로봇과 대피소를 정점(Vertex)으로 List에 저장한 후, 두 개의 vertex를 선으로 연결해 linesTemp에 누적하며
 * 새로 조사하려는 선의 경우 기존에 누적된 선들과 교차여부를 조사한다.
 * 모든 선들이 교차하지 않을 경우 (즉, 결과를 저장하는 stack의 크기 = N일 경우) 결과를 출력한다. (dfs를 이용하여 탐색한다.)
 * 중간에 교차하지 않는 선을 발견할 경우 선을 저장하는 linesTemp와 stack의 최상단 값을 pop 해주는데,
 * 현재 depth의 값과 비교하여 두 자료구조의 size가 depth - 1 크기일때만 pop한다.
 * (즉, depth 0, depth 1, depth 2,... 이런식으로 dfs로 조사해나갈 때, 같은 depth에 대하여 가지치기 과정에서 현재 depth까지의 기존 데이터를 유지하고
 * 해당 depth보다 하위 depth의 값들만 pop 할 수 있도록 한다)
 *
 * -----------------
 * Input 1
 * 2
 * 1 1
 * 1 99
 * 99 99
 * 99 1
 *
 * Output 1
 * 2
 * 1
 * -----------------
 * Input 2
 * 4
 * 3 2
 * 2 9
 * 3 0
 * 4 2
 * 88 3
 * 12 9
 * 100 0
 * 4 3
 *
 * Output 2
 * 4
 * 2
 * 3
 * 1
 * -----------------
 */
public class BOJ16491 {
    private static class Vertex implements Comparable<Vertex> {
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public int compareTo(Vertex v) {
            if (y == v.y) return Integer.compare(x, v.x);
            else return Integer.compare(y, v.y);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Vertex={");
            builder.append("x=").append(x);
            builder.append(", y=").append(y).append('}');

            return builder.toString();
        }
    }

    private static class LineSegment {
        Vertex v1, v2;

        public LineSegment(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int checkCross(LineSegment line) {
            int ccwDir1 = Vertex.ccwDir(v1, v2, line.v1);
            int ccwDir2 = Vertex.ccwDir(v1, v2, line.v2);
            int ccwDir3 = Vertex.ccwDir(line.v1, line.v2, v1);
            int ccwDir4 = Vertex.ccwDir(line.v1, line.v2, v2);

            if (ccwDir1 == 0 && ccwDir2 == 0) {
                Vertex sm1 = v1.compareTo(v2) < 0 ? v1 : v2;
                Vertex lg1 = v1.compareTo(v2) >= 0 ? v1 : v2;
                Vertex sm2 = line.v1.compareTo(line.v2) < 0 ? line.v1 : line.v2;
                Vertex lg2 = line.v1.compareTo(line.v2) >= 0 ? line.v1 : line.v2;

                Vertex temp;
                if (sm1.compareTo(lg2) >= 0) {
                    temp = sm1;
                    sm1 = sm2;
                    sm2 = temp;

                    temp = lg1;
                    lg1 = lg2;
                    lg2 = temp;
                }

                if (lg1.compareTo(sm2) >= 0 || sm1.compareTo(lg2) >= 0) {
                    if (lg1.compareTo(sm2) == 0) return 1;
                    else return -1;
                } else {
                    return 0;
                }
            }

            if (ccwDir1 * ccwDir2 <= 0 && ccwDir3 * ccwDir4 <= 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    static int N;
    static boolean[] visited;
    static List<Vertex> robots = new ArrayList<>();
    static List<Vertex> shelters = new ArrayList<>();
    static List<LineSegment> linesTemp = new ArrayList<>();
    static Stack<Integer> stack = new Stack<>();
    static StringBuilder result = new StringBuilder();

    public static boolean checkCrossWithLines(LineSegment l) {
        for (LineSegment ls : linesTemp) {
            if (l.checkCross(ls) != 0) {
                return true;
            }
        }
        return false;
    }

    public static void dfs(int rIdx, int sIdx) {
        LineSegment ls = new LineSegment(robots.get(rIdx), shelters.get(sIdx));
        if (!checkCrossWithLines(ls)) {
            linesTemp.add(ls);
            stack.add(sIdx + 1);
            visited[sIdx] = true;

            for (int i = 0; i < N; i++) {
                if (!visited[i]) {
                    dfs(rIdx + 1, i);
                }
            }
        }
        visited[sIdx] = false;

        if (stack.size() == N)
            return;

        if (rIdx == stack.size() - 1)
            stack.pop();

        if (rIdx == linesTemp.size() - 1)
            linesTemp.remove(linesTemp.size() - 1);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            robots.add(new Vertex(x, y));
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            shelters.add(new Vertex(x, y));
        }

        visited = new boolean[N];
        for (int i = 0; i < N; i++) {
            dfs(0, i);
        }

        for (int idx : stack) {
            result.append(idx).append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
