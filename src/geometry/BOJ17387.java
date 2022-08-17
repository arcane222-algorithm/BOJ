package geometry;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 선분 교차 2 - BOJ17387
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
 * Input 3
 * 1 1 5 5
 * 5 5 1 1
 *
 * Output 3
 * 1
 * -----------------
 * Input 4
 * 1 1 5 5
 * 3 3 5 5
 *
 * Output 4
 * 1
 * -----------------
 * Input 5
 * 1 1 5 5
 * 3 3 1 3
 *
 * Output 5
 * 1
 * -----------------
 */
public class BOJ17387 {

    private static class Vertex implements Comparable<Vertex>{
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

        @Override
        public int compareTo(Vertex v) {
            if(y == v.y)
                return Long.compare(x, v.x);
            else
                return Long.compare(y, v.y);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Vertex {");
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

        public LineSegment(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int checkCross(LineSegment ls) {
            int ccw1 = v1.ccwDirection(ls.v1, ls.v2);
            int ccw2 = v2.ccwDirection(ls.v1, ls.v2);
            int ccw3 = ls.v1.ccwDirection(v1, v2);
            int ccw4 = ls.v2.ccwDirection(v1, v2);

            // 한 직선 위에 네 점이 모두 있는 경우 먼저 체크
            if(ccw1 * ccw2 == 0 && ccw3 * ccw4 == 0) {
                // ((line1) sm1 ----- lg1  (line2) sm2 ------ lg2)
                Vertex sm1 = v1.compareTo(v2) < 0 ? v1 : v2;
                Vertex lg1 = v1.compareTo(v2) >= 0 ? v1 : v2;
                Vertex sm2 = ls.v1.compareTo(ls.v2) < 0 ? ls.v1 : ls.v2;
                Vertex lg2 = ls.v1.compareTo(ls.v2) >= 0 ? ls.v1 : ls.v2;

                // If (sm2 ------ lg2   sm1 ----- lg1) need to swap
                // (sm2 ------ lg2   sm1 ----- lg1) > (swap) > (sm1 ----- lg1    sm2 ------ lg2)
                Vertex temp;
                if(sm1.compareTo(sm2) >= 0) {
                    temp = sm1;
                    sm1 = sm2;
                    sm2 = temp;

                    temp = lg1;
                    lg1 = lg2;
                    lg2 = temp;
                }

                if(lg1.compareTo(sm2) >= 0 || sm1.compareTo(lg2) >= 0)
                    return 1;
                else
                    return 0;
            }

            if(ccw1 * ccw2 <= 0 && ccw3 * ccw4 <= 0)
                return 1;  // cross
            else
                return 0;  // not cross
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("LineSegment {");
            sb.append(v1.toString());
            sb.append(", ");
            sb.append(v2.toString());
            sb.append('}');

            return sb.toString();
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