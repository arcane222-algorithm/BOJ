package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 로버트 후드 - BOJ9420
 * -----------------
 *
 * BOJ10254와 함께 회전하는 캘리퍼스 (Rotating Calipers) 알고리즘의 기본 문제이다.
 *
 *  2차원 평면 위의 화살 (점)들이 있을 때, 가장 먼 화살의 거리를 구해야 한다.
 *  2차원 평면의 점들에 대하여 두 점의 거리가 최대가 되어야 하는 후보는 직관적으로 Convex hull 위의 점이어야 한다.
 *  (모든 점들을 포함할 수 있는 볼록 다각형이 convex hull이고, 그 안에 포함되어 있는 점들은 두 점을 골라 거리 값을 구하더라도
 *  convex hull 위의 어떠한 두 점의 거리가 더 멀기 때문이다.)
 *
 *  Convex hull을 이루는 점의 개수를 N이라 할 때, 가장 먼 두 점을 찾기 위해서는 일반적으로 N각형 내부의 두 점을 이어 대각선을 만드는 경우의 수를 모두 따져야 한다.
 *  즉 N(N - 1) / 2번 따져야 하므로 시간 복잡도는 O(N^2)이 된다.
 *  주어지는 N이 최대 10만이므로 O(N^2) 비교 시 시간초과가 발생할 수 있다.
 *  (이 문제의 경우 N의 범위가 크지만 x, y좌표의 범위가 절댓값 1000을 넘지 않으므로 실제로 따져봐야 할 점의 개수는 10만개보다 훨씬 작다.
 *  그러므로 brute-force 한 탐색으로 찾는 것이 가능하다.)
 *
 * 좌표 평면위의 가장 먼 두 점을 찾기 위한 정해로는 회전하는 캘리퍼스 (Rotating Calipers) 알고리즘을 이용한다.
 * 회전하는 캘리퍼스의 경우 컨벡스 헐을 찾았다는 가정하에 O(N)에 가장 먼 두 점을 찾을 수 있다.
 *
 * -----------------
 * Input 1
 * 2
 * 2 2
 * -1 -2
 *
 * Output 1
 * 5.0
 * -----------------
 * Input 2
 * 5
 * -4 1
 * -100 0
 * 0 4
 * 2 -3
 * 2 300
 *
 * Output 2
 * 316.86590223
 * -----------------
 */
public class BOJ9240 {

    private static class Vertex {
        static final Vertex ZERO = new Vertex(0, 0);
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int distSquare(Vertex v1, Vertex v2) {
            int dx = v1.x - v2.x;
            int dy = v1.y - v2.y;
            return dx * dx + dy * dy;
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
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

    static int C;
    static Vertex base;
    static List<Vertex> vertices;

    public static Stack<Vertex> convexHull() {
        base = vertices.get(0);
        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (base.y > v.y) {
                base = v;
            } else if (base.y == v.y) {
                if (base.x > v.x) {
                    base = v;
                }
            }
        }

        Collections.sort(vertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                int ccwDir = Vertex.ccwDir(base, v1, v2);
                if (ccwDir > 0) return -1;
                else if (ccwDir < 0) return 1;
                else {
                    return Integer.compare(Vertex.distSquare(base, v1), Vertex.distSquare(base, v2));
                }
            }
        });

        Stack<Vertex> stack = new Stack<>();
        stack.add(base);
        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            while (stack.size() > 1) {
                int ccwDir = Vertex.ccwDir(v, stack.get(stack.size() - 2), stack.get(stack.size() - 1));
                if (ccwDir <= 0) stack.pop();
                else break;
            }
            stack.add(v);
        }

        return stack;
    }

    public static double rotatingCallipers(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();
        int lIdx = 0, rIdx = 0;
        for (int i = 0; i < Size; i++) {
            Vertex left = convexHull.get(lIdx);
            Vertex right = convexHull.get(rIdx);
            Vertex curr = convexHull.get(i);
            if (curr.x < left.x) lIdx = i;
            if (curr.x > right.x) rIdx = i;
        }

        int distSquare = Vertex.distSquare(convexHull.get(lIdx), convexHull.get(rIdx));
        for (int i = 0; i < convexHull.size(); i++) {
            int lNextIdx = (lIdx + 1) % Size;
            int rNextIdx = (rIdx + 1) % Size;
            Vertex nxtLeft = new Vertex(convexHull.get(lNextIdx).x - convexHull.get(lIdx).x, convexHull.get(lNextIdx).y - convexHull.get(lIdx).y);
            Vertex nxtRight = new Vertex(convexHull.get(rNextIdx).x - convexHull.get(rIdx).x, convexHull.get(rNextIdx).y - convexHull.get(rIdx).y);
            int ccwDir = Vertex.ccwDir(Vertex.ZERO, nxtLeft, nxtRight);
            if (ccwDir > 0) rIdx = rNextIdx;
            else lIdx = lNextIdx;
            distSquare = Math.max(distSquare, Vertex.distSquare(convexHull.get(lIdx), convexHull.get(rIdx)));
        }

        return Math.sqrt(distSquare);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        C = Integer.parseInt(br.readLine());
        vertices = new ArrayList<>(C);
        for (int i = 0; i < C; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }

        Stack<Vertex> convexHull = convexHull();
        double maxLen = rotatingCallipers(convexHull);
        bw.write(String.valueOf(maxLen));

        // close the buffer
        br.close();
        bw.close();
    }
}