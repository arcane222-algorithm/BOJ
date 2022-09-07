package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 시계 사진들 - BOJ10266
 * -----------------
 *
 * 처음 보면 kmp 알고리즘을 이용하여 해결하는 문제라고 생각하기 어렵다.
 * 시간이 0 <= ai < 360000 까지 주어지기 때문에, 각 시간을 나타내는 시침이 36만개 있다고 생각하면 된다.
 * 즉, 어떤 시계가 0 1 2 3 4 5라고 나타낸다면 각각 0 1 2 3 4 5를 가리키는 시침이 있는 것이고 이 말은
 * 0과 1로 이루어진 길이 36만의 이진 문자열에 대하여 해당 시침이 존재하는 위치는 1로 표시할 수 있다.
 *
 * 여기서 시계를 돌릴 수 있으므로 검사하려는(회전하려는) 시계의 경우 두 이진 문자열을 더해서 표현해야 한다.
 * why?) 입력 예제 2의 경우 시계가 0, 90000, 180000, 270000만 표현할 수 있다고 하자.
 *       이 때 첫 번째 시계는 1001이 되고, 두 번째 시계는 0011이 된다.
 *       검사하려는 시계의 이진 문자열 (text)을 더하면 10011001이 되고
 *       일치 여부를 확인하려는 시계의 이진 문자열 (pattern) 0011이 해당 문자열에서 나타난다.
 *
 *       이러한 것이 가능한 것은 시계를 돌린다는 것을 생각해보면 가능하다.
 *       1001에서 시계를 4분의 1씩 돌린다고 한다면 좌측의 1이 빠져나가고 우측에 빠져나간 1이 들어온다. (<-(1) 001 <-(1) = 0011)
 *       (시계를 왼쪽으로 돌린다고 하면 0이 270000을, 270000이 180000을 가리키게 되므로 그렇다)
 *       0011에서 시계를 또 4분의 1을 돌리면 좌측 0이 빠져나가고 우측에 빠져나간 0이 들어온다. (0110)
 *       즉, 이런식으로 시계를 한바퀴 돌리는 과정을 시계 이진 문자열 두개를 합해서 표현할 수 있고 이 문자열에서 다른 시계의 상태를 탐색하면 되는 것이다.
 *
 * 위 설명은 시계를 축약한 것이고, 실제로는 36만개의 각도에 대하여 회전하는 것으로 생각하면 된다.
 * 이렇게 pattern 역할의 시계 이진 문자열이 text 역할의 시계 이진 문자열에서 나타나면 possible, 나타나지 않으면 impossible을 출력한다.
 *
 * -----------------
 * Input 1
 * 6
 * 1 2 3 4 5 6
 * 7 6 5 4 3 1
 *
 * Output 1
 * impossible
 * -----------------
 * Input 2
 * 2
 * 0 270000
 * 180000 270000
 *
 * Output 2
 * possible
 * -----------------
 * Input 3
 * 7
 * 140 130 110 120 125 100 105
 * 235 205 215 220 225 200 240
 *
 * Output 3
 * impossible
 * -----------------
 */
public class BOJ10266 {

    static final int MAX_SIZE = 360000;
    static byte[] clock1Text = new byte[MAX_SIZE * 2];
    static byte[] clock2Pattern = new byte[MAX_SIZE];
    static int n;

    public static int[] buildPi(byte[] pattern) {
        int[] pi = new int[pattern.length];

        int match = 0, search = 1;
        for (; search < pattern.length; search++) {
            while (match > 0 && pattern[search] != pattern[match]) {
                match = pi[match - 1];
            }

            if (pattern[search] == pattern[match]) {
                pi[search] = ++match;
            }
        }

        return pi;
    }

    public static boolean kmp(byte[] text, byte[] pattern) {
        int[] pi = buildPi(pattern);
        int search = 0, match = 0;

        for (; search < text.length; search++) {
            while (match > 0 && text[search] != pattern[match]) {
                match = pi[match - 1];
            }

            if (text[search] == pattern[match]) {
                if (match == pattern.length - 1) {
                    return true;
                    // result.add(search - match + 1);
                    // match = pi[match];
                } else {
                    match++;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int num = Integer.parseInt(st.nextToken());
            clock1Text[num] = 1;
            clock1Text[num + MAX_SIZE] = 1;
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int num = Integer.parseInt(st.nextToken());
            clock2Pattern[num] = 1;
        }

        bw.write(kmp(clock1Text, clock2Pattern) ? "possible" : "impossible");

        // close the buffer
        br.close();
        bw.close();
    }
}