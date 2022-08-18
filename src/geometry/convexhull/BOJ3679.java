package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 단순 다각형 - BOJ3679
 * -----------------
 * <p>
 * Convex hull을 구하는 알고리즘 중 graham scan 알고리즘 과정처럼 각 점들 중 가장 좌측 하단에 있는 점을 찾아 base로 설정한 후
 * base를 기준으로 반시계 방향으로 정렬해주면 된다.
 * convex hull은 각 점에 대하여 모든 vertex를 포함할 수 있는 볼록 다각형을 찾는 경우이지만,
 * 이 문제의 경우 볼록함과 오목함이 모두 공존하는 '단순 다각형' 이므로 위 정렬한 순서대로 점을 선택하면 단순 다각형을 완성할 수 있다.
 * <p>
 * 주의 할 점은 마지막 점들에 대하여 base와 직선을 이루는 경우 (즉, ccw 값이 0인 경우) 역순으로 출력해주어야 한다.
 * why?) 1
 * 5 0 0 1 0 2 0 0 1 0 2 입력에 대하여 좌표평면 위에 점을 찍어보면 아래와 같은 그림이 나온다.
 *  .
 *  .
 *  .  .  .
 *
 * 이 경우 정렬한 순서대로 좌표를 고르면 (0, 0), (0, 1), (0, 2), (0, 1), (0, 2)가 되고 0 1 2 3 4를 출력할 것이다.
 * 하지만 그렇게 선을 연결할 경우 단순다각형을 이루지 않는다. 그러므로 마지막 점들에 대하여 시작점(base)와 일직선을 이루는 점들은 역순으로 출력해야 정상적인
 * 다각형을 만들 수 있게 된다. 점들을 뒤에서부터 탐색하여 뒤의 두 점과 base 점이 ccw != 0 까지 마지막 점을 따로 저장하고
 * 역순으로 출력해야 하는 점 이전 까지는 정상적인 순서로 출력하고, 마지막 점들의 경우 역순으로 출력해주면 된다.
 * (queue를 이용하여 역순으로 출력해야 하는 점들의 경우, queue에 삽입하여 앞의 점들을 순방향으로 출력 후 queue의 점들을 출력하였다.)
 *
 * (위의 설명은 즉, 뒤에서부터 base와 두 점 v(i - 2), v(i - 1) 의 ccw를 검사할 때, ccw(base, v(i - 2), v(i - 1)) != 0 까지 마지막 점 (v(i - 1))을 queue에 넣어주면 된다)
 * <p>
 * -----------------
 * Input 1
 * 2
 * 4 0 0 2 0 0 1 1 0
 * 5 0 0 10 0 10 5 5 -1 0 5
 * <p>
 * Output 1
 * 0 3 1 2
 * 3 1 2 4 0
 * -----------------
 * Input 2
 * 1
 * 5 0 0 1 0 2 0 0 1 0 2
 * <p>
 * Output 2
 * 0 1 2 4 3
 * -----------------
 */
public class BOJ3679 {

    private static class Vertex {
        int x, y, idx;

        public Vertex(int x, int y, int idx) {
            this.x = x;
            this.y = y;
            this.idx = idx;
        }

        public int dist(Vertex v) {
            return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }
    }

    static int n, c;
    static Vertex base;
    static List<Vertex> vertices;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        c = Integer.parseInt(br.readLine());
        for (int i = 0; i < c; i++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            vertices = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                vertices.add(new Vertex(x, y, j));
            }

            // find a base point
            base = vertices.get(0);
            for (int j = 1; j < vertices.size(); j++) {
                Vertex v = vertices.get(j);
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
                        int dist1 = base.dist(v1);
                        int dist2 = base.dist(v2);
                        return Integer.compare(dist1, dist2);
                    }
                }
            });

            Queue<Vertex> queue = new LinkedList<>();
            result.append(base.idx).append(' ');
            for (int j = vertices.size() - 1; j >= 1; j--) {
                int ccwDir = Vertex.ccwDir(base, vertices.get(j), vertices.get(j - 1));
                queue.add(vertices.get(j));
                if (ccwDir != 0) {
                    break;
                }
            }

            for (int j = 1; j < vertices.size() - queue.size(); j++) {
                Vertex v = vertices.get(j);
                result.append(v.idx).append(' ');
            }
            while (!queue.isEmpty()) {
                result.append(queue.poll().idx).append(' ');
            }
            result.append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}