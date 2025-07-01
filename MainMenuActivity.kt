package com.example.simondice

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    private lateinit var musicPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnEnglish: Button = findViewById(R.id.btn_english)
        val btnSpanish: Button = findViewById(R.id.btn_spanish)

        btnEnglish.setOnClickListener {
            startGame("en")
        }

        btnSpanish.setOnClickListener {
            startGame("es")
        }

        musicPlayer = MediaPlayer.create(this, R.raw.menu_music)
        musicPlayer.isLooping = true
        musicPlayer.start()
    }

    private fun startGame(lang: String) {
        musicPlayer.stop()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("lang", lang)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
            musicPlayer.release()
        }
    }
}
