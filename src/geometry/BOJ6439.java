package geometry;

import java.io.*;
import java.util.*;


/**
 * 교차 - BOJ6439
 * -----------------
 *
 * -----------------
 * Input 1
 * 1
 * 4 9 11 2 1 5 7 1
 *
 * Output 1
 * F
 * -----------------
 */
public class BOJ6439 {

    private static class Vertex implements Comparable<Vertex> {
        int x, y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Integer.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public int compareTo(Vertex v) {
            if (y == v.y) return Integer.compare(x, v.x);
            else return Integer.compare(y, v.y);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Vertex={");
            sb.append("x=");
            sb.append(x);
            sb.append(", y=");
            sb.append(y);
            sb.append('}');

            return sb.toString();
        }
    }

    private static class LineSegment {
        Vertex v1, v2;

        public LineSegment(int x1, int y1, int x2, int y2) {
            this(new Vertex(x1, y1), new Vertex(x2, y2));
        }

        public LineSegment(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int checkCross(LineSegment ls) {
            int ccwDir1 = Vertex.ccwDir(v1, v2, ls.v1);
            int ccwDir2 = Vertex.ccwDir(v1, v2, ls.v2);
            int ccwDir3 = Vertex.ccwDir(ls.v1, ls.v2, v1);
            int ccwDir4 = Vertex.ccwDir(ls.v1, ls.v2, v2);

            if (ccwDir1 == 0 && ccwDir2 == 0) {
                Vertex sm1 = v1.compareTo(v2) < 0 ? v1 : v2;
                Vertex lg1 = v1.compareTo(v2) >= 0 ? v1 : v2;
                Vertex sm2 = ls.v1.compareTo(ls.v2) < 0 ? ls.v1 : ls.v2;
                Vertex lg2 = ls.v1.compareTo(ls.v2) >= 0 ? ls.v1 : ls.v2;

                Vertex tmp;
                if (sm1.compareTo(sm2) > 0) {
                    tmp = sm1;
                    sm1 = sm2;
                    sm2 = tmp;

                    tmp = lg1;
                    lg1 = lg2;
                    lg2 = tmp;
                }

                if (lg1.compareTo(sm2) > 0) {
                    return 1;
                } else if (lg1.compareTo(sm2) == 0) {
                    return 2;
                } else {
                    return 0;
                }
            }

            if (ccwDir1 * ccwDir2 <= 0 && ccwDir3 * ccwDir4 <= 0) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("LineSegment={");
            sb.append(v1.toString());
            sb.append(", ");
            sb.append(v2.toString());
            sb.append('}');

            return sb.toString();
        }
    }

    static int T;
    static StringBuilder result = new StringBuilder();

    public static boolean checkCross(LineSegment ls, int sx1, int sy1, int sx2, int sy2) {
        int minX = Math.min(sx1, sx2);
        int maxX = Math.max(sx1, sx2);
        int minY = Math.min(sy1, sy2);
        int maxY = Math.max(sy1, sy2);

        boolean condition1 = inRange(minX, maxX, ls.v1.x) && inRange(minY, maxY, ls.v1.y);
        boolean condition2 = inRange(minX, maxX, ls.v2.x) && inRange(minY, maxY, ls.v2.y);
        if (condition1 && condition2) return true;

        Vertex v0 = new Vertex(minX, minY);
        Vertex v1 = new Vertex(maxX, minY);
        Vertex v2 = new Vertex(maxX, maxY);
        Vertex v3 = new Vertex(minX, maxY);

        LineSegment ls0 = new LineSegment(v0, v1);
        LineSegment ls1 = new LineSegment(v1, v2);
        LineSegment ls2 = new LineSegment(v2, v3);
        LineSegment ls3 = new LineSegment(v3, v0);

        int cnt = ls0.checkCross(ls) + ls1.checkCross(ls) + ls2.checkCross(ls) + ls3.checkCross(ls);
        return cnt > 0;
    }

    public static boolean inRange(int min, int max, int val) {
        return min <= val && val <= max;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            LineSegment ls = new LineSegment(x1, y1, x2, y2);

            int sx1 = Integer.parseInt(st.nextToken());
            int sy1 = Integer.parseInt(st.nextToken());
            int sx2 = Integer.parseInt(st.nextToken());
            int sy2 = Integer.parseInt(st.nextToken());

            result.append(checkCross(ls, sx1, sy1, sx2, sy2) ? 'T' : 'F').append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}