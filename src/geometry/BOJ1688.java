package geometry;

import java.io.*;
import java.util.*;



/**
 * 지민이의 테러 - BOJ1688
 * -----------------
 *
 * 주어진 3명의 사람이 지민이에게 테러를 당하지 않고 보호받기 위해서는 주어진 오목 다각형 안에 존재해야 한다.
 * 주어진 점이 오목 다각형 안에 존재하는지 판별하기 위해서는 해당 점에서 양의 방향으로 반직선을 그었을 때,
 * 오목 다각형과 홀수번 만난다면 해당 점이 오목 다각형 안에 있는 것이고, 짝수번 만난다면 해당 점이 오목 다각형 밖에 있는 것이다.
 *
 * 주의 할 점은 주어진 점이 다각형 위에 있을 때도 보호받는 경우인데,
 * 다각형의 변을 이루는 두 점과 주어진 점을 ccw 했을 때 값이 0이면 해당 변 위에 있는 경우이다.
 * 다각형의 모든 변에 대하여 주어진 점과 ccw 값을 조사하여 0인 경우가 있다면 보호받는 경우로 처리한다.
 *
 * 다각형 위에 있지 않은 경우 주어진 점에서 양의 방향으로의 반직선을 그어 오목 다각형과의 교차 횟수를 센다.
 * 이때 양의 방향의 반직선의 우측 점 (x,y)는 = (최대 Integer값, 주어진점.y + 1)로 설정한다.
 * (y좌표를 주어진점.y로 하여 x축에 평행한 반직선으로 구할 경우 50%에서 틀렸습니다가 뜬다.)
 *
 * -----------------
 * Input 1
 * 3
 * 1 0
 * 5 0
 * 3 3
 * 2 2
 * 3 2
 * 4 3
 *
 * Output 1
 * 0
 * 1
 * 0
 * -----------------
 * Input 2
 * 3
 * 2 0
 * 0 2
 * 0 0
 * 0 0
 * 1 1
 * 1 1
 *
 * 1
 * 1
 * 1
 * -----------------
 * Input 3
 * 3
 * 0 0
 * 8 0
 * 4 4
 * 2 2
 * 2 2
 * 2 2
 *
 * Output 3
 * 1
 * 1
 * 1
 * -----------------
 * Input 4
 * 4
 * 1 1
 * 1 2
 * 2 2
 * 2 1
 * 1 3
 * 3 1
 * 0 1
 *
 * 0
 * 0
 * 0
 * -----------------
 */
public class BOJ1688 {

    private static class Vertex implements Comparable<Vertex> {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static long ccw(Vertex v1, Vertex v2, Vertex v3) {
            return (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
        }

        public static int ccwDir(Vertex v1, Vertex v2, Vertex v3) {
            return Long.compare(ccw(v1, v2, v3), 0);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Vertex={");
            builder.append("x=").append(x);
            builder.append(", y=").append(y).append('}');

            return builder.toString();
        }

        @Override
        public int compareTo(Vertex v) {
            if (y == v.y) return Long.compare(x, v.x);
            else return Long.compare(y, v.y);
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
                if (sm1.compareTo(lg2) >= 0) {
                    temp = sm1;
                    sm1 = sm2;
                    sm2 = temp;

                    temp = lg1;
                    lg1 = lg2;
                    lg2 = temp;
                }

                if (lg1.compareTo(sm2) >= 0 || sm1.compareTo(lg2) >= 0) {
                    if (lg1.compareTo(sm2) == 0) return 1;
                    else return -1;
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
    }

    private static int N;
    private static List<Vertex> vertices;
    private static StringBuilder result = new StringBuilder();

    public static boolean onPolygon(Vertex pos) {
        for (int i = 0; i < N; i++) {
            Vertex v1 = vertices.get(i);
            Vertex v2 = vertices.get((i + 1) % N);

            long xMin = Math.min(v1.x, v2.x);
            long xMax = Math.max(v1.x, v2.x);
            long yMin = Math.min(v1.y, v2.y);
            long yMax = Math.max(v1.y, v2.y);
            boolean condition1 = xMin <= pos.x && pos.x <= xMax;
            boolean condition2 = yMin <= pos.y && pos.y <= yMax;
            boolean condition3 = Vertex.ccwDir(pos, v1, v2) == 0;

            if (condition1 && condition2 && condition3) {
                return true;
            }
        }
        return false;
    }

    public static boolean canProtect(Vertex pos) {
        int crossCnt = 0;
        LineSegment base = new LineSegment(pos, new Vertex(Integer.MAX_VALUE, pos.y + 1));
        for (int i = 0; i < N; i++) {
            Vertex v1 = vertices.get(i);
            Vertex v2 = vertices.get((i + 1) % N);
            LineSegment ls = new LineSegment(v1, v2);
            if (base.checkCross(ls) == 1) {
                crossCnt++;
            }
        }

        return (crossCnt & 1) > 0;
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

        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Vertex pos = new Vertex(x, y);
            if (onPolygon(pos)) result.append(1).append('\n');
            else result.append(canProtect(pos) ? 1 : 0).append('\n');
        }

        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
