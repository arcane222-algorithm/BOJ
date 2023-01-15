package geometry;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 터렛 - BOJ1002
 * -----------------
 * category: 수학 (mathematics)
 *           기하학 (geometry)
 *           많은 조건 분기 (case work)
 * -----------------
 * Input 1
 * 3
 * 0 0 13 40 0 37
 * 0 0 3 0 7 4
 * 1 1 1 1 1 5
 *
 * Output 1
 * 2
 * 1
 * 0
 * -----------------
 */
public class BOJ1002 {

    public static int pow2(int num) {
        return num * num;
    }

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

    public static int getIntersectionCnt(int[] c1, int[] c2) {
        if(c1[0] == c2[0] && c1[1] == c2[1] && c1[2] == c2[2]) return -1;   // same circle
        int d = pow2((c1[0] - c2[0])) + pow2((c1[1] - c2[1]));  // sqrt is expensive operation
        int condition1 = pow2(c1[2] + c2[2]);
        int condition2 = pow2(c1[2] - c2[2]);

        if(d > condition1 || d < condition2) return 0;  // d > r1 + r2 so, d^2 > (r1 + r2)^2
        else if(d == condition1 || d == condition2) return 1;
        else return 2;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(System.out));
        StringTokenizer st = null;
        StringBuilder result = new StringBuilder();

        final int testCaseCnt = Integer.parseInt(br.readLine());
        int[] circle1 = new int[3], circle2 = new int[3];
        for(int i = 0; i < testCaseCnt; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < circle1.length; j++) circle1[j] = Integer.parseInt(st.nextToken());
            for(int j = 0; j < circle2.length; j++) circle2[j] = Integer.parseInt(st.nextToken());
            result.append(getIntersectionCnt(circle1, circle2));
            result.append('\n');
        }


        bw.write(result.toString());
        br.close();
        bw.close();
    }
}

