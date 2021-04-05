
import java.util.*

fun main(args: Array<String>) {

    val matrix: Matrix = Matrix()           // object of Matrix for use methods

    val sc: Scanner = Scanner(System.`in`)

    val n = sc.nextInt()
    val m = sc.nextInt()

    val firstMatrix = Array(n) {DoubleArray(m)}
    val secondMatrix = Array(m) {DoubleArray(n)}

    for(i in 0..n - 1) {
        for (j in 0..m - 1) {
            firstMatrix[i][j] = (Math.random()*1000)        // initialize the first matrix
        }
    }

    for(i in 0..m - 1) {
        for (j in 0..n - 1) {
            secondMatrix[i][j] = (Math.random()*1000)        // initialize the second matrix
        }
    }

    var l = System.currentTimeMillis()

    val kotlinStrassen: KotlinStrassen = KotlinStrassen()

    val result1 = kotlinStrassen.multiplyMatrix(firstMatrix, secondMatrix)

    print("Strassen: ")
    println(System.currentTimeMillis() - l)

    l = System.currentTimeMillis()

    var result2 = matrix.matrixCacheTranspositionMultiplication(firstMatrix, secondMatrix)

    print("Standard, cache transposition ")
    println(System.currentTimeMillis() - l)

    l = System.currentTimeMillis()

    print("Standard, cycle transposition ")
    var result3 = matrix.matrixFastTranspositionMultiplication(firstMatrix, secondMatrix)

    println(System.currentTimeMillis() - l)


//    l = System.currentTimeMillis()
//
//    print("Fork Join ")
//    var result4 = MatrixMultiplication().myRecursiveTask(firstMatrix, secondMatrix, n).compute()
//
//    println(System.currentTimeMillis() - l)


    print(result1.size)
    print(" ")
    print(result1[0].size)

    println()

    print(result2.size)
    print(" ")
    print(result2[0].size )

    println()

    print(result3.size)
    print(" ")
    print(result3[0].size )

    println()

//    var sum = 0
//    for (i in 0..n - 1) {
//        for (j in 0..n - 1) {
//            if (result2[i][j].toInt() != result3[i][j].toInt() || result1[i][j].toInt() != result3[i][j].toInt()
//                    || result2[i][j].toInt() != result1[i][j].toInt()) {
//                println("$i - $j")
//            }
//        }
//    }
//
//    for (i in 0 until result3.size) {
//        for (j in 0 until result3.size) {
//            print(result3[i][j])
//            print(" ")
//        }
//        println()
//    }
//    println("\n")
//
//    for (i in 0 until result1.size) {
//        for (j in 0 until result1.size) {
//            print(result1[i][j])
//            print(" ")
//        }
//        println()
//    }

}


