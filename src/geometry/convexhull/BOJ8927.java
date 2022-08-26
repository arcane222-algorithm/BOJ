package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * Squares - BOJ8927
 * -----------------
 *
 * BOJ9240 - 로버트 후드, BOJ10254 - 고속도로, BOJ1310 - 달리기 코스, BOJ2049 - 가장 먼 두 점과 함께
 * 쌍둥를 이루고 있는 Rotating calipers 알고리즘의 대표 문제이다.
 *
 * 2차원 좌표 평면 위에 N개의 정사각형들을 주고 정사각형 위의 꼭짓점들 중 가장 먼 두 점을 찾아야 한다.
 * 정사각형을 이루는 점들을 Graham's scan 알고리즘을 이용하여 O(NlogN)에 Convex hull을 구한 후
 * Rotating calipers를 이용하여 O(N)에 가장 먼 두점의 좌표를 찾아 거리의 제곱을 출력하면 된다.
 * 
 * (N이 최대 10만이고, 정사각형을 이루는 점은 4개 이므로 모든 점은 최대 40만개가 입력으로 들어올 수 있다.
 * 그러므로 두 점의 거리를 계산할 때 brute-force 한 방법으로는 시간초과가 발생하고 위 쌍둥이 문제들처럼 rotating calipers 를 이용하여 해결한다.)
 * (정사각형의 점들을 통해 Convex hull을 구하는 것 외로는 위 문제들과 다른 점이 없다.)
 *
 * -----------------
 * Input 1
 * 2
 * 3
 * 0 0 1
 * 1 0 2
 * 0 0 1
 * 6
 * 2 1 2
 * 1 4 2
 * 3 2 3
 * 4 4 4
 * 6 5 1
 * 5 1 3
 *
 * Output 1
 * 13
 * 85
 * -----------------
 */
public class BOJ8927 {

    private static class Vertex {
        static final Vertex ZERO = new Vertex(0, 0);
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vertex(Vertex v1, Vertex v2) {
            this.x = v2.x - v1.x;
            this.y = v2.y - v1.y;
        }

        public static int distSquare(Vertex v1, Vertex v2) {
            int dx = v2.x - v1.x;
            int dy = v2.y - v1.y;
            return dx * dx + dy * dy;
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
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

        vertices.sort(new Comparator<Vertex>() {
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

    public static int rotatingCalipers(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();
        int i = 0, j = 0;
        for (int k = 0; k < Size; k++) {
            Vertex v = convexHull.get(k);
            if (v.x < convexHull.get(i).x) i = k;
            if (v.x > convexHull.get(j).x) j = k;
        }

        int distSquare = Vertex.distSquare(convexHull.get(i), convexHull.get(j));
        for (int k = 0; k < Size; k++) {
            int iNext = (i + 1) % Size;
            int jNext = (j + 1) % Size;

            Vertex iv0 = convexHull.get(i);
            Vertex iv1 = convexHull.get(iNext);
            Vertex iv2 = new Vertex(iv0, iv1);

            Vertex jv0 = convexHull.get(j);
            Vertex jv1 = convexHull.get(jNext);
            Vertex jv2 = new Vertex(jv0, jv1);

            int ccwDir = Vertex.ccwDir(Vertex.ZERO, iv2, jv2);
            if (ccwDir > 0) j = jNext;
            else i = iNext;

            distSquare = Math.max(distSquare, Vertex.distSquare(convexHull.get(i), convexHull.get(j)));
        }

        return distSquare;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            vertices = new ArrayList<>(4 * N);
            for (int j = 0; j < N; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int w = Integer.parseInt(st.nextToken());

                vertices.add(new Vertex(x, y));
                vertices.add(new Vertex(x + w, y));
                vertices.add(new Vertex(x, y + w));
                vertices.add(new Vertex(x + w, y + w));
            }

            Stack<Vertex> convexHull = convexHull();
            int dist = rotatingCalipers(convexHull);
            result.append(dist).append('\n');
        }

        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}