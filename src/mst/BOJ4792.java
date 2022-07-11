package mst;

import java.io.*;
import java.util.*;


/**
 * 레드 블루 스패닝 트리 - BOJ4792
 * -----------------
 * Input 1
 * 3 3 2
 * B 1 2
 * B 2 3
 * R 3 1
 * 2 1 1
 * R 1 2
 * 0 0 0
 *
 * Output 1
 * 1
 * 0
 * -----------------
 */
public class BOJ4792 {

    private static class DisjointSet {
        int[] parents;

        public DisjointSet(int n) {
            init(n);
        }

        public void init(int n) {
            parents = new int[n + 1];
            for(int i = 1; i < n + 1; i++) {
                parents[i] = i;
            }
        }

        public int find(int a) {
            if(a == parents[a])
                return a;
            else
                return parents[a] = find(parents[a]);
        }

        public void union(int a, int b) {
            a = find(a);
            b = find(b);

            parents[b] = a;
        }

        public boolean compareParent(int a, int b) {
            return find(a) == find(b);
        }
    }

    private static class Edge implements Comparable<Edge> {
        char color;
        int v1, v2, cost;

        public Edge(char color, int v1, int v2) {
            this.color = color;
            this.v1 = v1;
            this.v2 = v2;
            this.cost = color == 'B' ? 1 : 0;
        }

        @Override
        public int compareTo(Edge e) {
            return Integer.compare(cost ,e.cost);
        }
    }

    public static int N, M, K;
    public static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        String line;
        DisjointSet dSet = null;
        PriorityQueue<Edge> minPQ = new PriorityQueue<>();
        PriorityQueue<Edge> maxPQ = new PriorityQueue<>(Collections.reverseOrder());

        while((line = br.readLine()).charAt(0) != '0') {
            // Read inputs
            st = new StringTokenizer(line);
            N = Integer.parseInt(st.nextToken());   // N, M, K;
            M = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            dSet = new DisjointSet(N);
            minPQ.clear();

            // Init edges
            for(int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                char c = st.nextToken().charAt(0);
                int f = Integer.parseInt(st.nextToken());
                int t = Integer.parseInt(st.nextToken());
                minPQ.add(new Edge(c, f, t));
                maxPQ.add(new Edge(c, f, t));
            }

            int minCnt = 0, maxCnt = 0;
            while(!minPQ.isEmpty()) {
                Edge e = minPQ.poll();

                if(!dSet.compareParent(e.v1, e.v2)) {
                    minCnt += e.cost;
                    dSet.union(e.v1, e.v2);
                }
            }

            dSet.init(N);
            while(!maxPQ.isEmpty()) {
                Edge e = maxPQ.poll();

                if(!dSet.compareParent(e.v1, e.v2)) {
                    maxCnt += e.cost;
                    dSet.union(e.v1, e.v2);
                }
            }

            if(maxCnt >= K && minCnt <= K) result.append(1);
            else result.append(0);
            result.append('\n');
        }

        // write the result
        bw.write(result.toString());
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}