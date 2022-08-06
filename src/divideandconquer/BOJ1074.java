package divideandconquer;

import java.io.*;
import java.util.*;


/**
 * Z - BOJ1074
 * -----------------
 *
 *
 * -----------------
 * Input 1
 * 2 3 1
 *
 * Output 1
 * 11
 * -----------------
 * Input 2
 * 3 7 7
 *
 * Output 2
 * 63
 * -----------------
 * Input 3
 * 1 0 0
 *
 * Output 3
 * 0
 * -----------------
 * Input 4
 * 4 7 7
 *
 * Output 4
 * 63
 * -----------------
 * Input 5
 * 10 511 511
 *
 * Output 5
 * 262143
 * -----------------
 * Input 6
 * 10 512 512
 *
 * Output 6
 * 786432
 * -----------------
 */
public class BOJ1074 {

    private static class Window {
        int minX, minY, maxX, maxY, size, minValue;

        public Window(int baseX, int baseY, int size) {
            this.size = size;
            this.minX = baseX;
            this.minY = baseY;
            this.maxX = baseX + size - 1;
            this.maxY = baseY + size - 1;
        }

        public boolean include(int x, int y) {
            boolean conditionX = minX <= x && x <= maxX;
            boolean conditionY = minY <= y && y <= maxY;
            return conditionX && conditionY;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append("Window=[");
            builder.append("size=").append(size).append(' ');
            builder.append(",minX=").append(minX).append(' ');
            builder.append(",minY=").append(minY).append(' ');
            builder.append(",maxX=").append(maxX).append(' ');
            builder.append(",maxY=").append(maxY).append(' ');
            builder.append(",minValue=").append(minValue).append(']');

            return builder.toString();
        }
    }

    static int N, r, c;
    static int[] dirX = {0, 1, 0, 1};
    static int[] dirY = {0, 0, 1, 1};

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        int size = 1 << N;
        Window parent =  new Window(0, 0, size);
        while (parent.size > 1) {
            int halfSize = parent.size >> 1;
            for (int i = 0; i < 4; i++) {
                Window subWindow = new Window(parent.minX + halfSize * dirX[i], parent.minY + halfSize * dirY[i], halfSize);
                subWindow.minValue = parent.minValue + (subWindow.size * subWindow.size) * i;
                if (subWindow.include(c, r)) {
                    parent = subWindow;
                    break;
                }
            }
        }

        bw.write(String.valueOf(parent.minValue));

        // close the buffer
        br.close();
        bw.close();
    }
}
