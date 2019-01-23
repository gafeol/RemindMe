package com.example.gafeol.remindme

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.db.insert

class AddWord : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
    }

    fun saveWord(view: View){
        val originalWord: TextView = findViewById(R.id.originalWord)
        val translatedWord: TextView = findViewById(R.id.translatedWord)

        val originalWordString = originalWord.text.toString()
        val translatedWordString = translatedWord.text.toString()


        val cv = ContentValues()
        cv.put("word", originalWordString)
        cv.put("translation", translatedWordString)

        database.use {
            insert(
                "WordTable",
                null,
                cv
            )
        }

        originalWord.setText("")
        translatedWord.setText("")
    }
}
