package string.hashing;

import java.io.*;
import java.util.*;



/**
 * 암호 찾기 - BOJ21559
 * -----------------
 *
 * 문자열 A에서 길이 K의 부분 문자열들과 문자열 B에서 길이 K의 부분 문자열들을 구해 서로 같은 것이 있다면 '폴리매스 문명’의 비밀번호가 된다.
 * 즉, 문자열 A와 B에서의 길이 K의 부분 문자열을 서로 비교해야 한다.
 * naive 하게 비교한다면 A와 B의 길이가 N 일때, 길이 K의 부분 문자열의 개수는 각 N - K + 1개이고, 서로 비교해야 하므로 (N - K + 1)^2이 되어 O(N^2)의 시간 복잡도를 갖는다.
 *
 * 공통 여부를 빠르게 비교하기 위해 해싱 (hashing)을 사용한다.
 * 라빈-카프(Rabin-karp) 알고리즘과 비슷하게 부분 문자열의 길이가 주어졌으므로 첫번째 부분 문자열 (0 ~ K - 1까지, )의 해시 값을 구한다.
 * (해시 값은 문자열의 각 문자에 대하여 P^(K - 1) * A[0] + P^(K - 2) * A[1] + ... + P^0 * A[K] 로 구할 수 있다. (A[i]는 문자열 A의 i번째 글자, P는 적절한 소수)
 *
 * 이후 부분 문자열에 대하여 (1~K까지, 2~K+1까지 등등) hash = 소수 P * (hash - oldChar * power) + newChar
 * 방식으로 구해주면 O(1)의 시간복잡도에 다음 부분 문자열의 해시 값을 계산할 수 있다.
 *
 * 위 방식대로 문자열 A와 B에 대하여 set을 이용하여 각각 해시 값을 저장하고
 * 공통되는 해시 값의 개수를 구하기 위해
 * (setA.size + setB.size) - setC.size 를 해주면 된다. (setC는 setA와 setB를 합쳐 공통 부분을 지운 새로운 set)
 *
 * -----------------
 * Input 1
 * 4 2
 * 1122
 * 6677
 *
 * Output 1
 * 0
 * -----------------
 * Input 2
 * 3 1
 * 122
 * 221
 *
 * Output 2
 * 2
 * -----------------
 * Input 3
 * 3 2
 * 124
 * 248
 *
 * Output 3
 * 1
 * -----------------
 */
public class BOJ21559 {

    static final int P = 31;
    static long Pow = 1;
    static int N, K;
    static Set<Long> setA, setB;

    public static long hash(String str, int end) {
        long hash = 0;
        for (int i = 0; i < end; i++) {
            hash = P * hash + str.charAt(i);
        }
        return hash;
    }

    public static long hash(String str) {
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = P * hash + str.charAt(i);
        }
        return hash;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        setA = new HashSet<>();
        setB = new HashSet<>();
        for (int i = 0; i < K - 1; i++) {
            Pow *= P;
        }

        String A = br.readLine();
        long hash = 0;
        for (int i = 0; i < N - K + 1; i++) {
            if (i == 0) {
                hash = hash(A, K);
            } else {
                char oldChar = A.charAt(i - 1);
                char newChar = A.charAt(i + K - 1);
                hash = P * (hash - Pow * oldChar) + newChar;
            }
            setA.add(hash);
        }

        String B = br.readLine();
        hash = 0;
        for (int i = 0; i < N - K + 1; i++) {
            if (i == 0) {
                hash = hash(B, K);
            } else {
                char oldChar = B.charAt(i - 1);
                char newChar = B.charAt(i + K - 1);
                hash = P * (hash - Pow * oldChar) + newChar;
            }
            setB.add(hash);
        }

        Set<Long> setC = new HashSet<>();
        setC.addAll(setA);
        setC.addAll(setB);
        bw.write(String.valueOf((setA.size() + setB.size()) - setC.size()));

        // close the buffer
        br.close();
        bw.close();
    }
}