package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * 감옥 건설 - BOJ2254
 * -----------------
 *
 *
 * 
 *
 *
 * -----------------
 * Input 1
 * 8 -1 0
 * 2 2
 * 2 -2
 * -2 2
 * -2 -2
 * 0 10
 * 8 0
 * -12 1
 * 1 -5
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ2254 {

    private static class Vertex {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static long distSquare(Vertex v1, Vertex v2) {
            long dx = v2.x - v1.x;
            long dy = v2.y - v1.y;
            return dx * dx + dy * dy;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Vertex) {
                Vertex v = (Vertex) o;
                return x == v.x && y == v.y;
            } else {
                return false;
            }
        }
    }

    static int N, Px, Py;
    static Vertex base, P;
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

        vertices.sort(new Comparator<Vertex>() {
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
        for (int i = 0; i < vertices.size(); i++) {
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

    public static boolean inside(Stack<Vertex> convexHull) {
        for (int i = 0; i < convexHull.size(); i++) {
            Vertex iv0 = convexHull.get(i);
            Vertex iv1 = convexHull.get((i + 1) % convexHull.size());

            int ccwDir = Vertex.ccwDir(iv0, P, iv1);
            if (ccwDir > 0) return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        int px = Integer.parseInt(st.nextToken());
        int py = Integer.parseInt(st.nextToken());
        P = new Vertex(px, py);
        vertices = new ArrayList<>(N);

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            vertices.add(new Vertex(x, y));
        }

        int cnt = 0;
        while (vertices.size() > 2) {
            Stack<Vertex> convexHull = convexHull();
            if (convexHull.size() < 3) break;
            if (inside(convexHull)) cnt++;

            for (Vertex v : convexHull) {
                vertices.remove(v);
            }
        }

        bw.write(String.valueOf(cnt));

        // close the buffer
        br.close();
        bw.close();
    }
}