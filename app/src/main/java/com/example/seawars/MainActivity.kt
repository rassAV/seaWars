package com.example.seawars

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val shipSizes = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    private val board: Array<Array<Boolean>> = Array(10) { Array(10) { false } }
    private val occupiedCells: MutableSet<Pair<Int, Int>> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        createGameBoard()
    }

    private fun createGameBoard() {
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                val square = View(this)
                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }
                square.layoutParams = params
                square.setBackgroundColor(Color.WHITE)
                square.setOnClickListener {
                    val position = it.tag as Pair<Int, Int>
                    if (!board[position.first][position.second]) {
                        square.setBackgroundColor(Color.BLACK)
                        board[position.first][position.second] = true
                        updateOccupiedCells(position.first, position.second)
                    }
                }
                square.tag = Pair(i, j)
                gridLayout.addView(square)
            }
        }

        placeShips()
    }

    private fun placeShips() {
        for (shipSize in shipSizes) {
            var validPlacement = false

            while (!validPlacement) {
                val isHorizontal = Random.nextBoolean()
                val x = Random.nextInt(10)
                val y = Random.nextInt(10)
                validPlacement = checkValidPlacement(x, y, shipSize, isHorizontal)
                if (validPlacement) {
                    drawShip(x, y, shipSize, isHorizontal)
                }
            }
        }
    }

    private fun checkValidPlacement(x: Int, y: Int, size: Int, isHorizontal: Boolean): Boolean {
        if (isHorizontal) {
            if (x + size > 10) return false
            for (i in x until x + size) {
                if (!isEmptySquare(i, y)) return false
            }
        } else {
            if (y + size > 10) return false
            for (j in y until y + size) {
                if (!isEmptySquare(x, j)) return false
            }
        }
        return true
    }

    private fun isEmptySquare(x: Int, y: Int): Boolean {
        return !occupiedCells.contains(Pair(x, y))
    }

    private fun drawShip(x: Int, y: Int, size: Int, isHorizontal: Boolean) {
        if (isHorizontal) {
            for (i in x until x + size) {
                drawSquare(i, y)
            }
        } else {
            for (j in y until y + size) {
                drawSquare(x, j)
            }
        }
    }

    private fun drawSquare(x: Int, y: Int) {
        val index = x * 10 + y
        val square = gridLayout.getChildAt(index)
        square?.setBackgroundColor(Color.BLACK)
        board[x][y] = true
        updateOccupiedCells(x, y)
    }

    private fun updateOccupiedCells(x: Int, y: Int) {
        for (i in x - 1..x + 1) {
            for (j in y - 1..y + 1) {
                if (i in 0 until 10 && j in 0 until 10) {
                    occupiedCells.add(Pair(i, j))
                }
            }
        }
    }
}
