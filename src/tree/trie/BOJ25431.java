package tree.trie;

import java.io.*;
import java.util.*;


/**
 * XOR 놀이 - BOJ25431
 * -----------------
 *
 *
 *
 *
 *
 * -----------------
 * Input 1
 * 7
 * 92 104 100 43 28 108 97
 * 9
 * 2 2 6 76
 * 1 3 4 66
 * 2 6 7 6
 * 3 6 16
 * 2 6 6 101
 * 1 7 7 69
 * 3 6 6
 * 2 1 3 108
 * 1 5 5 124
 *
 * Output 1
 * 4
 * 3
 * 6
 * 6
 * 7
 * 1
 * 5
 * -----------------
 */
public class BOJ25431 {

    private static class BinaryTrie {

        private static final int MAX_BIT_SIZE = 24;

        int binaryIdx, num;
        BinaryTrie parent;
        BinaryTrie[] nodes;
        TreeSet<Integer> arrIdxes;

        public BinaryTrie() {
            nodes = new BinaryTrie[2];
        }

        public boolean isLeap() {
            return nodes[0] == null && nodes[1] == null;
        }

        public void add(int num, int arrIdx) {
            BinaryTrie curr = this;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int binaryIdx = (num >> i) & 1;
                BinaryTrie next = curr.nodes[binaryIdx];

                if (next == null) {
                    next = new BinaryTrie();
                    next.binaryIdx = binaryIdx;
                    next.parent = curr;
                    curr.nodes[binaryIdx] = next;
                }
                curr = next;
            }

            if (curr.arrIdxes == null)
                curr.arrIdxes = new TreeSet<>();

            curr.arrIdxes.add(arrIdx);
            curr.num = num;
        }

        public void remove(int num, int arrIdx) {
            BinaryTrie curr = this;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int binaryIdx = (num >> i) & 1;
                BinaryTrie next = curr.nodes[binaryIdx];
                if (next == null) {
                    return;
                }
                curr = next;
            }
            curr.arrIdxes.remove(arrIdx);

