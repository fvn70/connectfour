package connectfour

import kotlin.math.max
import kotlin.math.min

var cnt = 0
var rows = 6
var cols = 7
var score1 = 0
var score2 = 0
var isP1go = true
var fld = mutableListOf<MutableList<Char>>()

fun main() {
    println("Connect Four")
    println("First player's name:")
    val pl1 = readLine()!!
    println("Second player's name:")
    val pl2 = readLine()!!

    val str = readBoard()
    if (!str.isBlank()) {
        rows = str.first().digitToInt()
        cols = str.last().digitToInt()
    }
//    fld = MutableList(rows) { MutableList(cols) { ' ' } }

    var gameNums = readNum()

    println("$pl1 VS $pl2")
    println("$rows X $cols board")
    println(if (gameNums > 1) "Total $gameNums games" else "Single game")

    runGame(pl1, pl2, gameNums)

    println("Game over!")
}

fun runGame(name1: String, name2: String, gameNums: Int) {
    var num = 0
    repeat(gameNums) {
        fld = MutableList(rows) { MutableList(cols) { ' ' } }
        isP1go = num % 2 == 0
        if (gameNums > 1) println("Game #${++num}")
        draw()

        while (true) {
            val name = if (isP1go) name1 else name2
            val col = readCol(name)
            if (col == -1) return
            val row = freeRow(col)
            if (row == -1) {
                println("Column $col is full")
                continue
            }
            fld[row - 1][col - 1] = if (isP1go) 'o' else '*'
            cnt++
            val check = checkFld(row - 1, col - 1)
            if (check == 0) {
                draw()
                println("It is a draw")
                if (gameNums > 1) {
                    score1++
                    score2++
                    println("Score")
                    println("$name1: $score1 $name2: $score2")
                }
                break
            } else if (check == 1) {
                draw()
                println("Player $name won")
                if (gameNums > 1) {
                    if (isP1go) score1 += 2 else score2 += 2
                    println("Score")
                    println("$name1: $score1 $name2: $score2")
                }
                break
            }
            isP1go = !isP1go
            draw()
        }
    }
}

fun readNum(): Int {
    while (true) {
        println("Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:")
        val str = readLine()!!.trim()
        if (str.isBlank()) return 1
        if (!str.matches("[1-9][0-9]?".toRegex())) {
            println("Invalid input")
            continue
        }
        return str.toInt()
    }
}

fun checkFld(r: Int, c: Int): Int {
    if (fld[0].filter { it != ' ' }.count() == cols) return 0
    val sreg = ".*(\\*{4}|o{4}).*".toRegex()
    var sh = fld[r].joinToString("")

    var sv = ""
    for (i in 0..rows - 1) {
        sv += fld[i][c]
    }

    var i = 0
    var j = 0
    if (r > c) i = r - c else j = c - r
    var sd1 = ""
    while (i < rows && j < cols) {
        sd1 += fld[i++][j++]
    }

    // y = y0 - x
    val y0 = r + c
    if (r < min(rows, cols) - c) { // A
        i = 0
        j = y0 - 1
    } else if (r > max(rows, cols) - c) { // C
        i = y0 - cols + 1
        j = cols - 1
    } else if (cols > rows) { // B horizontal
        i = 0
        j = y0 - 1
    } else { // B vertical
        i = y0 - cols + 1
        j = cols - 1
    }

    var sd2 = ""
    while (i < rows && j >= 0) {
        sd2 += fld[i++][j--]
    }

    if (sh.matches(sreg))
        return 1
    if (sv.matches(sreg))
        return 1
    if (sd1.matches(sreg))
        return 1
    if (sd2.matches(sreg))
        return 1

    return -1
}

fun readCol(name: String): Int {
    while (true) {
        println("$name's turn:")
        val str = readLine()!!.trim()
        if (str == "end") return -1
        if (!str.matches("[\\d]+".toRegex())) {
            println("Incorrect column number")
            continue
        }
        val col = str.toInt()
        if (col !in 1..cols) {
            println("The column number is out of range (1 - $cols)")
            continue
        }
        return col
    }
}

fun readBoard(): String {
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val str = readLine()!!.trim()
        if (str.isBlank()) return str
        if (!str.matches("\\d+\\s*[xX]\\s*\\d+".toRegex())) {
            println("Invalid input")
            continue
        }
        if (str.first() !in '5'..'9') {
            println("Board rows should be from 5 to 9")
            continue
        }
        if (str.last() !in '5'..'9') {
            println("Board columns should be from 5 to 9")
            continue
        }
        return str
    }
}

fun freeRow(col: Int): Int {
    for (i in rows - 1 downTo 0) {
        if (fld[i][col - 1] == ' ')
            return i + 1
    }
    return -1
}

fun draw() {
    for (j in 1..cols) print(" $j")
    println()
    for (i in 0..rows - 1) {
        for (j in 0..cols - 1) {
//            val ch = if (fld[i][j] == 1) 'o' else if (fld[i][j] == -1) '*' else ' '
            print("║${fld[i][j]}")
        }
        println("║")
    }
    println("╚${"═╩".repeat(cols - 1)}═╝")
}
