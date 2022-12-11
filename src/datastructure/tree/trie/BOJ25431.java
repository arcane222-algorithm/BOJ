package datastructure.tree.trie;

import java.io.*;
import java.util.*;


/**
 * XOR 놀이 - BOJ25431
 * -----------------
 * category: data structure (자료구조), tree (트리), trie (트라이), set / map by trees (트리를 사용한 집합과 맵), sqrt decomposition (제곱근 분할법)
 * Time-Complexity: O((N / sqrt(N)) * depth * Q)
 * -----------------
 *
 * 구간을 제곱근 분할법을 이용하여 sqrt(N) 개수의 bucket으로 나누고 bucket마다 trie를 구축하여 구간 쿼리를 처리한다.
 *
 * 각 쿼리 중 3번 부터 생각해보자.
 * 3번 쿼리의 경우 수열 A에서의 p번째 값 Ap를 x로 바꿔야 하는데, 이것은 기존에 trie에 존재하는 값을 제거하고 새로운 값을 삽입해야 한다.
 * p번째 index가 위치하는 bucket의 index를 구해 해당 bucket의 trie에서 기존의 Ap 값을 삭제하고 x를 추가하면 된다.
 * 이 때 trie에 해당 숫자가 여러 개 존재할 수 있고 1번이나 2번 쿼리 시 XOR한 수가 가장 작은 값을 출력하는 과정에서 결과가 여러 개 라면 index가 가장 작은 값을 출력해야 하므로
 * 특정 값을 나타내는 node에 (BinaryTrie에서 leaf 노드가 될 것이다) TreeSet 필드를 설정하여 같은 값이 들어오면 index들이 정렬되어 관리될 수 있도록 한다.
 * 만약 위 3번 쿼리 처럼 해당 값을 지우는 연산을 해야 할 경우 TreeSet에서 해당 index를 제거하고, 만약 TreeSet의 크기가 0이라면 수열의 해당 값이 trie에 존재하지 않게 된 것이므로
 * trie에서 제거하는 과정을 수행한다. (leaf부터 parent의 child가 없는 경우까지 거슬러 올라가며 노드를 지운다)
 *
 * 이제 1, 2번 쿼리에 대해 생각해보면 주어진 구간 [l, r]에 대해 x와 XOR하여 가장 작은 / 가장 큰 값의 index를 구해야 한다.
 * 위에서 설명한 것처럼 구간을 sqrt(N)의 사이즈로 나누었으므로 구간 [l, r]에 완전히 걸치는 부분의 bucket에서 trie의 각 값과 x를 xor 했을 때의 최대/최소를 구하면 된다.
 * 그리고 구간의 양 끝에 bucket에 완전히 걸치지 않는 영역은 선형탐색하며 각 값과 x 를 xor 했을 때의 최대/최소를 구하면 된다.
 *
 * 구간이 완전히 sqrt(N)개로 분할 되었다고 가정할 때 (즉, sqrt(N)의 값이 정수) worst case의 Time-complexity를 생각해보면
 * (i) 구간 [l, r]이 전체 bucket을 완전히 커버하는 경우
 *     이와 같은 쿼리가 Q개 들어온다고 할 때
 *     각 bucket의 trie에서 x와 xor 했을 때 최대/최소를 구하는 것은 trie의 depth만큼 한번 search 하면 구할 수 있으므로
 *     (N / sqrt(N)) * depth * Q의 연산이 필요하다.
 *     (bucket 하나의 크기는 sqrt(N)이고, 각 bucket의 개수는 N / sqrt(N)개 이므로)
 *
 * (ii) 구간 [l, r]이 전체 bucket의 좌우 1씩 빠진 범위를 커버하는 경우 (즉, 전체 범위가 [0, N - 1]이라 하면 [1, N - 2]인 경우)
 *      이와 같은 쿼리가 Q개 들어온다고 할 때
 *      sqrt(N) - 2개의 bucket의 trie에서 xor 최대/최소를 구하고 양쪽의 sqrt(N) - 1개의 원소에 대해서는 선형탐색 해야 한다.
 *      즉, Q * (depth * ((N / sqrt(N)) - 2) + 2 * (sqrt(N) - 1))
 *
 * 문제의 조건에서 Q = 최대 50000, N = 최대 100000, trie depth = 24(각 수가 최대 10^7이므로 최대 24개의 bit로 표현해야함)이므로
 * (sqrt(N) = 316.xxx이지만, 정확히 나누어 지도록 sqrt(N)과 근접한 400으로 설정함)
 *
 * (i) = (N / sqrt(N)) * depth * Q = 316 * 24 * 50000 = 300,000,000
 * (ii) = Q * (depth * (N / sqrt(N) - 2) + 2 * (sqrt(N) - 1)) = 50000 * (24 * 314 + 2 * 315) = 322,500,000
 * 즉, 문제의 제한시간이 3초이므로 약 3억 번의 계산을 하게 되면 TLE가 발생한다.
 * (전처리 등 여러 가지 요인이 추가되므로 1초에 1억번 계산이라는 경우보다 더 타이트하게 계산해야 함)
 *
 * 제곱근 분할법의 경우 bucket의 크기를 sqrt(N)의 크기로 잡는 것이 일반적이지만 위 문제의 경우 연산 횟수를 줄이기 위해
 * 시간 복잡도에서 N / sqrt(N) 부분의 값을 줄일 필요가 있다. (즉, bucket의 크기를 sqrt(N)보다 훨씬 크게 늘려야 한다.)
 * bucket의 크기를 늘릴 경우 (i)의 시간복잡도가 감소하지만 (ii)와 같은 입력에 대하여 선형탐색하는 부분의 구간이 길어지므로 시간복잡도가 증가할 수 있다.
 *
 * 대략적인 최적의 값으로 1000 ~ 1250을 설정했으나,
 * bucket size = 3000으로 설정 했을 때 가장 빠른 것으로 보아
 * (ii)와 같은 case 보단 (i)과 같은 case가 더 많거나 (ii)와 같은 case 중 선형 탐색 구간이 짧은 경우가 더 많이 주어지는 것으로 추정된다.
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
