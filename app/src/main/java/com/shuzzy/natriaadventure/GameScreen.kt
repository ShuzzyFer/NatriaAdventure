package com.shuzzy.natriaadventure

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shuzzy.natriaadventure.AppState.archery
import com.shuzzy.natriaadventure.AppState.archeryWon
import com.shuzzy.natriaadventure.AppState.fought

class GameScreen : AppCompatActivity() {

    lateinit var image: ImageView
    lateinit var text: TextView
    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var achievManager: AchievManager

    private lateinit var story: Story

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_screen)
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        achievManager = AchievManager()
        achievManager.saveAchievement("First time", this@GameScreen)

        // Привязка элементов интерфейса
        image = findViewById(R.id.ImageView)
        text = findViewById(R.id.MainText)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        story = Story(this, this)

        // Запускаем начальную точку истории
        story.StartingPoint()

        // Устанавливаем видимость кнопок, которые не используются в начале
        button3.visibility = View.INVISIBLE
        button4.visibility = View.INVISIBLE
    }

    fun resetButtonsVisibility() {
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button3.visibility = View.VISIBLE
        button4.visibility = View.VISIBLE
    }
}