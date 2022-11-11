package tree.lca;


/**
 * LCA 2 - BOJ11438
 * -----------------
 * category: tree (트리)
 *           lowest common ancestor (최소 공통 조상)
 *           sparse table (희소 배열)
 * -----------------
 * Input 1
 * 15
 * 1 2
 * 1 3
 * 2 4
 * 3 7
 * 6 2
 * 3 8
 * 4 9
 * 2 5
 * 5 11
 * 7 13
 * 10 4
 * 11 15
 * 12 5
 * 14 7
 * 6
 * 6 11
 * 10 9
 * 2 6
 * 7 6
 * 8 13
 * 8 15
 *
 * Output 1
 * 2
 * 4
 * 2
 * 1
 * 3
 * 1
 * -----------------
 */
import java.io.*;
import java.util.*;


public class BOJ11438 {

    static int N, M;
    static int maxDepth;

    static int[][] parentTable;
    static int[] depthTable;

    static List<List<Integer>> tree;
    static StringBuilder result = new StringBuilder();

    public static int getBinaryLen(int x) {
        int size = 0;
        while (x > 0) {
            x >>= 1;
            size++;
        }

        return size;
    }

    public static void dfs(int curr, int depth) {
        depthTable[curr] = depth;

        for (int adj : tree.get(curr)) {
            if (depthTable[adj] == 0) {
                parentTable[0][adj] = curr;
                dfs(adj, depth + 1);
            }
        }
    }

    public static void buildTable() {
        for (int i = 1; i < maxDepth; i++) {
            for (int j = 1; j <= N; j++) {
                parentTable[i][j] = parentTable[i - 1][parentTable[i - 1][j]];
            }
        }
    }

    public static int getLCA(int u, int v) {
        if (depthTable[u] < depthTable[v]) {
            int tmp = u;
            u = v;
            v = tmp;
        }

        for (int i = maxDepth; i >= 0; i--) {
            int gap = depthTable[u] - depthTable[v];
            int pow = 1 << i;
            if (gap >= pow) {
                u = parentTable[i][u];
            }
        }

        if (u == v) return u;

        for (int i = maxDepth; i >= 0; i--) {
            if (parentTable[i][u] != parentTable[i][v]) {
                u = parentTable[i][u];
                v = parentTable[i][v];
            }
        }

        return parentTable[0][u];
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        maxDepth = getBinaryLen(N);

        parentTable = new int[maxDepth + 1][N + 1];
        depthTable = new int[N + 1];

        tree = new ArrayList<>(N + 1);
        for (int i = 0; i <= N; i++) {
            tree.add(new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            tree.get(u).add(v);
            tree.get(v).add(u);
        }

        dfs(1, 1);
        buildTable();

        M = Integer.parseInt(br.readLine());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            result.append(getLCA(u, v));

            if (i < M - 1) result.append('\n');
        }
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
