package com.shuzzy.natriaadventure

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AchievManager {

    fun saveAchievement(str: String, context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            // Обновляем данные достижения в базе данных
            database.child("Users").child(userId).child("achievements").child(str).setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}