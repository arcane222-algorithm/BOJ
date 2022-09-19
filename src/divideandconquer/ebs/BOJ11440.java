package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 피보나치 수의 제곱의 합 - BOJ11440
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O(4logN) => O(logN)
 * -----------------
 *
 * N이 최대 10^18까지 들어오므로 선형으로 합을 구하게 되면 시간안에 해결이 불가능하다.
 * 피보나치 수는 F(n) = F(n - 1) + F(n - 2)이고,
 * n = k + 1이라고 할 때,
 * F(n) = F(k + 1) = F(k) + F(k - 1)이고, F(k - 1)을 이항하면
 * F(k) = F(k + 1) - F(k - 1) 이다.
 * 양변에 F(k)를 곱하면
 * (F(k))^2 = F(k + 1)F(k) - F(k)F(k - 1) 이 된다.
 *
 * 이제 피보나치 수의 제곱의 합을 나타내면
 * sigma (k = 1, n) (F(k))^2 = {F(n + 1)F(n) - F(n)F(n - 1)} + {F(n)F(n - 1) - F(n - 1)F(n - 2)} + ... + {F(3)F(2) - F(2)F(1)} + {F(2)F(1) - F(1)F(0)}
 *                           = F(n + 1)F(n) - F(1)F(0)
 *                           = F(n + 1)F(n) 이다. (F(0)은 0이므로)
 *
 * 이제 0항부터 n항까지 피보나치 수의 합의 경우 F(n + 1)F(n)의 값을 구해주면 되고
 * ( F(n)   )  = (1 1)^n-1 ( F(n-1) )
 * ( F(n-1) )    (1 0)     ( F(n-2) )
 * 를 통해 F(n)을 구할 수 있으므로 F(n), F(n + 1)를 구해 위 값을 계산한다.
 *
 * + 분할정복을 이용한 거듭제곱 구현 시 재귀 함수를 이용하여 구현할 경우 n이 커지면 StackOverflow가 발생할 수 있으므로 반복문을 이용하여 구현한다.
 *
 * -----------------
 * Input 1
 * 10
 *
 * Output 1
 * 4895
 * -----------------
 */
public class BOJ11440 {

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

    static final int MOD = (int) 1e9 + 7;
    static long n;

    public static Matrix2D fastPow(Matrix2D mat, long n) {
        Matrix2D result = Matrix2D.getIdentity(2, 2);

        while (n > 0) {
            if ((n & 1) == 1) result = result.multiply(mat).modular(MOD);
            mat = mat.multiply(mat).modular(MOD);
            n >>= 1;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        n = Long.parseLong(br.readLine());
        Matrix2D mat2D = new Matrix2D(2, 2);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == 1 && j == 1) mat2D.setElement(i, j, 0);
                else mat2D.setElement(i, j, 1);
            }
        }

        Matrix2D temp = new Matrix2D(2, 1);
        temp.setElement(0, 1);

        Matrix2D mat2DPowN_1 = fastPow(mat2D, n - 1);
        Matrix2D mat2DPowN = mat2DPowN_1.multiply(mat2D).modular(MOD);

        mat2DPowN_1 = mat2DPowN_1.multiply(temp);
        mat2DPowN = mat2DPowN.multiply(temp);

        long result = mat2DPowN_1.multiply(mat2DPowN).modular(MOD).getElement(0);
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}