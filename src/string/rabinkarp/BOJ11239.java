package string.rabinkarp;

import java.io.*;
import java.util.*;


/**
 * FindWords - BOJ11239
 * -----------------
 * <p>
 * 메모리 제한을 뚫기 위해 압축 트라이 (Compression Trie)를 이용하거나 라빈-카프 알고리즘 (해싱)을 이용하는 두 가지 방법이 있는데, 라빈-카프 알고리즘을 이용한 풀이이다.
 * N개의 단어를 Pattern이라고 생각하고 각각을 해싱한 후, 각 단어의 길이에 맞는 Set을 만들어 해시 값을 저장한다.
 * N개의 단어의 길이가 최대 50개까지 들어오므로 HashSet 배열을 크기 50만큼 선언하여 사용하면 된다.
 * 예를 들어, ambiguous의 해시 값을 9번째 set에 저장하면 되는 것이다. sets[8].add(hash value of "ambiguous");
 * <p>
 * 패턴을 찾을 M개의 문장에 대하여 최소 길이 6부터 최대 길이 50까지 라빈-카프 알고리즘을 통하여 각 substring의 해시 값을 구해 길이에 맞는 Set에 해당 해시 값이 있는지 조사한다.
 * 해당 해시 값이 있다면 해당 길이에 해당하는 패턴이 문장 안에 포함된 것이므로 해당 단어를 단어 Set에 저장한다.
 * 만약 단어 Set의 크기가 0이면 NO, 1이면 해당 단어를, 2 이상 될 경우 AMBIGUOUS를 출력하면 된다.
 * <p>
 * 예제 입력의 문자열 abcdefghijklmetamorphismnopqrstuvwxyz에 대하여
 * 길이 6부터 탐색한다고 하면 abcdef의 해시 값을 구한 후 이후 각각 부분 문자열 bcdefg, cdefgh ... uvwxyz은
 * 라빈-카프 알고리즘을 이용하여 hash = 소수 P * (hash - oldChar * power) + newChar 방식으로 구해주면 된다.
 * (빠져나간 맨 앞의 해시 값을 빼주고, 각 자리의 거듭제곱을 1씩 올려준 후 새로 들어온 글자의 해시 값을 더해주는 원리로 O(1)만에 계산이 가능하다)
 * <p>
 * 이러한 방식으로 길이 6의 경우를 모두 탐색하고 7의 경우를 모두 탐색하고 ... 50까지 탐색하며 각각 길이 N까리 단어들의 해시 값을 모아둔 길이 N Set에 해당 단어가 있는지 조사하는 방식으로 동작하는 것이다.
 * <p>
 * -----------------
 * Input 1
 * 1
 * 5
 * ambiguous
 * metamorphism
 * inevitably
 * impeccable
 * constellation
 * 3
 * inevitable
 * abcdefghijklmetamorphismnopqrstuvwxyz
 * itsambiguousbecausethereisconstellationtoo
 * <p>
 * Output 1
 * NO
 * metamorphism
 * AMBIGUOUS
 * -----------------
 */
public class BOJ11239 {

    static final int MAX_PATTERN_SIZE = 50;
    static final int P = 31;
    static int T, N, M;
    static Set<Long>[] sets = new HashSet[MAX_PATTERN_SIZE];
    static Set<String> wordSet = new HashSet<>();
    static StringBuilder result = new StringBuilder();

    static long[] powers = new long[MAX_PATTERN_SIZE];

    public static void buildPowers() {
        powers[0] = 1;
        for (int i = 1; i < MAX_PATTERN_SIZE; i++) {
            powers[i] = powers[i - 1] * P;
        }
    }

    public static long createHash(String s) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = P * hash + s.charAt(i);
        }

        return hash;
    }

    public static void rabinKarp(String s, int patternSize) {
        long hash = 0;
        for (int i = 0; i < s.length() - patternSize + 1; i++) {
            if (i == 0) {
                hash = createHash(s.substring(i, patternSize));
            } else {
                char oldChar = s.charAt(i - 1);
                char newChar = s.charAt(i + patternSize - 1);
                hash = P * (hash - oldChar * powers[patternSize - 1]) + newChar;
            }

            if (sets[patternSize - 1].contains(hash)) {
                wordSet.add(s.substring(i, i + patternSize));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        buildPowers();
        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            for (int j = 0; j < MAX_PATTERN_SIZE; j++) {
                sets[j] = new HashSet<>();
            }

            for (int j = 0; j < N; j++) {
                String pattern = br.readLine();
                long hash = createHash(pattern);
                sets[pattern.length() - 1].add(hash);
            }

            M = Integer.parseInt(br.readLine());
            for (int j = 0; j < M; j++) {
                wordSet.clear();
                String text = br.readLine();
                for (int k = 6; k < MAX_PATTERN_SIZE + 1; k++) {
                    if (wordSet.size() > 1) {
                        break;
                    }
                    rabinKarp(text, k);
                }

                if (wordSet.size() == 0) {
                    result.append("NO");
                } else if (wordSet.size() == 1) {
                    for (String s : wordSet) {
                        result.append(s);
                    }
                } else {
                    result.append("AMBIGUOUS");
                }
                result.append('\n');
            }
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
