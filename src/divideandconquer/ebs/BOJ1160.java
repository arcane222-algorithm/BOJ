package divideandconquer.ebs;

/**
 * Random Number Generator - BOJ1160
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 *
 *
 * -----------------
 * Input 1
 * 11 8 7 1 5 3
 *
 * Output 1
 * 2
 * -----------------
 * Input 2
 * 99999999999999999 99999999999999998 99999999999999997 99999999999999996 99999999999999995 100000000
 *
 * Output 2
 * 1
 * -----------------
 */
import java.io.*;
import java.util.*;

public class BOJ1160 {

    private static class Matrix2D {
        private int row, column, size;
        private long[] elements;

        public Matrix2D(int row, int column) {
            this.row = row;
            this.column = column;
            this.size = row * column;
            elements = new long[size];
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

        public boolean multiplyConstant(long constant) {
            for (int i = 0; i < size; i++) {
                elements[i] *= constant;
            }
            return true;
        }

        public Matrix2D multiply(Matrix2D mat, long m) {
            long element = 0;
            Matrix2D result = new Matrix2D(row, mat.getColumn());

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < mat.column; j++) {
                    element = 0;
                    for (int k = 0; k < column; k++) {
                        long value = modularMultiply(getElement(i, k), mat.getElement(k, j), m);
                        element = (element + value) % m;
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

        public static Matrix2D getIdentity(int row, int column) {
            Matrix2D identity = new Matrix2D(row, column);
            for (int i = 0; i < row; i++) {
                identity.setElement(i, i, 1);
            }

            return identity;
        }

        private static long modularMultiply(long a, long b, long m) {
            long result = 0;

            while (b > 0) {
                if ((b & 1) == 1) result = (result + a) % m;
                a = (a * 2) % m;
                b >>= 1;
            }

            return result;
        }

        public static Matrix2D fastPow(Matrix2D mat, long n, long m) {
            Matrix2D result = Matrix2D.getIdentity(2, 2);

            while (n > 0) {
                if ((n & 1) == 1) result = result.multiply(mat, m);
                mat = mat.multiply(mat, m);
                n >>= 1;
            }
            return result;
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

    static long m, a, c, X0, n, g;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        m = Long.parseLong(st.nextToken());
        a = Long.parseLong(st.nextToken());
        c = Long.parseLong(st.nextToken());
        X0 = Long.parseLong(st.nextToken());
        n = Long.parseLong(st.nextToken());
        g = Long.parseLong(st.nextToken());

        Matrix2D mat2DX0 = new Matrix2D(2, 2);
        mat2DX0.setElement(0, 0, X0);
        mat2DX0.setElement(0, 1, 1);

        Matrix2D mat2D = new Matrix2D(2, 2);
        mat2D.setElement(0, 0, a);
        mat2D.setElement(1, 0, c);
        mat2D.setElement(1, 1, 1);

        Matrix2D pow = Matrix2D.fastPow(mat2D, n, m);
        Matrix2D result = mat2DX0.modular(m).multiply(pow, m).modular(m);

        bw.write(String.valueOf(result.getElement(0) % g));

        // close the buffer
        br.close();
        bw.close();
    }
}
