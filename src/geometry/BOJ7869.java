package geometry;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 두 원 - BOJ7869
 * -----------------
 *
 * -----------------
 * Input 1
 * 20.0 30.0 15.0 40.0 30.0 30.0
 *
 * Output 1
 * 608.366
 * -----------------
 */
public class BOJ7869 {

    public static void circleDump(int[] c) {
        StringBuilder result = new StringBuilder();
        result.append("x: ");
        result.append(c[0]);
        result.append(" y: ");
        result.append(c[1]);
        result.append(" r: ");
        result.append(c[2]);

        System.out.println(result);
    }

    public static double abs(double num) {
        return num < 0 ? -num : num;
    }

    public static double pow2(double num) {
        return num * num;
    }

    public static double getIntersectionAreaSize(double[] c1, double[] c2) {
        double d2 = pow2((c1[0] - c2[0])) + pow2((c1[1] - c2[1]));
        double d = Math.sqrt(d2);

        if(c1[2] + c2[2] <= d) {
            return 0;
        } else if(abs(c1[2] - c2[2]) >= d) {
            double r = c1[2] < c2[2] ? c1[2] : c2[2];
            return Math.PI * r * r;
        } else {
            // a^2 = b^2 + c^2 - 2bc * cosA, cos^-1(A) = (b^2 + c^2 - a^2) / 2 * b * c
            double theta1half = Math.acos((pow2(c1[2]) + d2 - pow2(c2[2])) / (2 * c1[2] * d));
            double theta2half = Math.acos((pow2(c2[2]) + d2 - pow2(c1[2])) / (2 * c2[2] * d));

            // S = PI * r^2 * (theta / 2 * PI) = r^2 * (theta / 2)
            double S1 = pow2(c1[2]) * theta1half - (pow2(c1[2]) * Math.sin(2 * theta1half) / 2);
            double S2 = pow2(c2[2]) * theta2half - (pow2(c2[2]) * Math.sin(2 * theta2half) / 2);

            return S1 + S2;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        double[] circle1 = new double[3], circle2 = new double[3];
        for(int j = 0; j < circle1.length; j++) circle1[j] = Double.parseDouble(st.nextToken());
        for(int j = 0; j < circle2.length; j++) circle2[j] = Double.parseDouble(st.nextToken());

        bw.write(String.format("%.3f", getIntersectionAreaSize(circle1, circle2)));
        br.close();
        bw.close();
    }
}