package geometry;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


/**
 * 사과나무 - BOJ2987
 * -----------------
 *
 * 삼각형의 세 점의 ccw 값에 절대값을 취하고 0.5를 곱하면 삼각형의 넓이가 나온다.
 * 이후 각 점들에 대하여 삼각형 내부에 있는지를 판별해야 한다.
 * 삼각형의 점들을 반시계 방향으로 정렬한 후 각 점들에 대하여 우선 점이 변 위에 있는지 조사한다.
 * 즉, 변을 이루는 선분의 양끝 점 v1, v2의 x, y 범위안에 검사하려는 점이 포함되는지 확인하고 포함된다면 ccw 값을 조사하여 이 값이 0인지 조사한다.
 * 세 변에 대하여 모두 조사를 하여 해당하는 경우가 있다면 삼각형 변 위에 점이 있는 것이다.
 *
 * 그렇지 않다면 삼각형의 세 점중 반시계 방향으로 순서대로 두 점을 골라 v1, 검사하려는점, v2 순으로 ccw 했을 때 > 0인 경우가 나온다면 삼각형 밖에 있는 것이고 반대면 삼각형 안에 있는 것이다.
 * why?) v1 -> 검사하려는 점 -> v2 순서대로 직선을 연결했을 때, 만약 점이 삼각형 내부에 있다면 이것은 오목 다각형이 된다. 즉, ccw 값이 시계 방향 ( < 0) 이어야 한다.
 *       반대로 점이 외부에 있다면 여전히 볼록 다각형을 유지하므로 (점이 외부에 하나 늘어나 사각형이 된다) ccw 값이 반시계 방향 ( > 0)이어야 한다.
 *
 * -----------------
 * Input 1
 * 1 1
 * 5 1
 * 3 3
 * 4
 * 3 1
 * 3 2
 * 3 3
 * 3 4
 *
 * Output 1
 * 4.0
 * 3
 * -----------------
 * Input 2
 * 3 2
 * 5 4
 * 1 6
 * 3
 * 2 4
 * 3 5
 * 4 3
 *
 * Output 2
 * 6.0
 * 3
 * -----------------
 * Input 3
 * 2 6
 * 5 1
 * 7 8
 * 5
 * 1 4
 * 3 5
 * 6 4
 * 6 5
 * 4 7
 *
 * Output 3
 * 15.5
 * 2
 * -----------------
 * Input 4
 * 3 2
 * 5 4
 * 1 6
 * 1
 * 6 5
 *
 * Output 4
 * 6.0
 * 0
 * -----------------
 */
public class BOJ2987 {

    private static class Vertex {
        int x, y;

        public Vertex(int x, int y) {
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

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }

        public static double triangleSize(Vertex v1, Vertex v2, Vertex v3) {
            return ccw(v1, v2, v3) * 0.5;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Vertex={");
            sb.append("x=");
            sb.append(x);
            sb.append(", y=");
            sb.append(y);
            sb.append('}');

            return sb.toString();
        }
    }

    static int N;
    static List<Vertex> triangle = new ArrayList<>(3);
    static StringBuilder result = new StringBuilder();

    public static void sortByCCW(List<Vertex> vertices) {
        Vertex base = vertices.get(0);
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

        Vertex base2 = base;
        Collections.sort(vertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                int ccwDir = Vertex.ccwDir(base2, v1, v2);
                if (ccwDir > 0) return -1;
                else if (ccwDir < 0) return 1;
                else {
                    return Integer.compare(Vertex.distSquare(base2, v1), Vertex.distSquare(base2, v2));
                }
            }
        });
    }

    public static boolean onPolygon(Vertex pos) {
        for (int i = 0; i < 3; i++) {
            Vertex v1 = triangle.get(i);
            Vertex v2 = triangle.get((i + 1) % 3);

            int xMin = Math.min(v1.x, v2.x);
            int xMax = Math.max(v1.x, v2.x);
            int yMin = Math.min(v1.y, v2.y);
            int yMax = Math.max(v1.y, v2.y);
            boolean condition1 = xMin <= pos.x && pos.x <= xMax;
            boolean condition2 = yMin <= pos.y && pos.y <= yMax;
            boolean condition3 = Vertex.ccw(pos, v1, v2) == 0;

            if (condition1 && condition2 && condition3) {
                return true;
            }
        }
        return false;
    }

    public static boolean inside(Vertex pos) {
        if (onPolygon(pos)) return true;

        for (int i = 0; i < 3; i++) {
            Vertex v1 = triangle.get(i);
            Vertex v2 = triangle.get((i + 1) % 3);
            int ccwDir = Vertex.ccwDir(v1, pos, v2);
            if (ccwDir > 0) return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            triangle.add(new Vertex(x, y));
        }
        sortByCCW(triangle);

        int cnt = 0;
        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Vertex v = new Vertex(x, y);
            if (inside(v)) cnt++;
        }

        double size = Vertex.triangleSize(triangle.get(0), triangle.get(1), triangle.get(2));
        result.append(new DecimalFormat("0.0").format(size)).append('\n').append(cnt);

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
