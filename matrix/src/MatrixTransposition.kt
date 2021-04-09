import kotlin.math.min

/**
 * Class for multiplication matrix using standard transposition methods
 */
class MatrixTransposition {

    /**
     * Method method for multiplying two matrices. It find the most optimal algorithm
     */
    fun matrixMultiplication(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>):
            Array<DoubleArray>? {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        if (firstMatrix[0].size != secondMatrix.size) {
            return null
        }

        if (n != m) {

            return if (n in 0..500) {

                matrixFastTranspositionMultiplication(firstMatrix, secondMatrix)

            } else {

                matrixCacheTranspositionMultiplication(firstMatrix, secondMatrix)

            }

        } else {

            return if (n in 0..500) {

                matrixFastTranspositionMultiplication(firstMatrix, secondMatrix)

            } else if ((n in 501..900) || (n in 1025..1700 ) || (n in 2049..3800)) {

                matrixCacheTranspositionMultiplication(firstMatrix, secondMatrix)

            } else {

                val kotlinStrassen = KotlinStrassen()
                kotlinStrassen.multiplyMatrix(firstMatrix, secondMatrix)

            }



        }

    }

    /**
     * Method that multiplies matrices using cache transposition
     */
    fun matrixCacheTranspositionMultiplication(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>):
            Array<DoubleArray> {

        val firstRows = firstMatrix.size
        val firstColumns = firstMatrix[0].size
        val secondRows = secondMatrix.size
        val secondColumns = secondMatrix[0].size

        val secondTransposition = cacheTransposition(secondMatrix, if (firstRows >= 1250) 256 else 128)

        //val secondTransRows = secondTransposition.size
        //val secondTransColumns = secondTransposition[0].size

        val result = Array(firstRows) { DoubleArray(secondColumns) }

        for (i in 0 until firstRows) {

            for (j in 0 until secondColumns) {
                var sum = 0.0
                for (k in 0 until secondRows) {

                    sum += firstMatrix[i][k] * secondTransposition[j][k]

                }

                result[i][j] = sum

            }

        }

        return result

    }

    /**
     * Method that multiplies matrices using fast transposition
     */
    fun matrixFastTranspositionMultiplication(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>):
            Array<DoubleArray> {

        val firstRows = firstMatrix.size
        val firstColumns = firstMatrix[0].size
        val secondRows = secondMatrix.size
        val secondColumns = secondMatrix[0].size

        val result = Array(firstRows) { DoubleArray(secondColumns) }

        val nowRow = Array(secondRows, {0.0})

        for (j in 0 until secondColumns) {


            for (k in 0 until firstColumns) {
                nowRow[k] = secondMatrix[k][j]      // Transpose one column of the second matrix
            }


            for (i in 0 until firstRows) {

                var sum = 0.0
                for (k in 0 until firstColumns) {

                    sum += nowRow[k] * firstMatrix[i][k]

                }

                result[i][j] = sum

            }

        }

        return result

    }




    /**
     * Method that transposes a matrix using cache
     */
    private fun cacheTransposition(matrix: Array<DoubleArray>, subMatrixSize: Int = 128): Array<DoubleArray> {

        val rows = matrix.size

        val columns = matrix[0].size

        val result = Array(columns) {DoubleArray(rows)}

        for (i in 0 until rows step subMatrixSize) {
            for (j in 0 until columns step subMatrixSize) {

                for (k in i until min(rows, i + subMatrixSize)) {
                    for (s in j until min(columns, j + subMatrixSize)) {

                        result[s][k] = matrix[k][s]

                    }
                }
            }
        }
        //matrix = result
        return result

    }





}