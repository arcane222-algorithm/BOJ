package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * 수열과 쿼리 20 - BOJ16903
 * -----------------
 * 
 * 기존의 BOJ13505 (두 수 XOR) 의 Trie 에 Remove 연산을 생각해볼 수 있는 문제
 * 일반적인 문자열에 대한 Trie 의 경우 접두어(prefix) 의 유무에 따라 isTerminate 변수를 false 로 해주거나, 영향이 없다면 노드를 직접 삭제함.
 * But. 이진수 Trie 에서 특정 값이 들어있는지가 중요한 것이 아니라, 특정 값에 대한 XOR 최대 값 (Trie에서 input number의 bit를 반대로 취하면서 내려가면 최댓값)
 * 을 구하는 것이 문제의 핵심이므로, 10011을 삭제 연산 한다고 하면 우측에서 좌측으로 (1->1->0->0->1 순) (즉, bottom-up) 탐색하며 자식 노드가 없는 node 들만 삭제함.
 * Trie 에 4, 5, 6, 7이 들어있을 때,
 *             Root
 *            /   \
 *           0     1
 *               /  \
 *              0    1
 *             / \  / \
 *            0  1 0  (1)
 *
 * 7을 삭제한다고 하면, (1)만 삭제하면 된다.
 * (해당 Trie는 110 (6)을 넣게 되면 11 (3)이 접두어가 가능하므로 Trie안에 3, 6이 둘다 들어있는지, 6만 들어있는지 알 수 가 없다. why? isTerminate 변수 명시 x 이므로)
 * (But. 위에 설명한 것처럼 input 값에 대한 XOR의 최댓값만 구하면 되므로 isTerminate 변수 필요 X)
 *
 * -----------------
 * Input 1
 * 10
 * 1 8
 * 1 9
 * 1 11
 * 1 6
 * 1 1
 * 3 3
 * 2 8
 * 3 3
 * 3 8
 * 3 11
 *
 * Output 1
 * 11
 * 10
 * 14
 * 13
 * -----------------
 */
public class BOJ16903 {

    private static class Trie {
        public static final int MAX_LENGTH = 30;

        private Trie[] nodes;
        private Trie parent;
        private int idx;

        public Trie() {
            nodes = new Trie[2];
        }

        public void add(int value) {
            Trie current = this;

            for (int i = MAX_LENGTH; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 1 : 0;
                if (current.nodes[idx] == null) {
                    Trie child = new Trie();
                    child.idx = idx;
                    child.parent = current;
                    current.nodes[idx] = child;
                }
                current = current.nodes[idx];
            }
        }

        public void remove(int value) {
            Trie current = this;

            for (int i = MAX_LENGTH; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 1 : 0;
                if (current.nodes[idx] == null) {
                    return;
                } else {
                    current = current.nodes[idx];
                }
            }

            while (!current.hasChildren()) {
                int idx = current.idx;
                current = current.parent;

                if (current != null) {
                    current.nodes[idx] = null;
                } else {
                    break;
                }
            }
        }

        public boolean hasChildren() {
            return nodes[0] != null || nodes[1] != null;
        }
    }

    static final int ADD = 1, DELETE = 2, XOR = 3;
    static int M;

    public static int XORMax(Trie trie, int value) {
        Trie current = trie;
        int result = 0;

        for(int i = Trie.MAX_LENGTH; i > -1; i--) {
            int mask = 1 << i;
            int bit = value & mask;
            int idx = bit > 0 ? 0 : 1;
            if(current.nodes[idx] == null) {
                idx = bit > 0 ? 1 : 0;
            } else {
                result += mask;
            }
            current = current.nodes[idx];
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        // parse M
        M = Integer.parseInt(br.readLine());
        Trie trie = new Trie();
        trie.add(0);
        HashMap<Integer, Integer> numCntMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int query = Integer.parseInt(st.nextToken());
            int num = Integer.parseInt(st.nextToken());
            Integer cnt = numCntMap.get(num);

            switch (query) {
                case ADD:
                    if (cnt == null) {
                        numCntMap.put(num, 1);
                        trie.add(num);
                    } else {
                        numCntMap.put(num, cnt + 1);
                    }
                    break;

                case DELETE:
                    if (cnt == 1) {
                        numCntMap.remove(num);
                        trie.remove(num);
                    } else {
                        numCntMap.put(num, cnt - 1);
                    }
                    break;

                case XOR:
                    bw.write(String.valueOf(XORMax(trie, num)));
                    bw.write('\n');
                    break;
            }
        }

        // close the buffer
        br.close();
        bw.close();
    }
}