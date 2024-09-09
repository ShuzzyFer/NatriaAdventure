package com.shuzzy.natriaadventure

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shuzzy.natriaadventure.AppState.fought

class Fight : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector
    private var SwipeNumber =5
    private var swipes = 0
    private var hasSwiped = false
    private val swipeTimeout = 12000L // 5 секунд

    private lateinit var timerTextView: TextView
    private var remainingTime = swipeTimeout // 5 секунд
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fight)

        timerTextView = findViewById(R.id.Timer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Инициализация GestureDetector
        gestureDetector = GestureDetector(this, SwipeGestureListener())

        // Запуск таймера на 5 секунд
        Handler(Looper.getMainLooper()).postDelayed({
            if (!hasSwiped) {
                // Действие, если свайпа не было
                showFailMessage()
            }
        }, swipeTimeout)

        startTimer()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            swipes++
            if (swipes>=SwipeNumber) {
                swipes=0
                hasSwiped = true
                fought = true
                val intent = Intent(this@Fight, GameScreen::class.java)
                startActivity(intent)
                finish()
            }
            return true
        }
    }

    private fun showFailMessage() {
        Toast.makeText(this, "Вы не успели свайпнуть!", Toast.LENGTH_SHORT).show()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(swipeTimeout, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateTimerDisplay()
            }

            override fun onFinish() {
                if (!hasSwiped) {
                    // Действие, если свайпа не было
                    showFailMessage()
                }
            }
        }
        timer.start()
    }

    private fun updateTimerDisplay() {
        val seconds = (remainingTime / 1000).toInt()
        val minutes = seconds / 60
        val displaySeconds = seconds % 60
        timerTextView.text = String.format("%02d:%02d", minutes, displaySeconds)
    }
}
