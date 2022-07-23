package geometry;

import java.io.*;
import java.util.StringTokenizer;


/**
 * CCW - BOJ11758
 * -----------------
 * Input 1
 * 1 1
 * 5 5
 * 7 3
 *
 * Output 1
 * -1
 * -----------------
 * Input 2
 * 1 1
 * 3 3
 * 5 5
 *
 * Output 2
 * 0
 * -----------------
 * Input 3
 * 1 1
 * 7 3
 * 5 5
 *
 * Output 3
 * 1
 * -----------------
 */
public class BOJ11758 {

    private static class Vertex {
        long x, y;

        public Vertex(long x, long y) {
            this.x = x;
            this.y = y;
        }

        private double ccw(Vertex v1, Vertex v2) {
            return (x * v1.y + v1.x * v2.y + v2.x * y) -
                    (y * v1.x + v1.y * v2.x + v2.y * x);
        }

        public double triangleSize(Vertex v1, Vertex v2) {
            double size = ccw(v1, v2) * 0.5;
            return size;
        }

        public int ccwDirection(Vertex v1, Vertex v2) {
            double ccwVal = ccw(v1, v2);
            return ccwVal < 0 ? -1 : (ccwVal == 0 ? 0 : 1);   // val > 0: ccw, val < 0: cw
        }
    }

    static final int N = 3;

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        Vertex[] vertices = new Vertex[N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            vertices[i] = new Vertex(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        bw.write(String.valueOf(vertices[0].ccwDirection(vertices[1], vertices[2])));
        br.close();
        bw.close();
    }
}