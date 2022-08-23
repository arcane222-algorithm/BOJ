package geometry.convexhull;
import java.io.*;
import java.util.*;


/**
 * Building the Moat - BOJ6194
 * -----------------
 *
 * 농장의 물 웅덩이를 모두 커버할 수 있는 컨벡스 헐을 구한다.
 * Graham's Scan 알고리즘을 이용하여 컨벡스 헐을 구할 수 있고 이 결과는 기준점에 대하여 반시계 방향으로 정렬된 상태이다.
 * 그러므로 컨벡스 헐의 결과에서 앞에서부터 두점씩 선택하여 두 점의 길이를 모두 합하여 컨벡스 헐의 둘레를 구하면 답이 된다.
 *
 * -----------------
 * Input 1
 * 20
 * 2 10
 * 3 7
 * 22 15
 * 12 11
 * 20 3
 * 28 9
 * 1 12
 * 9 3
 * 14 14
 * 25 6
 * 8 1
 * 25 1
 * 28 4
 * 24 12
 * 4 15
 * 13 5
 * 26 5
 * 21 11
 * 24 4
 * 1 8
 *
 * Output 1
 * 70.87
 * -----------------
 */
public class BOJ6194 {

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

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }
    }

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

        N = Integer.parseInt(br.readLine());
        vertices = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }

        Stack<Vertex> vertexStack = convexHull();
        double dist = 0;
        for (int i = 0; i < vertexStack.size(); i++) {
            Vertex v1 = vertexStack.get(i);
            Vertex v2 = vertexStack.get((i + 1) % vertexStack.size());
            dist += Vertex.dist(v1, v2);
        }

        bw.write(String.format("%.2f", dist));

        // close the buffer
        br.close();
        bw.close();
    }
}