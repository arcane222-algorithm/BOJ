package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * XOR 부분 행렬 - BOJ16905
 * -----------------
 * <p>
 * 크기가 각각 N, M인 두 1차원 배열 V와 U에 대하여 행렬 A의 (i, j)의 원소는 A(ij) = V(i) XOR V(j)로 정의된다.
 * N, M이 최대 1000까지 들어오므로 행렬 A의 크기는 1000 x 1000까지 만들어질 수 있고 이것의 부분 행렬에 포함된 원소를 모두 XOR 한 것 중 가장 큰 것을 구해야 한다.
 * 행렬 A 내부에서 정사각 혹은 직사각 부분 행렬을 얻을 때, 부분행렬의 크기를 Nsub x Msub 라고 한다면 Nsub와 Msub의 크기에 따라 문제를 간단하게 만들 수 있다.
 * 예를 들어, 이해하기 쉽게 가장 좌측 (1, 1)부터 직사각 부분 행렬을 구했다고 했을 때 각 원소를 모두 나열해 보면
 * <p>
 * (V(1) xor U(1)) xor (V(1) xor U(2)) xor (V(1) xor U(3)) ... (V(1) xor U(Msub))
 * (V(2) xor U(1)) xor (V(2) xor U(2)) xor (V(2) xor U(3)) ... (V(2) xor U(Msub))
 * ...
 * (V(Nsub) xor U(1)) xor (V(Nsub) xor U(2)) xor (V(Nsub) xor U(3)) ... (V(Nsub) xor U(Msub)) 와 같은 모양이 된다.
 * <p>
 * 이 모양을 잘 보면 만약
 * (i) Nsub, Msub가 모두 짝수 일 때, XOR 연산은 교환법칙이 성립하므로 V는 V끼리 U는 U끼리 묶어 보면
 * V(1), V(2), ... , V(Nsub) 가 각 Msub개 (Nsub개가 아니라 Msub개다. 헷갈리지 말 것),
 * U(1), U(2), ... , U(Msub)가 각 Nsub개 (Msub개가 아니라 Nsub개다. 헷갈리지 말 것) 있고 각각 짝수개 만큼 있으므로 XOR 연산의 결과는 0이 나옴을 알 수 있다.
 * (짝수개의 수를 XOR 하면 0이 나온다. why? xor 연산의 항등원은 0이고, 역원은 자기자신이므로, 같은 수 짝수개의 결과는 항등원 0이 나온다.)
 * <p>
 * (ii) Nsub가 짝수, Msub가 홀수 일 때,
 * U(1), U(2), ... , U(Msub) 가 각 Nsub개가 있고, 각 짝수개이므로 모두 사라져서 XOR의 결과는
 * V(1) xor V(2) xor ... xor V(Nsub)가 남게 된다. (V항은 각 Msub개가 있고 각 홀수이므로 1개씩 남게 된다.)
 * <p>
 * (iii) Nsub가 홀수, Msub가 짝수 일 때,
 * V(1), V(2), ... , V(Nsub) 가 각 Msub개가 있고, 각 짝수개이므로 모두 사라져서 XOR의 결과는
 * U(1) xor U(2) xor ... xor U(Msub) 가 남게 된다. (U항은 각 Nsub개가 있고 각 홀수 이므로 1개씩 남게 된다.)
 * <p>
 * (iv) Nsub, Msub가 모두 홀수 일 때,
 * (ii)와 (iii)의 예시처럼 각각 홀수개가 있으므로 결과적으로 1개씩 남아 XOR의 결과는
 * (V(1) xor V(2) xor ... xor V(Nsub)) xor (U(1) xor U(2) xor ... xor U(Msub)) 가 남게 된다.
 * <p>
 * 즉, 세로 길이인 Nsub의 길이가 짝수면 반대편 원소 U들이 사라져서 짝수개의 부분 수열 Vsub가 남고,
 * 가로 길이인 Msub의 길이가 짝수면 반대편 원소 V들이 사라져서 짝수개의 부분 수열 Usub가 남고,
 * 둘다 홀수이면 두 부분 수열 Vsub, Usub가 모두 남게 되는 것이다.
 * <p>
 * 부분 행렬의 XOR 최댓값을 구해야 하므로 (i), (ii), (iii)의 경우
 * 수열 V와 U의 부분 수열의 XOR의 최댓값을 구하는 BOJ13504, BOJ13505와 문제와 비슷한 문제가 된다.
 * 주의 할 점은 모든 부분수열에 대한 최댓값이 아니라 짝수길이의 부분 수열 Vsub와 짝수길이의 부분 수열 Usub에 대한 최댓값을 구하는 문제이므로 13505처럼
 * Trie에 삽입 후 반대 접미사를 찾는 방식으로 구하기 애매하다.
 * <p>
 * N와 M의 최댓값이 1000이므로, O(N^2)의 경우의 수를 탐색해도 시간복잡도상 시간초과가 발생하지 않는다.
 * 수열 V와 U에 대하여 xor 누적합 prefixV, prefixU을 구한 다음 XOR[i, j] = XOR[1, i - 1] xor XOR[1, j]의 성질을 이용하되,
 * 짝수인 수열의 경우만 최댓값을 구하여 비교한다. (XOR[i, j]는 수열의 구간 [i, j]의 부분 수열의 XOR 합이다)
 * <p>
 * 짝수 길이의 부분수열임을 확인하려면 위 성질에 대하여 j - i 값이 홀수라면 짝수 길이의 부분 수열을 고른 것이므로 (j - i) & 1 == 1일 때
 * max = Math.max(max, prefixV[i - 1] ^ prefixV[j])를 계산해준다.
 * 이 과정은 (ii)를 계산한 것이고, (iii) 또한 처리해주기 위해 prefixU 배열에 대해서도 동일하게 계산한다.
 * <p>
 * (iv)에 대해서는 홀수길이의 부분 수열 Vsub, Usub를 모두 XOR 한 경우인데,
 * Vsub를 모두 XOR한 경우를 하나의 새로운 수열의 원소로 생각하고 Usub또한 하나의 새로운 원소로 생각하면
 * 위 Prefix XOR에 사용된 성질을 동일하게 적용할 수 있다.
 * 즉, 위 O(N^2)탐색 과정에서 (j - i) & 1 == 0일 때 (즉, (j - i)가 짝수이고 이것은 Nsub의 길이가 홀수 일 때)
 * trie에 홀수 길이의 부분 수열 Vsub를 모두 XOR 한 것을 하나의 원소로 취급하여 삽입한다.
 * (조건을 만족하는 각 Vsub를 구하는 것은 위의 성질을 이용하여 prefixV의 (j - i) & 1 == 0인 두 i, j 쌍을 고르는 것과 같다)
 * <p>
 * 그 다음 (j - i) & 1 == 0 일 때 (즉, Msub의 길이가 홀수 일 때)
 * BOJ13504, BOJ13505에서 사용된 것과 같이 Usub를 모두 xor한 것을 하나의 원소로 취급하여 Vsub들이 들어있는 trie안에서
 * XORMax 값을 계산하면 된다. (접두사가 반대되는 방향으로 trie를 탐색하면 xor 값이 최댓값이 되는 방법)
 * <p>
 * -----------------
 * Input 1
 * 3 4
 * 5 3 1
 * 2 1 2 4
 * <p>
 * Output 1
 * 7
 * -----------------
 * Input 2
 * 3 3
 * 10 12 4
 * 5 10 9
 * <p>
 * Outpout 2
 * 15
 * -----------------
 * Input 3
 * 3 3
 * 1 2 1
 * 4 2 8
 * <p>
 * Output 3
 * 15
 * -----------------
 */
