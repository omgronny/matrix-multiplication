import java.util.*

/**
 * Class for multiplication matrix using Strassen - algorithm
 */
class KotlinStrassen {

    private fun splitMatrix(a: Array<DoubleArray>, a11: Array<DoubleArray>, a12: Array<DoubleArray>, a21: Array<DoubleArray>,
                            a22: Array<DoubleArray>) {

        val n = a.size shr 1

        for (i in 0 until n) {

            System.arraycopy(a[i], 0, a11[i], 0, n)
            System.arraycopy(a[i], n, a12[i], 0, n)
            System.arraycopy(a[i + n], 0, a21[i], 0, n)
            System.arraycopy(a[i + n], n, a22[i], 0, n)

        }

    }


    private fun collectMatrix(a11: Array<DoubleArray>, a12: Array<DoubleArray>, a21: Array<DoubleArray>,
                              a22: Array<DoubleArray>): Array<DoubleArray> {

        val n = a11.size
        val a = Array(n shl 1) { DoubleArray(n shl 1) }

        for (i in 0 until n) {

            System.arraycopy(a11[i], 0, a[i], 0, n)
            System.arraycopy(a12[i], 0, a[i], n, n)
            System.arraycopy(a21[i], 0, a[i + n], 0, n)
            System.arraycopy(a22[i], 0, a[i + n], n, n)

        }


        return a

    }

    private fun summation(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val aRows = a.size
        val aColumns: Int = a[0].size

        val bColumns: Int = b[0].size
        val result = Array(aRows) { DoubleArray(aColumns) }

        for (i in 0 until aRows) {
            for (j in 0 until aColumns) {

                result[i][j] = a[i][j] + b[i][j]

            }
        }

        return result
    }

    private fun subtraction(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val aRows = a.size
        val aColumns: Int = a[0].size

        val bColumns: Int = b[0].size
        val result = Array(aRows) { DoubleArray(aColumns) }

        for (i in 0 until aRows) {
            for (j in 0 until aColumns) {

                result[i][j] = a[i][j] - b[i][j]

            }
        }

        return result
    }

    private fun multiStrassen(a: Array<DoubleArray>, b: Array<DoubleArray>, n: Int): Array<DoubleArray> {

        var n = n

        if (n <= 128) {
            val matrix = MatrixTransposition()
            return matrix.matrixFastTranspositionMultiplication(a, b)
        }

        n = n shr 1

        val a11 = Array(n) { DoubleArray(n) }
        val a12 = Array(n) { DoubleArray(n) }
        val a21 = Array(n) { DoubleArray(n) }
        val a22 = Array(n) { DoubleArray(n) }
        val b11 = Array(n) { DoubleArray(n) }
        val b12 = Array(n) { DoubleArray(n) }
        val b21 = Array(n) { DoubleArray(n) }
        val b22 = Array(n) { DoubleArray(n) }

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

    /**
     * Method that multiplies matrices using Strassen's algorithm
     */
    fun multiplyMatrix(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val n = a.size

        var newSize = 1

        while(n > newSize) {
            newSize *= 2
        }

        val aToPowerOf = Array(newSize) { DoubleArray(newSize) }
        val bToPowerOf = Array(newSize) { DoubleArray(newSize) }

        for (i in 0 until n) {

            System.arraycopy(a[i], 0, aToPowerOf[i], 0, n)
            System.arraycopy(b[i], 0, bToPowerOf[i], 0, n)

        }

        val resultOfMultiplication = multiStrassen(aToPowerOf, bToPowerOf, aToPowerOf.size)

        val finalResult = Array(a.size) {DoubleArray(b[0].size)}

        for (i in 0 until a.size) {

            System.arraycopy(resultOfMultiplication[i], 0, finalResult[i], 0,
                    finalResult[i].size)

        }

        return  finalResult


    }

}