package greedy;

import java.io.*;
import java.util.*;


/**
 * 멀티탭 스케줄링 - BOJ 1700
 * -----------------
 * Input 1
 * 2 7
 * 2 3 2 3 1 2 7
 *
 * Output 1
 * 2
 * -----------------
 */
public class BOJ1700 {

    static int N, K;
    static int[] process;

    public static int indexOf(int start, int val) {
        for(int i = start; i < process.length; i++) {
            if(process[i] == val) {
                return i;
            }
        }

        return -1;
    }

    public static int findProcess(Set<Integer> multiTap, int start) {
        Iterator<Integer> iterator = multiTap.iterator();

        int pTmp = 0, idxTmp = 0;
        while(iterator.hasNext()) {
            int p = iterator.next();
            int idx = indexOf(start, p);

            if(idx == -1) {
                return p;
            } else {
                if(idxTmp < idx) {
                    idxTmp = idx;
                    pTmp = p;
                }
            }
        }

        return pTmp;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        process = new int[K];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < K; i++) {
            process[i] = Integer.parseInt(st.nextToken());
        }

        int ans = 0;
        Set<Integer> multiTap = new HashSet<>();
        for(int i = 0; i < process.length; i++) {
            int p = process[i];

            if(multiTap.size() < N) {
                if(!multiTap.contains(p)) {
                    multiTap.add(p);
                }
            } else {
                if(!multiTap.contains(p)) {
                    multiTap.remove(findProcess(multiTap, i + 1));
                    multiTap.add(p);
                    ans++;
                }
            }
        }

        // write the result
        bw.write(String.valueOf(ans));
        bw.flush();

        // close the i/o stream
        br.close();
        bw.close();
    }
}
