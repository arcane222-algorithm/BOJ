package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * Matrix Again - BOJ12987
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(N^3logK)
 * -----------------
 *
 * 크기가 N*N인 정사각행렬 A에 대하여 S(K) = A + A^2 + A^3 + ... + A^K를 구해야 한다.
 * 'BOJ13246 - 행렬 제곱의 합' 과 거의 같은 문제이지만 주어진 행렬의 원소에 음수가 올 수 있기 때문에
 * modular 연산 시 음수 처리를 잘 해주어야 한다. (기존 element % mod 로 처리하던 연산을
 * element < 0 ? ((element % mod) + mod) % mod : element % mod)
 * 와 같이 element 값이 음수일 때 따로 처리해주어야 한다.
 *
 * 'BOJ13246 - 행렬 제곱의 합'의 문제처럼 S(K)를 일반화 하면
 * 이 식을 일반화 하면
 * (i) K = 2n (K >= 1), S(K) = S(n)(I + A^n)
 * (ii) K = 2n + 1 (K >= 1) S(K) = S(n)(I + A^(n+1)) + A^(n+1) 로 표현할 수 있다.
 *
 * S(K) 자체도 분할정복 과정을 거치며 계산을 수행하고 K가 홀수인 상황에서 발생하는 A^(n+1) 경우
 * 단일 행렬의 분할정복을 이용한 거듭제곱을 이용해 N^3log(n+1)에 계산하도록 한다.
 *
 * -----------------
 * Input 1
 * 2 2 4
 * 0 1
 * 1 1
 *
 * Output 1
 * 1 2
 * 2 3
 * -----------------
 * Input 2
 * 2 1 1007
 * 2500 -5000
 * -7500 -10000
 *
 * Output 2
 * 486 35
 * 556 70
 * -----------------
 * Input 3
 * 2 3 1007
 * -2500 -5000
 * -7500 10000
 *
 * Output 3
 * 733 202
 * 303 228
 * -----------------
 * Input 4
 * 2 7 1007
 * -2500 -5000
 * -7500 10000
 *
 * Output 4
 * 644 769
 * 650 232
 * -----------------
 * Input 5
 * 2 99 1007
 * -2500 -5000
 * -7500 10000
 *
 * Output 5
 * 969 855
 * 779 342
 * -----------------
 */
public class BOJ12987 {

    private static class Matrix2D {
        private int row, column, size;
        private long[] elements;

        public Matrix2D(int row, int column) {
            this.row = row;
            this.column = column;
            this.size = row * column;
            elements = new long[size];
        }

        public static Matrix2D getIdentity(int n) {
            Matrix2D identity = new Matrix2D(n, n);
            for (int i = 0; i < n; i++) {
                identity.setElement(i, i, 1);
            }
            return identity;
        }

        public boolean addToMat(Matrix2D mat) {
            if (mat.getRow() != row || mat.getColumn() != column) return false;
            for (int i = 0; i < size; i++) {
                elements[i] += mat.getElement(i);
            }
            return true;
        }

        public Matrix2D add(Matrix2D mat) {
            Matrix2D result = new Matrix2D(row, column);
            for (int i = 0; i < size; i++) {
                result.setElement(i, elements[i] + mat.getElement(i));
            }

            return result;
        }

        public Matrix2D addIdentity(int n) {
            if (row != column) return this;

            Matrix2D result = new Matrix2D(row, column);
            result.elements = Arrays.copyOf(elements, elements.length);
            for (int i = 0; i < n; i++) {
                result.setElement(i, i, result.getElement(i, i) + 1);
            }
            return result;
        }

        public boolean subToMat(Matrix2D mat) {
            if (mat.getRow() != row || mat.getColumn() != column) return false;
            for (int i = 0; i < size; i++) {
                elements[i] -= mat.getElement(i);
            }
            return true;
        }

        public Matrix2D subtract(Matrix2D mat) {
            Matrix2D result = new Matrix2D(row, column);
            for (int i = 0; i < size; i++) {
                result.setElement(i, elements[i] - mat.getElement(i));
            }

            return result;
        }

        public Matrix2D multiply(Matrix2D mat) {
            long element = 0;
            Matrix2D result = new Matrix2D(row, mat.getColumn());

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < mat.column; j++) {
                    element = 0;
                    for (int k = 0; k < column; k++) {
                        element += getElement(i, k) * mat.getElement(k, j);
                    }
                    result.setElement(i, j, element);
                }
            }

            return result;
        }

        public Matrix2D modular(long n) {
            for (int i = 0; i < elements.length; i++) {
                elements[i] = elements[i] < 0 ? ((elements[i] % n) + n) % n : elements[i] % n;
            }

            return this;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public long getElement(int idx) {
            if (idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            return elements[idx];
        }

        public long getElement(int r, int c) {
            int idx = r * column + c;
            if (idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            return elements[idx];
        }

        public void setElement(int idx, long e) {
            if (idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            elements[idx] = e;
        }

        public void setElement(int r, int c, long e) {
            int idx = r * column + c;
            if (idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            elements[idx] = e;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    result.append(getElement(i, j));
                    if (j < column - 1)
                        result.append(' ');
                }
                if (i < row - 1)
                    result.append('\n');
            }

            return result.toString();
        }
    }

    static int N;
    static long K, M;
    static Matrix2D mat2D;

    public static Matrix2D fastPow(Matrix2D mat2D, long n) {
        Matrix2D result = Matrix2D.getIdentity(N);
        mat2D.modular(M);

        while (n > 0) {
            if ((n & 1) == 1) result = result.multiply(mat2D).modular(M);
            mat2D = mat2D.multiply(mat2D).modular(M);
            n >>= 1;
        }

        return result.modular(M);
    }

    public static Matrix2D powSum(Matrix2D mat2D, long K) {
        if (K == 1) {
            return mat2D.modular(M);
        } else {
            Matrix2D halfSum = powSum(mat2D, K >> 1);
            Matrix2D halfTerm = fastPow(mat2D, (K & 1) == 0 ? K >> 1 : (K + 1) >> 1);

            if ((K & 1) == 0) {
                return halfSum.multiply(halfTerm.addIdentity(N)).modular(M);
            } else {
                return halfSum.multiply(halfTerm.addIdentity(N)).add(halfTerm).modular(M);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Long.parseLong(st.nextToken());
        M = Long.parseLong(st.nextToken());
        mat2D = new Matrix2D(N, N);
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                mat2D.setElement(i, j, Integer.parseInt(st.nextToken()));
            }
        }

        Matrix2D result = powSum(mat2D, K);
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}