
import java.io.File
import java.util.*

/**
 * Method for generate matrix and testing results
 */
fun main(args: Array<String>) {

    val matrix = Matrix()           // object of Matrix for use methods

    val sc = Scanner(System.`in`)

    val n = sc.nextInt()
    val m = sc.nextInt()

    val firstMatrix = Array(n) {DoubleArray(m)}
    val secondMatrix = Array(m) {DoubleArray(n)}

    for(i in 0 until n) {
        for (j in 0 until m ) {

            firstMatrix[i][j] = (Math.random()*1000)        // initialize the first matrix

        }
    }

    for(i in 0 until m) {
        for (j in 0 until n) {

            secondMatrix[i][j] = (Math.random()*1000)        // initialize the second matrix

        }
    }



    var l = System.currentTimeMillis()
    val kotlinStrassen = KotlinStrassen()

    val kotlinStrassenResult = kotlinStrassen.multiplyMatrix(firstMatrix, secondMatrix)

    print("Strassen: ")
    println(System.currentTimeMillis() - l)



    l = System.currentTimeMillis()

    var cacheResult = matrix.matrixCacheTranspositionMultiplication(firstMatrix, secondMatrix)

    print("Standard, cache transposition ")
    println(System.currentTimeMillis() - l)


    l = System.currentTimeMillis()

    print("Standard, fast transposition ")
    var fastResult = matrix.matrixFastTranspositionMultiplication(firstMatrix, secondMatrix)

    println(System.currentTimeMillis() - l)

    l = System.currentTimeMillis()

    print("Optimal ")
    var optimal = matrix.matrixMultiplication(firstMatrix, secondMatrix)

    println(System.currentTimeMillis() - l)



}


