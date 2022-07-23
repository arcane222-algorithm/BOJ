package geometry;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 다각형의 면적 - BOJ2166
 * -----------------
 * Input 1
 * 4
 * 0 0
 * 0 10
 * 10 10
 * 10 0
 *
 * Output 1
 * 100.0
 * -----------------
 * Input 2
 * 5
 * -100000 100000
 * -100000 -100000
 * 100000 -100000
 * 100000 99999
 * 99999 100000
 *
 * Output 2
 * 39999999999.5
 * -----------------
 * Input 3
 * 4
 * 0 0
 * 5 0
 * 2 4
 * 3 3
 *
 * Output 3
 * 7.0
 * -----------------
 */
public class BOJ2166 {

    private static class Vertex {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public double ccwSize(Vertex v1, Vertex v2) {
            double val = ((x * v1.y + v1.x * v2.y + v2.x * y) -
                    (y * v1.x + v1.y * v2.x + v2.y * x)) * 0.5;
            return val;
        }

        public double ccwDirection(Vertex v1, Vertex v2) {
            double val = (x * v1.y + v1.x * v2.y + v2.x * y) - (y * v1.x + v1.y * v2.x + v2.y * x);
            return val < 0 ? -1 : (val == 0 ? 0 : 1);
        }
    }

    static int N;

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        Vertex[] vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            vertices[i] = new Vertex(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken()));
        }

        double S = 0;
        for (int i = 1; i < N - 1; i++) {
            S += vertices[0].ccwSize(vertices[i], vertices[i + 1]);
        }

        bw.write(String.format("%.1f", Math.abs(S)));
        br.close();
        bw.close();
    }
}
