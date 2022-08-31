package implementation;

import java.io.*;
import java.util.*;



/**
 * 회문 - BOJ17609
 * -----------------
 *
 * 투 포인터 방식으로 문자열 길이 S / 2의 크기만큼 나누어 왼쪽과 오른쪽의 문자가 같은지 비교한다.
 * 만약 불일치가 발생핸다면 불일치가 발생한 위치의 인덱스를 idx라고 할 때,
 * S[idx, S - idx - 2], S[idx + 1, S - idx - 1]의 부분 문자열을 이용하여 다시 팰린드롬 검사를 수행한다.
 * 둘 중 부분 문자열이 팰린드롬인 경우 문제의 조건에 따라 유사회문(pseudo palindrome) 이므로 1을 출력한다.
 * 부분 문자열에 대해서도 불일치가 발생하면 유사회문이 될 수 없으므로 2를 출력한다.
 * 유사회문인 경우가 발생하지 않으면서 투 포인트의 글자가 모두 일치하면 회문이므로 0을 출력한다.
 *
 * -----------------
 * Input 1
 * 7
 * abba
 * summuus
 * xabba
 * xabbay
 * comcom
 * comwwmoc
 * comwwtmoc
 *
 * Output 1
 * 0
 * 1
 * 1
 * 2
 * 2
 * 0
 * 1
 * -----------------
 * Input 2
 * 21
 * abbab
 * aab
 * aaab
 * aaaab
 * aaaaab
 * aaaaaab
 * axaaxaa
 * abcddadca
 * aabcbcaa
 * ababbabaa
 * abca
 * babba
 * sumumuus
 * XYXYAAYXY
 * abc
 * cccfccfcc
 * abcddcdba
 * ppbpppb
 * aabcdeddcba
 * aabab
 * aapqbcbqpqaa
 *
 * Output 2
 * 1
 * 1
 * 1
 * 1
 * 1
 * 1
 * 1
 * 2
 * 1
 * 1
 * 1
 * 1
 * 1
 * 1
 * 2
 * 1
 * 1
 * 2
 * 2
 * 2
 * 1
 * -----------------
 */
public class BOJ17609 {

    public static int isPalindrome(String s, boolean toggle) {
        int half = s.length() >> 1;

        for (int i = 0; i < half; i++) {
            if (s.charAt(i) != s.charAt(s.length() - i - 1)) {
                if (!toggle) {
                    int res1 = isPalindrome(s.substring(i, s.length() - i - 1), true);
                    int res2 = isPalindrome(s.substring(i + 1, s.length() - i), true);
                    if (res1 == 2 && res2 == 2) {
                        return 2;
                    } else {
                        return 1;
                    }
                } else {
                    return 2;
                }
            }
        }

        if (toggle) return 1;
        else return 0;
    }

    static int N;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            result.append(isPalindrome(br.readLine(), false)).append('\n');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
