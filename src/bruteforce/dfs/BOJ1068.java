package bruteforce.dfs;

import java.io.*;
import java.util.*;


/**
 * 트리 - BOJ 1068
 * -----------------
 *
 * 트리에서 노드 하나를 지웠을 때, 남은 트리의 리프 노드의 수를 세어야 한다.
 * 트리에서 노드 하나를 지우면 해당 노드를 루트로 하는 서브트리는 탐색할 수 없게 된다.
 * 트리를 입력받을 때 트리의 각 노드가 순서에 상관없이 주어진다면 양방향 인접리스트를 통해 구성해야 한다.
 * 하지만 이 문제는 0번부터 순서대로 해당 노드의 부모의 값을 주기 때문에,
 * 각 노드를 List로 구현하고 해당 노드 List에 자식 노드들의 값을 넣어주면 된다. (양방향은 아니고 단방향 리스트 구현이다)
 *
 * 이제 각 노드들에서 지워야할 노드의 값을 삭제하고
 * dfs를 수행하며 리프노드를 찾아 세어주면 된다. (리프 노드의 경우 자식이 없으므로 List의 size가 0일 것이다.)
 *
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