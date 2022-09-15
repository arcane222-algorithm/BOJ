package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 정수 수열 - BOJ14440
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(logN)
 * -----------------
 *
 * 일반적인 피보나치 수열의 일반항 f(n) = f(n - 1) + f(n - 2)이므로 이것을 행렬로 나타내면
 * (   f(n)   )  =  (1 1)( f(n - 1) )
 * ( f(n - 1) )     (1 0)( f(n - 2) ) 으로 나타낼 수 있다
 *
 * 이 식은 앞의 (1 1)
 *            (1 0) 행렬의 거듭 제곱 꼴로 나타낼 수 있다.
 *
 *  (   f(n)   )  =  (1 1)^(n-1) ( f(1) )
 *  ( f(n - 1) )     (1 0)       ( f(0) )
 *
 * 위 문제의 경우 f(n) = x * f(n - 1) + y * f(n - 2)꼴이므로
 * (   f(n)   )  =  (x y)( f(n - 1) )
 * ( f(n - 1) )     (1 0)( f(n - 2) ) 으로 나타낼 수 있다.
 *
 * 즉, 위 행렬 거듭제곱 식은
 *  (   f(n)   )  =  (x y)^(n-1) ( f(1) )
 *  ( f(n - 1) )     (1 0)       ( f(0) )
 * 위와 같이 나타낼 수 있고
 * 이제 이 행렬 거듭제곱을 분할정복을 이용한 거듭제곱 기법을 이용해 O(logN) 만에 계산하여 구할 수 있다.
 *
 * f(n)의 값의 끝 두자리를 구해야 하므로 거듭제곱 과정에서 100으로 modular 연산을 수행하도록 한다.
 *
 * -----------------
 * Input 1
 * 1 1 00 01 10
 *
 * Output 1
 * 55
 * -----------------
 * Input 2
 * 1 2 01 01 10
 *
 * Output 2
 * 83
 * -----------------
 */
public class BOJ14440 {
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

    public static Matrix2D multiply(Matrix2D mat, long exponent) {
        if (exponent == 1) {
            return mat.modular(MOD);
        } else {
            Matrix2D result;
            Matrix2D temp = multiply(mat, exponent >> 1);

            if ((exponent & 1) == 0) {
                result = temp.multiply(temp).modular(MOD);    // (temp * temp % P)
            } else {
                result = temp.multiply(temp).modular(MOD).multiply(mat.modular(MOD)).modular(MOD);    // ((temp * temp % P) * (mat % P)) % P
            }

            return result;
        }
    }

    static int MOD = 100;
    static int x, y, a0, a1, a2, n;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        x = Integer.parseInt(st.nextToken());
        y = Integer.parseInt(st.nextToken());
        a0 = Integer.parseInt(st.nextToken());
        a1 = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());

        long value;
        if (n == 0) value = a0;
        else if (n == 1) value = a1;
        else {
            Matrix2D mat2D = new Matrix2D(2, 2);
            mat2D.setElement(0, 0, x);
            mat2D.setElement(0, 1, y);
            mat2D.setElement(1, 0, 1);
            mat2D.setElement(1, 1, 0);

            Matrix2D temp = new Matrix2D(2, 1);
            temp.setElement(0, 0, a1);
            temp.setElement(1, 0, a0);

            Matrix2D result = multiply(mat2D, n - 1);
            result = result.multiply(temp).modular(MOD);
            value = result.getElement(0);
        }
        bw.write(value / 10 + "" + (value % 10));


        // close the buffer
        br.close();
        bw.close();
    }
}