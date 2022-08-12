package rabinkarp;

import java.io.*;
import java.util.*;


/**
 * Searching for Strings - BOJ19608
 * -----------------
 *
 * 라빈-카프 (Rabin-Karp) 문자열 패턴 매칭 알고리즘의 기초이자 응용문제이다.
 * 패턴 문자열 N과 비교해야될 문자열 H에 대하여 N의 각 글자를 조합하여 나올 수 있는 문자열의 패턴을 H에서 찾는 문제이다.
 * 예를 들어, N이 aab 라면 aab, aba, baa의 패턴을 H에서 찾는 것이다.
 * 기존 해싱 방식을 이용하면 aab, aba, baa의 해시 값이 모두 다르게 나오기 때문에 aab, aba, baa를 모두 같은 문자열로 처리하기 위해서
 * 문자열의 각 글자를 누적하고 이를 이용하여 해싱한다.
 * aab라면, a가 2개, b가 1개이므로 2 x 31^0 + 1 x 31^1 = 33이 된다.
 * xyyz라면, x가 1개, yy가 1개, z가 1개이므로 1 x 31^23 + 2 x 31^24 + 1 x 31^25가 된다.
 *
 * 문자열 N과 H를 비교할 때, 길이 N에 해당하는 H의 부분 문자열의 해시값을 매번 새로 계산하여 N과 비교하게 되면 선형탐색하는 O(NH) 시간복잡도가 걸리게 된다.
 * 라빈-카프 알고리즘을 이용하여 H의 N길이의 첫 부문 문자열에 대한 해시값을 계산하고
 * 그 다음 문자열부터 hash = hash - (줄어든 문자 1개의 값) + (늘어난 문자 1개의 값) 으로 계산하는데,
 * 각 문자에 대한 power 값을 미리 계산하여 시간복잡도를 줄일 수 있도록 한다.
 * 문자는 소문자 'a' ~ 'z' 까지 있으므로 각 문자에 대한 소수의 거듭제곱 powers 배열은 powers[i] = 31 ^ i 이 될 것이다. (0 <= i < 26)
 * 즉, 위 식은 hash = hash - powers[줄어든 문자 - 'a'] + powers[늘어난 문자 - 'a'] 가 될 것이다.
 * 이렇게 얻은 H의 부분 문자열 해시 값을 hHash 라고 할 때, 위 N의 해시 값을 nHash라고 한다면
 * nHash 와 hHash 값이 같을 때 우리가 원하는 패턴의 문자열을 발견한 것이 된다.
 *
 * 이때 N이 aab이고 H에 aab가 10개 있더라도 한가지 종류로 취급하므로 set을 이용하여
 * nHash == hHash 일 때, 해당 부분 문자열을 set에 넣어주도록 한다.
 *
 * 이 때, 각 문자열을 set에 그대로 넣게 되면 패턴을 찾을 때마다 각각의 긴 부분 문자열이 set에 있는지 문자열 비교를 해야 해서 시간초과가 발생한다.
 * 부분 문자열의 문자의 개수를 이용한 해싱 말고, 원래 문자열 해싱 방식을 이용해서 부분 문자열의 해시값 또한 계산한다음 nHash == hHash 라면, 해당 해시 값을
 * set에 넣어 문자열의 종류를 구분 짓도록 한다.
 * 해시 값을 int로 선언하면 충돌이 발생할 확률이 높아지므로, long을 이용하여 hash를 계산하도록 한다.
 *
 * (+) 기존 문자열에 대한 해시 값 계산은
 *     각 문자열의 글자 Ai 마다 sigma Ai * 31^(i - 1) 를 해주면 된다.
 *     이때, 해당 문자열의 해시값에 라빈-카프 알고리즘을 적용하면
 *     hash = 31 * (hash - 이전 문자열의 맨앞의 글자의 해시값) 새로 들어온 글자의 해시값이 되므로
 *     hash = 31 * (hash - oldChar * 31^(n - 1)) + newChar 가 된다.
 *     빠른 계산을 위해 고정 상수 값인 31^(n - 1) 값을 미리 계산하여 저장 후 사용한다.
 *
 * -----------------
 * Input 1
 * aab
 * abacabaa
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * xyyz
 * xyyzzxyyzyzyzyxzzy
 *
 * Output 2
 * 3
 * -----------------
 */
public class BOJ19608 {

    static final int CHAR_SIZE = 26;
    static String N, H;
    static int[] charCnt;
    static long pow;
    static long[] powers;
    static Set<Long> set = new HashSet<>();

    public static long normalHash(String s) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = 31 * hash + s.charAt(i);
        }

        return hash;
    }

    public static long pow(int len) {
        long pow = 1;
        for (int i = 0; i < len; i++) {
            pow = 31 * pow;
        }

        return pow;
    }

    public static void buildPower() {
        powers = new long[CHAR_SIZE];
        powers[0] = 1;
        for (int i = 1; i < CHAR_SIZE; i++) {
            powers[i] = (powers[i - 1] * 31);
        }
    }

    public static void buildTable(String s) {
        charCnt = new int[CHAR_SIZE];
        for (int i = 0; i < s.length(); i++) {
            charCnt[s.charAt(i) - 'a']++;
        }
    }

    public static long buildHash() {
        long hash = 0;
        for (int i = 0; i < CHAR_SIZE; i++) {
            hash = (hash + charCnt[i] * powers[i]);
        }

        return hash;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = br.readLine();
        H = br.readLine();
        buildPower();
        buildTable(N);
        long nHash = buildHash();
        long hHash = 0;
        long subStrHash = 0;
        pow = pow(N.length() - 1);


        for (int i = 0; i < H.length() - N.length() + 1; i++) {
            char pastChar, newChar;
            pastChar = newChar = '\0';
            if (i == 0) {
                String begin = H.substring(i, N.length());
                buildTable(begin);
                hHash = buildHash();
                subStrHash = normalHash(begin);
            } else {
                pastChar = H.charAt(i - 1);
                newChar = H.charAt(i + N.length() - 1);
                hHash = (hHash - powers[pastChar - 'a'] + powers[newChar - 'a']);
                subStrHash = 31 * (subStrHash - pastChar * pow) + newChar;
            }
            //System.out.println(nHash + " / " + hHash + " / " + H.substring(i, i + N.length()) + " / " + pastChar + " / " + newChar + " / " + subStrHash);
            if (nHash == hHash) {
                set.add(subStrHash);
            }
        }

        bw.write(String.valueOf(set.size()));

        // close the buffer
        br.close();
        bw.close();
    }
}