package com.shuzzy.natriaadventure

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class TitleScreen : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var achievementsButton: Button
    private lateinit var authorizationButton: Button

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_title_screen)
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        if(FirebaseAuth.getInstance().currentUser==null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
            finish()
            return
        }

        startButton = findViewById(R.id.startButton)
        achievementsButton = findViewById(R.id.achievementsButton)
        authorizationButton = findViewById(R.id.authorizationButton)

        startButton.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            startActivity(intent)
        }

        achievementsButton.setOnClickListener {
            val intent = Intent(this, Achievements::class.java)
            startActivity(intent)
        }

        authorizationButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}