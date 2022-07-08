package mathematics;

import java.io.*;
import java.util.*;


/**
 * XOR 방정식 - BOJ14257
 * -----------------
 *
 * A ^ B = X에 대하여, 결과 X의 각 자리 비트의 경우 1이 나오기 위해서는 A, B의 각 자리 비트가 달라야 하고, 0이 나오기 위해서는 같아야 한다.
 * 이 때, X의 각 자리 비트가 0이 나오는 경우 A, B의 각 자리 비트가 0, 0 이거나 1, 1 이어야 하는데 둘다 0이라고 가정한다.
 * 그럴 경우, 1이 나오는 각 자리 비트의 경우 A가 1이라면 B가 0이어야 하고 B가 1이면 A가 0이어야 하므로 만들 수 있는 경우의 수는 2 ^ (X에서 1인 자리 비트 수)가 된다.
 * 재미있는 것은 이 경우 A = 1000, B = 0001, X = 1001과 같은 상황인데, 0이 나오는 경우는 두 수 A, B의 각 자리를 0으로 하자고 가정 했으니,
 * 1이 나오는 경우는 두 수의 비트가 다르므로 A + B = X가 성립하게 된다.
 * 이 상황에서 문제에 주어진 조건은 A + B = S인 것이고, A + B = X인 경우에서 두 수의 XOR 각 자리가 0이 나오도록 하는 경우를 1를 하나씩 켜보며
 * A + B = S가 되는지 체크한다.
 *
 *         for(int i = MAX_LENGTH; i > -1; i--) {
 *             long mask = 1L << i;
 *             if((X & mask) > 0) {
 *                 bit1Count++;
 *             } else {
 *                 if(Gap >= mask * 2) {
 *                     Gap -= mask * 2;
 *                 }
 *             }
 *         }
 *
 *         만약 X의 각 자리에 대하여 mask를 이용한 bit-masking을 통해 결과 bit 가 1이라면 (즉, 0보다 크다면) 비트 1를 세는 count를 증가시켜준다.
 *         그렇지 않고 0이 나온다면 X를 S로 만들 수 있는 차이 Gap (S - X) 값을 좁혀나가는데,
 *         위에 설명한 것과 같이 0인 자리의 bit를 둘다 1로 켜서 A ^ B = X를 유지하면서 A + B = X인 상황에서 A + B = S를 만들어 나가는 것이므로
 *         두 비트 모두 키게 되는 것이므로 각 자리의 mask에 x2를 한 값을 Gap에서 빼준다.
 *         이렇게 각 1인 자리는 값을 카운팅 하고 0인 자리는 1로 바꿔주며 계산을 이어나가고
 *         만약 Gap의 결과가 0이 아니라는 것은, A + B = X인 상황에서 A + B = S의 결과를 만들어낼 수 없는 것이므로, 경우의 수가 0이 된다.
 *
 * 만약 S와 X가 같게 되면,
 * A = 0000, B = 1010, X = 1010, S = 1010
 * A = 1010, B = 0000, X = 1010, S = 1010 와 같은 예시가 생길 수 있는데
 * 문제의 조건에서 A, B > 0이라고 가정하였으므로 A,B가 0이되는 2가지 경우의 수를 결과에서 뺴주어야 한다.
 *
 * -----------------
 * Input 1
 * 9 5
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 3 3
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 5 2
 *
 * Output 3
 * 0
 * -----------------
 */
public class BOJ14257 {
    static final int MAX_LENGTH = 39;
    static long S, X, Gap;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        // parse N, K
        st = new StringTokenizer(br.readLine());
        S = Long.parseLong(st.nextToken());
        X = Long.parseLong(st.nextToken());
        Gap = S - X;

        int bit1Count = 0;
        for(int i = MAX_LENGTH; i > -1; i--) {
            long mask = 1L << i;
            if((X & mask) > 0) {
                bit1Count++;
            } else {
                if(Gap >= mask * 2) {
                    Gap -= mask * 2;
                }
            }
        }

        // write the result
        long result = (1L << bit1Count);
        result = Gap != 0 ? 0 : S == X ? result - 2 : result;
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}