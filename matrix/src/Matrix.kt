class Matrix {

    fun matrixMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        if (n != m) {
            return matrixCacheMultiplication(firstMatrix, secondMatrix)
        }

        return result
    }

    fun matrixStandartMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        //println("$n, $m")

        for (i in 0 until n) {
            for (j in 0 until m) {
                for (k in 0 until firstMatrix[0].size) {

                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j]

                }
            }
        }

        return result



    }

    fun matrixCacheMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})


        return result

    }

    fun matrixCacheTranspositionMultiplication(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>):
            Array<DoubleArray> {

        val firstRows = firstMatrix.size
        val firstColumns = firstMatrix[0].size
        val secondRows = secondMatrix.size
        val secondColumns = secondMatrix[0].size

        val secondTransposition = cacheTransposition(secondMatrix)

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
                nowRow[k] = secondMatrix[k][j]
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


    fun matrixStrassenMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        return result

    }

    fun matrixStrassenCacheMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        return result

    }

//    fun spliter(firstMatrix11: Array<Array<Int>>, secondMatrix11: Array<Array<Int>>): Array<Array<Int>> {
//
//    }

    fun cacheTransposition(matrix: Array<DoubleArray>): Array<DoubleArray> {

        val subMatrixSize = 256

        val rows = matrix.size

        val columns = matrix[0].size

        val result = Array(columns) {DoubleArray(rows)}

        for (i in 0 until rows step subMatrixSize) {
            for (j in 0 until columns step subMatrixSize) {

                for (k in i until Math.min(rows, i + subMatrixSize)) {
                    for (s in j until Math.min(columns, j + subMatrixSize)) {

                        result[s][k] = matrix[k][s]

                    }
                }
            }
        }
        //matrix = result
        return result
    }





}