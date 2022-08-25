package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 달리기 코스 - BOJ1310
 * -----------------
 *
 *  캠프의 기둥 중 가장 먼 기둥의 거리의  제곱을 출력하면 된다.
 *  캠프의 기둥을 정점이라고 할때, Graham's scan 알고리즘을 이용하여 O(NlogN)에 Convex hull을 구한 후,
 *  rotating calipers 알고리즘을 이용하여 O(N)만에 가장 먼 두 점의 거리를 구하면 된다.
 *
 * -----------------
 * Input 1
 * 5
 * 1 1
 * 5 1
 * 3 3
 * 2 4
 * 6 5
 *
 * Output 1
 * 41
 * -----------------
 */
public class BOJ1310 {

    private static class Vertex {

        static final Vertex ZERO = new Vertex(0, 0);
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vertex(Vertex v1, Vertex v2) {
            this.x = v2.x - v1.x;
            this.y = v2.y - v1.y;
        }

        public static long distSquare(Vertex v1, Vertex v2) {
            long dx = v2.x - v1.x;
            long dy = v2.y - v1.y;
            return dx * dx + dy * dy;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (long) (v2.x - v1.x) * (v3.y - v1.y) - (long) (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }
    }

    static int N;
    static List<Vertex> vertices;

    public static Stack<Vertex> convexHull() {
        Vertex base = vertices.get(0);
        for (int i = 1; i < N; i++) {
            Vertex v = vertices.get(i);
            if (base.y > v.y) {
                base = v;
            } else if (base.y == v.y) {
                if (base.x > v.x) {
                    base = v;
                }
            }
        }

        Vertex finalBase = base;
        vertices.sort(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                int ccwDir = Vertex.ccwDir(finalBase, v1, v2);
                if (ccwDir > 0) return -1;
                else if (ccwDir < 0) return 1;
                else {
                    return Long.compare(Vertex.distSquare(finalBase, v1), Vertex.distSquare(finalBase, v2));
                }
            }
        });

        Stack<Vertex> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
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

    public static long rotatingCalipers(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();
        int i = 0, j = 0;
        for (int k = 0; k < Size; k++) {
            Vertex v = convexHull.get(k);
            if (v.x < convexHull.get(i).x) i = k;
            if (v.x > convexHull.get(j).x) j = k;
        }

        long distSquare = Vertex.distSquare(convexHull.get(i), convexHull.get(j));
        for (int k = 0; k < Size; k++) {
            int iNext = (i + 1) % Size;
            int jNext = (j + 1) % Size;

            Vertex iv0 = convexHull.get(i);
            Vertex iv1 = convexHull.get(iNext);
            Vertex iv2 = new Vertex(iv0, iv1);

            Vertex jv0 = convexHull.get(j);
            Vertex jv1 = convexHull.get(jNext);
            Vertex jv2 = new Vertex(jv0, jv1);

            int ccwDir = Vertex.ccwDir(Vertex.ZERO, iv2, jv2);
            if (ccwDir > 0) j = jNext;
            else i = iNext;

            distSquare = Math.max(distSquare, Vertex.distSquare(convexHull.get(i), convexHull.get(j)));
        }

        return distSquare;
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

        Stack<Vertex> convexHull = convexHull();
        long dist = rotatingCalipers(convexHull);

        bw.write(String.valueOf(dist));

        // close the buffer
        br.close();
        bw.close();
    }
}