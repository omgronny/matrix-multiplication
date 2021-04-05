import java.util.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

/**
 * The `MatrixMultiplication` class implements
 * fast multiplication 2 matrices at each other.
 * The `MatrixMultiplication` uses Strassen algorithm and
 * parallelize it with the [java.util.concurrent.ForkJoinPool]
 *
 * @author Evgeny Usov
 * @author Alexey Falko
 */
class MatrixMultiplication {
    fun multiStrassen(a: Array<DoubleArray?>, b: Array<DoubleArray?>, n: Int): Array<DoubleArray?> {
        var n = n
        if (n <= 128) {
            return multiplyTransposed(a, b)
        }
        n = n shr 1
        val objects = ArrayList<Any>()
        val a11 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val a12 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val a21 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val a22 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val b11 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val b12 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val b21 = Array<DoubleArray?>(n) { DoubleArray(n) }
        val b22 = Array<DoubleArray?>(n) { DoubleArray(n) }
        splitMatrix(a, a11, a12, a21, a22)
        splitMatrix(b, b11, b12, b21, b22)
        val p1 = multiStrassen(summation(a11, a22), summation(b11, b22), n)
        val p2 = multiStrassen(summation(a21, a22), b11, n)
        val p3 = multiStrassen(a11, subtraction(b12, b22), n)
        val p4 = multiStrassen(a22, subtraction(b21, b11), n)
        val p5 = multiStrassen(summation(a11, a12), b22, n)
        val p6 = multiStrassen(subtraction(a21, a11), summation(b11, b12), n)
        val p7 = multiStrassen(subtraction(a12, a22), summation(b21, b22), n)
        val c11 = summation(summation(p1, p4), subtraction(p7, p5))
        val c12 = summation(p3, p5)
        val c21 = summation(p2, p4)
        val c22 = summation(subtraction(p1, p2), summation(p3, p6))
        return collectMatrix(c11, c12, c21, c22)
    }

    internal inner class myRecursiveTask(var a: Array<DoubleArray?>, var b: Array<DoubleArray?>, var n: Int)
        : RecursiveTask<Array<DoubleArray?>>() {

        /**
         * @return the integer matrix by
         * multiplying 2 matrices at each other
         */
        override fun compute(): Array<DoubleArray?> {
            if (n <= 128) {
                return multiplyTransposed(a, b)
            }
            n = n shr 1
            val a11 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val a12 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val a21 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val a22 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val b11 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val b12 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val b21 = Array<DoubleArray?>(n) { DoubleArray(n) }
            val b22 = Array<DoubleArray?>(n) { DoubleArray(n) }
            splitMatrix(a, a11, a12, a21, a22)
            splitMatrix(b, b11, b12, b21, b22)
            val task_p1 = myRecursiveTask(summation(a11, a22), summation(b11, b22), n)
            val task_p2 = myRecursiveTask(summation(a21, a22), b11, n)
            val task_p3 = myRecursiveTask(a11, subtraction(b12, b22), n)
            val task_p4 = myRecursiveTask(a22, subtraction(b21, b11), n)
            val task_p5 = myRecursiveTask(summation(a11, a12), b22, n)
            val task_p6 = myRecursiveTask(subtraction(a21, a11), summation(b11, b12), n)
            val task_p7 = myRecursiveTask(subtraction(a12, a22), summation(b21, b22), n)
            task_p1.fork()
            task_p2.fork()
            task_p3.fork()
            task_p4.fork()
            task_p5.fork()
            task_p6.fork()
            task_p7.fork()
            val p1 = task_p1.join()
            val p2 = task_p2.join()
            val p3 = task_p3.join()
            val p4 = task_p4.join()
            val p5 = task_p5.join()
            val p6 = task_p6.join()
            val p7 = task_p7.join()
            val c11 = summation(summation(p1, p4), subtraction(p7, p5))
            val c12 = summation(p3, p5)
            val c21 = summation(p2, p4)
            val c22 = summation(subtraction(p1, p2), summation(p3, p6))
            return collectMatrix(c11, c12, c21, c22)
        }


    }

    //******************************************************************************************
//    fun multiStrassenForkJoin(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
//        val nn = getNewDimension(a, b)
//        val a_n = addition2SquareMatrix(a, nn)
//        val b_n = addition2SquareMatrix(b, nn)
//        val task = myRecursiveTask(a_n, b_n, nn)
//        val pool = ForkJoinPool()
//        val fastFJ = pool.invoke<Array<DoubleArray>>(task)
//        return getSubmatrix(fastFJ, a.size, b[0].size)
//    }

