package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 볼록 껍질 - BOJ1708
 * -----------------
 *
 *  Convex hull (컨벡스 헐, 볼록 껍질) 알고리즘의 기본 문제이다.
 *  Convex hull 알고리즘에는 jarvis march - O(N^2), Monotone Chain - O(NlogN), Graham Scan - O(NlogN)등 여러 종류가 있다.
 *  Monotone Chain과 Graham Scan의 경우 O(NlogN)에 볼록 껍질을 구할 수 있는 알고리즘인데,
 *  Monotone Chain의 경우 각 점을 x 또는 y축에 대하여 정렬한 후, ccw를 이용하여 위 아래 두 개의 볼록 껍질을 구한 후 두 껍질을 합쳐주는 방식으로 동작한다.
 *  (Graham Scan 방식과 같이 동작하는데, 좌표순으로만 정렬 했기 때문에 위 아래 껍질 두개를 구해서 합친다)
 *
 *  아래 문제에서 사용된 방식은 Graham Scan 알고리즘이다. Graham Scan 알고리즘의 경우 y좌표가 가장 작은 시작점 (base)를 찾는다.
 *  (만약 y좌표가 가장 작은 시작점이 여러 개라면, 그중 x좌표가 가장 작은 점을 선택한다.)
 *  이 base 지점에 대하여 각 점들을 반시계 방향으로 정렬한다.
 *  반시계 방향으로 정렬하기 위해서는 base와 두 점 v1, v2를 선택하여 ccw 한 값을 비교하여 정렬한다.
 *  만약 ccw(base, v1, v2) > 0 이라면, 반시계 방향을 이루고 있으므로 비교하려는 두 점 v1, v2의 순서를 유지한다.
 *  만약 ccw(base, v1, v2) < 0 이라면, 시계 방향을 이루고 있으므로 비교하려는 두 점 v1, v2의 순서를 바꾼다.
 *  만약 ccw(base, v1, v2) = 0 이라면, 세 점이 직선을 이루고 있으므로 두 점과 base의 거리를 비교하여 base와 가까운 순으로 정렬한다.
 *
 *  base와의 반시계 방향으로 각도 순으로 정렬을 완료했다면 stack을 이용하여 convex hull을 구한다.
 *  stack에 base 점을 넣고 그 다음 정렬한 순서대로 점을 추가하는데,
 *  만약 stack에 점 두개가 들어가면 다음 세번 째 점을 얻어 스텍 안의 상위 두 점고 함께 세 점을 ccw 하고, 만약 ccw <= 0이라면 (시계 방향이거나 직선),
 *  convex hull될 수 없으므로 stack에서 pop한다. (ccw가 양수라면 세 점이 시계방향을 이루고 있고 이것은 오목 다각형이 되어 convex 하지 않다.)
 *  ccw > 0이라면, convex hull이 될 수 있으므로 stack에 들어있는 상태로 다음 점을 탐색한다.
 *
 *  여기서 주의할 점은, stack에 vertex(1), ... , vertex(i - 2), vertex (i - 1) 까지 들어온 상황에서
 *  vertex(i)를 선택하여 vertex(i - 2), vertex (i - 1), vertex(i) 세 점의 ccw를 계산하고 위의 설명처럼 조건에 맞지 않다면
 *  stack의 맨위, 즉, vertex(i - 1)를 pop 하는 것이다.
 *  (왜냐하면 v1, v2, v3를 비교할 때 시계, 반시계 방향을 결정하는 것은 가운데 점 v2이기 때문이다.)
 *  (즉, ccw값이 조건에 맞지 않다면 vertex(i) 혹은 v3 (현재 비교하려는 점)을 discard 하는 것이 아니라 스텍의 맨위에 있는 점 (vertex(i - 1) 혹은 v2)를 pop 해야 한다.)
 *
 *  이때, 새로들어온 점 vertex(i)에 대하여 stack 위의 두 점을 계속 선택하며 만약 ccw <= 0 인 경우가 있다면 반복하며 전부 pop 하도록 한다.
 *  (맨 위의 점말고 기존에 들어있는 점들과도 ccw를 비교해서 오목 다각형이 되지 않도록 해야한다.)
 *
 * -----------------
 * Input 1
 * 8
 * 1 1
 * 1 2
 * 1 3
 * 2 1
 * 2 2
 * 2 3
 * 3 1
 * 3 2
 *
 * Output 1
 * 5
 * -----------------
 */
