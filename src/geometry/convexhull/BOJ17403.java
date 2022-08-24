package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 가장 높고 넓은 성 - BOJ17403
 * -----------------
 *
 * 문제의 조건에 따라 성을 지을 때, 각 층의 경계는 다각형이고 각 층의 경계의 꼭짓점에는 표지판이 있어야 한다. 또한
 * 1. 높이를 가능한 한 제일 높게 지어야 한다.
 * 2. 성의 모든 층의 넓이의 합이 가능한 한 제일 크게 지어야 한다.
 * 3. 가능한 적은 수의 표지판을 사용하여야 한다.
 * 라는 조건이 제시된다.
 *
 * 위 조건을 만족하기 위해서는 맨 아래층이 가장 넓은 다각형을 이루고 위로 올라갈 수록 점점 좁아지는 형태가 되어야 높이가 가장 높게 지을 수 있다.
 * 또한 각 층의 다각형을 만들 때, Convex hull의 형태로 만들면 최소한의 점 (표지판)을 선택하여 가장 넓은 다각형을 만들 수 있다.
 * 즉, n층 (n >= 2)에 대하여 n - 1층에서 Convex hull을 이루는 점들을 제외한 점들을 가지고 다시 Convex hull을 만들며 층을 쌓아가면 된다.
 *
 * Graham's scan 알고리즘을 이용하여 각 층마다 Convex hull을 구하는데 n - 1층에서 구한 점들은 n층을 구할 때 정점 목록 (Vertex List)에서 제외하고 계산하면 된다.
 * 주의할 점은, 각 층은 넓이가 정의되어야 하므로 정점 목록의 크기가 3 미만이거나, 구한 convex hull의 크기가 3 미만이면
 * 현재 구할 수 있는 최대 크기의 Convex hull에 대하여 넓이가 정의되지 않으므로 탐색을 종료한다.
 *
 * -----------------
 * Input 1
 * 9
 * 0 0
 * -1 3
 * -1 -2
 * -5 -5
 * 2 -2
 * 2 2
 * 3 1
 * 3 -5
 * 1 -1
 *
 * Output 1
 * 2 1 2 1 2 1 1 1 0
 * -----------------
 * Input 2
 * 12
 * 0 0
 * 1 0
 * 2 0
 * 3 0
 * 4 0
 * 3 1
 * 2 2
 * 1 3
 * 0 4
 * 0 3
 * 0 2
 * 0 1
 *
 * Output 2
 * 1 2 3 2 1 2 3 2 1 2 3 2
 * -----------------
 * Input 3
 * 3
 * 1 1
 * 2 2
 * 3 3
 *
 * Output 3
 * 0 0 0
 * -----------------
 */
public class BOJ17403 {

    private static class Vertex {

        int idx, x, y;

        public Vertex(int idx, int x, int y) {
            this.idx = idx;
            this.x = x;
            this.y = y;
        }

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
        }

        public static int distSquare(Vertex v1, Vertex v2) {
            int dx = v1.x - v2.x;
            int dy = v1.y - v2.y;
            return dx * dx + dy * dy;
        }

        public static int dotProduct(Vertex v1, Vertex v2) {
            return v1.x * v2.x + v1.y * v2.y;
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vertex) {
                Vertex v = (Vertex) o;
                return idx == v.idx;
            } else {
                return false;
            }
        }
    }

    static int N;
    static int[] heights;
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

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        vertices = new ArrayList<>(N);
        heights = new int[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(i, x, y));
        }

        for (int i = 1; ; i++) {
            Stack<Vertex> vertexStack = convexHull();
            if (vertexStack.size() < 3) break;

            for (Vertex v : vertexStack) {
                heights[v.idx] = i;
                vertices.remove(v);
            }
            if (vertices.size() < 3) break;
        }

        for (int i = 0; i < N; i++) {
            result.append(heights[i]).append(' ');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}