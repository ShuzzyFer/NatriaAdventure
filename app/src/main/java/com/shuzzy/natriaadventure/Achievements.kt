package com.shuzzy.natriaadventure

import android.content.pm.ActivityInfo
import android.content.res.Configuration
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
    val imageResId: Int,
    val title: String
)

class Achievements : AppCompatActivity() {

    private fun isTablet(): Boolean {
        val screenLayout = resources.configuration.screenLayout
        val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        if (!isTablet()) {
            // Если это не планшет, то блокируем ориентацию
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val stringPairs: MutableList<Pair<String, String>> = mutableListOf(
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
            Pair("???", "???"),
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
                    if(achievementsMap.containsKey("Bow")) {
                        stringPairs[3] = Pair("Стрельба из лука", "Покажите свои навыки стрельбы из лука в тире у дороги")
                    }
                    if(achievementsMap.containsKey("Help")) {
                        stringPairs[4] = Pair("Помощь стражникам", "Помогите стражникам в беде")
                    }
                    if(achievementsMap.containsKey("Amulet")) {
                        stringPairs[5] = Pair("Амулет", "Получите странный амулет")
                    }
                    if(achievementsMap.containsKey("Stories")) {
                        stringPairs[6] = Pair("Невыдуманные истории", "Послушайте истории в таверне")
                    }
                    if(achievementsMap.containsKey("Player")) {
                        stringPairs[7] = Pair("Игрок", "Сыграйте в азартные игры в таверне")
                    }
                    if(achievementsMap.containsKey("Merc")) {
                        stringPairs[8] = Pair("Наемник наемника", "Примите задание наемника в таверне")
                    }
                    if(achievementsMap.containsKey("Art")) {
                        stringPairs[9] = Pair("Артефакт", "Исследуйте подземелье и найдите артефакт")
                    }
                }

                val achievements = listOf(
                    Achievement(stringPairs[0].first, R.drawable.achievement1_icon, stringPairs[0].second),
                    Achievement(stringPairs[1].first, R.drawable.achievement2_icon, stringPairs[1].second),
                    Achievement(stringPairs[2].first, R.drawable.achievement3_icon, stringPairs[2].second),
                    Achievement(stringPairs[3].first, R.drawable.achievement4_icon, stringPairs[3].second),
                    Achievement(stringPairs[4].first, R.drawable.achievement5_icon, stringPairs[4].second),
                    Achievement(stringPairs[5].first, R.drawable.achievement6_icon, stringPairs[5].second),
                    Achievement(stringPairs[6].first, R.drawable.achievement7_icon, stringPairs[6].second),
                    Achievement(stringPairs[7].first, R.drawable.achievement8_icon, stringPairs[7].second),
                    Achievement(stringPairs[8].first, R.drawable.achievement9_icon, stringPairs[8].second),
                    Achievement(stringPairs[9].first, R.drawable.achievement10_icon, stringPairs[9].second)
                )
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