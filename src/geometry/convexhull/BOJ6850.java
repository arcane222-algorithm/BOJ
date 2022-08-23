package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * Cows - BOJ6850
 * -----------------
 *
 * 목장에 있는 나무들의 점을 이용하여 모든 나무들을 커버할 수 있는 Convex hull을 구한다.
 * Convex hull을 구할 때, Graham scan 알고리즘을 사용할 경우 Convex hull을 이루는 점들을 반시계 방향으로 정렬된 상태로 구할 수 있다.
 * 이 것을 이용하여 Convex hull 내부의 첫번째 점 (index 0)을 base로 잡고, 이후 두 점들에 대하여 base와 ccw한 값 * 0.5을 모두 더하면
 * Convex hull 목장의 넓이를 구할 수 있다.
 * (세 점을 ccw 한 값은 벡터의 외적이고, 이 값에 절대값을 취하면 두 벡터가 만드는 평행사변형의 넓이가 된다.)
 * (즉, 이 평행사변형의 절반인 삼각형의 넓이를 구하기 위해 ccw에 0.5를 곱하는 것이고, 기준점과 반시계 방향으로 정렬된 점들 중 연속한 두 점들을 골라 base와
 * 만들어지는 삼각형의 넓이를 모두 더하면 볼록 다각형의 넓이를 구할 수 있다.)
 *
 * 문제에서 소 한마리당 50제곱 미터의 공간이 있어야 살아갈 수 있다고 명시되어 있으므로 (구한 넓이 / 50)한 값을 소수점을 버림하면 답이 된다.
 *
 * -----------------
 * Input 1
 * 4
 * 0 0
 * 0 101
 * 75 0
 * 75 101
 *
 * Output 1
 * 151
 * -----------------
 */
public class BOJ6850 {

    private static class Vertex {
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

        public static double ccwSize(Vertex v1, Vertex v2, Vertex v3) {
            return Math.abs(Vertex.ccw(v1, v2, v3)) * 0.5;
        }
    }

    static final int M = 50;
    static int N;
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
                    int dist1 = Vertex.distSquare(base, v1);
                    int dist2 = Vertex.distSquare(base, v2);
                    return Integer.compare(dist1, dist2);
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

    public static double convexHullSize(Stack<Vertex> convexHull) {
        Vertex base = convexHull.get(0);
        double size = 0;
        for (int i = 1; i < convexHull.size() - 1; i++) {
            size += Vertex.ccwSize(base, convexHull.get(i), convexHull.get(i + 1));
        }

        return size;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        vertices = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }
        Stack<Vertex> convexHull = convexHull();
        double size = convexHullSize(convexHull);
        bw.write(String.valueOf((int) (size / M)));

        // close the buffer
        br.close();
        bw.close();
    }
}