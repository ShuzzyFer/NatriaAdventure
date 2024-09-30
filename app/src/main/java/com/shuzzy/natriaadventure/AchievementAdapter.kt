package com.shuzzy.natriaadventure

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AchievementAdapter(context: Context, private val achievements: List<Achievement>) :
    ArrayAdapter<Achievement>(context, 0, achievements) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.achievement_item, parent, false)

        val achievement = achievements[position]

        val imageView = view.findViewById<ImageView>(R.id.achievementImage)
        val nameTextView = view.findViewById<TextView>(R.id.achievementNumber)
        val titleTextView = view.findViewById<TextView>(R.id.achievementTitle)

        // Заполняем вид данными
        imageView.setImageResource(achievement.imageResId)
        nameTextView.text = achievement.name
        titleTextView.text = achievement.title

        return view
    }
}