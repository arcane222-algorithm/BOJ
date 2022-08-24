package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 민코프스키 합 - BOJ2244
 * -----------------
 *
 * 다각형 A의 점들의 집합에 속한 vA와 다각형 B의 점들의 집합에 속한 vB에 대하여 민코프스키 합은 (vA.x + vB.x, vA.y + vB.y) 가 나타내는 영역의 도형이다.
 * 두 다각형 A와 B에 대하여 민코프스키 합의 우선순위는
 * 1. 꼭짓점의 개수가 가장 작은 것
 * 2. 면적이 가장 작은 것
 * 3. 다각형을 이루는 모든 좌표들 중 최소 x좌표가 가장 작은 것
 * 4. 다각형을 이루는 모든 좌표들 중 최소 y좌표가 가장 작은 것
 * 이다.
 *
 * 또한 이 문제에서 다각형의 경우
 * 1. 다각형 안의 임의의 두 점을 잇는 선분이 완전히 그 다각형 안에 속해 있다.
 * 2. 세 개 이상의 꼭짓점으로 이루어져 있다.
 * 3. 세 꼭짓점이 한 직선 위에 있는 경우는 없다.
 * 의 조건을 만족한다.
 *
 * 즉, 위 조건들을 고려할 때, 다각형 A, B의 민코프스키 합으로 표현되는 다각형 C는
 * 다각형 A의 점들의 집합에 속한 vA와 다각형 B의 점들의 집합에 속한 vB에 대하여 조합 (N x M개)을 구해 이 점들을 이용한 Convex hull을 구하면 된다.
 * Convex hull을 구하는 Graham's scan 알고리즘의 시간복잡도는 O(NlogN)이고, 민코프스키 합의 다각형 C에서 고려해야 할 최대 정점의 수는 N x M = 1000000 이므로
 * 제한시간안에 해결이 가능하다.
 * 중복되는 점들을 거르기 위해 Set에 (vA.x + vB.x, vA.y + vB.y) 점들을 넣어준 후 이 점들을 이용하여 Convex hull을 구하면 된다.
 *
 * -----------------
 * Input 1
 * 3 3
 * 0 0
 * 1 0
 * 1 1
 * 0 1
 * 0 0
 * 1 0
 *
 * Output 1
 * 5
 * 0 0
 * 2 0
 * 2 1
 * 1 2
 * 0 1
 * -----------------
 */
public class BOJ11620 {

    private static class Vertex {

        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
        }

        public static long distSquare(Vertex v1, Vertex v2) {
            long dx = v1.x - v2.x;
            long dy = v1.y - v2.y;
            return dx * dx + dy * dy;
        }

        public static long dotProduct(Vertex v1, Vertex v2) {
            return v1.x * v2.x + v1.y * v2.y;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vertex) {
                Vertex v = (Vertex) o;
                return x == v.x && y == v.y;
            } else {
                return false;
            }
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

    static int N, M;
    static Vertex base;
    static List<Vertex> vertices;
    static StringBuilder result = new StringBuilder();

    public static Stack<Vertex> convexHull() {
        base = vertices.get(0);
        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (base.x > v.x) {
                base = v;
            } else if (base.x == v.x) {
                if (base.y > v.y) {
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        List<Vertex> verticesA = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            verticesA.add(new Vertex(x, y));
        }

        List<Vertex> verticesB = new ArrayList<>(M);
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            verticesB.add(new Vertex(x, y));
        }

        Set<Vertex> set = new HashSet<>();
        for (int i = 0; i < N; i++) {
            Vertex vA = verticesA.get(i);
            for (int j = 0; j < M; j++) {
                Vertex vB = verticesB.get(j);
                set.add(new Vertex(vA.x + vB.x, vA.y + vB.y));
            }
        }

        vertices = new ArrayList<>(set);
        Stack<Vertex> stack = convexHull();
        Queue<Vertex> queue = new LinkedList<>();
        for (int i = stack.size() - 1; i > -1; i--) {
            int ccwDir = Vertex.ccwDir(base, stack.get(i - 1), stack.get(i));
            queue.add(stack.get(i));
            if (ccwDir != 0) {
                break;
            }
        }

        result.append(stack.size()).append('\n');
        for (int i = 0; i < stack.size() - queue.size(); i++) {
            Vertex v = stack.get(i);
            result.append(v.x).append(' ').append(v.y).append('\n');
        }
        for (Vertex v : queue) {
            result.append(v.x).append(' ').append(v.y).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
