package geometry.convexhull;

import java.io.*;
import java.util.*;



/**
 * Breaking Biscuits - BOJ15028
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 4
 * 0 0
 * 5 0
 * 5 2
 * 0 2
 *
 * Output 1
 * 2.0
 * -----------------
 * Input 2
 * 6
 * 81444 14017
 * 80944 13517
 * 81127 12834
 * 81810 12651
 * 82310 13151
 * 82127 13834
 *
 * Output 2
 * 1224.7089450046291
 * -----------------
 * Input 3
 * 8
 * 197 239
 * 208 246
 * 221 241
 * 250 254
 * 220 265
 * 211 258
 * 198 268
 * 163 256
 *
 * Output 3
 * 28.816782
 * -----------------
 */
public class BOJ15028 {

    private static class Vertex {
        static Vertex ZERO = new Vertex(0, 0);
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

        public static double distBetweenLineSegAndVertex(Vertex v1, Vertex v2, Vertex v3) {
            long ccw = Vertex.ccw(v1, v2, v3);
            if (ccw < 0) ccw = -ccw;
            double dist = dist(v1, v2);

            return ccw / dist;
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

    public static double getMaxLength(Stack<Vertex> convexHull) {
        double length = Double.MAX_VALUE;
        for (int i = 0; i < convexHull.size(); i++) {
            int j = (i + 1) % convexHull.size();
            int k = (j + 1) % convexHull.size();
            double lengthTmp = 0;
            while (true) {
                if (k == i) break;
                Vertex iv = convexHull.get(i);
                Vertex jv = convexHull.get(j);
                Vertex kv = convexHull.get(k);
                lengthTmp = Math.max(lengthTmp, Vertex.distBetweenLineSegAndVertex(iv, jv, kv));
                k = (k + 1) % convexHull.size();
            }
            length = Math.min(length, lengthTmp);
        }

        return length;
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
        double length = getMaxLength(convexHull);
        bw.write(String.valueOf(length));

        // close the buffer
        br.close();
        bw.close();
    }
}