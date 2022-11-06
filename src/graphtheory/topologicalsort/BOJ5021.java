package graphtheory.topologicalsort;

import java.io.*;
import java.util.*;


/**
 * 왕위 계승 - BOJ5021
 * -----------------
 * category: graph theory (그래프이론)
 *           topological sorting (위상 정렬)
 *           set / map by hashing (해시를 사용한 집합과 맵)
 * -----------------
 * Input 1
 * 9 2
 * edwardi
 * charlesi edwardi diana
 * philip charlesi mistress
 * wilhelm mary philip
 * matthew wilhelm helen
 * edwardii charlesi laura
 * alice laura charlesi
 * helen alice bernard
 * henrii edwardii roxane
 * charlesii elizabeth henrii
 * charlesii
 * matthew
 *
 * Output 1
 * matthew
 * -----------------
 * Input 2
 * 4 5
 * andrew
 * betsy andrew flora
 * carol andrew betsy
 * dora andrew carol
 * elena andrew dora
 * carol
 * dora
 * elena
 * flora
 * gloria
 *
 * Output 2
 * elena
 * -----------------
 */
public class BOJ5021 {

    static int N, M;
    static HashMap<String, Integer> inDegrees;
    static HashMap<String, Double> compatibility;
    static HashMap<String, HashSet<String>> graph;

    public static void topologicalSort() {
        Queue<String> queue = new LinkedList<>();

        for (String key : inDegrees.keySet()) {
            if (inDegrees.get(key) == 0) {
                queue.offer(key);
            }
        }

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            for (String adj : graph.get(curr)) {
                inDegrees.put(adj, inDegrees.get(adj) - 1);

                if (inDegrees.get(adj) == 0) {
                    queue.add(adj);
                }

                double com = !compatibility.containsKey(curr) ? 0 : compatibility.get(curr) * 0.5;
                if (!compatibility.containsKey(adj)) {
                    compatibility.put(adj, 0.0);
                }
                compatibility.put(adj, compatibility.get(adj) + com);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        inDegrees = new HashMap<>();
        compatibility = new HashMap<>();
        graph = new HashMap<>();

        String king = br.readLine();
        compatibility.put(king, 1.0);

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String child = st.nextToken();
            String lParent = st.nextToken();
            String rParent = st.nextToken();

            if (!graph.containsKey(lParent)) {
                graph.put(lParent, new HashSet<>());
                inDegrees.put(lParent, 0);
            }

            if (!graph.containsKey(rParent)) {
                graph.put(rParent, new HashSet<>());
                inDegrees.put(rParent, 0);
            }

            if (!graph.containsKey(child)) {
                graph.put(child, new HashSet<>());
                inDegrees.put(child, 0);
            }

            graph.get(lParent).add(child);
            graph.get(rParent).add(child);
            inDegrees.put(child, inDegrees.get(child) + 2);
        }

        topologicalSort();
        double max = 0;
        String result = "";
        for (int i = 0; i < M; i++) {
            String candidate = br.readLine();
            if (compatibility.containsKey(candidate)) {
                if (max < compatibility.get(candidate)) {
                    max = compatibility.get(candidate);
                    result = candidate;
                }
            }
        }
        bw.write(result);

        // close the buffer
        br.close();
        bw.close();
    }
}