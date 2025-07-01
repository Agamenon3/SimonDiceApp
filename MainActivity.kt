package com.example.simondice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var buttons: List<Button>
    private lateinit var statusText: TextView
    private lateinit var prefs: SharedPreferences

    private var sequence = mutableListOf<Int>()
    private var playerIndex = 0
    private var highScore = 0

    private val handler = Handler()
    private var sounds = mutableListOf<MediaPlayer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lang = intent.getStringExtra("lang") ?: "en"
        val config = resources.configuration
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        config.setLocale(locale)
        createConfigurationContext(config)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        highScore = prefs.getInt("high_score", 0)

        buttons = listOf(
            findViewById(R.id.btn_red),
            findViewById(R.id.btn_green),
            findViewById(R.id.btn_blue),
            findViewById(R.id.btn_yellow)
        )
        statusText = findViewById(R.id.status_text)

        sounds = mutableListOf(
            MediaPlayer.create(this, R.raw.sound1),
            MediaPlayer.create(this, R.raw.sound2),
            MediaPlayer.create(this, R.raw.sound3),
            MediaPlayer.create(this, R.raw.sound4)
        )

        for ((i, btn) in buttons.withIndex()) {
            btn.setOnClickListener { onColorClicked(i) }
        }

        startGame()
    }

    private fun startGame() {
        sequence.clear()
        addStep()
    }

    private fun addStep() {
        sequence.add(Random.nextInt(4))
        playSequence()
    }

    private fun playSequence() {
        playerIndex = 0
        statusText.text = getString(R.string.watch)
        var delay = 500L

        for (i in sequence.indices) {
            handler.postDelayed({
                val index = sequence[i]
                buttons[index].alpha = 0.5f
                sounds[index].start()
                handler.postDelayed({
                    buttons[index].alpha = 1.0f
                }, 300)
            }, delay)
            delay += 800
        }

        handler.postDelayed({
            statusText.text = getString(R.string.your_turn)
        }, delay)
    }

    private fun onColorClicked(index: Int) {
        sounds[index].start()
        if (sequence[playerIndex] == index) {
            playerIndex++
            if (playerIndex == sequence.size) {
                statusText.text = getString(R.string.correct)
                handler.postDelayed({
                    addStep()
                }, 1000)
            }
        } else {
            statusText.text = getString(R.string.game_over, sequence.size - 1)
            if (sequence.size - 1 > highScore) {
                highScore = sequence.size - 1
                prefs.edit().putInt("high_score", highScore).apply()
            }
        }
    }
}
