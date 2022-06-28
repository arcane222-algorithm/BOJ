package bruteforce;

import java.io.*;
import java.util.*;


/**
 * 트리 - BOJ 1068
 * -----------------
 * Input 1
 * 5
 * -1 0 0 1 1
 * 2
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 5
 * -1 0 0 1 1
 * 1
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 5
 * -1 0 0 1 1
 * 0
 *
 * Output 3
 * 0
 * -----------------
 * Input 4
 * 9
 * -1 0 0 2 2 4 4 6 6
 * 4
 *
 * Output 4
 * 2
 * -----------------
 * Input 5
 * 2
 * -1 0
 * 1
 *
 * Output 5
 * 1
 * -----------------
 */
public class BOJ1068 {

    static int N, D;
    static List<Integer>[] nodes;

    public static int dfs(int root) {
        if(nodes[root].size() == 0) return 1;

        int count = 0;
        for(int i = 0; i < nodes[root].size(); i++) {
            int childNode = nodes[root].get(i);
            count += dfs(childNode);
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N
        N = Integer.parseInt(br.readLine());
        nodes = new ArrayList[N];
        for(int i = 0; i < N; i++) {
            nodes[i] = new ArrayList<>();
        }

        // parse nodes
        int root = 0;
        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            int parent = Integer.parseInt(st.nextToken());
            if (parent == -1) {
                root = i;
            } else {
                nodes[parent].add(i);
            }
        }

        // parse D (delete)
        D = Integer.parseInt(br.readLine());
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < nodes[i].size(); j++) {
                if(nodes[i].get(j) == D) {
                    nodes[i].remove(j);
                }
            }
        }

        // find leaf nodes using dfs, write the result
        int leafCount = D == root ? 0 : dfs(root);
        bw.write(String.valueOf(leafCount));

        // close the buffer
        br.close();
        bw.close();
    }
}