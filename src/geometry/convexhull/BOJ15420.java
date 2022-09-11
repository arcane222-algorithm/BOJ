package geometry.convexhull;

import java.io.*;
import java.util.*;


/**
 * Blowing Candles - BOJ15420
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 3 10
 * 0 0
 * 10 0
 * 0 10
 *
 * Output 1
 * 7.0710678118654755
 * -----------------
 */
public class BOJ15420 {

    private static class Vertex {
        static final Vertex ZERO = new Vertex(0, 0);
        long x, y;

        public Vertex(long x, long y) {
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

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
        }

        public static double distSqBetweenVertexAndLineSeg(Vertex v1, Vertex v2, Vertex v3) {
            double ccw = Vertex.ccw(v1, v2, v3);
            double dist = Vertex.distSquare(v1, v2);
            return (ccw * ccw) / dist;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    static int N, R;
    static Vertex base;
    static List<Vertex> vertices;

    public static Stack<Vertex> convexHull() {
        base = vertices.get(0);
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

        vertices.sort((v1, v2) -> {
            int ccwDir = Vertex.ccwDir(base, v1, v2);
            if (ccwDir > 0) return -1;
            else if (ccwDir < 0) return 1;
            else {
                return Long.compare(Vertex.distSquare(base, v1), Vertex.distSquare(base, v2));
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

    public static double rotatingCalipers(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();
        int l = 0, r = 0;
        for (int i = 0; i < Size; i++) {
            Vertex v = convexHull.get(i);
            if (v.x < convexHull.get(l).x) l = i;
            if (v.x > convexHull.get(r).x) r = i;
        }

        double dist = Double.MAX_VALUE;
        for (int i = 0; i < Size; i++) {
            int lNext = (l + 1) % Size;
            int rNext = (r + 1) % Size;

            Vertex lv0 = convexHull.get(l);
            Vertex lv1 = convexHull.get(lNext);
            Vertex lv2 = new Vertex(lv0, lv1);

            Vertex rv0 = convexHull.get(r);
            Vertex rv1 = convexHull.get(rNext);
            Vertex rv2 = new Vertex(rv1, rv0);

            int ccwDir = Vertex.ccwDir(Vertex.ZERO, lv2, rv2);
            if (ccwDir > 0) {
                double dist1 = Vertex.distSqBetweenVertexAndLineSeg(lv0, lv1, rv0);
                double dist2 = Vertex.distSqBetweenVertexAndLineSeg(lv0, lv1, rv1);
                dist = Math.min(Math.max(dist1, dist2), dist);
                l = lNext;
            } else {
                double dist1 = Vertex.distSqBetweenVertexAndLineSeg(rv0, rv1, lv0);
                double dist2 = Vertex.distSqBetweenVertexAndLineSeg(rv0, rv1, lv1);
                dist = Math.min(Math.max(dist1, dist2), dist);
                r = rNext;
            }
        }

        return Math.sqrt(dist);
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
        Stack<Vertex> convexHull = convexHull();
        double dist = rotatingCalipers(convexHull);
        System.out.println(String.format("%.8f", dist));

        // close the buffer
        br.close();
        bw.close();
    }
}