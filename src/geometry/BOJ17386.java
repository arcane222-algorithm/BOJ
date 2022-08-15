package geometry;



/**
 * 선분 교차 1 - BOJ17386
 * -----------------
 * Input 1
 * 1 1 5 5
 * 1 5 5 1
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 1 1 5 5
 * 6 10 10 6
 *
 * Output 2
 * 0
 * -----------------
 */
import java.io.*;
import java.util.StringTokenizer;

public class BOJ17386 {

    private static class Vertex {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        private long ccw(Vertex v1, Vertex v2) {
            return (x * v1.y + v1.x * v2.y + v2.x * y) -
                    (y * v1.x + v1.y * v2.x + v2.y * x);
        }

        public int ccwDirection(Vertex v1, Vertex v2) {
            long ccwVal = ccw(v1, v2);
            return ccwVal < 0 ? -1 : (ccwVal == 0 ? 0 : 1);   // val > 0: ccw, val < 0: cw
        }
    }

    private static class LineSegment {
        Vertex v1, v2;

        public LineSegment(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int checkCross(LineSegment ls) {
            int ccw1 = v1.ccwDirection(ls.v1, ls.v2);
            int ccw2 = v2.ccwDirection(ls.v1, ls.v2);
            int ccw3 = ls.v1.ccwDirection(v1, v2);
            int ccw4 = ls.v2.ccwDirection(v1, v2);

            if(ccw1 != ccw2 && ccw3 != ccw4) return 1;  // cross
            else return 0;  // not cross
        }
    }

    static final int N = 2;

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        LineSegment[] lines = new LineSegment[N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            lines[i] = new LineSegment(new Vertex(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken())),
                    new Vertex(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken())));
        }

        bw.write(String.valueOf(lines[0].checkCross(lines[1])));
        br.close();
        bw.close();
    }
}

