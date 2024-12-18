package com.shuzzy.natriaadventure

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    init {
        System.loadLibrary("native-lib")
    }

    private external fun registerUserNative(email: String, password: String): Boolean

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        registerButton = findViewById(R.id.register_button)
        emailEditText = findViewById(R.id.register_email)
        passwordEditText = findViewById(R.id.register_password)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userInfo = hashMapOf<String, String>()
                            userInfo["email"] = email

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                .setValue(userInfo)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        startActivity(Intent(this, TitleScreen::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, "Ошибка при сохранении данных: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(applicationContext, "Ошибка регистрации: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}