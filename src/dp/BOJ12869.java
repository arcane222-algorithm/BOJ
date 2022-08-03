package dp;

import java.io.*;
import java.util.StringTokenizer;


/**
 * 뮤탈리스크 - BOJ12869
 * -----------------
 *
 * -----------------
 * Input 1
 * 3
 * 12 10 4
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 3
 * 54 18 6
 *
 * Output 2
 * 6
 * -----------------
 * Input 3
 * 1
 * 60
 *
 * Output 3
 * 7
 * -----------------
 * Input 4
 * 3
 * 1 1 1
 *
 * Output 4
 * 1
 * -----------------
 * Input 5
 * 2
 * 60 40
 *
 * Output 5
 * 9
 * -----------------
 */
public class BOJ12869 {

    static final int[][] ATK_DMG = new int[][]{ {1, 3, 9}, {1, 9, 3}, {3, 1, 9},
            {3, 9, 1}, {9, 1, 3}, {9, 3, 1} };
    static final int MAX_HP = 60;
    static int N;

    public static int attack(int x, int y, int z, int[][][] dpTmp) {
        if(x < 0) return attack(0, y, z, dpTmp);
        if(y < 0) return attack(x, 0, z, dpTmp);
        if(z < 0) return attack(x, y,0, dpTmp);

        if(x == 0 && y == 0 && z == 0) return 0;
        if(dpTmp[x][y][z] != 0) return dpTmp[x][y][z];

        dpTmp[x][y][z] = Integer.MAX_VALUE;
        for(int i = 0; i < ATK_DMG.length; i++) {
            dpTmp[x][y][z] = Math.min(dpTmp[x][y][z], attack(x - ATK_DMG[i][0], y - ATK_DMG[i][1], z - ATK_DMG[i][2], dpTmp) + 1);
        }

        return dpTmp[x][y][z];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(System.out));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        int[][][] dpTmp = new int[MAX_HP + 1][MAX_HP + 1][MAX_HP + 1];
        int[] scv = new int[3];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            scv[i] = Integer.parseInt(st.nextToken());
        }

        // write the result & close the i/o stream
        bw.write(String.valueOf(attack(scv[0], scv[1], scv[2], dpTmp)));

        br.close();
        bw.close();
    }
}
