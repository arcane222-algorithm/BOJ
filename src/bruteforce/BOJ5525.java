package bruteforce;

import java.io.*;
import java.util.*;


/**
 * IOIOI - BOJ5525
 * -----------------
 *
 * 단순 선형탐색 시 시간 복잡도는 O(NM)이 되므로 시간초과가 발생한다.
 * 선형탐색 시 O(N)에 탐색할 수 있도록
 * 한 index를 비교할 때 +2까지 비교하여 IOI 패턴인지를 확인하고 맞다면 +2만큼 index를 증가시킨다.
 * IOI 패턴이 나올때마다 counting을 해주어 값이 N이 된다면 원하는 패턴을 찾은 것이므로 결과를 1증가시킨다.
 * 패턴을 찾은 후 뒤에 바로 같은 패턴이 또 올 수 있으므로 counting 값을 -1 하여 다음에 IOI 패턴이 나오면 바로 결과값을 증가시킬 수 있도록 한다.
 * 만약 IOI 패턴이 나타나지 않는다면 counting 값을 0으로 바꿔주어 다시 카운팅을 한다.
 *
 * -----------------
 * Input 1
 * 1
 * 13
 * OOIOIOIOIIOII
 *
 * Output 1
 * 4
 * -----------------
 * Input 2
 * 2
 * 13
 * OOIOIOIOIIOII
 *
 * Output 2
 * 2
 * -----------------
 */
public class BOJ5525 {

    static int N, M;
    static String S;

    public static boolean checkIOI(int begin) {
        if (begin + 2 > S.length() - 1) return false;

        boolean c1 = S.charAt(begin) == 'I';
        boolean c2 = S.charAt(begin + 1) == 'O';
        boolean c3 = S.charAt(begin + 2) == 'I';

        return c1 & c2 & c3;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        M = Integer.parseInt(br.readLine());
        S = br.readLine();

        int result = 0, size = 0;
        for (int i = 0; i < M - 2; i++) {
            if (checkIOI(i)) {
                size++;
                i++;
                if (size == N) {
                    result++;
                    size--;
                }
            } else {
                size = 0;
            }
        }

        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}