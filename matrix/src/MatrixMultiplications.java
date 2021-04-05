import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * The {@code MatrixMultiplication} class implements
 * fast multiplication 2 matrices at each other.
 * The {@code MatrixMultiplication} uses Strassen algorithm and
 * parallelize it with the {@link java.util.concurrent.ForkJoinPool}
 *
 * @author Evgeny Usov
 * @author Alexey Falko
 */
public class MatrixMultiplications {

    //******************************************************************************************

    public static double[][] multiply(double[][] a, double[][] b) {

        int rowsA = a.length;
        int columnsB = b[0].length;
        int columnsA_rowsB = a[0].length;

        double[][] c = new double[rowsA][columnsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsB; j++) {
                int sum = 0;
                for (int k = 0; k < columnsA_rowsB; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }

        return c;
    }

    //******************************************************************************************

    /*
    public static double[][] multiplyTransposed(double[][] a, double[][] b) {

        int rowsA = a.length;
        int columnsB = b[0].length;
        int columnsA_rowsB = a[0].length;

        double columnB[] = new double[columnsA_rowsB];
        double[][] c = new double[rowsA][columnsB];


        for (int j = 0; j < columnsB; j++) {
            for (int k = 0; k < columnsA_rowsB; k++) {
                columnB[k] = b[k][j];
            }

            for (int i = 0; i < rowsA; i++) {
                double rowA[] = a[i];
                int sum = 0;
                for (int k = 0; k < columnsA_rowsB; k++) {
                    sum += rowA[k] * columnB[k];
                }
                c[i][j] = sum;
            }
        }

        return c;
    }

     */

    //******************************************************************************************

    private static double[][] summation(double[][] a, double[][] b) {

        int n = a.length;
        int m = a[0].length;
        double[][] c = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    //******************************************************************************************

    private static double[][] subtraction(double[][] a, double[][] b) {

        int n = a.length;
        int m = a[0].length;
        double[][] c = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }

    //******************************************************************************************

    private static double[][] addition2SquareMatrix(double[][] a, int n) {

        double[][] result = new double[n][n];

        for (int i = 0; i < a.length; i++) {
            System.arraycopy(a[i], 0, result[i], 0, a[i].length);
        }
        return result;
    }

    //******************************************************************************************

    private static double[][] getSubmatrix(double[][] a, int n, int m) {

        double[][] result = new double[n][m];

        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, result[i], 0, m);
        }
        return result;
    }

    //******************************************************************************************

