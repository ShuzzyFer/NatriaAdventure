package com.shuzzy.natriaadventure

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var login_button: Button
    private lateinit var login_email: EditText
    private lateinit var login_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        login_button = findViewById(R.id.login_button)
        login_email = findViewById(R.id.login_email)
        login_password = findViewById(R.id.login_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        login_button.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Успешная авторизация, можно перейти на другой экран
                            Toast.makeText(applicationContext, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, TitleScreen::class.java) // Переход в окно достижений
                            startActivity(intent)
                            finish()
                        } else {
                            // Ошибка при авторизации
                            Toast.makeText(applicationContext, "Email или пароль введены неправильно", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    fun onTextClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}