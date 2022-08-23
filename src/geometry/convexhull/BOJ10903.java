package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * Wall construction - BOJ10903
 * -----------------
 *
 * 맹독방벽 - BOJ7420 과 쌍둥이 문제이다.
 * 주어진 xy 평면위의 원들을 모두 포함하며 최소 길이의 유리벽을 세우기 위해서는 각 원들을 convex hull의 점이라고 했을 때
 * 원들에 접하는 직선을 그어 다각형을 만들면된다.
 * 각 원들의 좌표를 이용하여 컨벡스 헐의 점들을 구한 후 컨벡스 헐 위의 두 점의 길이를 모두 더해준다.
 * 이후 코너점 마다 호 부분의 길이를 계산해야 하는데 컨벡스헐의 반시계 방향 순의 세 점 v1, v2, v3에 대하여 코너점이 v2라고 한다면
 * 두 벡터 v1-v2, v3-v2가 이루는 사이각 theta는
 * dot product = |a||b|cos(theta), theta = acos(dot product / |a||b|)가 된다.
 * (dot product의 값은ㅇ v1-v2를 v4, v3-v2를 v5라 할때, dot = v4.x * v5.x + v4.y * v5.y 가 된다.)
 * 반대편 호의 각은 PI - theta가 되고 호의 길이는 R * 사이각이 되므로
 * 각 호의 길이 R * (Math.PI - theta) 값을 전부 더해주면 답이 된다.
 *
 * -----------------
 * Input 1
 * 4 1
 * -2 -2
 * -2 2
 * 2 2
 * 2 -2
 *
 * Output 1
 * 22.283185307180
 * -----------------
 */
public class BOJ10903 {

    private static class Vertex {
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int dotProduct(Vertex v1, Vertex v2) {
            return v1.x * v2.x + v1.y * v2.y;
        }

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
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
    }

    static int N, R;
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


    public static double getGirthLength(Stack<Vertex> vertexStack) {
        final int size = vertexStack.size();
        double length = 0;
        for (int i = 0; i < vertexStack.size(); i++) {
            Vertex v1 = vertexStack.get(i);
            Vertex v2 = vertexStack.get((i + 1) % size);
            Vertex v3 = vertexStack.get((i + 2) % size);
            Vertex v12 = new Vertex(v1.x - v2.x, v1.y - v2.y);
            Vertex v32 = new Vertex(v3.x - v2.x, v3.y - v2.y);
            double dist1 = Vertex.dist(v1, v2);
            double dist2 = Vertex.dist(v2, v3);
            int dotProduct = Vertex.dotProduct(v12, v32);
            double theta = Math.acos(dotProduct / dist1 / dist2);
            length += dist1;
            length += R * (Math.PI - theta);
        }

        return length;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());
        vertices = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }

        bw.write(String.valueOf(getGirthLength(convexHull())));

        // close the buffer
        br.close();
        bw.close();
    }
}