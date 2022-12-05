package graphtheory.scc;
import java.io.*;
import java.util.*;


/**
 * I번은 쉬운 문제 - BOJ20504
 * -----------------
 * category: graph theory (그래프이론)
 *           strongly connected component (강한 연결 요소)
 * -----------------
 * Input 1
 * 2 1
 * 2 1
 * 2
 * 1
 * 2
 *
 * Output 1
 * 1
 * -----------------
 * Input 2
 * 3 1
 * 1 2
 * 1
 * 1
 *
 * Output 2
 * -1
 * -----------------
 * Input 3
 * 3 2
 * 3 1
 * 1 1
 * 3
 * 1
 * 2
 * 3
 *
 * Output 3
 * 2
 * -----------------
 */
public class BOJ20504 {

    static class Reader {
        private final int BUFFER_SIZE = 1 << 16;
        private final DataInputStream din;
        private final byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            din.close();
        }
    }

    static int N, M, T;
    static int nodeCount, groupCount;

    static boolean[] finish;
    static int[] nodeIds, groupIds;

    static List<List<Integer>> graph, sccGraph;
    static ArrayDeque<Integer> stack;

    public static void init(Reader r) throws IOException {
        N = r.nextInt();
        M = r.nextInt();

        finish = new boolean[N + 1];
        nodeIds = new int[N + 1];
        groupIds = new int[N + 1];

        graph = new ArrayList<>(N + 1);
        sccGraph = new ArrayList<>();
        stack = new ArrayDeque<>();

        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int u = r.nextInt();
            int v = r.nextInt();
            graph.get(u).add(v);
        }
    }

    public static void getScc() {
        for (int i = 1; i <= N; i++) {
            if (nodeIds[i] == 0) {
                dfs(i);
            }
        }
    }

    public static int dfs(int currIdx) {
        nodeIds[currIdx] = ++nodeCount;
        stack.addLast(currIdx);

        int parentId = nodeIds[currIdx];
        for (int adjIdx : graph.get(currIdx)) {
            if (nodeIds[adjIdx] == 0) {
                parentId = Math.min(parentId, dfs(adjIdx));
            } else if (!finish[adjIdx]) {
                parentId = Math.min(parentId, nodeIds[adjIdx]);
            }
        }

        if (parentId == nodeIds[currIdx]) {
            int nodeIdx = -1;
            while (nodeIdx != currIdx) {
                nodeIdx = stack.removeLast();
                finish[nodeIdx] = true;
                groupIds[nodeIdx] = groupCount;
            }
            groupCount++;
        }
        return parentId;
    }

    public static int[] getSccInDegrees() {
        int[] inDegrees = new int[groupCount];
        for (int i = 0; i < groupCount; i++) {
            sccGraph.add(new ArrayList<>());
        }

        for (int i = 1; i <= N; i++) {
            for (int adjIdx : graph.get(i)) {
                if (groupIds[i] != groupIds[adjIdx]) {
                    inDegrees[groupIds[adjIdx]]++;
                    sccGraph.get(groupIds[i]).add(groupIds[adjIdx]);
                }
            }
        }

        return inDegrees;
    }

    public static void sccDfs(int currGroupId, boolean[] visited) {
        visited[currGroupId] = true;

        for (int adj : sccGraph.get(currGroupId)) {
            if (!visited[adj]) {
                sccDfs(adj, visited);
            }
        }
    }

    public static int getResult(Reader r, int[] inDegrees) throws IOException {
        T = r.nextInt();
        int count = 0;
        boolean[] visited = new boolean[groupCount];
        for (int i = 0; i < T; i++) {
            int ti = r.nextInt();
            int groupId = groupIds[ti];

            if (visited[groupId]) continue;

            if (inDegrees[groupId] == 0) {
                sccDfs(groupId, visited);
                count++;
            }
        }

        boolean check = true;
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                check = false;
                break;
            }
        }

        return check ? count : -1;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        Reader r = new Reader();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        init(r);
        getScc();

        int result = getResult(r, getSccInDegrees());
        bw.write(String.valueOf(result));

        // close the buffer
        r.close();
        bw.close();
    }
}
