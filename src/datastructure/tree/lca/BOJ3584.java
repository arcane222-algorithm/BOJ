package datastructure.tree.lca;

import java.io.*;
import java.util.*;


/**
 * 가장 가까운 공통 조상 - BOJ3584
 * -----------------
 * category: tree (트리)
 *           dfs (깊이 우선 탐색)
 *           lowest common ancestor (최소 공통 조상)
 * -----------------
 * Input 1
 * 2
 * 16
 * 1 14
 * 8 5
 * 10 16
 * 5 9
 * 4 6
 * 8 4
 * 4 10
 * 1 13
 * 6 15
 * 10 11
 * 6 7
 * 10 2
 * 16 3
 * 8 1
 * 16 12
 * 16 7
 * 5
 * 2 3
 * 3 4
 * 3 1
 * 1 5
 * 3 5
 *
 * Output 1
 * 4
 * 3
 * -----------------
 */
public class BOJ3584 {

    static int T, N;
    static StringBuilder result = new StringBuilder();

    public static int getDepth(int[] parents, int a) {
        int depth = 0;

        while(parents[a] != 0) {
            a = parents[a];
            depth++;
        }

        return depth;
    }

    public static int findLCA(int[] parents, int a, int b) {
        int aDepth = getDepth(parents, a);
        int bDepth = getDepth(parents, b);

        if(aDepth > bDepth) {
            for(int i = 0; i < aDepth - bDepth; i++) {
                if(parents[a] != 0)
                    a = parents[a];
                else
                    break;
            }
        }

        if(bDepth > aDepth) {
            for(int i = 0; i < bDepth - aDepth; i++) {
                if(parents[b] != 0)
                    b = parents[b];
                else
                    break;
            }
        }

        int lca = 0;
        while(true) {
            if(a == b) {
                lca = a;
                break;
            } else {
                if(parents[a] != 0) {
                    a = parents[a];
                    b = parents[b];
                } else {
                    lca = a;
                    break;
                }
            }
        }

        return lca;
    }

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            int[] parents = new int[N + 1];
            for(int j = 0; j < N - 1; j++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                parents[b] = a;
            }
            st = new StringTokenizer(br.readLine());
            result.append(findLCA(parents, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
            result.append('\n');
        }

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the i/o stream
        br.close();
        bw.close();
    }
}
