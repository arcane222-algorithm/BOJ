package geometry.convexhull;

import java.io.*;
import java.util.*;



/**
 * 고속도로 - BOJ10254
 * -----------------
 *
 * BOJ9240 - 로버트 후드와 함께 회전하는 캘리퍼스 (Rotating Calipers) 알고리즘의 기본 문제이다.
 *
 *  2차원 평면 점들 중 가장 거리가 먼 두 점의 x, y좌표를 출력해야 한다.
 *  BOJ9240 - 로버트 후드 문제의 경우 두 점의 거리를 출력하는 문제라면, 이 문제는 두 점의 x, y좌표를 출력하면 된다.
 *
 *  2차원 평면 위의 점들에 대하여 Graham's scan 알고리즘을 이용하여 O(NlogN)에 Convex hull을 구한다.
 *  Convex hull을 이루는 점들에 대하여 Rotating Calipers 알고리즘을 이용하여 가장 먼 두 점을 O(N)만에 구할 수 있다.
 *
 * -----------------
 * Input 1
 * 2
 * 4
 * -100 -50
 * 20 -50
 * -20 50
 * 100 50
 * 9
 * -1 -1
 * 3 -3
 * 6 -6
 * -3 -6
 * 12 0
 * 3 4
 * -6 3
 * 0 9
 * 6 9
 *
 * Output 1
 * -100 -50 100 50
 * -6 3 12 0
 * -----------------
 */
public class BOJ10254 {

    private static class Vertex {
        static final Vertex ZERO = new Vertex(0, 0);
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public Vertex(Vertex v1, Vertex v2) {
            this(v1.x, v2.x, v1.y, v2.y);
        }

        public Vertex(long x1, long x2, long y1, long y2) {
            this.x = x2 - x1;
            this.y = y2 - y1;
        }

        public static long distSquare(Vertex v1, Vertex v2) {
            long dx = v1.x - v2.x;
            long dy = v1.y - v2.y;
            return dx * dx + dy * dy;
        }

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }
    }

    static int T, N;
    static Vertex base;
    static List<Vertex> vertices;
    static StringBuilder result = new StringBuilder();

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
                    return Long.compare(Vertex.distSquare(base, v1), Vertex.distSquare(base, v2));
                }
            }
        });

        Stack<Vertex> stack = new Stack<>();
        for (int i = 0; i < vertices.size(); i++) {
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

    public static void rotatingCalipers(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();
        int lIdx = 0, rIdx = 0;
        for (int i = 0; i < Size; i++) {
            Vertex v = convexHull.get(i);
            Vertex left = convexHull.get(lIdx);
            Vertex right = convexHull.get(rIdx);
            if (v.x < left.x) lIdx = i;
            if (v.x > right.x) rIdx = i;
        }

        Vertex resV1 = convexHull.get(lIdx), resV2 = convexHull.get(rIdx);
        long distSquare = Vertex.distSquare(convexHull.get(lIdx), convexHull.get(rIdx));
        for (int i = 0; i < Size; i++) {
            int lNext = (lIdx + 1) % Size;
            int rNext = (rIdx + 1) % Size;

            Vertex l0 = convexHull.get(lIdx);
            Vertex l1 = convexHull.get(lNext);
            Vertex l2 = new Vertex(l0, l1);

            Vertex r0 = convexHull.get(rIdx);
            Vertex r1 = convexHull.get(rNext);
            Vertex r2 = new Vertex(r1, r0);

            int ccwDir = Vertex.ccwDir(Vertex.ZERO, l2, r2);
            if (ccwDir > 0) lIdx = lNext;
            else rIdx = rNext;

            long distSquareTmp = Math.max(distSquare, Vertex.distSquare(convexHull.get(lIdx), convexHull.get(rIdx)));
            if (distSquare < distSquareTmp) {
                distSquare = distSquareTmp;
                resV1 = convexHull.get(lIdx);
                resV2 = convexHull.get(rIdx);
            }
        }

        result.append(resV1.x).append(' ').append(resV1.y).append(' ');
        result.append(resV2.x).append(' ').append(resV2.y).append('\n');
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            vertices = new ArrayList<>(N);
            for (int j = 0; j < N; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                vertices.add(new Vertex(x, y));
            }

            Stack<Vertex> convexHull = convexHull();
            rotatingCalipers(convexHull);
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