public class BOJ1708 {

    private static class Vertex {
        long x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            long ccw = ccw(v1, v2, v3);
            return Long.compare(ccw, 0);
        }
    }

    static int N;
    static Vertex base;
    static Vertex[] vertices;

    public static long dist(Vertex v1, Vertex v2) {
        return (v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y);
    }

    public static Stack<Vertex> convexHull() {
        // y좌표가 가장 작은 값을 base로 설정함
        // y좌표가 가장 작은 base가 여러 개라면, 그 중 x좌표가 가장 작은 위치를 base로 설정함
        base = vertices[0];
        for (int i = 1; i < N; i++) {
            if (base.y > vertices[i].y) {
                base = vertices[i];
            } else if (base.y == vertices[i].y) {
                if (base.x > vertices[i].x) {
                    base = vertices[i];
                }
            }
        }

        // ccw를 이용해서 base점에 대하여 각 점들을 반시계 방향으로 정렬함
        // ccw값이 > 0 이라면, 반시계 방향이므로 (-1, 두 값의 위치를 유지함) return
        // ccw값이 < 0 이라면, 시계 방향이므로 (1, 두 값의 위치를 바꿈) return
        // ccw값이 0이라면, 세 점이 한 직선위에 있는 상황이므로 비교하려는 두 점과 base 사이의 거리를 측정하여 가까운 점을 기준으로 정렬
        Arrays.sort(vertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                int ccwDir = Vertex.ccwDir(base, v1, v2);
                if (ccwDir > 0) return -1;
                else if (ccwDir < 0) return 1;
                else {
                    long dist1 = dist(base, v1);
                    long dist2 = dist(base, v2);
                    return Long.compare(dist1, dist2);
                }
            }
        });

        // stack에 점 두개가 들어가면 반시계 방향으로 다음 세번 째 점을 얻어 세 점을 ccw 하고, 만약 ccw <= 0이라면 (시계 방향이거나 직선),
        // convex hull될 수 없으므로 stack에서 pop함
        // 여기서 주의할 점은, stack에 vertex(1), ... , vertex(i - 2), vertex (i - 1) 까지 들어온 상황에서
        // vertex(i)를 선택하여 vertex(i - 2), vertex (i - 1), vertex(i) 세 점의 ccw를 계산하고 위의 설명처럼 조건에 맞지 않다면
        // stack의 맨위, 즉, vertex(i - 1)를 pop 하는 것이다. (왜냐하면 v1, v2, v3를 비교할 때 시계, 반시계 방향을 결정하는 것은 가운데 점 v2이기 때문이다)
        // 이때, 새로들어온 점 vertex(i)에 대하여 stack 위의 두 점을 계속 선택하며 만약 ccw <= 0 인 경우가 있다면 반복하며 전부 pop 하도록 한다.
        Stack<Vertex> stack = new Stack<>();
        stack.add(base);
        for (int i = 1; i < N; i++) {
            while (stack.size() > 1) {
                int ccwDir = Vertex.ccwDir(vertices[i], stack.get(stack.size() - 2), stack.get(stack.size() - 1));
                if (ccwDir <= 0) stack.pop();
                else break;
            }
            stack.add(vertices[i]);
        }

        return stack;
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices[i] = new Vertex(x, y);
        }

        bw.write(String.valueOf(convexHull().size()));

        // close the buffer
        br.close();
        bw.close();
    }
}
