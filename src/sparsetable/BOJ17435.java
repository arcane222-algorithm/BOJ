package sparsetable;

import java.io.*;
import java.util.*;


/**
 * 합성함수와 쿼리 - BOJ17435
 * -----------------
 * Input 1
 * 5
 * 3 3 5 4 3
 * 5
 * 1 1
 * 2 1
 * 11 3
 * 1000 4
 * 5 1
 *
 * Output 1
 * 3
 * 5
 * 5
 * 4
 * 3
 * -----------------
 */
public class BOJ17435 {

    static final int MAX_N = 19, MAX_M = 200001;
    static int M, Q;
    static int[][] sparseTable;
    static StringBuilder result = new StringBuilder();

    public static int query(int n, int x) {
        for(int i = MAX_N - 1; i > -1; i--) {
            int current = (1 << i);
            if(n >= current) {
                x = sparseTable[i][x];
                n -= current;
                if(n == 0)
                    break;
            }
        }

        return x;
    }

    public static void buildSparseTable(int[] functions) {
        sparseTable = new int[MAX_N][MAX_M];
        for(int i = 1; i < functions.length + 1; i++) {
            sparseTable[0][i] = functions[i - 1];
        }

        for(int i = 1; i < MAX_N; i++) {
            for(int j = 1; j < MAX_M; j++) {
                sparseTable[i][j] = sparseTable[i - 1][sparseTable[i - 1][j]];
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        M = Integer.parseInt(br.readLine());
        int[] functions = new int[M];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < M; i++) {
            functions[i] = Integer.parseInt(st.nextToken());
        }
        buildSparseTable(functions);

        Q = Integer.parseInt(br.readLine());
        for(int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            result.append(query(n, x));
            result.append('\n');
        }


        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
