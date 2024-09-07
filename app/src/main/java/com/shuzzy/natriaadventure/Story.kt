package com.shuzzy.natriaadventure

import android.content.Context
import android.view.View
import java.io.BufferedReader
import java.io.InputStreamReader

class Story(private val context: Context, private val gameScreen: GameScreen) {

    fun StartingPoint() {
        gameScreen.text.setText(readAssetText("StartingStory"))

        gameScreen.button1.setText("Пойти в город Нирон")
        gameScreen.button2.setText("Осмотреть окрестности")
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

    }

    // Метод для чтения текста из ассетов
    private fun readAssetText(fileName: String): String {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}