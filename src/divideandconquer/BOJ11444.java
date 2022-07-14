package divideandconquer;

import java.io.*;


/**
 * 피보나치 수 6 - BOJ11444
 *
 * 분할 정복을 이용한 거듭제곱 문제이다.
 * 기본적으로 N번째 피보나치 수를 구하기 위해서는 O(N)의 연산이 필요하다.
 * 문제의 경우 N의 범위가 1<= N <= 1,000,000,000,000,000,000이므로 O(n)의 연산 시 시간초과가 발생한다.
 * 행렬을 이용할 경우 O(logN)의 시간복잡도로 계산할 수 있다.
 *
 * 피보나치 수열의 일반항 f(n) = f(n - 1) + f(n - 2)이므로
 * 행렬로 나타내면
 * (   f(n)   )  =  (1 1)( f(n - 1) )
 * ( f(n - 1) )     (1 0)( f(n - 2) ) 으로 나타낼 수 있다
 *
 * 이 식은 앞의 (1 1)
 *            (1 0) 행렬의 거듭 제곱 꼴로 나타낼 수 있다.
 *
 *  (1 1)^(n-2) ( f(1) )
 *  (1 0)       ( f(2) )
 *
 *  이제 이 행렬 거듭제곱을 분할정복을 이용한 거듭제곱 기법을 이용해 O(logN) 만에 계산하면 된다.
 *
 * -----------------
 * Input 1
 * 1000
 *
 * Output 1
 * 517691607
 * -----------------
 */
public class BOJ11444 {

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
            if(mat.getRow() != row || mat.getColumn() != column) return false;
            for(int i = 0; i < size; i++) {
                elements[i] += mat.getElement(i);
            }
            return true;
        }

        public Matrix2D add(Matrix2D mat) {
            Matrix2D result = new Matrix2D(row, column);
            for(int i = 0; i < size; i++) {
                result.setElement(i, elements[i] + mat.getElement(i));
            }

            return result;
        }

        public boolean subToMat(Matrix2D mat) {
            if(mat.getRow() != row || mat.getColumn() != column) return false;
            for(int i = 0; i < size; i++) {
                elements[i] -= mat.getElement(i);
            }
            return true;
        }

        public Matrix2D subtract(Matrix2D mat) {
            Matrix2D result = new Matrix2D(row, column);
            for(int i = 0; i < size; i++) {
                result.setElement(i, elements[i] - mat.getElement(i));
            }

            return result;
        }

        public Matrix2D multiply(Matrix2D mat) {
            long element = 0;
            Matrix2D result = new Matrix2D(row, mat.getColumn());

            for(int i = 0; i < row; i++) {
                for(int j = 0; j < mat.column; j++) {
                    element = 0;
                    for(int k = 0; k < column; k++) {
                        element += getElement(i, k) * mat.getElement(k, j);
                    }
                    result.setElement(i, j, element);
                }
            }

            return result;
        }

        public Matrix2D modular(int n) {
            for(int i = 0; i < elements.length; i++) {
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
            if(idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            return elements[idx];
        }

        public long getElement(int r, int c) {
            int idx = r * column + c;
            if(idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            return elements[idx];
        }

        public void setElement(int idx, long e) {
            if(idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            elements[idx] = e;
        }

        public void setElement(int r, int c, long e) {
            int idx = r * column + c;
            if(idx >= size || idx < 0) throw new IndexOutOfBoundsException();
            elements[idx] = e;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < row; i++) {
                for(int j = 0; j < column; j++) {
                    result.append(getElement(i, j));
                    if(j < column - 1)
                        result.append(' ');
                }
                if(i < row - 1)
                    result.append('\n');
            }

            return result.toString();
        }
    }
    final static int P = (int)(1e9 + 7);
    static long N;

    public static Matrix2D multiply(Matrix2D mat, long exponent) {
        if(exponent == 1) {
            return mat.modular(P);
        } else {
            Matrix2D result;
            Matrix2D temp = multiply(mat, exponent >> 1);

            if(exponent % 2 == 0) {
                result = temp.multiply(temp).modular(P);    // (temp * temp % P)
            } else {
                result = temp.multiply(temp).modular(P).multiply(mat.modular(P)).modular(P);    // ((temp * temp % P) * (mat % P)) % P
            }

            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Long.parseLong(br.readLine());
        Matrix2D mat = new Matrix2D(2, 2);
        for(int i = 0; i < mat.getRow(); i++) {
            for(int j = 0; j < mat.getColumn(); j++) {
                if(i == mat.getRow() - 1 && j == mat.getColumn() -1)
                    mat.setElement(i, j, 0);
                else
                    mat.setElement(i, j, 1);
            }
        }

        if(N == 1 || N == 2) {
            bw.write("1");
        } else {
            Matrix2D result = multiply(mat, N - 2);
            Matrix2D temp = new Matrix2D(2, 1);
            temp.setElement(0, 0, 1);
            temp.setElement(1, 0, 1);
            result = result.multiply(temp).modular(P);
            bw.write(String.valueOf(result.getElement(0)));
        }

        br.close();
        bw.close();
    }
}