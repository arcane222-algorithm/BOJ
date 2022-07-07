package bitmasking;

import java.io.*;


/**
 * 행성 X3 - BOJ2830
 * -----------------
 *
 * 행성 거주민의 친밀도를 계산 하는 것 = 두 사람의 이름을 XOR 하는 것
 * 행성에 N명의 사람이 있을 때 모든 친밀도를 구하는 것은 N개의 수에서 2개를 골라 XOR 하여 모두 합한 것과 같다 (NC2 = N(N - 1) / 2)
 * brute-force 하게 연산 시 시간복잡도는 O(N^2)이 되므로 bit-masking을 이용한다.
 * 모든 N개의 수에 대하여 입력 값이 최대 100만 까지 들어오므로 20개의 비트에 대해 각 자리가 1인 개수를 계산해준다 (int[] bit1Counts)
 * (각 자리가 0인 수의 개수는 (전체 수 N - 각 자리가 1인 수의 개수) 로 구할 수 있다)
 * 각 자리에 대하여 (1L << k) * bit1Counts[k] * (N - bit1Counts[k]) 를 계산한 값을 합하면 답이 됨.
 *
 * why?) 각 자리에 대하여 두 수의 XOR A(i) ^ A(j) 에서 k번째 비트가 1이 나오기 위해서는 A(i) 와 A(j)의 k번째 비트의 값이 달라야 한다.
 *       이 때 N개의 수 중 각 자리에 대하여 두 수를 XOR 하여 1이 나올 수 있는 경우의 수는 각 자리가 1인 수의 개수 x 각 자리가 0인 수의 개수가 된다.
 *       ex) 1000
 *           1010
 *           1001
 *           1101
 *           1111 의 경우 k = 0번째 비트에서 0의 개수는 2개, 1의 개수는 3개이고
 *           1이 나올 수 있는 경우는 첫 번째 0 선택, 나머지 1 선택 3가지 + 두 번째 0 선택, 나머지 1 선택 3가지
 *           = 총 6가지, 즉 0의 개수 x 1의 개수의 곱으로 표현되므로 이를 통해 각 자리에서 1이 나올 수 있는 경우를 구할 수 있다.
 *           각 자리의 경우 2의 거듭제곱으로 표현되므로 결과적으로 각 자리의 (1L << k) * bit1Counts[k] * (N - bit1Counts[k])에 대한 합으로 답을 구할 수 있다.
 *           (즉, 각 자리에 대해 naive 하게 계산 시, O(N^2)인 연산을 경우의 수를 곱으로 한번에 계산하여 최대 비트의 길이를 k라 할때, 선형시간 O(kn)에 계산할 수 있다.)
 *
 * -----------------
 * Input 1
 * 2
 * 19
 * 10
 *
 * Output 1
 * 25
 * -----------------
 * Input 2
 * 3
 * 7
 * 3
 * 5
 *
 * Output 2
 * 12
 * -----------------
 * Input 3
 * 5
 * 9
 * 13
 * 1
 * 9
 * 6
 *
 * Output 3
 * 84
 * -----------------
 */
public class BOJ2830 {

    static final int MAX_LENGTH = 20;
    static int N;
    static int[] bit1Counts;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // parse N
        N = Integer.parseInt(br.readLine());
        bit1Counts = new int[MAX_LENGTH];

        // parse numbers
        for(int i = 0; i < N; i++) {
            int person = Integer.parseInt(br.readLine());
            for(int j = 0; j < MAX_LENGTH; j++) {
                int mask = 1 << j;
                if((person & mask) > 0) {
                    bit1Counts[j] += 1;
                }
            }
        }

        long result = 0;
        for(int i = 0; i < bit1Counts.length; i++) {
            result += (1L << i) * bit1Counts[i] * (N - bit1Counts[i]);
        }

        // write the result
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}