package datastructure.priorityqueue;

import java.io.*;
import java.util.*;


/**
 * 가운데를 말해요 - BOJ1655
 * -----------------
 * Input 1
 * 7
 * 1
 * 5
 * 2
 * 10
 * -99
 * 7
 * 5
 *
 * Output 1
 * 1
 * 1
 * 2
 * 2
 * 2
 * 2
 * 5
 * -----------------
 */
public class BOJ1655 {

    static int N;

    public static void main(String[] args) throws IOException {
        // open the io stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // MaxHeap: [-99 1 2 5(root)], MinHeap: [5(root) 7 10]
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        N = Integer.parseInt(br.readLine());
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < N; i++) {
            int num = Integer.parseInt(br.readLine());

            if(minHeap.size() == maxHeap.size()) maxHeap.add(num);
            else minHeap.add(num);

            if(!minHeap.isEmpty() && !maxHeap.isEmpty()) {
                if(minHeap.peek() < maxHeap.peek()) {
                    int tmp = maxHeap.poll();
                    maxHeap.add(minHeap.poll());
                    minHeap.add(tmp);
                }
            }
            result.append(maxHeap.peek());
            result.append('\n');
        }

        // write the result
        bw.write(result.toString());

        // close the io stream
        br.close();
        bw.close();
    }
}