    private static void splitMatrix(double[][] a, double[][] a11, double[][] a12, double[][] a21, double[][] a22) {

        int n = a.length >> 1;

        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, a11[i], 0, n);
            System.arraycopy(a[i], n, a12[i], 0, n);
            System.arraycopy(a[i + n], 0, a21[i], 0, n);
            System.arraycopy(a[i + n], n, a22[i], 0, n);
        }
    }

    //******************************************************************************************

    private static double[][] collectMatrix(double[][] a11, double[][] a12, double[][] a21, double[][] a22) {

        int n = a11.length;
        double[][] a = new double[n << 1][n << 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(a11[i], 0, a[i], 0, n);
            System.arraycopy(a12[i], 0, a[i], n, n);
            System.arraycopy(a21[i], 0, a[i + n], 0, n);
            System.arraycopy(a22[i], 0, a[i + n], n, n);
        }

        return a;
    }

    public double[][] multiStrassen(double[][] a, double[][] b, int n) {
        if (n <= 128) {
            return multiply(a, b);
        }

        n = n >> 1;
        ArrayList<Object> objects = new ArrayList<>();

        double[][] a11 = new double[n][n];
        double[][] a12 = new double[n][n];
        double[][] a21 = new double[n][n];
        double[][] a22 = new double[n][n];

        double[][] b11 = new double[n][n];
        double[][] b12 = new double[n][n];
        double[][] b21 = new double[n][n];
        double[][] b22 = new double[n][n];

        splitMatrix(a, a11, a12, a21, a22);
        splitMatrix(b, b11, b12, b21, b22);

        double[][] p1 = multiStrassen(summation(a11, a22), summation(b11, b22), n);
        double[][] p2 = multiStrassen(summation(a21, a22), b11, n);
        double[][] p3 = multiStrassen(a11, subtraction(b12, b22), n);
        double[][] p4 = multiStrassen(a22, subtraction(b21, b11), n);
        double[][] p5 = multiStrassen(summation(a11, a12), b22, n);
        double[][] p6 = multiStrassen(subtraction(a21, a11), summation(b11, b12), n);
        double[][] p7 = multiStrassen(subtraction(a12, a22), summation(b21, b22), n);

        double[][] c11 = summation(summation(p1, p4), subtraction(p7, p5));
        double[][] c12 = summation(p3, p5);
        double[][] c21 = summation(p2, p4);
        double[][] c22 = summation(subtraction(p1, p2), summation(p3, p6));

        return collectMatrix(c11, c12, c21, c22);
    }

    //******************************************************************************************

    private static int log2(int x) {
        int result = 1;
        while ((x >>= 1) != 0) {
            result++;
        }

        return result;
    }

    //******************************************************************************************

    private static int getNewDimension(double[][] a, double[][] b) {
        return 1 << log2(Collections.max(Arrays.asList(a.length, a[0].length, b[0].length)));
    }

    class myRecursiveTask extends RecursiveTask<double[][]> {
        private static final long serialVersionUID = -433764214304695286L;

        int n;
        double[][] a;
        double[][] b;

        public myRecursiveTask(double[][] a, double[][] b, int n) {
            this.a = a;
            this.b = b;
            this.n = n;
        }

        /**
         * @return the integer matrix by
         * multiplying 2 matrices at each other
         */
        @Override
        protected double[][] compute() {
            if (n <= 128) {
                return multiply(a, b);
            }

            n >>= 1;

            double[][] a11 = new double[n][n];
            double[][] a12 = new double[n][n];
            double[][] a21 = new double[n][n];
            double[][] a22 = new double[n][n];

            double[][] b11 = new double[n][n];
            double[][] b12 = new double[n][n];
            double[][] b21 = new double[n][n];
            double[][] b22 = new double[n][n];

            splitMatrix(a, a11, a12, a21, a22);
            splitMatrix(b, b11, b12, b21, b22);

            myRecursiveTask task_p1 = new myRecursiveTask(summation(a11, a22), summation(b11, b22), n);
            myRecursiveTask task_p2 = new myRecursiveTask(summation(a21, a22), b11, n);
            myRecursiveTask task_p3 = new myRecursiveTask(a11, subtraction(b12, b22), n);
            myRecursiveTask task_p4 = new myRecursiveTask(a22, subtraction(b21, b11), n);
            myRecursiveTask task_p5 = new myRecursiveTask(summation(a11, a12), b22, n);
            myRecursiveTask task_p6 = new myRecursiveTask(subtraction(a21, a11), summation(b11, b12), n);
            myRecursiveTask task_p7 = new myRecursiveTask(subtraction(a12, a22), summation(b21, b22), n);

            task_p1.fork();
            task_p2.fork();
            task_p3.fork();
            task_p4.fork();
            task_p5.fork();
            task_p6.fork();
            task_p7.fork();

            double[][] p1 = task_p1.join();
            double[][] p2 = task_p2.join();
            double[][] p3 = task_p3.join();
            double[][] p4 = task_p4.join();
            double[][] p5 = task_p5.join();
            double[][] p6 = task_p6.join();
            double[][] p7 = task_p7.join();

            double[][] c11 = summation(summation(p1, p4), subtraction(p7, p5));
            double[][] c12 = summation(p3, p5);
            double[][] c21 = summation(p2, p4);
            double[][] c22 = summation(subtraction(p1, p2), summation(p3, p6));

            return collectMatrix(c11, c12, c21, c22);
        }

    }

    //******************************************************************************************

    public double[][] multiStrassenForkJoin(double[][] a, double[][] b) {

        int nn = getNewDimension(a, b);
        double[][] a_n = addition2SquareMatrix(a, nn);
        double[][] b_n = addition2SquareMatrix(b, nn);

        myRecursiveTask task = new myRecursiveTask(a_n, b_n, nn);
        ForkJoinPool pool = new ForkJoinPool();
        double[][] fastFJ = pool.invoke(task);

        return getSubmatrix(fastFJ, a.length, b[0].length);
    }

}
