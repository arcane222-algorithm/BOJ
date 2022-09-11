package string.hashing;

import java.io.*;
import java.util.*;


/**
 * 구간 성분 - BOJ10840
 * -----------------
 *
 * 비교하려는 길이가 같은 두 문자열에 대하여 포함된 문자의 종류와 개수가 순서에 상관없이 같다면 두 문자열은 같은 문자열이다.
 * 해싱을 이용해서 해결하되, 기존의 해싱 방법은 길이가 같더라도 안의 문자열의 문자의 순서가 다르다면 다른 문자로 판별하게 된다.
 * 비교하려는 두 문자열의 문자열의 각 글자를 누적하고 이를 이용하여 해싱한다.
 *
 * ex) aab라면, a가 2개, b가 1개이므로 2 x 31^0 + 1 x 31^1 = 33이 된다.
 *     xyyz라면, x가 1개, yy가 1개, z가 1개이므로 1 x 31^23 + 2 x 31^24 + 1 x 31^25가 된다.
 *
 * buildTable 함수를 이용하여 table[26] 배열을 정의하고 문자열에 각 문자가 몇글자씩 있는지 세어준다.
 * 만들어진 table을 이용하여 위에서 설명한대로 각 자리마다 소수 P (일반적으로 17이나 31을 이용)의 거듭제곱을 이용하여 문자열을 해싱한다.
 *
 * 두 입력 문자열에 대하여 A와 B중 짧은 문자열을 A, 긴 문자열을 B로 설정하고
 * A의 부분 문자열의 각 길이에 대하여 (길이 1, 2, 3, ... , |A|)
 * 길이가 1일 때, A의 길이 1의 모든 부분 문자열의 해시값을 구해 setA에 넣고
 * B의 길이 1의 모든 부분 문자열의 해시 값을 구해 setB에 넣는다.
 * 이제 setA의 길이 + setB의 길이 != setA와 B를 합친 set의 길이 라면 중복되는 값이 있는 것이므로 구간성분이 존재하는 것이므로 해당 길이를 result에 넣는다.
 *
 * 이러한 방식으로 길이 1, 2, 3, ... , |A|에 대하여 모두 조사하면 된다.
 * 해싱을 하는 과정에서 매번 새롭게 선형 계산을 통해 해시 값을 구하면 길이 N 문자열에 대하여 길이 M의 부분 문자열들의 해시 값을 구하면
 * 시간복잡도가 O(NM)이 되므로
 * M 길이의 첫 부문 문자열에 대한 해시값을 계산하고
 * 그 다음 문자열부터 hash = hash - (줄어든 문자 1개의 값) + (늘어난 문자 1개의 값) 으로 계산하는데,
 * 각 문자에 대한 power 값을 미리 계산하여 시간복잡도를 줄일 수 있도록 한다.
 * 문자는 소문자 'a' ~ 'z' 까지 있으므로 각 문자에 대한 소수의 거듭제곱 pows 배열은 pows[i] = 31 ^ i 이 될 것이다. (0 <= i < 26)
 * 즉, 위 방식은 hash = hash - powers[줄어든 문자 - 'a'] + powers[늘어난 문자 - 'a'] 가 될 것이다.
 * 이러한 방식으로 해시를 계산하면 O(N + M) 만에 길이 M을 가지는 모든 부분 문자열의 해시 값을 계산할 수 있게 된다.
 *
 * -----------------
 * Input 1
 * xraphy
 * edgeedgem
 *
 * Output 1
 * 0
 * -----------------
 * Input 2
 * afcdrdesdefwszr
 * gedsrddemzr
 *
 * Output 2
 * 7
 * -----------------
 * Input 3
 * computersystem
 * sesystuercomplexity
 *
 * Output 3
 * 11
 * -----------------
 */
public class BOJ10840 {

    static final int P = 31;
    static final int CHAR_SIZE = 26;
    static String A, B;
    static long[] pows;

    public static void buildPow() {
        pows = new long[CHAR_SIZE];
        pows[0] = 1;
        for (int i = 1; i < CHAR_SIZE; i++) {
            pows[i] = P * pows[i - 1];
        }
    }

    public static int[] buildTable(String str, int end) {
        int[] table = new int[CHAR_SIZE];
        for (int i = 0; i < end; i++) {
            table[str.charAt(i) - 'a']++;
        }
        return table;
    }

    public static long hash(String str, int end) {
        int[] table = buildTable(str, end);

        long hash = 0;
        for (int i = 0; i < CHAR_SIZE; i++) {
            hash = hash + table[i] * pows[i];
        }
        return hash;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        A = br.readLine();
        B = br.readLine();
        if (A.length() > B.length()) {
            String tmp = A;
            A = B;
            B = tmp;
        }
        buildPow();

        int maxGap = 0;
        for (int pSize = 1; pSize < A.length() + 1; pSize++) {
            Set<Long> setA = new HashSet<>();
            Set<Long> setB = new HashSet<>();

            boolean isSame = false;
            long hashA = 0, hashB = 0;
            for (int j = 0; j < B.length() - pSize + 1; j++) {
                if (j == 0) {
                    hashA = hash(A, pSize);
                    hashB = hash(B, pSize);
                } else {
                    if (j + pSize - 1 < A.length()) {
                        int aIdxLeft = A.charAt(j - 1) - 'a';
                        int aIdxRight = A.charAt(j + pSize - 1) - 'a';
                        hashA = j + pSize - 1 < A.length() ? hashA - pows[aIdxLeft] + pows[aIdxRight] : hashA;
                    }

                    int bIdxLeft = B.charAt(j - 1) - 'a';
                    int bIdxRight = B.charAt(j + pSize - 1) - 'a';
                    hashB = hashB - pows[bIdxLeft] + pows[bIdxRight];
                }
                setA.add(hashA);
                setB.add(hashB);
            }

            int sum1 = setA.size() + setB.size();
            setA.addAll(setB);
            isSame = sum1 != setA.size();

            if (isSame) {
                maxGap = pSize;
            }
        }
        bw.write(String.valueOf(maxGap));

        // close the buffer
        br.close();
        bw.close();
    }
}