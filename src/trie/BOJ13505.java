package trie;

import java.io.*;
import java.util.*;


/**
 * 두 수 XOR - BOJ13505
 * -----------------
 *
 * 아래 그림과 같이 input number에 대한 이진수 값을 Trie로 만들어 해결
 * 모든 수를 일정하게 맞춰서 넣어야 하므로 integer 양의 정수범위 (부호비트 1를 제외한 나머지 31비트) 에 맞춰 Trie에 삽입연산
 *
 *             Root
 *            /   \
 *           0     1
 *         /  \   /  \
 *        0   1  0    1
 *      / \ / \ / \  / \
 *     0  n 0 n 0  1 0  1  (3비트에 대해서만 Trie를 구성해보면 다음과 같다 (0 ~ 7까지 1, 3을 제외 하고 모두 삽입, n은 null)
 * input number에 대해 XOR 최대값을 찾기 위해서 각 자리 bit에 대해 반대의 값을 Trie에서 Root부터 타고 내려가면 된다.
 * Ex) 위 Trie에 대하여 010 (2)에 대한 XOR 최댓값을 찾는다면
 * 그 반대인 101 순으로 Trie에서 타고 내려가는데, 만약 타고 내려가는 과정에서 자식 node가 있다면 해당 bit level 만큼 return 값에 더해준다.
 * (즉, 101순으로 타고 내려갈 때, 1이 있다면 4, 0이 있다면 2, 1이 있다면 1을 return 값에 더해주므로 XOR 최댓값은 111인 7이 된다.)
 * (반대로 타고 내려갈때 해당 자리 값이 있다는 것은 서로 XOR 해서 1이 될 대칭되는 두 값이 서로 존재한다는 뜻이므로 위와 같이 구현하는 것이다.)
 *
 * -----------------
 * Input 1
 * 5
 * 1 2 3 4 5
 *
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 5
 * 0 1 0 1 0
 *
 * Output 2
 * 1
 * -----------------
 * Input 3
 * 6
 * 1 2 4 8 16 32
 *
 * Output 3
 * 48
 * -----------------
 */
public class BOJ13505 {

    private static class Trie {
        public static final int MAX_HEIGHT = 30;
        private Trie[] nodes;

        public Trie() {
            nodes = new Trie[2];
        }

        public void add(int value) {
            Trie current = this;

            for(int i = MAX_HEIGHT; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 1 : 0;
                if(current.nodes[idx] == null) {
                    current.nodes[idx] = new Trie();
                }
                current = current.nodes[idx];
            }
        }
    }

    static int N;
    static int[] nums;

    public static int XOR(Trie trie, int value) {
        Trie current = trie;
        int result = 0;

        for(int i = Trie.MAX_HEIGHT; i > -1; i--) {
            int mask = 1 << i;
            int bit = value & mask;
            int idx = bit > 0 ? 0 : 1; // because it is XOR, so it is opponent of add
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

        // parse N
        N = Integer.parseInt(br.readLine());
        nums = new int[N];
        Trie trie = new Trie();

        // parse numbers
        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
            trie.add(nums[i]);
        }

        // XOR each numbers
        int max = XOR(trie, nums[0]);
        for(int i = 1; i < N; i++) {
            int res = XOR(trie, nums[i]);
            if(max < res) max = res;
        }

        // write the result
        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}