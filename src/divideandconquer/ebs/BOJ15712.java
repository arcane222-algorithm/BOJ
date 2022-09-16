package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 등비수열 - BOJ15712
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * 첫항이 a, 공비가 r, 항의 수가 n인 등비수열의 합은
 * Sn = a + ar + ar^2 + ... + ar^n으로 나타낼 수 있다.
 *
 * 이 값은 행렬
 * (r 0)
 * (a 1) 의 거듭제곱으로 나타낼 수 있다.
 *
 * n = 2라면,
 * (r^2      0) = (r 0)^2
 * (a(r + 1) 1)   (a 1)
 *
 * n = k라면,
 * (                r^k                0) = (r 0)^k
 * (a(r^k-1 + r^k-2 + .. + r^1 + 1)    1)   (a 1)
 *
 * 즉 행렬
 * (r 0)
 * (a 1) 을 거듭제곱한 다음 (r, c) = (1, 0) 의 값을 구하면 된다.
 *
 * -----------------
 * Input 1
 * 3 5 2 10
 *
 * Output 1
 * 8
 * -----------------
 * Input 2
 * 1 2 9 100
 *
 * Output 2
 * 11
 * -----------------
 */
public class BOJ15712 {

    private static class Matrix2D {
        private int row, column, size;
        private long[] elements;

        public Matrix2D(int row, int column) {
            this.row = row;
            this.column = column;
            this.size = row * column;
            elements = new long[size];
        }

        public static Matrix2D getIdentity(int row, int column) {
            Matrix2D identity = new Matrix2D(row, column);
            for (int i = 0; i < row; i++) {
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

    static int a, r, n, mod;

    public static Matrix2D fastPow(Matrix2D mat, int n) {
        Matrix2D result = Matrix2D.getIdentity(2, 2);

        while (n > 0) {
            if ((n & 1) == 1) result = result.multiply(mat).modular(mod);
            mat = mat.multiply(mat).modular(mod);
            n >>= 1;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        a = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        mod = Integer.parseInt(st.nextToken());

        Matrix2D mat2D = new Matrix2D(2, 2);
        mat2D.setElement(0, 0, r);
        mat2D.setElement(1, 0, a);
        mat2D.setElement(1, 1, 1);

        Matrix2D result = fastPow(mat2D, n);
        bw.write(String.valueOf(result.getElement(1, 0)));

        // close the buffer
        br.close();
        bw.close();
    }
}