package binarysearch;

import java.io.*;
import java.util.*;


/**
 * 두 배열의 합 - BOJ2143
 * -----------------
 * -----------------
 * Input 1
 * 5
 * 4
 * 1 3 1 2
 * 3
 * 1 3 2
 *
 * Output 1
 * 7
 * -----------------
 */
public class BOJ2143 {

    static int T, N, M;

    public static ArrayList<Long> getSubSumArr(int[] arr) {
        ArrayList<Long> list = new ArrayList<>();
        for(int i = 0; i < arr.length; i++) {
            long sum = arr[i];
            list.add(sum);
            for(int j = i + 1; j < arr.length; j++) {
                sum += arr[j];
                list.add(sum);
            }
        }

        return list;
    }

    public static long getDuplicateCnt(int pivot, ArrayList<Long> list, int order) {
        if(order == 0 && pivot > list.size() - 1) throw new IndexOutOfBoundsException();
        if(order == 1 && pivot < 0) throw new IndexOutOfBoundsException();

        long cnt = 0;
        long val = list.get(pivot);
        switch(order) {
            case 0:
                for(int i = pivot; i < list.size(); i++) {
                    if(val == list.get(i)) cnt++;
                    else break;
                }
                break;

            case 1:
                for(int i = pivot; i > -1; i--) {
                    if(val == list.get(i)) cnt++;
                    else break;
                }
                break;
        }

        return cnt;
    }

    public static void main(String[] args) throws IOException {
        // open the io stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());

        N = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        int[] arrA = new int[N];
        for(int i = 0; i < N; i++) {
            arrA[i] = Integer.parseInt(st.nextToken());
        }

        M = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        int[] arrB = new int[M];
        for(int i = 0; i < M; i++) {
            arrB[i] = Integer.parseInt(st.nextToken());
        }

        ArrayList<Long> subA = getSubSumArr(arrA);
        ArrayList<Long> subB = getSubSumArr(arrB);
        Collections.sort(subA);
        Collections.sort(subB);

        int p1 = 0, p2 = subB.size() - 1;
        long sum = 0, cnt = 0;
        while(true) {
            if(p1 > subA.size() - 1 || p2 < 0) break;

            sum = subA.get(p1) + subB.get(p2);
            if(sum > T) {
                p2--;
            } else if (sum < T) {
                p1++;
            } else {
                long dupACnt = getDuplicateCnt(p1, subA, 0);
                long dupBCnt = getDuplicateCnt(p2, subB, 1);
                cnt += dupACnt * dupBCnt;
                p1 += dupACnt;
                p2 -= dupBCnt;
            }
        }

        // write the result
        bw.write(String.valueOf(cnt));

        // close the io stream
        br.close();
        bw.close();
    }
}
