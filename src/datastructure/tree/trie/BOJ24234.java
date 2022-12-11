package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * 1차원 체스 - BOJ24234
 * -----------------
 *
 * 두 개의 기보를 비교할 때 처음으로 값이 달라지는 위치가 존재한다면 그 이전 위치의 수를 기점으로 한다.
 * 즉, 두 개의 기보(두 개의 수열)을 비교할 때 처음으로 값이 달라지는 위치의 그 이전의 수에 대한 경우의 수를 계산해야 한다.
 * 이 말은 즉, 두 개의 수열을 비교할 때 앞의 공통 접두사 (prefix)를 가지는 부분이 있고 그 이후 값이 달라지는 첫 위치를 찾아야 하는 것이다.
 * Trie 자료구조를 이용하여 각 수열을 Trie에 넣고 Trie의 Node가 분기되는 지점이 위 공통 접두사 이후에 값이 달라지는 위치를 나타낸다.
 *
 * Trie에 기보(수열)의 값을 모두 넣고 이후 탐색을 하며 값을 찾아내면 시간초과가 발생할 수 있으므로
 * 삽입을 진행하며 기점의 경우의 수를 계산하도록 한다.
 *
 * Trie의 Node마다 count를 설정하여 기보(수열)의 값을 삽입할 때 그 값을 1씩 증가시켜 해당 종류의 Node가 몇개씩 존재하는지 나타내도록 한다.
 * 그렇게 되면 공통 접두사 이후 분기되는 지점에 대하여 parent.count - current.count 값이 기점의 경우의 수가 된다.
 *
 * why?) 1 2 3 4, 1 2 3 4, 1 2 3 5, 1 2 3 5, 1 2 3 6 총 5개의 기보가 있을 때,
 * Trie node에서 분기되는 지점은 3이고 (기점), Trie안에 1 2 3 4, 1 2 3 4, 1 2 3 6의 기보가 들어있다고 할 때,
 * 1 2 3 5를 추가한다고 한다면 기점의 경우의 수는 현재 공통 접두사(1 2 3)를 제외한 지점(5) 에서 자기자신과 다른 형제 노드들의 종류의 수가 되므로
 * (4)2개 + (6)1개= 3가지가 된다.
 * 다른 예시로 1 2 3 4, 1 2 3 4, 1 2 3 5, 1 2 3 5가 들어있는 상태에서 위와 같이 계산을 진행하면
 * (4 2가지 + 5 2가지) = 총 4가지가 된다.
 * 이 값을 식으로 표현하면 parent.count (노드 3의 count의 수 5, 3까지는 삽입이 완료되었으므로 5개) - current.count (노드 6의 count의 수 1) = 4가 된다.
 *
 * 위 방법에 반례가 존재하는데, 기보의 기점의 정의를 보면
 * 1 2, 1 2 3 두 기보에 대하여 2는 기점이 될 수 없다.
 * 만약 위와같이 계산한다면
 * 1 2
 * 1 2 3 순의 삽입의 경우 두번째 기보의 3을 삽입할 시 parent.count (2의 count 수 2) - current.count (3의 count 수 1) = 1이 되고,
 * 1 2 3
 * 1 2 순으로 삽입한다면 두번째 기보 2를 삽입할 시 parent.count (1의 count 수 2) - current.count (2의 count의 수 2) = 0이 된다.
 * 이후 Query(2)를 실행하면 각각 1과 0이 나오게 된다.
 *
 * 즉, 값이 다르게 나오는데 후자의 경우가 맞는 결과 값이다.
 * 위와 같은 예외를 처리해주기 위해 각 노드에 isEnd라는 int 값을 추가한다.
 * 노드를 삽입하는 과정에서 마지막으로 도달하게 되는 node에 대하여 isEnd 값을 ++하여 1씩 증가시켜준다.
 * 결과적으로 위 방식 계산시 두 기보를 비교할 때 짧은 쪽 기보의 경우의 수를 제외시켜주기 위해서 (parent.count - parent.isEnd - current.count) 값으로 계산하면 된다.
 *
 * ex) 1 2, 1 2 3, 1 2 4 3개의 기보가 있을 때 2의 기점의 경우의 수는 1이 되는데,
 * 2번 째 1 2 3을 삽입할 때,
 * 2의 count 수는 2가 되고, 3의 count 수는 1이므로
 * 2 - 1 = 1로 계산하는 것이 아니라, 1 2를 삽입할 때 증가시킨 2의 isEnd 값이 1이므로
 * 2 - 1 - 1 = 0으로 계산한다.
 * 3번 째 1 2 4를 삽입할 때,
 * 2의 count 수는 3이 되고, 4의 count수가 1이므로
 * 3 - 1 = 2로 계산 하는 것이 아니라, 1 2를 삽입할 때 증가시킨 2의 isEnd 값이 1이므로
 * 3 - 1 - 1 = 1로 계산한다.
 * 그러므로 2의 기점의 경우의 수는 1이 되는 것이다.
 *
 * 수의 종류가 1 ~ 10000이므로 dp[10001] 크기의 int 배열을 선언하여 매 값을 삽입할 때 node 마다 위의 계산을 실행하여
 * dp[parent.key] += (parent.count - parent.isEnd - current.count); 을 저장해주면 된다.
 *
 * (+) Trie를 구성할 때, 수의 종류가 10000개 이므로 Child Node를 저장할 자료구조를 배열로 선언하면 당연히 메모리 초과가 발생한다.
 *     HashMap을 이용하여 필요한 종류의 Node만 저장할 수 있도록 구현한다.
 *
 * -----------------
 * Input 1
 * 6 4
 * 3 1 2 3
 * 4 1 2 4 5
 * 4 1 2 4 6
 * 6 2 23 23 3 3 23
 * 6 2 23 23 3 3 23
 * 3 2 23 21
 * 2
 * 3
 * 4
 * 23
 *
 * Output 1
 * 2
 * 0
 * 1
 * 2
 * -----------------
 * Input 2
 * 10 4
 * 3 1 2 3
 * 4 1 2 4 5
 * 4 1 2 4 6
 * 4 1 2 4 5
 * 4 1 2 4 7
 * 4 1 2 4 6
 * 4 1 2 4 7
 * 6 2 23 23 3 3 23
 * 6 2 23 23 3 3 23
 * 3 2 23 21
 * 2
 * 3
 * 4
 * 23
 *
 * Output 2
 * 6
 * 0
 * 12
 * 2
 * -----------------
 * Input 3
 * 5 6
 * 3 1 2 3
 * 2 1 2
 * 4 1 2 4 5
 * 4 1 2 4 6
 * 4 2 3 4 6
 * 1
 * 2
 * 3
 * 4
 * 5
 * 6
 *
 * Output 3
 * 0
 * 2
 * 0
 * 1
 * 0
 * 0
 * -----------------
 * Input 4
 * 2 1
 * 2 1 2
 * 3 1 2 3
 * 2
 *
 * Output 4
 * 0
 * -----------------
 */
public class BOJ24234 {

    private static class Trie {
        HashMap<Integer, Trie> nodes;
        Trie parent;
        int key, count, isEnd;

        public Trie() {
            nodes = new HashMap<>();
        }

        public void add(int size, StringTokenizer st) {
            Trie current = this;
            for (int i = 0; i < size; i++) {
                int key = Integer.parseInt(st.nextToken());
                Trie next = current.nodes.get(key);
                if (next == null) {
                    next = new Trie();
                    next.key = key;
                    next.parent = current;
                    current.nodes.put(key, next);
                }
                current = next;
                current.count++;
                dp[current.parent.key] += (current.parent.count - current.parent.isEnd - current.count);
            }
            current.isEnd++;
        }
    }

    static final int MAX_NUM = 10000;
    static int N, Q;
    static long[] dp = new long[MAX_NUM + 1];
    static Trie root = new Trie();
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int size = Integer.parseInt(st.nextToken());
            root.add(size, st);
        }

        for (int i = 0; i < Q; i++) {
            int query = Integer.parseInt(br.readLine());
            result.append(dp[query]).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
