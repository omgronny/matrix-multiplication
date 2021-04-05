import java.util.*

class KotlinStrassen {

    //******************************************************************************************

    fun upper(a: Array<DoubleArray>): Array<DoubleArray> {

        val n = a.size
        var newSize = 1

        while(n > newSize) {
            newSize *= 2
        }

        val result = Array(newSize) { DoubleArray(newSize) }

        for (i in 0..newSize-1) {
            for (j in 0..newSize-1) {
                if (j < n && i < n) {
                    result[i][j] = a[i][j]
                } else {
                    result[i][j] = 0.0
                }

            }
        }

        return result
    }

    fun splitMatrix(a: Array<DoubleArray>, a11: Array<DoubleArray>, a12: Array<DoubleArray>, a21: Array<DoubleArray>,
                    a22: Array<DoubleArray>) {

        val n = a.size shr 1

        val rows = a.size
        val columns = a[0].size

        for (i in 0..n-1) {

            System.arraycopy(a[i], 0, a11[i], 0, n)
            System.arraycopy(a[i], n, a12[i], 0, n)
            System.arraycopy(a[i + n], 0, a21[i], 0, n)
            System.arraycopy(a[i + n], n, a22[i], 0, n)

        }

//        for (i in 0..columns-1) {
//
//            for (j in 0..rows-1) {
//
//                if (i < columns/2 && j < rows/2) {
//                    a11[j][i] = a[j][i]
//                } else if (i >= columns/2 && j < rows/2) {
//                    a12[j][i % rows/2] = a[j][i]
//                } else if (i < columns/2 && j >= rows/2) {
//                    a21[j % rows/2][i] = a[j][i]
//                } else if (i >= columns/2 && j >= rows/2) {
//                    a22[j % rows/2][i % rows/2] = a[j][i]
//                }
//
//            }
//
//        }

    }

    //******************************************************************************************
    fun collectMatrix(a11: Array<DoubleArray>, a12: Array<DoubleArray>, a21: Array<DoubleArray>,
                      a22: Array<DoubleArray>): Array<DoubleArray> {

        val n = a11.size
        val a = Array(n shl 1) { DoubleArray(n shl 1) }

        val rows = a.size
        val columns = a[0].size

        for (i in 0..n-1) {

            System.arraycopy(a11[i], 0, a[i], 0, n)
            System.arraycopy(a12[i], 0, a[i], n, n)
            System.arraycopy(a21[i], 0, a[i + n], 0, n);
            System.arraycopy(a22[i], 0, a[i + n], n, n)

        }


        return a

    }

    fun summation(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val Arows = a.size
        val Acolumns: Int = a[0].size

        val Bcolumns: Int = b[0].size

        val C = Array(Arows) { DoubleArray(Bcolumns) }

        for (j in 0 until Acolumns) {

            for (i in 0 until Arows) {

                C[i][j] = a[i][j] + b[i][j]

            }
        }
        return C
    }

    fun subtraction(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val Arows = a.size
        val Acolumns: Int = a[0].size

        val Bcolumns: Int = b[0].size
        val C = Array(Arows) { DoubleArray(Bcolumns) }

        for (j in 0..Acolumns-1) {

            for (i in 0..Arows-1) {
                C[i][j] = a[i][j] - b[i][j]
            }
        }

        return C
    }

    fun multiStrassen(a: Array<DoubleArray>, b: Array<DoubleArray>, n: Int): Array<DoubleArray> {

        var n = n
        if (n <= 256) {

            val matrix = Matrix()
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

    public fun multiplyMatrix(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {

        val aToPowerOf = upper(a)
        val bToPowerOf = upper(b)

        val resultOfMultiplication = multiStrassen(aToPowerOf, bToPowerOf, aToPowerOf.size)

        val finalResult = Array(a.size) {DoubleArray(b[0].size)}

        return  resultOfMultiplication


    }
}