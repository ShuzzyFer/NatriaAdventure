package com.shuzzy.natriaadventure

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EndGameScreen : AppCompatActivity() {

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_game_screen)
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val textView = findViewById<TextView>(R.id.textView4)
        val restartButton = findViewById<Button>(R.id.restartButton)

        val isTablet = resources.configuration.smallestScreenWidthDp >= 600

        // Если планшет, увеличиваем размеры текста и кнопок
        if (isTablet) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)  // Увеличенный размер текста
            restartButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)  // Увеличенный размер текста кнопки

            // Опционально: установить ширину кнопки на 70% экрана
            val params = restartButton.layoutParams as ConstraintLayout.LayoutParams
            params.matchConstraintPercentWidth = 0.7f
            restartButton.layoutParams = params
        } else {
            // Размеры по умолчанию для обычных экранов
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            restartButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        restartButton.setOnClickListener {
            val intent = Intent(this, TitleScreen::class.java)
            startActivity(intent)
        }
    }
}