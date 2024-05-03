package tictactoe

class Game {
    enum class State(val result: String) {
        GAME_NOT_FINISHED("Game not finished"),
        DRAW("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins"),
    }

    enum class Cell(val sign: Char) {
        X('X'),
        O('O'),
        EMPTY(' ')
    }

    enum class Player(val cell: Cell, val state: State) {
        X(Cell.X, State.X_WINS),
        O(Cell.O, State.O_WINS)
    }

    private val matrix: List<MutableList<Cell>> = (1..3).map { mutableListOf(Cell.EMPTY, Cell.EMPTY, Cell.EMPTY) }
    private var player: Player
    private var state: State
    private var emptyCount = 9

    init {
        player = Player.X
        state = State.GAME_NOT_FINISHED
    }

    fun updateState() {
        state = when {
            matrix.any { it.all { it == player.cell } } -> player.state
            matrix.indices.any { matrix.map { row -> row[it] }.all { it == player.cell } } -> player.state
            matrix.indices.all { matrix[it][it] == player.cell } -> player.state
            matrix.indices.all { matrix[it][matrix.lastIndex - it] == player.cell } -> player.state
            emptyCount == 0 -> State.DRAW
            else -> state
        }
    }

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

    fun updateMatrix() {
        while (true) {
            val line = readln()
            if (line.contains("[^\\d\\s]".toRegex())) {
                println("You should enter numbers!")
            } else if (line.contains("[04-9]".toRegex())) {
                println("Coordinates should be from 1 to 3!")
            } else {
                val (a, b) = line.split(" ").map { x -> x.toInt() }
                if (matrix[a - 1][b - 1] != Cell.EMPTY) {
                    println("This cell is occupied! Choose another one!")
                } else {
                    matrix[a - 1][b - 1] = player.cell
                    emptyCount--
                    break
                }
            }
        }
    }

    fun updatePlayer() {
        player = if (player == Player.X) Player.O else Player.X
    }

    fun loop() {
        printMatrix()
        while (state == State.GAME_NOT_FINISHED) {
            updateMatrix()
            printMatrix()
            updateState()
            updatePlayer()
        }
        printState()
    }
}

fun main() {
    Game().loop()
}
