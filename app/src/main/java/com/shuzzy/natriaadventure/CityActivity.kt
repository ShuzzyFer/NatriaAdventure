package com.shuzzy.natriaadventure

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shuzzy.natriaadventure.AppState.archery
import com.shuzzy.natriaadventure.AppState.archeryWon

class CityActivity : AppCompatActivity() {

    private lateinit var targetTimeTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var mainTextView: TextView
    private var targetTime: Long = 0
    private var isPressing: Boolean = false
    private var pressStartTime: Long = 0
    private var timer: CountDownTimer? = null

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        // Инициализация текстовых полей
        targetTimeTextView = findViewById(R.id.targetTimeTextView)
        timerTextView = findViewById(R.id.timerTextView)
        mainTextView = findViewById(R.id.MainText)
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }



        // Устанавливаем начальные инструкции
        mainTextView.text = "Вы пришли в тир города Нирон. Удерживайте палец на экране ровно в течение указанного времени, чтобы попасть в цель."

        // Генерация случайного времени удержания от 5 до 5.5 секунд
        targetTime = (5000..5500).random().toLong()
        targetTimeTextView.text = "Удерживайте в течение ${targetTime / 1000.0} секунд"

        // Инициализация области, реагирующей на нажатие
        val screen = findViewById<View>(R.id.main)

        screen.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isPressing) {
                        isPressing = true
                        pressStartTime = System.currentTimeMillis()
                        startTimer(targetTime)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isPressing = false
                    stopTimer()
                    val pressDuration = System.currentTimeMillis() - pressStartTime
                    checkPressDuration(pressDuration)
                }
            }
            true
        }
    }

    private fun startTimer(duration: Long) {
        // Отображаем начальное значение таймера
        timerTextView.visibility = View.VISIBLE
        timerTextView.text = (duration / 1000.0).toString()

        timer = object : CountDownTimer(duration, 100) {
            override fun onTick(millisUntilFinished: Long) {
                // Обновление таймера в интерфейсе
                timerTextView.text = (millisUntilFinished / 1000.0).toString()
            }

            override fun onFinish() {
                // Таймер закончился, проверяем результат
                checkPressDuration(0)
            }
        }.start()
    }

    private fun stopTimer() {
        // Скрываем таймер и останавливаем обратный отсчет
        timer?.cancel()
        timerTextView.visibility = View.GONE
    }

    private fun checkPressDuration(duration: Long) {
        if (duration in (targetTime - 500)..(targetTime + 500)) {
            // Успех, возвращаемся на GameScreen
            Toast.makeText(this, "Успех! Возвращаемся в игру.", Toast.LENGTH_SHORT).show()
            archery=true
            val intent = Intent(this@CityActivity, GameScreen::class.java)
            startActivity(intent)
            finish()
        } else {
            // Проигрыш, если удержание слишком короткое или слишком длинное
            if (duration > targetTime) {
                Toast.makeText(this, "Неудача! Таймер истек.", Toast.LENGTH_SHORT).show()
                archery = true
                archeryWon = false
                val intent = Intent(this@CityActivity, GameScreen::class.java)
                startActivity(intent)
                finish()
            } else if (targetTime - duration > 500) {
                Toast.makeText(this, "Неудача! Осталось больше полусекунды.", Toast.LENGTH_SHORT).show()
                archery = true
                archeryWon = false
                val intent = Intent(this@CityActivity, GameScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}