public class BOJ16905 {

    private static class BinaryTrie {
        public static final int MAX_LENGTH = 29;

        BinaryTrie[] nodes;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public void add(int value) {
            BinaryTrie current = this;
            for (int i = MAX_LENGTH; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 1 : 0;
                if (current.nodes[idx] == null) {
                    current.nodes[idx] = new BinaryTrie();
                }
                current = current.nodes[idx];
            }
        }

        public int XORMax(int value) {
            int result = 0;
            BinaryTrie current = this;

            for (int i = BinaryTrie.MAX_LENGTH; i > -1; i--) {
                int mask = 1 << i;
                int bit = value & mask;
                int idx = bit > 0 ? 0 : 1;
                if (current.nodes[idx] == null) {
                    idx = bit > 0 ? 1 : 0;
                } else {
                    result += mask;
                }
                current = current.nodes[idx];
            }

            return result;
        }
    }

    static int N, M;
    static int[] prefixV, prefixU;
    static BinaryTrie trieV = new BinaryTrie();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        prefixV = new int[N + 1];
        prefixU = new int[M + 1];

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i < N + 1; i++) {
            prefixV[i] = prefixV[i - 1] ^ Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i < M + 1; i++) {
            prefixU[i] = prefixU[i - 1] ^ Integer.parseInt(st.nextToken());
        }

        int max = 0;
        for (int i = 1; i < N + 1; i++) {
            for (int j = i; j < N + 1; j++) {
                int val = prefixV[i - 1] ^ prefixV[j];
                if (((j - i) & 1) == 1) {
                    // Nsub가 짝수 길이이고 Msub가 홀수 길이인 경우 U의 원소들은 모두 xor 결과가 0이되어 사라지고 V만 남음
                    // j - i가 홀수 일때, (즉, 구간 [i, j] 사이의 부분 수열의 길이가 짝수 일 때, ex) [2, 5] 안에는 2, 3, 4, 5 짝수개가 존재
                    max = Math.max(max, val);
                } else {
                    // j - i가 짝수 일때, (즉, 구간 [i, j] 사이의 부분 수열의 길이가 홀수 일 때, ex) [2, 4] 안에는 2, 3, 4 홀수개가 존재
                    trieV.add(val);
                }
            }
        }

        for (int i = 1; i < M + 1; i++) {
            for (int j = i; j < M + 1; j++) {
                int val = prefixU[i - 1] ^ prefixU[j];
                if (((j - i) & 1) == 1) {
                    // Msub가 짝수 길이이고 Nsub가 홀수 길이인 경우 V의 원소들은 모두 xor 결과가 0이되어 사라지고 U만 남음
                    // j - i가 홀수 일때, (즉, 구간 [i, j] 사이의 부분 수열의 길이가 짝수 일 때, ex) [2, 5] 안에는 2, 3, 4, 5 짝수개가 존재
                    max = Math.max(max, val);
                } else {
                    // j - i가 짝수 일때, (즉, 구간 [i, j] 사이의 부분 수열의 길이가 홀수 일 때, ex) [2, 4] 안에는 2, 3, 4 홀수개가 존재
                    max = Math.max(max, trieV.XORMax(val));
                }
            }
        }

        bw.write(String.valueOf(max));

        // close the buffer
        br.close();
        bw.close();
    }
}