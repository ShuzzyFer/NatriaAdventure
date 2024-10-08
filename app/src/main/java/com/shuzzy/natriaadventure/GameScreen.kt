package com.shuzzy.natriaadventure

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_screen)

        achievManager = AchievManager()
        achievManager.saveAchievement("First time", this@GameScreen)
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
        story.StartingPoint()

        if(fought) {
            button2.visibility = View.INVISIBLE
        }
    }
}