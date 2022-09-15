package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * Matrix Powers - BOJ5095
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * -----------------
 * Input 1
 * 2 17 2
 * 1 2
 * 3 4
 * 0 0 0
 *
 * Output 1
 * 7 10
 * 15 5
 * -----------------
 * Input 2
 * 2 17 2
 * 1 2
 * 3 4
 * 2 17 2
 * 1 2
 * 3 4
 * 0 0 0
 *
 * Output 2
 * 7 10
 * 15 5
 *
 * 7 10
 * 15 5
 * -----------------
 */
public class BOJ5095 {

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

    public static Matrix2D fastPow(Matrix2D mat, int exponent, int mod) {
        if (exponent == 1) return mat.modular(mod);
        else {
            Matrix2D result, temp;
            temp = fastPow(mat, exponent >> 1, mod);

            if ((exponent & 1) == 0) {
                result = temp.multiply(temp).modular(mod);
            } else {
                result = temp.multiply(temp).modular(mod).multiply(mat.modular(mod)).modular(mod);
            }
            return result;
        }
    }

    static int N, M, P;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        while (true) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            P = Integer.parseInt(st.nextToken());
            if (N == 0 && M == 0 && P == 0) break;

            Matrix2D mat2D = new Matrix2D(N, N);
            for (int j = 0; j < N; j++) {
                st = new StringTokenizer(br.readLine());
                for (int k = 0; k < N; k++) {
                    int value = Integer.parseInt(st.nextToken());
                    mat2D.setElement(j, k, value);
                }
            }

            result.append(fastPow(mat2D, P, M)).append("\n\n");
        }
        result.delete(result.length() - 2, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}