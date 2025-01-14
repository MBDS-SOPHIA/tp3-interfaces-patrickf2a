package com.example.dicerollermbdsFP

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Configure Edge-to-Edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val targetNumberEdit: EditText = findViewById(R.id.targetNumber)
        // Roll dice automatically when input changes
        targetNumberEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val number = s.toString().toIntOrNull()
                if (number != null && number in 2..12) {
                    rollDice()
                }
            }
        })
    }

    /**
     * Animate the dice views up and down
     */
    private fun animateDiceWin(view1: TextView, view2: TextView) {
        // Animation for first dice
        ObjectAnimator.ofFloat(view1, "translationY", 0f, -100f, 0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        // Animation for second dice
        ObjectAnimator.ofFloat(view2, "translationY", 0f, -100f, 0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        // Color animation
        ObjectAnimator.ofArgb(view1, "textColor", Color.BLACK, Color.GREEN, Color.BLACK).apply { duration = 1000
            start() }

        ObjectAnimator.ofArgb(view2, "textColor", Color.BLACK, Color.GREEN, Color.BLACK).apply { duration = 1000
            start()
        }
    }

    /**
     * Roll both dice and update the screen with the results.
     * Show a winning message and animation if the sum equals the target number.
     */
    private fun rollDice() {
        // Get target number
        val targetNumberEdit: EditText = findViewById(R.id.targetNumber)
        val targetNumber = targetNumberEdit.text.toString().toInt()

        // Create two dice objects with 6 sides and roll them
        val dice1 = Dice(6)
        val dice2 = Dice(6)
        val diceRoll1 = dice1.roll()
        val diceRoll2 = dice2.roll()
        val sum = diceRoll1 + diceRoll2

        // Update the screen with both dice rolls
        val resultTextView1: TextView = findViewById(R.id.textView1)
        val resultTextView2: TextView = findViewById(R.id.textView2)
        resultTextView1.text = diceRoll1.toString()
        resultTextView2.text = diceRoll2.toString()

        // Check if user won (sum equals target number)
        if (sum == targetNumber) {
            Toast.makeText(this, "Félicitations Champions vous avez gagné!", Toast.LENGTH_SHORT).show()
            animateDiceWin(resultTextView1, resultTextView2)
        } else {
            Toast.makeText(this, "Perdu! La somme est $sum", Toast.LENGTH_SHORT).show()
        }
    }
}

/**
 * Dice with a fixed number of sides.
 */
class Dice(private val numSides: Int) {
    /**
     * Do a random dice roll and return the result.
     */
    fun roll(): Int {
        return (1..numSides).random()
    }
}