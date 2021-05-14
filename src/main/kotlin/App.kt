import java.util.Collections.frequency

class TakuzuResolver(var grid: ArrayList<ArrayList<Int>>) {
    fun resolve() {
        /**
         * Règles :
         * - pas possible d'avoir plus de 2 fois le même chiffre à la suite
         * - Même nombre de 0 et de 1 dans la même ligne / même colonne
         * -
         *
         * prévoir une gestion du remplissage random
         */
        var cpt = 0
        while (!isFullFilled()) {
            fillThanksToSameDoubleDigit()
            fillRowsOrColumnsAlmostFull()
            fillRowsOrColumnsHalfFull()

            cpt++
            if (cpt == 5)
                break
        }
        printGrid()
    }

    private fun fillRowsOrColumnsHalfFull() {
        var totalCounter = 0
        do {
            var loopCounter = 0
            // On fait 1 parcours horizontal > flip > parcours vertical > flip
            for (cpt in 0..1) {
                for (i in grid.indices) {
                    while (isRowHalfFullOfValue(grid[i])) {
                        val emptyCellIndex = grid[i].indexOf(-1)
                        val newValue = getValueInMinorityInRow(grid[i])
                        grid[i][emptyCellIndex] = newValue
                        loopCounter++
                    }
                }
                grid = flipArray(grid)
            }
            totalCounter += loopCounter
        } while (loopCounter != 0)
        println("$totalCounter value replaced with fillRowsOrColumnsHalfFull")
    }

    private fun isRowHalfFullOfValue(row: ArrayList<Int>): Boolean {
        return (frequency(row, 0) == grid.size / 2 && frequency(row, -1) > 0) ||
                (frequency(row, 1) == grid.size / 2 && frequency(row, -1) > 0)
    }

    /**
     * Remplie :
     * - soit les lignes ou colonnes où il ne reste qu'une seule valeure à rentrer
     */
    private fun fillRowsOrColumnsAlmostFull() {
        var totalCounter = 0
        do {
            var loopCounter = 0
            // On fait 1 parcours horizontal > flip > parcours vertical > flip
            for (cpt in 0..1) {
                for (i in grid.indices) {
                    if (frequency(grid[i].toList(), -1) == 1) {
                        val emptyCellIndex = grid[i].indexOf(-1)
                        val newValue = getValueInMinorityInRow(grid[i])
                        grid[i][emptyCellIndex] = newValue
                        loopCounter++
                    }
                }
                grid = flipArray(grid)
            }
            totalCounter += loopCounter
        } while (loopCounter != 0)
        println("$totalCounter value replaced with fillRowsOrColumnsAlmostFull")
    }

    private fun flipArray(array: ArrayList<ArrayList<Int>>): ArrayList<ArrayList<Int>> {
        val flippedArray = arrayListOf<ArrayList<Int>>()
        for (i in array[0].indices) {
            val row = arrayListOf<Int>()
            for (j in array.indices) {
                row.add(array[j][i])
            }
            flippedArray.add(row)
        }
        return flippedArray
    }

    private fun getValueInMinorityInRow(row: ArrayList<Int>): Int {
        val nbZero = frequency(row.toList(), 0)
        val nbOne = frequency(row.toList(), 1)
        return if (nbOne < nbZero) 1 else 0
    }

    private fun isFullFilled(): Boolean {
        for (row in grid) {
            if (row.contains(-1)) {
                return false
            }
        }
        return true
    }

    /**
     * Faire une fonction pour détecter si on trouve un vide à la suite de 2 même chiffre,
     * si c'est le cas, on peut le remplir avec le chiffre opposé
     * Faire 2 parcours :
     * - une fois ligne par ligne
     * - une fois col par col
     */
    private fun fillThanksToSameDoubleDigit() {
        var totalCounter = 0
        do {
            var loopCounter = 0
            // Parcours horizontal
            for (i in grid.indices) {
                for (j in 0..grid[i].size - 3) {
                    val trioCell = listOf(grid[i][j], grid[i][j + 1], grid[i][j + 2])
                    if (canDeduceThirdCell(trioCell)) {
                        val dominantValue = trioCell.maxOrNull()
                        val newValue = if (dominantValue == 0) 1 else 0
                        val indexCell = trioCell.indexOf(-1)
                        grid[i][j + indexCell] = newValue
                        loopCounter++
                    }
                }
            }

            // Parcours vertical
            for (i in 0..grid.size - 3) {
                for (j in grid[i].indices) {
                    val trioCell = listOf(grid[i][j], grid[i + 1][j], grid[i + 2][j])
                    if (canDeduceThirdCell(trioCell)) {
                        val dominantValue = trioCell.maxOrNull()
                        val newValue = if (dominantValue == 0) 1 else 0
                        val indexCell = trioCell.indexOf(-1)
                        grid[i + indexCell][j] = newValue
                        loopCounter++
                    }
                }
            }
            totalCounter += loopCounter
        } while (loopCounter != 0)
        println("$totalCounter value replaced with fillTanksToSameDoubleDigit")
    }

    /**
     * 2 conditions pour savoir si on peut déduire qqchose :
     * - est-ce qu'il y a strictement 1 seule case vide dans la suite des 3 ?
     * - est-ce que les 2 autres sont identiques ? (on teste si l'une des 2 autres valeurs possible n'est pas égal à 1 occurence
     */
    private fun canDeduceThirdCell(trioCell: List<Int>): Boolean {
        return frequency(trioCell, -1) == 1 && frequency(trioCell, 1) != 1
    }

    fun printGrid() {
        for (row in grid) {
            for (col in row) {
                if (col in 0..1)
                    print(" ")
                print(" $col")
            }
            println()
        }
    }
}

fun main(args: Array<String>) {
    val grid1 = arrayListOf(
        arrayListOf(-1, 0, -1, -1, -1, 1, -1, -1),
        arrayListOf(-1, -1, -1, 0, -1, 1, -1, -1),
        arrayListOf(-1, 1, -1, 1, -1, -1, 0, -1),
        arrayListOf(-1, -1, -1, -1, -1, -1, -1, -1),
        arrayListOf(0, -1, -1, -1, -1, 1, -1, -1),
        arrayListOf(-1, 0, -1, 0, -1, -1, -1, -1),
        arrayListOf(0, 0, -1, -1, -1, 1, 0, -1),
        arrayListOf(-1, -1, -1, 1, -1, -1, 1, 1)
    )

    // TODO : import from image
    val game = TakuzuResolver(grid1)
    game.resolve()
}
