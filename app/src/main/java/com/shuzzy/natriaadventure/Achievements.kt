package com.shuzzy.natriaadventure

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class Achievement(
    val name: String,
    val imageResId: Int, // ID ресурса изображения
    val title: String // Название или условие получения
)

class Achievements : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        // Инициализация списка пар для достижений
        val stringPairs: MutableList<Pair<String, String>> = mutableListOf(
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???")
        )

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val achievementsMap = dataSnapshot.value as Map<String, Boolean>
                    if (achievementsMap.containsKey("Green lose")) {
                        stringPairs[0] = Pair("Зеленое поражение", "Проиграть битву с гоблином")
                    }
                    if(achievementsMap.containsKey("Easy win")) {
                        stringPairs[1] = Pair("Легкая победа", "Победить гоблина с битве")
                    }
                    if(achievementsMap.containsKey("First time")) {
                        stringPairs[2] = Pair("Первый раз", "Впервые начните свое путешествие")
                    }
                }

                // Создание списка достижений после загрузки данных
                val achievements = listOf(
                    Achievement(stringPairs[0].first, R.drawable.achievement1_icon, stringPairs[0].second),
                    Achievement(stringPairs[1].first, R.drawable.achievement2_icon, stringPairs[1].second),
                    Achievement(stringPairs[2].first, R.drawable.achievement3_icon, stringPairs[2].second)

                    // Добавьте другие достижения, если нужно
                )

                // Установка адаптера для ListView
                val listView = findViewById<ListView>(R.id.achievementsListView)
                val adapter = AchievementAdapter(this, achievements)
                listView.adapter = adapter

            }.addOnFailureListener {
                Toast.makeText(this, "Ошибка загрузки достижений", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
        }
    }
}