package geometry;

import java.io.*;
import java.util.*;


/**
 * 교차점 - BOJ10255
 * -----------------
 *
 * 선분 교차 판정 2 (BOJ17387)을 이용한 문제이다.
 * 직사각형의 네 변을 이루는 선분 4개와 주어지는 선분 1개의 교점의 개수를 구하면 된다.
 * 이 때, 각 네 변의 선분과 주어지는 선분 1개의 교점 중 모서리와 교차하는 경우가 존재한다. 이 경우의 예외처리를 잘 해주면 된다.
 * 각 네 선분과의 교점 판정 중 하나의 선분이라도 무한히 만나는 경우가 존재하면 4를 출력한다.
 * 이 경우를 제외하고 각 선분과 만나는 교점의 개수를 합한 값을 sum이라고 할 때, sum 값이 0이면 당연히 만나는 지점이 없으므로 0을 출력한다.
 * sum 값이 1일 때, 무조건 모서리가 아닌 한 지점과 만나는 점이 있으므로 1을 출력한다.
 * sum 값이 3일 때, 한 코너점과 다른 코너점이 아닌 변과 만나 세 점에서 만난다고 나타내는 것이므로 2를 출력한다.
 * sum 값이 4일 때, 주어지는 선분이 직사각형의 두 코너점을 지나 각 네 변에 대하여 전부 교점이 존재한다고 판정하여 4개가 나오는 것이므로 실제로는 두 점과 만나는 것이므로 2를 출력한다.
 * sum 값이 2일 때, 서로 만나지 않는 직사각형의 두 변 (변 1, 변 3 or 변 2, 변 4)과 주어지는 선분이 만난다면 코너점을 통과하는 것이 아니므로 2를 출력한다.
 *                그렇지 않다면 어느 한 코너점과 접하기 때문에 한 점과 만나는 것임에도 두 선분에서 교차판정을 내어 두 점에서 만난다고 (sum이 2라고) 판정하는 것인데,
 *                이 경우 주어지는 선분위의 두 점과 직사각형의 코너점과 ccw를 하여 0이 나온다면, 코너를 지나는 직선이 존재하므로 이 경우 1을 출력한다.
 *
 * -----------------
 * Input 1
 * 16
 * 0 0 8 4
 * 2 6 -2 3
 * 0 0 8 4
 * 0 4 9 4
 * 0 0 8 4
 * 3 5 6 6
 * 0 0 8 4
 * -2 5 10 -1
 * 0 0 8 4
 * 0 5 8 5
 * 0 0 8 4
 * 4 3 4 1
 * 0 0 8 4
 * -2 3 2 5
 * 0 0 8 4
 * 2 4 6 4
 * 0 0 8 4
 * 0 4 4 7
 * 0 0 8 4
 * 4 2 4 4
 * 0 0 8 4
 * 4 2 8 4
 * 0 0 8 4
 * 0 2 3 4
 * 0 0 8 4
 * -4 0 12 4
 * 0 0 8 4
 * 4 8 4 -1
 * 0 0 8 4
 * 0 -2 0 6
 * 0 0 8 4
 * 3 4 10 4
 *
 * Output 1
 * 0
 * 4
 * 0
 * 2
 * 0
 * 0
 * 1
 * 4
 * 1
 * 1
 * 1
 * 2
 * 2
 * 2
 * 4
 * 4
 * -----------------
 */
public class BOJ10255 {

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

        public LineSegment(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int checkCross(LineSegment line) {
            int ccwDir1 = Vertex.ccwDir(v1, v2, line.v1);
            int ccwDir2 = Vertex.ccwDir(v1, v2, line.v2);
            int ccwDir3 = Vertex.ccwDir(line.v1, line.v2, v1);
            int ccwDir4 = Vertex.ccwDir(line.v1, line.v2, v2);

            if (ccwDir1 == 0 && ccwDir2 == 0) {
                Vertex sm1 = v1.compareTo(v2) < 0 ? v1 : v2;
                Vertex lg1 = v1.compareTo(v2) >= 0 ? v1 : v2;
                Vertex sm2 = line.v1.compareTo(line.v2) < 0 ? line.v1 : line.v2;
                Vertex lg2 = line.v1.compareTo(line.v2) >= 0 ? line.v1 : line.v2;

                Vertex temp;
                if (sm1.compareTo(sm2) >= 0) {
                    temp = sm1;
                    sm1 = sm2;
                    sm2 = temp;

                    temp = lg1;
                    lg1 = lg2;
                    lg2 = temp;
                }

                if (lg1.compareTo(sm2) >= 0 || sm1.compareTo(lg2) >= 0) {
                    if (lg1.x == sm2.x && lg1.y == sm2.y)
                        return 1;
                    else
                        return -1;
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

    public static int getIntersectionCount(int inter1, int inter2, int inter3, int inter4, boolean check) {
        if (inter1 == -1 || inter2 == -1 || inter3 == -1 || inter4 == -1) return 4;

        int sum = inter1 + inter2 + inter3 + inter4;
        if (sum == 0) return 0;
        else if (sum == 1) return 1;
        else if (sum == 3 || sum == 4) return 2;
        else {
            if (inter1 == inter3 || inter2 == inter4) return 2;
            else {
                if (check) return 1;
                else return 2;
            }
        }
    }


    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int xMin = Integer.parseInt(st.nextToken());
            int yMin = Integer.parseInt(st.nextToken());
            int xMax = Integer.parseInt(st.nextToken());
            int yMax = Integer.parseInt(st.nextToken());

            Vertex v1 = new Vertex(xMin, yMin);
            Vertex v2 = new Vertex(xMax, yMin);
            Vertex v3 = new Vertex(xMax, yMax);
            Vertex v4 = new Vertex(xMin, yMax);
            LineSegment l1 = new LineSegment(v1, v2);
            LineSegment l2 = new LineSegment(v2, v3);
            LineSegment l3 = new LineSegment(v3, v4);
            LineSegment l4 = new LineSegment(v4, v1);

            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            LineSegment l0 = new LineSegment(new Vertex(x1, y1), new Vertex(x2, y2));

            int cross1 = l0.checkCross(l1);
            int cross2 = l0.checkCross(l2);
            int cross3 = l0.checkCross(l3);
            int cross4 = l0.checkCross(l4);

            int ccw1 = Vertex.ccwDir(l0.v1, l0.v2, v1);
            int ccw2 = Vertex.ccwDir(l0.v1, l0.v2, v2);
            int ccw3 = Vertex.ccwDir(l0.v1, l0.v2, v3);
            int ccw4 = Vertex.ccwDir(l0.v1, l0.v2, v4);

            if (ccw1 == 0 || ccw2 == 0 || ccw3 == 0 || ccw4 == 0)
                result.append(getIntersectionCount(cross1, cross2, cross3, cross4, true)).append('\n');
            else
                result.append(getIntersectionCount(cross1, cross2, cross3, cross4, false)).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}