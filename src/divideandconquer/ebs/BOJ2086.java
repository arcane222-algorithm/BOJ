package divideandconquer.ebs;

import java.io.*;
import java.util.*;


/**
 * 피보나치 수의 합 - BOJ2086
 * -----------------
 * category: mathematics (수학), exponentiation by squaring (분할정복을 이용한 거듭제곱)
 * Time-Complexity: O( 4(log(b + 2) + log(a + 1)) ) => O( log(a) + log(b) )
 * -----------------
 *
 * 일반적인 피보나치 수열의 일반항 f(n) = f(n - 1) + f(n - 2) 이고 이것을 행렬로 나타내면
 * (   f(n)   )  =  (1 1)( f(n - 1) )
 * ( f(n - 1) )     (1 0)( f(n - 2) ) 이다.
 *
 * 이 식은 앞의 (1 1)
 *            (1 0) 행렬의 거듭 제곱 꼴로 나타낼 수 있다.
 * 문제에서 f(1) = f(2) = 1이므로
 *
 *  (   f(n)   )  =  (1 1)^(n-2) ( f(2) ) = (1 1)^(n-2) ( 1 )
 *  ( f(n - 1) )     (1 0)       ( f(1) ) = (1 0)       ( 1 ) (n >= 3) 으로 나타낼 수 있다.
 *
 *  이제 a항부터 b까지 피보나치 항의 합에 대해 생각해보자. 1항부터 k항 까지의 피보나치 수열의 합을 sigma(k)라고 하자.
 *  a항부터 b항까지의 합은 sigma(b) - sigma(a - 1) 으로 나타낼 수 있다. (b >= a)
 *  worst case의 경우 a = 1, b = 9 x 10 ^18이고, 이러한 값이 input으로 들어온다면 선형시간 안에 해결이 불가능하다.
 *
 *  sigma(k)값을 나열하여 규칙을 찾아볼 수 있다.
 *  sigma(k)를 k = 1 ~ 7까지 내열하면 1 2 4 7 12 20 33 ... 이다.
 *  피보나치 수열의 값은 1 1 2 3 5 8 13 21 ... 이다.
 *  이 둘을 잘 비교해보면 1씩 차이나는 모양새를 취하고 있음을 알 수 있다.
 *  즉, sigma(k) = F{k+2} - 1의 규칙을 발견할 수 있다.
 *  ex) 1항부터 6항까지의 합은 20이고 이것은 F{8} - 1 = 21 - 1 = 20임을 알 수 있다.
 *
 *  이러한 규칙을 이용하여
 *  sigma(b) - sigma(a) = F{b+2} - 1 - (F{a+2-1} -1) = F{b+2} - F{a+1}이라는 사실을 알 수 있게 된다.
 *  위에서 설명한 행렬을 이용한 표현을 분할정복을 이용한 거듭제곱 방식으로 계산하여 F{b+2}, F{a+1} 모두 O(logN) 시간 안에 해결할 수 있고
 *  두 값을 뺀 값을 구해주면 된다.
 *
 *  주의할 점은 계산을 진행하며 modular 연산을 해주어야 하는데 뺄셈의 modular의 경우 음수가 나오는 경우가 발생할 수 있으므로 이 것을 고려해야 한다.
 *  F{b+2} - F{a+1}의 계산에 대하여
 *  (F{b+2} % MOD - F{a+1} % MOD) % MOD
 *  방식으로 모듈러 분배법칙을 적용하면 되지만 음수를 방지하기 위해
 *
 *  (F{b+2} % MOD - F{a+1} % MOD + MOD) % MOD
 *  위와 같이 최종적으로 MOD 하기 전에 MOD 값을 더해 음수를 방지한다.
 *
 * -----------------
 * Input 1
 * 4 10
 *
 * Output 1
 * 139
 * -----------------
 * Input 2
 * 2 2
 *
 * Output 2
 * 1
 * -----------------
 */
public class BOJ2086 {

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

    static final int MOD = 1000000000;

    static long a, b;

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
            temp.setElement(1, 0, 1);

            Matrix2D pow = fastPow(base, n - 2);
            Matrix2D result = pow.multiply(temp).modular(MOD);
            return result.getElement(0);
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        a = Long.parseLong(st.nextToken());
        b = Long.parseLong(st.nextToken());

        a = fibonacci(a + 1);
        b = fibonacci(b + 2);
        long result = (b % MOD - a % MOD + MOD) % MOD;
        bw.write(String.valueOf(result));

        // close the buffer
        br.close();
        bw.close();
    }
}