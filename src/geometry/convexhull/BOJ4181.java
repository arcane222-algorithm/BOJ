package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * Convex Hull - BOJ4181
 * -----------------
 *
 * 문제에서 컨벡스 헐을 이루는 점들을 이미 구했으므로 이 점들을 Graham's Scan 알고리즘의 전처리 과정중 반시계 방향으로 정렬하는 작업만 수행해주면 된다.
 * 주어진 점들 중 Y인 점들을 List에 담아 x좌표가 가장 작고 그러한 점이 여러 개라면 y좌표가 가장 작은 base 점을 찾는다.
 * base점을 기준으로 List안의 점들을 반시계 방향으로 정렬해주면 된다.
 *
 * 주의할 점은, 반시계 방향으로 정렬할 때 비교 중인 두 점 v1, v2에 대하여 만약 base 점과 ccw 값이 0인 (즉, base 점과 일직선을 이루는 경우) 경우
 * 거리가 짧은 쪽을 앞쪽으로 오도록 정렬하는데 x축에 대해서는 상관이 없지만 y축에 대하여 비교할 때, 점이 역순으로 쌓이게 된다.
 * (즉, 예제 Input 2에 대하여 마지막 점 (0, 1), (0, 2)는 반시계 방향에 따라 (0, 2), (0, 1)로 출력되는 것이 맞는데 거리가 짧은 순인 (0, 1), (0, 2)로 출력된다.)
 * (이 경우를 고려하는 것은 BOJ3679 - 단순 다각형 과 유사한 케이스이다.)
 *
 * List안의 점들을 우선 반시계 방향으로 정렬한 후 점들을 뒤에서부터 조사하며 ccw값이 0이라면 조사중인 마지막 점을 queue에 담아
 * 앞의 점들 (List.size - Queue.size 만큼의 크기)은 원래 순서대로 출력하고
 * 뒤 base와 ccw 값이 0인 점들은 queue에서 하나씩 dequeue하여 출력하면 역순으로 출력되므로 올바른 답이 된다.
 *
 * -----------------
 * Input 1
 * 5
 * 1 1 Y
 * 1 -1 Y
 * 0 0 N
 * -1 -1 Y
 * -1 1 Y
 *
 * Output 1
 * 4
 * -1 -1
 * 1 -1
 * 1 1
 * -1 1
 * -----------------
 * Input 2
 * 8
 * 0 0 Y
 * 0 1 Y
 * 1 0 Y
 * 0 2 Y
 * 2 0 Y
 * 1 2 Y
 * 2 1 Y
 * 2 2 Y
 *
 *
 * Output 2
 * 8
 * 0 0
 * 1 0
 * 2 0
 * 2 1
 * 2 2
 * 1 2
 * 0 2
 * 0 1
 * -----------------
 * Input 2
 * 12
 * 0 0 Y
 * 0 1 Y
 * 0 2 Y
 * 0 3 Y
 * 1 3 Y
 * 2 3 Y
 * 3 3 Y
 * 3 2 Y
 * 3 1 Y
 * 3 0 Y
 * 2 0 Y
 * 1 0 Y
 *
 * Output 2
 * 12
 * 0 0
 * 1 0
 * 2 0
 * 3 0
 * 3 1
 * 3 2
 * 3 3
 * 2 3
 * 1 3
 * 0 3
 * 0 2
 * 0 1
 * -----------------
 */
public class BOJ4181 {

    private static class Vertex {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
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
    static List<Vertex> vertices = new ArrayList<>();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            char check = st.nextToken().charAt(0);
            if (check == 'Y') {
                vertices.add(new Vertex(x, y));
            }
        }

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

        Queue<Vertex> queue = new LinkedList<>();
        for (int i = vertices.size() - 1; i > 0; i--) {
            int ccwDir = Vertex.ccwDir(base, vertices.get(i - 1), vertices.get(i));
            queue.add(vertices.get(i));
            if (ccwDir != 0) {
                break;
            }
        }

        result.append(vertices.size()).append('\n');
        for (int i = 0; i < vertices.size() - queue.size(); i++) {
            Vertex v = vertices.get(i);
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