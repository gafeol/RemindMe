package com.example.gafeol.remindme

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.db.*
import java.util.*


class Menu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)


        database.use {
            createTable("WordTable", true,
                "word" to TEXT + PRIMARY_KEY + UNIQUE,
                "translation" to TEXT)
        }


        updateList()


        fab.setOnClickListener { _ ->
            val addWordIntent = Intent(this, AddWord::class.java)
            startActivity(addWordIntent)
        }

        /*
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(findAll().toTypedArray())

        recyclerView = findViewById<RecyclerView>(R.id.wordsListRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
        */
    }

    fun updateList() {
        val pair = findAll()
        val wordsTextView: TextView = findViewById(R.id.wordsTextView)
        var wordsString = ""
        for (line in pair){
            wordsString += line
        }
        wordsTextView.setText(wordsString)

    }

    fun findAll() : ArrayList<String> = database.use {
        val notes = ArrayList<String>()

        select("WordTable", "word", "translation")
            .parseList(object: MapRowParser<List<String>>{
                override fun parseRow(columns: Map<String, Any?>): List<String> {
                    val word = columns.getValue("word")
                    val translation = columns.getValue("translation")

                    val note = word.toString() +  " = " + translation.toString() + "\n"
                    notes.add(note)

                    return notes
                }
            })

        notes
    }

    fun resetDatabase(view: View) {
        val numRowsDeleted = database.use {
            delete("WordTable", null, null)
        }
        val message: String = ("DELETED: "+numRowsDeleted.toString()+" rows")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        updateList()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

