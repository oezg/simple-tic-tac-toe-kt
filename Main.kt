package tictactoe

fun main() {
    val grid = readln()
    println("-".repeat(9))
    for (i in 0..2) {
        val (_, a, b, c) = grid.slice(i*3..i*3+2).split("")
        val lineTemplate = "| $a $b $c |"
        println(lineTemplate)
    }
    println("-".repeat(9))
}