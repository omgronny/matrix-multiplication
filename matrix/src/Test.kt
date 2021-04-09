

/**
 * Class for benchmarking the algorithms
 */
object Test {

    private val matrixTransposition = MatrixTransposition()

    fun test(n: Int, m: Int) {

        val firstMatrix = generateMatrix(n , m)
        val secondMatrix = generateMatrix(m , n)

        println(getWorkingTime(firstMatrix, secondMatrix, AlgorithmType.STRASSEN))
        println(getWorkingTime(firstMatrix, secondMatrix, AlgorithmType.CACHE))
        println(getWorkingTime(firstMatrix, secondMatrix, AlgorithmType.FAST))


    }

    private fun generateMatrix(rows: Int, columns: Int): Array<DoubleArray> {

        val matrix = Array(rows) {DoubleArray(columns)}

        for(i in 0 until rows) {
            for (j in 0 until columns ) {

                matrix[i][j] = (Math.random()*1000)        // initialize the matrix

            }
        }

        return matrix

    }

    private fun getWorkingTime(firstMatrix: Array<DoubleArray>,
                       secondMatrix: Array<DoubleArray>,
                       type: AlgorithmType): String {

        val l = System.currentTimeMillis()

        when (type) {

            AlgorithmType.CACHE -> {

                var cacheResult = matrixTransposition.matrixCacheTranspositionMultiplication(firstMatrix, secondMatrix)

            }
            AlgorithmType.FAST -> {

                var fastResult = matrixTransposition.matrixFastTranspositionMultiplication(firstMatrix, secondMatrix)

            }
            else -> {

                val kotlinStrassen = KotlinStrassen()
                val kotlinStrassenResult = kotlinStrassen.multiplyMatrix(firstMatrix, secondMatrix)

            }

        }

        return "$type: " + (System.currentTimeMillis() - l).toString()

    }

}