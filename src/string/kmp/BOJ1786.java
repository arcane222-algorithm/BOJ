package string.kmp;

import java.io.*;
import java.util.*;


/**
 * 찾기 - BOJ1786
 * -----------------
 *
 * KMP (Knuth-Morris-Pratt) 문자열 패턴매칭 알고리즘의 기본문제이다.
 * KMP 알고리즘의 경우 부분문자열에 대한 접두사와 접미사의 최대 일치 여부를 저장하는 pi table을 구축하는 것과 pi table을 이용하여
 * 문자열을 검색하는 검색함수로 구성되어 있다.
 *
 * 1) pi table 구축
 * 예제 입력 ABCDABD에 대한 부분 문자열의 접두사와 접미사의 최대 공통 길이를 계산하면 다음과 같다.
 * (부분 문자열과 접두사가 같은 경우는 세지 않으므로 pi[0]의 값은 1이 아니라 항상 0이다.)
 *    i         str[i]        pi[i]
 *    0          A             0
 *    1          AB            0
 *    2          ABC           0
 *    3          ABCD          0
 *    4          ABCDA         1
 *    5          ABCDAB        2
 *    6          ABCDABD       0
 *
 * pi table은 두개의 포인터를 이용해서 선형시간안에 계산할 수 있다.
 * 패턴 문자열 P에 대하여 1번 인덱스부터 탐색을 시작하므로 pattern = 1, 접두, 접미사의 최대 공통 길이를 match = 0이라고 선언한다.
 *
 * ABCDABD
 *  ABCDABD 와 같이 놓고 보면 search는 B를 가리키고, match는 A를 가리킨다.
 *  만약 두 위치의 문자가 다르고 (P.charAt(search) != P.charAt(match)) 최대 일치 길이가 0이라면, search까지의 부분문자열 (현재 AB)에 대한 접두사 = 접미사인 부분이 없으므로
 *  pi[search] = 0이 되며, 다음 부분 문자열로 넘어간다. (search++)
 *
 *  ABCDABD
 *    ABCDABD 다음 단계의 경우 위와 같이 search는 C를 가리키고, match는 A를 가리킨다.
 *    마찬가지로 search까지의 부분문자열 (현재 ABC)에 대한 접두사 = 접미사인 부분이 없으므로 위와 같이 처리 한다.
 *  위와 같은 반복되는 부분을 계산했다고 가정하고 그다음 최초로 매칭되는 부분에 대해 생각해보자.
 *
 *  ABCDABD
 *      ABCDABD 다음과 같은 경우 위와 같이 search는 A를 가리키고, match도 A를 가리킨다.
 *      접두사 A와 접미사 A가 일치하므로 (P.charAt(search) == P.charAt(match)) 매칭되는 길이를 1 늘려주고 (match++) 해당 길이의 부분 문자열에 이를 저장한다. pi[search] = match;
 *
 *  ABCDABD
 *      ABCDABD 다음과 같은 경우 search값은 B를 가리키는데, match에서 A를 가리키던 포인터가 전 과정에서 1 증가하여 B를 가리킨다.
 *      즉, 접두사 AB와 접미사 AB가 일치하므로 매칭되는 길이를 역시 1 늘려주고 해당 길이의 부분 문자열 pi 테이블에 이를 저장한다.
 *
 * ABCDABD
 *     ABCDABD 마지막으로 search값은 D, match값은 C를 가리키고, 접두, 접미사가 일치하지 않으므로 (P.charAt(search) != P.charAt(match))
 *     match = pi[match - 1]을 해준다.
 *     이때, 이 부분은 while문을 이용하여 최대 일치 길이를 찾도록 하는데,
 *     while(match > 0 && P.charAt(search) != P.charAt(match))
 *          match = pi[match - 1];
 *     과 같이 구성한다.
 *
 *     이 과정을 따라가보면 match가 2인 상태에서 불일치가 발생했기 때문에, match = pi[1] = 0이 되고, pi[search] = 0이 들어가게 된다.
 *     즉, search 값을 0~search 까지의 부분 문자열 중 접미사를 대표하는 포인터라고 생각하고 match를 해당 문자열의 접두사를 나타내는 포인터라고 생각한다면
 *     불일치가 발생했을 때, match 값이 0이라면 접두사와 접미사의 최대 매칭 길이가 0인 것이므로 pi[search] = 0이 들어가게 되고,
 *     match 값이 1이상 이면, 현재 부분 문자열의 앞에서부터 match까지의 접두사가 부분 문자열의 접미사와 일치하지 않는다는 것이므로
 *     앞서 조사했던 접두사와 접미사의 일치 여부 데이터를 이용해서 다시 한번 조사하는 것이다.
 *
 *
 * 2) pi table을 이용한 문자열 검색
 * 패턴 문자열 P를 탐색하려는 문자열 T에 대하여 어느 위치에서 나타나는지 찾기 위해서
 * 앞에서부터 비교하되, 불일치가 발생할 경우 pi table을 이용하여 검색 위치를 점프한다.
 * 각 자리를 비교하며 탐색 문자열의 현재 탐색 위치(search)와 패턴 문자열의 현재 위치(match)값이 같다면 (T.charAt(search) == P.charAt(match))
 * 매칭되는 길이를 늘려준다 (match++)
 * 만약 불일치가 발생한다면, 일치하는 패턴의 길이 - 최대 공통 접두/접미사의 길이 만큼 검색 위치를 뛰어넘는다.
 * 즉, match = pi[match - 1]값을 해준다.
 *
 * 만약 탐색 중 match 값이 pattern의 길이 -1과 같아진다면, 원하는 패턴 P를 문자열 T에서 찾은 것이고,
 * 일치 위치의 시작 index는 (현재 탐색하는 위치 search) - (pattern의 길이) + 1이 될 것이다. (찾은 문자열 search의 index부터 앞으로 pattern 만큼 빼주는 것)
 *
 * -----------------
 * Input 1
 * ABC ABCDAB ABCDABCDABDE
 * ABCDABD
 *
 * Output 1
 * 1
 * 16
 * -----------------
 */
public class BOJ1786 {

    static String T, P;
    static StringBuilder result = new StringBuilder();

    public static int[] buildPI(String p) {
        int[] pi = new int[p.length()];
        int search = 1, match = 0;
        for (; search < p.length(); search++) {
            while (match > 0 && p.charAt(search) != p.charAt(match)) {
                match = pi[match - 1];
            }

            if (p.charAt(search) == p.charAt(match)) {
                match++;
                pi[search] = match;
            }
        }

        return pi;
    }


    public static List<Integer> kmp(String t, String p) {
        int[] pi = buildPI(p);
        List<Integer> result = new ArrayList<>();

        int search = 0, match = 0;
        for (; search < t.length(); search++) {
            while (match > 0 && t.charAt(search) != p.charAt(match)) {
                match = pi[match - 1];
            }

            if (t.charAt(search) == p.charAt(match)) {
                if (match == p.length() - 1) {
                    result.add(search - match + 1);
                    match = pi[match];
                } else {
                    match++;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = br.readLine();
        P = br.readLine();
        List<Integer> res = kmp(T, P);
        result.append(res.size()).append('\n');
        for (int i : res) {
            result.append(i).append(' ');
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}