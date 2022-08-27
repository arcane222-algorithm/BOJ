package geometry.convexhull;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


/**
 * 쓰레기 슈트 - BOJ4225
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 3
 * 0 0
 * 3 0
 * 0 4
 * 4
 * 0 10
 * 10 0
 * 20 10
 * 10 20
 * 0
 *
 * Output 1
 * Case 1: 2.40
 * Case 2: 14.15
 * -----------------
 */
public class BOJ4225 {

    private static class Vertex {
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static double distBetweenLineSegAndVertex(Vertex v1, Vertex v2, Vertex v3) {
            int ccw = ccw(v1, v2, v3);
            double dist = dist(v1, v2);

            return ccw / dist;
        }

        public static int distSquare(Vertex v1, Vertex v2) {
            int dx = v2.x - v1.x;
            int dy = v2.y - v1.y;
            return dx * dx + dy * dy;
        }

        public static double dist(Vertex v1, Vertex v2) {
            return Math.sqrt(distSquare(v1, v2));
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }
    }

    static int N;
    static Vertex base;
    static List<Vertex> vertices;
    static StringBuilder result = new StringBuilder();

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
                    return Integer.compare(Vertex.distSquare(base, v1), Vertex.distSquare(base, v2));
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

    public static double getMinLength(Stack<Vertex> convexHull) {
        final int Size = convexHull.size();

        double length = Double.MAX_VALUE;
        for (int i = 0; i < Size; i++) {
            int j = (i + 1) % Size;
            int k = (j + 1) % Size;
            double lengthTmp = 0;
            while (true) {
                if (k == i) break;

                Vertex iv = convexHull.get(i);
                Vertex jv = convexHull.get(j);
                Vertex kv = convexHull.get(k);
                lengthTmp = Math.max(lengthTmp, Vertex.distBetweenLineSegAndVertex(iv, jv, kv));
                k = (k + 1) % Size;
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

        String line;
        for (int t = 1; ; t++) {
            line = br.readLine();
            if (line.equals("0")) break;
            N = Integer.parseInt(line);
            vertices = new ArrayList<>(N);
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                vertices.add(new Vertex(x, y));
            }

            Stack<Vertex> convexHull = convexHull();
            double length = Math.ceil(getMinLength(convexHull) * 100) * 0.01;
            result.append("Case ").append(t).append(": ").append(new DecimalFormat("0.00").format(length)).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