            if (curr.arrIdxes.size() == 0) {
                while (curr.isLeap()) {
                    int binaryIdx = curr.binaryIdx;
                    curr = curr.parent;

                    if (curr != null) {
                        curr.nodes[binaryIdx] = null;
                    } else {
                        break;
                    }
                }
            }
        }

        public void xorMin(int num) {
            BinaryTrie curr = this;
            int xorValue = 0;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int binaryIdx = (num >> i) & 1;
                BinaryTrie next = curr.nodes[binaryIdx];

                if (next == null) {
                    binaryIdx ^= 1;
                    next = curr.nodes[binaryIdx];
                    xorValue += (1 << i);
                }
                curr = next;
            }

            int arrIdx = curr.arrIdxes.first();
            if (xorValue < resXorVal) {
                resXorVal = xorValue;
                resIdx = arrIdx;
            } else if (xorValue == resXorVal) {
                if (arrIdx < resIdx) {
                    resIdx = arrIdx;
                }
            }
        }

        public void xorMax(int num) {
            BinaryTrie curr = this;
            int xorValue = 0;

            for (int i = MAX_BIT_SIZE - 1; i > -1; i--) {
                int oppositeIdx = ((num >> i) & 1) ^ 1;
                BinaryTrie next = curr.nodes[oppositeIdx];

                if (next == null) {
                    oppositeIdx ^= 1;
                    next = curr.nodes[oppositeIdx];
                } else {
                    xorValue += (1 << i);
                }
                curr = next;
            }

            int arrIdx = curr.arrIdxes.first();
            if (xorValue > resXorVal) {
                resXorVal = xorValue;
                resIdx = arrIdx;
            } else if (xorValue == resXorVal) {
                if (arrIdx < resIdx) {
                    resIdx = arrIdx;
                }
            }
        }

        public void printAll() {
            BinaryTrie curr = this;
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] != null) {
                    nodes[i].printAll();
                }
            }

            if (curr.isLeap()) {
                System.out.println("idx: " + curr.arrIdxes + " | " + "num: " + curr.num);
            }
        }
    }

    static int N, Q, sqrtN, resIdx, resXorVal;
    static int[] nums;
    static BinaryTrie[] tries;
    static StringBuilder result = new StringBuilder();

    public static void xorMinQuery(int l, int r, int x) {
        resIdx = Integer.MAX_VALUE;
        resXorVal = Integer.MAX_VALUE;

        for (; l % sqrtN != 0 && l <= r; l++) {
            int xorValue = nums[l] ^ x;
            if (xorValue < resXorVal) {
                resXorVal = xorValue;
                resIdx = l;
            } else if (xorValue == resXorVal) {
                if (l < resIdx) {
                    resIdx = l;
                }
            }
        }

        for (; (r + 1) % sqrtN != 0 && l <= r; r--) {
            int xorValue = nums[r] ^ x;
            if (xorValue < resXorVal) {
                resXorVal = xorValue;
                resIdx = r;
            } else if (xorValue == resXorVal) {
                if (r < resIdx) {
                    resIdx = r;
                }
            }
        }

        for (; l <= r; l += sqrtN) {
            int trieArrIdx = l / sqrtN;
            tries[trieArrIdx].xorMin(x);
        }

        result.append(resIdx + 1).append('\n');
    }

    public static void xorMaxQuery(int l, int r, int x) {
        resIdx = -1;
        resXorVal = -1;

        for (; l % sqrtN != 0 && l <= r; l++) {
            int xorValue = nums[l] ^ x;
            if (xorValue > resXorVal) {
                resXorVal = xorValue;
                resIdx = l;
            } else if (xorValue == resXorVal) {
                if (l < resIdx) {
                    resIdx = l;
                }
            }
        }

        for (; (r + 1) % sqrtN != 0 && l <= r; r--) {
            int xorValue = nums[r] ^ x;
            if (xorValue > resXorVal) {
                resXorVal = xorValue;
                resIdx = r;
            } else if (xorValue == resXorVal) {
                if (r < resIdx) {
                    resIdx = r;
                }
            }
        }

        for (; l <= r; l += sqrtN) {
            int trieArrIdx = l / sqrtN;
            tries[trieArrIdx].xorMax(x);
        }

        result.append(resIdx + 1).append('\n');
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        nums = new int[N];

        sqrtN = (int) Math.sqrt(N) <= 80 ? (int) Math.sqrt(N) : N / 80;
        int trieArrSize = N / sqrtN + 1;
        tries = new BinaryTrie[trieArrSize];
        for (int i = 0; i < trieArrSize; i++) {
            tries[i] = new BinaryTrie();
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());
            int trieArrIdx = i / sqrtN;
            nums[i] = num;
            tries[trieArrIdx].add(num, i);
        }

        int l, r, p, x;
        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int qType = Integer.parseInt(st.nextToken());

            switch (qType) {
                case 1:
                    l = Integer.parseInt(st.nextToken()) - 1;
                    r = Integer.parseInt(st.nextToken()) - 1;
                    x = Integer.parseInt(st.nextToken());
                    xorMinQuery(l, r, x);
                    break;

                case 2:
                    l = Integer.parseInt(st.nextToken()) - 1;
                    r = Integer.parseInt(st.nextToken()) - 1;
                    x = Integer.parseInt(st.nextToken());
                    xorMaxQuery(l, r, x);
                    break;

                case 3:
                    p = Integer.parseInt(st.nextToken()) - 1;
                    x = Integer.parseInt(st.nextToken());
                    int trieArrIdx = p / sqrtN;
                    tries[trieArrIdx].remove(nums[p], p);
                    tries[trieArrIdx].add(x, p);
                    nums[p] = x;
                    break;

                default:
                    assert false;
            }
        }

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
