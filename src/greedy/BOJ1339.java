package greedy;

import java.io.*;
import java.util.*;


/**
 * 단어 수학 - BOJ1339
 * -----------------
 * Input 1
 * 2
 * AAA
 * AAA
 *
 * Output 1
 * 1998
 * -----------------
 * Input 2
 * 2
 * GCF
 * ACDEB
 *
 * Output 2
 * 99437
 * -----------------
 * Input 3
 * 10
 * A
 * B
 * C
 * D
 * E
 * F
 * G
 * H
 * I
 * J
 *
 * Output 3
 * 45
 * -----------------
 * Input 4
 * 2
 * AB
 * BA
 *
 * Output 4
 * 187
 * -----------------
 */
public class BOJ1339 {

    static int N;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Integer.parseInt(br.readLine());
        HashMap<Character, Integer> map = new HashMap<>();
        for(int i = 0; i < N; i++) {
            String line = br.readLine();
            int num = (int) Math.pow(10, line.length() - 1);

            for(int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if(map.get(c) != null)
                    map.put(c, map.get(c) + num);
                else
                    map.put(c, num);
                num /= 10;
            }
        }

        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

        int num = 9, ans = 0;
        for(int i = 0; i < list.size(); i++) {
            ans += list.get(i).getValue() * num;
            num--;
        }

        bw.write(String.valueOf(ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
