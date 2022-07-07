package bitmasking;

import java.io.*;
import java.util.StringTokenizer;


/**
 * XOR 합 3 - BOJ13710
 * -----------------
 *
 * 행성 X3에서 이용하였던 각 자리 수 별 bit 0의 개수 * 1의 개수로 1이 나오는 경우의 수를 계산 하는 아이디어를 활용함.
 * 수열에 A 대하여 i ~ j 사이의 부분 수열에 대한 XOR 합을 A[i, j]라고 하고, 1 ~ i 사이의 누적합을 S[i]라고 할 때,
 * A[i, j] = ai ^ a(i + 1) ^ ... ^ aj,  S[0] = 0, S[i] = a1 ^ a2 ^ ... ^ ai라고 표현할 수 있다.
 * A[i, j]에 대하여 A[i, j] = S[i - 1] ^ S[j]로 표현할 수 있고,
 * 수열 A에 대하여 모든 연속하는 부분 수열의 합은 A[i, j] = S[i - 1] ^ S[j]에 대하여
 * 두 S[i - 1], S[j]를 뽑아 XOR 하는 경우의 수를 모두 더하는 문제로 변환될 수 있다.
 *
 * 수열에 대한 부분수열의 누적 합 S[k]를 구한 후, 위에서 설명한 행성 X3에서 이용한 각 자리별 bit-masking을 통한 경우의 수 계산을 통해 결과를 구한다.
 * 이 때 주의 할 점은,
 * 행성 X3의 경우 임의의 수열에 대하여 두 수를 뽑아 XOR 한 경우의 수를 모두 더하는 것이므로 위 방법대로 진행하면 문제가 없지만,
 * 부분 수열의 경우 누적 합 두개로 쪼개는 과정에서 누적 합 S에 대하여 S[0] = 0으로 정의되고,
 * A[1, K] = S[0] ^ S[K] = 0 ^ S[K] = S[K]로 표현될 수 있으며 이 말은 처음부터 시작되는 부분수열은 누적 합에서 0과 XOR 하여 표현된다.
 * (즉, 누적 합으로 이루어진 수열 S에 대하여 0을 추가하여 계산하여야 한다)
 * 그러므로 (1L << k) * (각 자리의 1의 개수 k) * (각 자리의 0의 개수 (N - 각 자리의 1의 개수 k)) 계산 시
 * 0의 개수를 구하는 k * (N - 각 자리의 1의 개수 k) 과정에서 누적 합 수열 S에 0이 하나 추가 된 것을 반영하여
 * k * (N + 1 - 각 자리의 1의 개수 k)처럼 +1을 하여 계산해주면 된다.
 *
 * -----------------
 * Input 1
 * 2
 * 1 2
 *
 * Output 1
 * 6
 * -----------------
 * Input 2
 * 4
 * 1 2 3 4
 *
 * Output 2
 * 30
 * -----------------
 * Input 3
 * 2
 * 999999999 999999998
 *
 * Output 3
 * 1999999998
 * -----------------
 */
public class BOJ13710 {

    static final int MAX_LENGTH = 30;
    static int N;
    static int[] bit1Counts;

    public static void countBits(int value) {
        for(int i = 0; i < MAX_LENGTH; i++) {
            int mask = 1 << i;
            if((value & mask) > 0) {
                bit1Counts[i] += 1;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N
        N = Integer.parseInt(br.readLine());
        bit1Counts = new int[MAX_LENGTH];

        StringTokenizer st = new StringTokenizer(br.readLine());
        int prefixSum = Integer.parseInt(st.nextToken());
        countBits(prefixSum);

        for(int i = 1; i < N; i++) {
            prefixSum = prefixSum ^ Integer.parseInt(st.nextToken());
            countBits(prefixSum);
        }

        long result = 0;
        for(int i = 0; i < bit1Counts.length; i++) {
            result += (1L << i) * bit1Counts[i] * (N + 1 - bit1Counts[i]);
        }

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}
