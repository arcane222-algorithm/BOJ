package priorityqueue;

import java.io.*;
import java.util.*;


/**
 * 보석 도둑 - BOJ1202
 * -----------------
 * Input 1
 * 2 1
 * 5 10
 * 100 100
 * 11
 *
 * Output 1
 * 10
 * -----------------
 * Input 2
 * 3 2
 * 1 65
 * 5 23
 * 2 99
 * 10
 * 2
 *
 * Output 2
 * 164
 * -----------------
 */
public class BOJ1202 {

    private static class Jewel {
        public int weight, value;

        public Jewel(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        public void dump() {
            StringBuilder sb = new StringBuilder();
            sb.append("Jewel: ");
            sb.append(" weight ");
            sb.append(weight);
            sb.append(" value ");
            sb.append(value);

            System.out.println(sb);
        }
    }

    static int N, K;

    public static void main(String[] args) throws IOException {

        // Input & Output
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        LinkedList<Jewel> jewels = new LinkedList<>();
        int[] backpacks = new int[K];

        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            jewels.add(new Jewel(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
        }

        for(int i = 0; i <  K; i++) {
            st = new StringTokenizer(br.readLine());
            backpacks[i] = Integer.parseInt(st.nextToken());
        }

        // Sort by weight (ASC)
        Collections.sort(jewels, Comparator.comparingInt(j -> j.weight));
        // Sort by weight (ASC)
        Arrays.sort(backpacks);

        // Sort by value (DESC)
        PriorityQueue<Jewel> jewelPQ = new PriorityQueue<>((j1, j2) -> Integer.compare(j2.value, j1.value));

        long sum = 0;
        for(int i = 0; i < K; i++) {
            while(!jewels.isEmpty()) {
                if(jewels.getFirst().weight <= backpacks[i]) {
                    jewelPQ.add(jewels.poll());
                } else {
                    break;
                }
            }
            if(!jewelPQ.isEmpty()) {
                sum += jewelPQ.poll().value;
            }
        }

        // Write the result
        bw.write(String.valueOf(sum));

        br.close();
        bw.close();
    }
}