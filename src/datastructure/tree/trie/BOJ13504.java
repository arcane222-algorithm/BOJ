package datastructure.tree.trie;

import java.io.*;
import java.util.*;

/**
 * XOR 합 - BOJ13504
 * -----------------
 *
 * 수열의 i ~ j (i <= j) 까지의 XOR 합을 XOR[i, j] 라고 할 때,
 * XOR[i, j] = XOR[1, i - 1] ^ XOR[1, j] 로 표현 가능
 * why) (n1 ^ n2 ^ n3 ... ^ n(i - 1)) ^ (n1 ^ n2 ^ n3 ... ^ n(j))
 *     = {(n1 ^ n1) ^ (n2 ^ n2) ^ (n3 ^ n3) ... ^ (n(i - 1) ^ n(i - 1))} ^ n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j) (XOR 연산은 교환법칙과 결합법칙이 성립하므로 ..)
 *     = 0 ^ 0 ^ 0 ^ ... ^ 0 ^ n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j) (XOR 연산의 0은 항등원(identity) 이므로 결과값에 영향 X)
 *     = n(i) ^ n(i + 1) ^ ... ^ n(j - 1) ^ n(j)
 *     = XOR[i, j]
 *
 * 이 점을 활용하여 구간 XOR 합 XOR[i, j]이 최대가 되는 것은 [i, k]까지의 XOR 누적합 XOR[i, k] 의 값들 중 두 쌍인
 * XOR[1, i - 1] ^ XOR[1, j]를 찾아 최대가 되게 하는 것과 같다.
 * XOR 합의 누적합을 Trie 에 저장하여 최대가 되는 쌍을 찾는 것이 핵심.
 *
 * 각 누적합을 Trie에 삽입한 후 누적합 배열을 순차 탐색하며 각 누적합의 값을 이진수로 표현할 때, 접두사 부분이 반대가 되도록 Trie를 조사하면 XOR 값이 최대가 된다.
 * ex) 누적합의 값이 10 (1010)이라면, 다른 누적합이 0101이 되어야 두 누적합의 XOR 값이 최대가 된다.
 *     두 누적합의 XOR가 최대가 된다는 것은 해당 두 누적합으로 표현되는 수열의 연속된 부분수열의 XOR가 최대가 된다는 것이다.
 * 이때, 한 누적합의 접두사에 대한 반대의 비트 값이 트라이에 저장되어 있지 않을 수 있다.
 * 위의 예시처럼 누적합 10 (1010) 과 XOR 시 최대가 되는 누적합 5 (0101)를 탐색해도 각 자리비트 0101이 들어있지 않을 수 있다. (즉, 결과가 1111이 나오지 않을 수 있다.)
 * 그러므로 누적합 배열을 선형탐색하며 최대가 되도록 모두 탐색해보아야 한다.
 *
 * -----------------
 * Input 1
 * 2
 * 5
 * 3 7 7 7 0
 * 5
 * 3 8 2 6 4
 *
 * Output 1
 * 7
 * 15
 * -----------------
 */
public class BOJ13504 {

    private static class Trie {
        private static final int MAX_LENGTH = 30;

        private Trie[] nodes;

        public Trie() {
            nodes = new Trie[2];
        }

        public void add(int value) {
            Trie current = this;
            for(int i = MAX_LENGTH; i > -1; i--) {
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

    static int T, N;
    static int[] prefixSums;

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

        // parse T
        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            // parse N
            N = Integer.parseInt(br.readLine());
            prefixSums = new int[N];
            Trie trie = new Trie();
            trie.add(0);

            st = new StringTokenizer(br.readLine());
            prefixSums[0] = Integer.parseInt(st.nextToken());
            trie.add(prefixSums[0]);
            for(int j = 1; j < N; j++) {
                prefixSums[j] = prefixSums[j - 1] ^ Integer.parseInt(st.nextToken());
                trie.add(prefixSums[j]);
            }

            int max = 0;
            for(int j = 0; j < N; j++) {
                int result = XORMax(trie, prefixSums[j]);
                if(max < result) max = result;
            }

            bw.write(String.valueOf(max));
            bw.write('\n');
        }

        // close the buffer
        br.close();
        bw.close();
    }
}