package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 행렬 제곱의 합 - BOJ13246
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(N^3logB)
 * -----------------
 *
 * 크기가 N*N인 정사각행렬 A에 대하여 A의 1제곱부터 A의 B제곱까지 더한 행렬을 구해야 한다.
 * B가 최대 100,000,000,000 = 10^11 까지 들어오므로 선형으로 계산하면 TLE가 발생한다.
 *
 * (행렬 거듭제곱의 합 행렬을 S(B)이라고 하자.)
 * 행렬의 거듭제곱의 합을 유심히 살펴보면
 * (i) B & 1 == 0 (짝수) 일때,
 *    예시로 N = 4라면 S(4) = A + A^2 + A^3 + A^4을 구해야 한다.
 *    이때, log scale로 계산으로 처리하기 위해 반씩 쪼개면
 *    S(4) = (A + A^2) + (A^3 + A^4) = (A + A^2) + (A + A^2)A^2 = (A + A^2)(I + A^2) = S(2)(I + A^2)
 *    이런식으로 S(B)의 B를 절반씩 쪼개며 분할정복으로 계산한다.
 *
 * (ii) B ^ 1 == 1 (홀수) 일때,
 *     예시로 N = 5라면 S(5) = A + A^2 + A^3 + A^4 + A^5을 구해야 한다.
 *     이때, log scale로 계산으로 처리하기 위해 반씩 쪼개면
 *     S(4) = (A + A^2) + (A^4 + A^5) + A^3 = (A + A^2) + (A + A^2)A^3 + A^3 = (A + A^2)(I + A^3) + A^3 = S(2)(I + A^3) + A^3
 *     이런식으로 S(B)의 B를 마찬가지고 절반식 쪼개며 분할정복으로 계산한다.
 *
 * 이 식을 일반화 하면
 * (i) B = 2k (K >= 1), S(B) = S(k)(I + A^k)
 * (ii) B = 2k + 1 (K >= 1) S(B) = S(k)(I + A^(k+1)) + A^(k+1) 로 표현할 수 있다.
 *
 * S(B) 자체도 분할정복 과정을 거치며 계산을 수행하고 B가 홀수인 상황에서 발생하는 A^(k+1) 경우
 * 단일 행렬의 분할정복을 이용한 거듭제곱을 이용해 N^3log(k+1)에 계산하도록 한다.
 *
 * -----------------
 * Input 1
 * 2 5
 * 1 2
 * 3 4
 *
 * Output 1
 * 313 914
 * 871 184
 * -----------------
 * Input 2
 * 3 3
 * 1 2 3
 * 4 5 6
 * 7 8 9
 *
 * Output 2
 * 499 614 729
 * 132 391 650
 * 765 168 571
 * -----------------
 * Input 3
 * 5 10
 * 1 0 0 0 1
 * 1 0 0 0 1
 * 1 0 0 0 1
 * 1 0 0 0 1
 * 1 0 0 0 1
 *
 * Output 3
 * 23 0 0 0 23
 * 23 0 0 0 23
 * 23 0 0 0 23
 * 23 0 0 0 23
 * 23 0 0 0 23
 * -----------------
 */
public class BOJ13246 {

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
                elements[i] = elements[i] % n;
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
        while (n > 0) {
            if ((n & 1) == 1) result = result.multiply(mat2D).modular(M);
            mat2D = mat2D.multiply(mat2D).modular(M);
            n >>= 1;
        }

        return result;
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