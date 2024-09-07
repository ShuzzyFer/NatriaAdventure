package com.shuzzy.natriaadventure

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class Story(private val context: Context, private val gameScreen: GameScreen) {

    fun StartingPoint() {
        gameScreen.text.setText(readAssetText("StartingStory"))
    }

    // Метод для чтения текста из ассетов
    private fun readAssetText(fileName: String): String {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}