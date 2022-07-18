package geometry;

import java.io.*;
import java.util.*;


/**
 * 직각삼각형 - BOJ1711
 * -----------------
 * Input 1
 * 5
 * -1 1
 * -1 0
 * 0 0
 * 1 0
 * 1 1
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ1711 {

    private static class Point {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    static int N;
    static List<Point> points = new ArrayList();

    public static boolean isTriangle(Point p1, Point p2, Point p3) {
        long gapX1 = p1.x - p2.x;
        long gapY1 = p1.y - p2.y;
        long gapX2 = p2.x - p3.x;
        long gapY2 = p2.y - p3.y;
        long gapX3 = p3.x - p1.x;
        long gapY3 = p3.y - p1.y;

        long length1 = gapX1 * gapX1 + gapY1 * gapY1;
        long length2 = gapX2 * gapX2 + gapY2 * gapY2;
        long length3 = gapX3 * gapX3 + gapY3 * gapY3;

        return length1 == (length2 + length3) || length2 == (length3 + length1) || length3 == (length1 + length2);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            long x = Long.parseLong(st.nextToken());
            long y = Long.parseLong(st.nextToken());
            points.add(new Point(x, y));
        }

        int result = 0;
        for (int i = 0; i < N - 2; i++) {
            for (int j = i + 1; j < N - 1; j++) {
                for (int k = j + 1; k < N; k++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(j);
                    Point p3 = points.get(k);

                    if (isTriangle(p1, p2, p3)) {
                        result++;
                    }
                }
            }
        }

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}