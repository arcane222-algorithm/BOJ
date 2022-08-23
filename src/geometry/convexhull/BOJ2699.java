package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 격자점 컨벡스헐 - BOJ2699
 * -----------------
 *
 * Convex hull을 구하는 알고리즘 중 Graham's scan 알고리즘을 사용하여 구하면 된다.
 * 해당 알고리즘 사용 시 보통 기준점을 좌측 하단 (y값이 가장 작고, 그러한 점이 여러 개라면 x값이 가장 작은 점)의 점으로 잡고
 * 점들을 기준점에 대하여 반시계 방향으로 정렬한 다음 스택에 점들을 순서대로 추가하며 새로 들어와야 할 점과 스택위의 두 점,
 * 즉 세 점을 골라 ccw 값이 > 0인지 (즉, 반시계 방향인지)를 검사하는 방식으로 동작한다.
 * (ccw 값이 > 0이면 (반시계 방향이면) 넘어가고 <= 0 (시계 방향이거나 일직선) 이면 stack 맨위의 점을 pop한다.)
 *
 * 문제에는 기준점이 좌측 상단 (y 값이 가장 크고, 그러한 점이 여러 개라면 x 값이 가장 작은 점)의 점으로 잡고
 * 점들을 기준점에 대하여 시계방향으로 정렬한다. 점들을 시계방향으로 정렬 했으므로 세 점을 골라 ccw 값을 조사할 때, 기준 역시 위의
 * 반시계 방향에서 시계 방향으로 바꾸어 생각해야 한다.
 * (즉, ccw 값이 < 0이면 (시계 방향이면 넘어가고 >=0 (반시계 방향이거나 일직선) 이면 stack 맨위의 점을 pop한다.)
 *
 * -----------------
 * Input 1
 * 4
 * 25
 * 2 1 7 1 1 2 9 2 1 3
 * 10 3 1 4 10 4 1 5 10 5
 * 2 6 10 6 2 7 9 7 3 8
 * 8 8 4 9 7 9 6 2 3 3
 * 5 4 7 5 8 6 4 6 3 7
 * 30
 * 3 9 6 9 3 8 9 8 3 7
 * 12 7 2 6 12 6 2 5 12 5
 * 2 4 12 4 1 3 11 3 1 2
 * 11 2 1 1 11 1 1 0 10 0
 * 4 -1 10 -1 7 -2 10 -2 5 0
 * 7 3 4 5 6 8 3 1 2 6
 * 3
 * 3 1 2 2 1 3
 * 6
 * 1 3 19 1 4 2 2 1 11 2
 * 10 1
 *
 * Output 1
 * 10
 * 4 9
 * 7 9
 * 10 6
 * 10 3
 * 9 2
 * 7 1
 * 2 1
 * 1 2
 * 1 5
 * 2 7
 * 8
 * 3 9
 * 6 9
 * 12 7
 * 12 4
 * 10 -2
 * 7 -2
 * 1 0
 * 1 3
 * 2
 * 1 3
 * 3 1
 * 4
 * 1 3
 * 11 2
 * 19 1
 * 2 1
 * -----------------
 */
public class BOJ2699 {

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
    }

    static int P, N;
    static Vertex base;
    static List<Vertex> vertices;
    static StringBuilder result = new StringBuilder();

    public static Stack<Vertex> convexHull() {
        base = vertices.get(0);
        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (base.y < v.y) {
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
                if (ccwDir < 0) return -1;
                else if (ccwDir > 0) return 1;
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
                if (ccwDir >= 0) stack.pop();
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

        P = Integer.parseInt(br.readLine());
        for (int i = 0; i < P; i++) {
            N = Integer.parseInt(br.readLine());
            vertices = new ArrayList<>(N);
            int cnt = N % 5 == 0 ? N / 5 : N / 5 + 1;
            for (int j = 0; j < cnt; j++) {
                st = new StringTokenizer(br.readLine());
                while (st.hasMoreTokens()) {
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());
                    vertices.add(new Vertex(x, y));
                }
            }

            Stack<Vertex> vertexStack = convexHull();
            result.append(vertexStack.size()).append('\n');
            for (int j = 0; j < vertexStack.size(); j++) {
                Vertex v = vertexStack.get(j);
                result.append(v.x).append(' ').append(v.y).append('\n');
            }
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}