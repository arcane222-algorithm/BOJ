package datastructure.priorityqueue;

import java.io.*;
import java.util.*;


/**
 * XOR Sum - BOJ11338
 * -----------------
 *
 * insert 연산이 주어졌을 때 숫자를 우선순위 큐에 넣고 print 연산이 주어졌을 때
 * 매번 우선순위 큐에서 큰 순서대로 수들을 xor해서 결과를 출력하면 시간초과가 발생한다.
 *
 * insert 연산을 수행하며 우선순위 큐에 값을 삽입할 때마다 xor sum을 미리 계산해준다.
 * 이때 삽입 연산을 수행한 후 우선순위 큐의 크기가 K보다 커진다면 xor sum에 우선순위 큐의 가장 작은 값을 xor 해준다.
 *
 * why?) 현재까지 우선순위 큐에 삽입 된 숫자들 중에 큰 순서대로 K개의 수를 XOR 해야 하는데, 이는 전체 xor 결과에서 가장 작은 수의 xor 결과를 빼는 것과 같다.
 *       Num ^ x ^ x = Num의 성질을 이용하여 (즉, 같은 수의 xor는 0이고 0과의 xor는 자기 자신이므로)
 *       지금까지 합한 xor sum에 가장 작은 수의 xor도 포함되어 있을 테고, 여기서 다시 가장 작은 수를 xor 하여 없애는 것이다.
 *       ex) 숫자가 1 2 3 4 5가 우선순위 큐에 들어가 있고, K = 5 일 때, 다음 입력으로 6이 들어온다면
 *           xor sum은 1^2^3^4^5 에서 1^2^3^4^5^6이 될 것이고 PriorityQueue.size = 6 > K 이므로, 1^2^3^4^5^6 에서 pq의 가장 작은 1을 다시 xor 하여
 *           1^2^3^4^5^6 ^ 1 = 1 ^ 1 ^2^3^4^5^6 = 2^3^4^5^6 이 되므로 xor sum이 큰 순서대로 K개를 뽑아 xor 한 것과 같아진다.
 *       이때, 가장 작은 값을 다시 xor 해줬으므로 해당 값을 po에서 삭제한다.
 *       (pq 에서 삭제하지 않는다면 위 상황에서 7이 삽입 될 때, xor sum에서 2를 지워야 하나 다시 1이 지워지는 상황이 발생할 것이다.)
 *
 * -----------------
 * Input 1
 * 1
 * 5 2
 * insert 1
 * insert 2
 * print
 * insert 3
 * print
 *
 * Output 1
 * 3
 * 1
 * -----------------
 */
public class BOJ11338 {

    static int T, Q, K;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            Q = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            PriorityQueue<Integer> pq = new PriorityQueue<>();

            int xorSum = 0;
            for (int j = 0; j < Q; j++) {
                st = new StringTokenizer(br.readLine());
                String inst = st.nextToken();

                switch (inst) {
                    case "insert":
                        int value = Integer.parseInt(st.nextToken());
                        xorSum ^= value;
                        pq.add(value);
                        if (pq.size() > K)
                            xorSum ^= pq.poll();
                        break;

                    case "print":
                        result.append(xorSum).append('\n');
                        break;

                    default:
                        assert false;
                }
            }
        }

        bw.write(result.toString());


        // close the buffer
        br.close();
        bw.close();
    }
}