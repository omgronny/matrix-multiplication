class Matrix {

    fun matrixStandartMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        //println("$n, $m")

        for (i in 0..n - 1) {
            for (j in 0..m - 1) {
                for (k in 0..firstMatrix[0].size - 1) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j]
                }
            }
        }

        return result



    }

    fun matrixCashMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})


        return result

    }

    fun matrixCashTranspositionMultiplication(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>):
            Array<DoubleArray> {

        val firstRows = firstMatrix.size
        val firstColumns = firstMatrix[0].size
        //val secondRows = secondMatrix.size
        val secondColumns = secondMatrix[0].size

        val secondTransposition = cashTransposition(secondMatrix)

        //val secondTransRows = secondTransposition.size
        //val secondTransColumns = secondTransposition[0].size

        val result = Array(firstRows) { DoubleArray(secondColumns) }

        for (j in 0 until firstColumns) {

            for (i in 0 until firstRows) {
                var sum = 0.0
                for (k in 0 until secondColumns) {

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

        var result = Array(firstRows) { DoubleArray(secondColumns) }

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

    fun matrixStrassenCashMultiplication(firstMatrix: Array<Array<Double>>, secondMatrix: Array<Array<Double>>):
            Array<Array<Double>> {

        val n = firstMatrix.size

        val m = secondMatrix[0].size

        var result: Array<Array<Double>> = Array(n, {Array(m, {0.0})})

        return result

    }

//    fun spliter(firstMatrix11: Array<Array<Int>>, secondMatrix11: Array<Array<Int>>): Array<Array<Int>> {
//
//    }

    fun cashTransposition(matrix: Array<DoubleArray>): Array<DoubleArray> {

        val subMatrixSize = 256

        val rows = matrix.size

        val columns = matrix[0].size

        val result = Array(rows) {DoubleArray(columns)}

        for (j in 0 until columns step subMatrixSize) {
            for (i in 0 until rows step subMatrixSize) {
                for (k in j until Math.min(columns, j + subMatrixSize)) {
                    for (s in i until Math.min(rows, i + subMatrixSize)) {

                        result[s][k] = matrix[k][s]

                    }
                }
            }
        }
        //matrix = result
        return result
    }





}