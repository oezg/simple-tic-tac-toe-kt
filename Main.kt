package tictactoe

import kotlin.math.abs

enum class State(val result: String) {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    IMPOSSIBLE("Impossible")
}

enum class Cell(val sign: Char) {
    X('X'),
    O('O'),
    EMPTY('_');

    companion object {
        fun fromChar(value: Char): Cell {
            for (x in Cell.values()) {
                if (x.sign == value) {
                    return x
                }
            }
            throw IllegalArgumentException(value.toString())
        }
    }
}

class Game(input: String) {
    val matrix: List<List<Cell>>
    var state: State
    val frequencyCounter: Map<Cell, Int>

    init {
        if (input.length != 9) {
            throw IllegalArgumentException(input)
        }
        matrix = toMatrix(input)
        frequencyCounter = countCells()
        state = setState()
    }

    private fun countCells(): Map<Cell, Int> {
        val counter = mutableMapOf<Cell, Int>()
        for (row in matrix) {
            for (cell in row) {
                counter[cell] = counter.getOrDefault(cell, 0) + 1
            }
        }
        return counter.toMap()
    }

    private fun f(coll: List<Cell>, cll: Cell, crrnt: State, result: State): Result<State> =
        if (coll.any { it != cll }) {
            Result.success(crrnt)
        } else if (crrnt == State.GAME_NOT_FINISHED) {
            Result.success(result)
        } else {
            Result.failure(Exception())//State.IMPOSSIBLE
        }

    fun checkRow(currentState: State): Result<State> {
        for (row in matrix) {
            var t = f(row, Cell.X, currentState, State.X_WINS)
            if (t.isSuccess) {
                currentState = t.getOrDefault(currentState)
            } else if (row.all { it == Cell.O }) {
                if (currentState == State.GAME_NOT_FINISHED) {
                    currentState = State.O_WINS
                } else {
                    return Result.failure(Exception())
                }
            }
        }
    }

    fun setState(): State {
        var currentState = State.GAME_NOT_FINISHED
        if (abs(frequencyCounter.getOrDefault(Cell.X, 0) - frequencyCounter.getOrDefault(Cell.O, 0)) > 1) {
            return State.IMPOSSIBLE
        }



        for (i in 0..2) {
            val column = matrix.map { it[i] }
            if (column.all { it == Cell.X }) {
                if (currentState == State.GAME_NOT_FINISHED) {
                    currentState = State.X_WINS
                } else {
                    return State.IMPOSSIBLE
                }
            } else if (column.all { it == Cell.O }) {
                if (currentState == State.GAME_NOT_FINISHED) {
                    currentState = State.O_WINS
                } else {
                    return State.IMPOSSIBLE
                }
            }
        }

        if ((0..2).all { matrix[it][it] == Cell.X }) {
            if (currentState == State.GAME_NOT_FINISHED) {
                currentState = State.X_WINS
            } else {
                return State.IMPOSSIBLE
            }
        } else if ((0..2).all { matrix[it][it] == Cell.O }) {
            if (currentState == State.GAME_NOT_FINISHED) {
                currentState = State.O_WINS
            } else {
                return State.IMPOSSIBLE
            }
        } else if ((0..2).all { matrix[it][2 - it] == Cell.X }) {
            if (currentState == State.GAME_NOT_FINISHED) {
                currentState = State.X_WINS
            } else {
                return State.IMPOSSIBLE
            }
        } else if ((0..2).all { matrix[it][2 - it] == Cell.O }) {
            if (currentState == State.GAME_NOT_FINISHED) {
                currentState = State.O_WINS
            } else {
                return State.IMPOSSIBLE
            }
        } else if (frequencyCounter.getOrDefault(Cell.EMPTY, 0) == 0) {
            if (currentState == State.GAME_NOT_FINISHED) {
                currentState = State.DRAW
            }
        }
        return currentState
    }

    private fun toMatrix(input: String): List<List<Cell>> =
        input.chunked(3) { it.toList().map { x -> Cell.fromChar(x) } }


    fun printMatrix() {
        val printBorder = {  println("-".repeat(9)) }
        printBorder()
        for (row in matrix) {
            val (a, b, c) = row.map { it.sign }
            val lineTemplate = "| $a $b $c |"
            println(lineTemplate)
        }
        printBorder()
    }

    fun printState() {
        println(state.result)
    }
}

fun main() {
    val game = Game(readln())
    game.printMatrix()
    game.printState()
}
