package tree.trie;

import java.io.*;
import java.util.*;


/**
 * 전설 - BOJ19585
 * -----------------
 *
 * Trie 구성 시 영어 소문자로 Children을 구성해준다.
 * C개의 색상을 먼저 Trie에 삽입한 후 N개의 닉네임의 경우 HashSet<String>에 저장해준다.
 * 이후 Q개의 팀명에 대하여 Trie에서 탐색하여 색상명의 접두사 index 값을 찾는다.
 * (즉, 해당 Trie는 색상에 대한 Trie 이므로 팀명 탐색 시 각 노드가 isEnd 부분을 찾는다)
 * 해당 부분의 뒤에부터는 닉네임이 와야 하므로 index + 1의 위치를 시작점으로 하는 substring을 HashSet에서 있는지 탐색한다.
 *
 * <주의할 점>은
 * 2 1
 * orang
 * orange
 * e
 * 2
 * orange
 * orangee
 * > Test case 2에서 orangee의 경우 index 4 - g와 index 5 - e가 모두 isEnd == true가 되므로
 * > 각 위치에서의 substring은 ee, e가 될 것이고, e의 경우 HashSet에 있으므로 true가 된다.
 * > 즉, 팀명 안에서 isEnd가 될 수 있는 경우를 모두 탐색한다.
 * 
 * 이 문제의 경우 HashSet이 아닌 Trie 두개를 활용하여 색상 Trie와 닉네임 Trie를 각각 만들고
 * 위와 같이 색상 Trie를 탐색하여 접두사 index를 찾아 해당 위치부터 닉네임 Trie를 탐색하는 방식으로도 해결할 수 있으나, 
 * Java의 경우 시간 초과가 발생한다.
 * 그러므로 색상 Trie를 이용해 index 값을 얻고 HashSet을 이용하여 닉네임의 경우 O(1)에 비교할 수 있도록 한다.
 *
 * -----------------
 * Input 1
 * 4 3
 * red
 * blue
 * purple
 * orange
 * shift
 * joker
 * noon
 * 5
 * redshift
 * bluejoker
 * purplenoon
 * orangeshift
 * whiteblue
 *
 * Output 1
 * Yes
 * Yes
 * Yes
 * Yes
 * No
 * -----------------
 * Input 2
 * 2 1
 * orang
 * orange
 * e
 * 2
 * orange
 * orangee
 *
 * Output 2
 * Yes
 * Yes
 * -----------------
 */
public class BOJ19585 {

    private static class Trie {
        private static final int MAX_SIZE = 26;
        private static final int BASE_CHAR = 'a';

        Trie[] children;
        char c;
        boolean isEnd;

        public Trie() {
            children = new Trie[MAX_SIZE];
        }

        public void add(String word) {
            Trie current = this;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - BASE_CHAR;
                if (current.children[idx] == null) {
                    Trie trie = new Trie();
                    trie.c = c;
                    current.children[idx] = trie;
                }
                current = current.children[idx];
            }
            current.isEnd = true;
        }
    }

    static int N, C, Q;
    static Trie colorTrie;
    static HashSet<String> nicknameSet;
    static StringBuilder result = new StringBuilder();

    public static boolean check(String teamName) {
        Trie current = colorTrie;

        for (int i = 0; i < teamName.length(); i++) {
            char c = teamName.charAt(i);
            int idx = c - Trie.BASE_CHAR;

            if (current.children[idx] == null) {
                break;
            } else if (current.children[idx].isEnd) {
                String nickname = teamName.substring(i + 1);
                if(nicknameSet.contains(nickname)) return true;
            }

            current = current.children[idx];
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // parse N, C
        C = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        colorTrie = new Trie();
        nicknameSet = new HashSet<>();

        // parse colors
        for (int i = 0; i < C; i++) {
            String color = br.readLine();
            colorTrie.add(color);
        }

        // parse nicknames
        for (int i = 0; i < N; i++) {
            String nickname = br.readLine();
            nicknameSet.add(nickname);
        }

        //parse Q
        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; i++) {
            String teamName = br.readLine();
            result.append(check(teamName) ? "Yes" : "No");
            if (i < Q - 1) result.append('\n');
        }

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