    companion object {
        //******************************************************************************************
        fun multiply(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
            val rowsA = a.size
            val columnsB: Int = b[0].size
            val columnsA_rowsB: Int = a[0].size
            val c = Array(rowsA) { DoubleArray(columnsB) }
            for (i in 0 until rowsA) {
                for (j in 0 until columnsB) {
                    var sum = 0.0
                    for (k in 0 until columnsA_rowsB) {
                        sum += a[i][k] * b[k][j]
                    }
                    c[i][j] = sum
                }
            }
            return c
        }

        //******************************************************************************************
        fun multiplyTransposed(a: Array<DoubleArray?>, b: Array<DoubleArray?>): Array<DoubleArray?> {
            val rowsA = a.size
            val columnsB: Int = b[0]!!.size
            val columnsA_rowsB: Int = a[0]!!.size
            val columnB = DoubleArray(columnsA_rowsB)
            val c = Array<DoubleArray?>(rowsA) { DoubleArray(columnsB) }
            for (j in 0 until columnsB) {
                for (k in 0 until columnsA_rowsB) {
                    columnB[k] = b[k]!![j]
                }
                for (i in 0 until rowsA) {
                    val rowA = a[i]
                    var sum = 0.0
                    for (k in 0 until columnsA_rowsB) {
                        sum += rowA!![k] * columnB[k]
                    }
                    c[i]!![j] = sum
                }
            }
            return c
        }

        //******************************************************************************************
        private fun summation(a: Array<DoubleArray?>?, b: Array<DoubleArray?>?): Array<DoubleArray?> {
            val n = a!!.size
            val m: Int = a[0]!!.size
            val c = Array<DoubleArray?>(n) { DoubleArray(m) }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    c[i]!![j] = a[i]!![j] + b!![i]!![j]
                }
            }
            return c
        }

        //******************************************************************************************
        private fun subtraction(a: Array<DoubleArray?>?, b: Array<DoubleArray?>?): Array<DoubleArray?> {
            val n = a!!.size
            val m: Int = a[0]!!.size
            val c = Array<DoubleArray?>(n) { DoubleArray(m) }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    c[i]!![j] = a[i]!![j] - b!![i]!![j]
                }
            }
            return c
        }

        //******************************************************************************************
        private fun addition2SquareMatrix(a: Array<DoubleArray>, n: Int): Array<DoubleArray> {
            val result = Array(n) { DoubleArray(n) }
            for (i in a.indices) {
                System.arraycopy(a[i], 0, result[i], 0, a[i].size)
            }
            return result
        }

        //******************************************************************************************
        private fun getSubmatrix(a: Array<DoubleArray>, n: Int, m: Int): Array<DoubleArray> {
            val result = Array(n) { DoubleArray(m) }
            for (i in 0 until n) {
                System.arraycopy(a[i], 0, result[i], 0, m)
            }
            return result
        }

        //******************************************************************************************
        private fun splitMatrix(a: Array<DoubleArray?>, a11: Array<DoubleArray?>, a12: Array<DoubleArray?>, a21: Array<DoubleArray?>, a22: Array<DoubleArray?>) {
            val n = a.size shr 1
            for (i in 0 until n) {
                System.arraycopy(a[i], 0, a11[i], 0, n)
                System.arraycopy(a[i], n, a12[i], 0, n)
                System.arraycopy(a[i + n], 0, a21[i], 0, n)
                System.arraycopy(a[i + n], n, a22[i], 0, n)
            }
        }

        //******************************************************************************************
        private fun collectMatrix(a11: Array<DoubleArray?>, a12: Array<DoubleArray?>, a21: Array<DoubleArray?>, a22: Array<DoubleArray?>): Array<DoubleArray?> {
            val n = a11.size
            val a = Array<DoubleArray?>(n shl 1) { DoubleArray(n shl 1) }
            for (i in 0 until n) {
                System.arraycopy(a11[i], 0, a[i], 0, n)
                System.arraycopy(a12[i], 0, a[i], n, n)
                System.arraycopy(a21[i], 0, a[i + n], 0, n)
                System.arraycopy(a22[i], 0, a[i + n], n, n)
            }
            return a
        }

        //******************************************************************************************
        private fun log2(x: Int): Int {
            var x = x
            var result = 1
            while (1.let { x = x shr it; x } != 0) {
                result++
            }
            return result
        }

        //******************************************************************************************
        private fun getNewDimension(a: Array<DoubleArray>, b: Array<DoubleArray>): Int {
            return 1 shl log2(Collections.max(Arrays.asList(a.size, a[0].size, b[0].size)))
        }
    }
}