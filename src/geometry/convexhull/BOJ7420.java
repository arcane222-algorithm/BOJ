package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 맹독 방벽 - BOJ7420
 * -----------------
 *
 * Convex hull 알고리즘을 이용하여 모든 점을 포함할 수 있는 볼록 다각형을 구한 후
 * 다각형의 각 변에 변과 평행한 직사각형을 붙인다고 생각하면 맹독 방벽의 길이는 다각형의 각 변의 길이의 합과 각 직사각형 사이를 잇는 호의 길이의 합이 된다.
 * (각 변에 붙인 직사각형 사이의 코너의 길이는 특정 지점에 대하여 다각형의 점과 모두 L의 길이를 유지해야 하므로 호의 형태를 띄어야 한다.)
 *
 * 해당 지점의 호의 길이를 구하는 방법은 세점 v1, v2, v3에 대하여 벡터 v2-v1, v3-v2가 이루는 사이각을 theta라고 할 때,
 * 위에서 각 변에 대하여 직사각형을 붙였다고 했으므로 각 변에 수직인 선의 각도는 90도가 되고,
 * 두 벡터 v2-v1, v3-v2 반대편의 각도는 360 - 90 - 90 - theta가 되어 결과적으로 180 - theta가 된다.
 * 호의 길이는 R * theta이므로 이 값을 다각형의 각 변의 길이의 합에 더해주면 된다. (문제에서는 반지름 R이 L로 나온다)
 *
 * 즉 다각형에서 순서대로 v1, v2, v3 세점을 선택하며 (v1이 마지막 점이 될 때까지) v1, v2 사이의 거리를 더해주고,
 * 호의 길이는 위의 방식대로 theta값을 구해 L * theta 값을 더해주면 되는 것이다.
 * theta값의 경우 dot product (벡터의 내적) 를 이용해 구할 수 있다.
 * a dot b = (a.x * b.x) + (a.y * b.y) = |a||b|cos(theta) 이고,
 * cos(theta) = (a dot b) / |a||b| = ((a.x * b.x) + (a.y * b.y)) / |a||b| 가 되어
 * theta = acos(((a.x * b.x) + (a.y * b.y)) / |a||b|) 값을 통해 구할 수 있다. (acos은 cos의 역함수이다)
 *
 * -----------------
 * Input 1
 * 9 100
 * 200 400
 * 300 400
 * 300 300
 * 400 300
 * 400 400
 * 500 400
 * 500 200
 * 350 200
 * 200 200
 *
 * Output 1
 * 1628
 * -----------------
 */
public class BOJ7420 {

    private static class Vertex {
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Vertex v) {
            return Math.sqrt(dist2(v));
        }

        public int dotProduct(Vertex v) {
            return (x * v.x) + (y * v.y);
        }

        public int dist2(Vertex v) {
            return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
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
            builder.append("Vertex=[");
            builder.append("x=").append(x);
            builder.append(", y=").append(y).append(']');

            return builder.toString();
        }
    }

    static int N, L;
    static Vertex base;
    static List<Vertex> vertices = new ArrayList<>();

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
                    int dist1 = base.dist2(v1);
                    int dist2 = base.dist2(v2);
                    return Integer.compare(dist1, dist2);
                }
            }
        });

        Stack<Vertex> stack = new Stack<>();
        stack.add(base);
        for (int i = 1; i < vertices.size(); i++) {
            while (stack.size() > 1) {
                int ccwDir = Vertex.ccwDir(vertices.get(i), stack.get(stack.size() - 2), stack.get(stack.size() - 1));
                if (ccwDir <= 0) stack.pop();
                else break;
            }
            stack.add(vertices.get(i));
        }

        return stack;
    }

    public static double getHullLength(Stack<Vertex> vertexStack) {
        double length = 0;
        int size = vertexStack.size();
        for (int i = 0; i < size; i++) {
            Vertex v1 = vertexStack.get(i);
            Vertex v2 = i > vertexStack.size() - 2 ? vertexStack.get(i + 1 - size) : vertexStack.get(i + 1);
            Vertex v3 = i > vertexStack.size() - 3 ? vertexStack.get(i + 2 - size) : vertexStack.get(i + 2);
            Vertex v4 = new Vertex(v2.x - v1.x, v2.y - v1.y);
            Vertex v5 = new Vertex(v3.x - v2.x, v3.y - v2.y);
            double dist1 = v1.dist(v2);
            double dist2 = v2.dist(v3);
            int dot = v4.dotProduct(v5);
            double theta = Math.acos(dot / dist1 / dist2);
            length += v1.dist(v2);
            length += theta * L;
        }

        return length;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }

        double size = getHullLength(convexHull());
        bw.write(String.format("%.0f", size));

        // close the buffer
        br.close();
        bw.close();
    }
}