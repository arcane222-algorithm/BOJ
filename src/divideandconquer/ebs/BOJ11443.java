package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 짝수번째 피보나치 수의 합 - BOJ11443
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * 일반적인 피보나치 수열의 일반항 f(n) = f(n - 1) + f(n - 2) 이고 이것을 행렬로 나타내면
 * (   f(n)   )  =  (1 1)( f(n - 1) )
 * ( f(n - 1) )     (1 0)( f(n - 2) ) 이다.
 *
 * 이 식은 앞의 (1 1)
 *            (1 0) 행렬의 거듭 제곱 꼴로 나타낼 수 있다.
 * 문제에서 f(0) = f(1) = 1이므로
 *
 *  (   f(n)   )  =  (1 1)^(n-2) ( f(1) ) = (1 1)^(n-1) ( 1 )
 *  ( f(n - 1) )     (1 0)       ( f(0) ) = (1 0)       ( 1 ) (n >= 2) 으로 나타낼 수 있다.
 *
 * 이제 짝수번째 피보나치 수열 항의 합에 대해 생각해보자
 * 피보나치 수열의 경우 0 1 1 2 3 5 8 13 21 34 55 89 144 ... 이고 n >= 1에 대하여 짝수번째만 나열하면
 * 1 3 8 21 55 144 ... 이다.
 * 이들의 합을 나열하면 1 4 12 33 88 232 ...이다.
 * 즉, 짝수번째 피보나치 수열의 합의 경우 피보나치 수열에 속하는 수 - 1의 값의 형태를 보여주고 있다.
 *
 * 이것을 일반화 하면
 * (i) n & 1 == 0 이라면 (짝수)
 *  1 ~ n 사이 짝수 번째 피보나치 수열의 합은 F(n + 1) - 1이 된다.
 * (ii) n & 1 == 1 이라면 (홀수)
 * 1 ~ n 사이 짝수 번째 피보나치 수열의 합은 F(n) - 1이 된다.
 *
 * 이제 F(n)의 값을 행렬과 분할정복을 이용한 거듭제곱을 이용하여 값을 구하면 된다.
 *
 * -----------------
 * Input 1
 * 7
 *
 * Output 1
 * 12
 * -----------------
 * Input 2
 * 10
 *
 * Output 2
 * 88
 * -----------------
 */
public class BOJ11443 {

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

        public Matrix2D modular(int n) {
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

    static final int MOD = (int) 1e9 + 7;

    static long n;

    public static Matrix2D fastPow(Matrix2D mat, long n) {
        Matrix2D result = Matrix2D.getIdentity(2);

        while (n > 0) {
            if ((n & 1) == 1) result = result.multiply(mat).modular(MOD);
            mat = mat.multiply(mat).modular(MOD);
            n >>= 1;
        }

        return result;
    }

    public static long fibonacci(long n) {
        if (n == 1 || n == 2) return 1;
        else {
            Matrix2D base = new Matrix2D(2, 2);
            base.setElement(0, 0, 1);
            base.setElement(1, 0, 1);
            base.setElement(0, 1, 1);
            base.setElement(1, 1, 0);

            Matrix2D temp = new Matrix2D(2, 1);
            temp.setElement(0, 0, 1);
            temp.setElement(1, 0, 0);

            Matrix2D pow = fastPow(base, n - 1);
            Matrix2D result = pow.multiply(temp).modular(MOD);
            return result.getElement(0);
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Long.parseLong(br.readLine());
        if ((n & 1) == 0) n += 1;
        long result = fibonacci(n) - 1;

        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}