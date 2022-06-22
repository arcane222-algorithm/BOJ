package implementation;

import java.io.*;
import java.util.*;

/**
 * 드래곤 커브 - BOJ 15685
 * -----------------
 * Input 1
 * 3
 * 3 3 0 1
 * 4 2 1 3
 * 4 2 2 1
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 4
 * 50 50 0 10
 * 50 50 1 10
 * 50 50 2 10
 * 50 50 3 10
 *
 * Output 2
 * 1992
 * -----------------
 */
public class BOJ15685 {

    private static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Point{");
            sb.append("x=");
            sb.append(x);
            sb.append(", y=");
            sb.append(y);
            sb.append('}');

            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            Point p = (Point) obj;
            return (x == p.x) && (y == p.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static final int[] dirX = {1, 0, -1, 0};
    static final int[] dirY = {0, -1, 0, 1};
    static final int MAP_SIZE = 101;

    static int N;
    static int x, y, d, g;

    public static Point rotate(Point point, Point pivot) {
        int x = -point.y + pivot.x + pivot.y;
        int y = point.x - pivot.x + pivot.y;
        return new Point(x, y);
    }

    public static void processGeneration(int[][] grid, int x, int y, int d, int g) {
        Point pivot, pivotTmp = null;
        List<Point> points = new ArrayList<>();
        points.add(new Point(x, y));
        points.add(new Point(x + dirX[d], y + dirY[d]));
        pivot = points.get(points.size() - 1);

        for(int i = 0; i < g; i++) {
            final int len = points.size();

            for(int j = 0; j < len; j++) {
                Point p = points.get(j);
                Point r = rotate(p, pivot);
                points.add(r);
                if(p.x == x && p.y == y) {
                    pivotTmp = r;
                }
            }
            pivot = pivotTmp;
        }

        for(int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            grid[p.y][p.x] = 1;
        }
    }

    public static int countSquare(int[][] grid) {
        int cnt = 0;
        for(int i = 0; i < MAP_SIZE - 1; i++) {
            for(int j = 0; j < MAP_SIZE - 1; j++) {
                if(grid[j][i] != 1) continue;
                if(grid[j + 1][i] != 1) continue;
                if(grid[j][i + 1] != 1) continue;
                if(grid[j + 1][i + 1] != 1) continue;
                cnt++;
            }
        }

        return cnt;
    }

    public static void dumpGrid(int[][] grid) {
        for(int i = 0; i < grid.length; i++) {
            System.out.println(Arrays.toString(grid[i]));
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;
        int[][] grid = new int[MAP_SIZE][MAP_SIZE];

        // parse N
        N = Integer.parseInt(br.readLine());
        for(int i = 0; i < N; i++) {
            // parse x, y, d, g
            st = new StringTokenizer(br.readLine());
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());
            d = Integer.parseInt(st.nextToken());
            g = Integer.parseInt(st.nextToken());

            // Process dragon curve's generation
            processGeneration(grid, x, y, d, g);
        }

        // write the answer
        int answer = countSquare(grid);
        bw.write(String.valueOf(answer));

        // close the buffer
        br.close();
        bw.close();
    }
}
