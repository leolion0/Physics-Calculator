package com.example.physicscalculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_equations_list.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_first.*

import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.KeyStore

class EquationsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equations_list)

        val listView = findViewById<ListView>(R.id.equationsListView)

        listView.adapter = MyCustomAdapter(this)

        listView.setOnItemClickListener { parent, view, position, id ->
            if (position == 0){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                intent.putExtra("Eq", (listView.adapter as MyCustomAdapter).exampleEq)
        }

        }






    }
    private  class MyCustomAdapter constructor(context: Context) : BaseAdapter() {
        private var context = context
         var exampleEq: Equation
        init{
            var gson = Gson()
            val filename = "physics.lib"

            var fileInputStream: FileInputStream = context.applicationContext.openFileInput(filename)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            var inJsonString = stringBuilder.toString()
            this.exampleEq = gson.fromJson(inJsonString, Equation::class.java)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val textView = TextView(context)
            textView.text = exampleEq.eqName
            return textView
        }

        override fun getItem(position: Int): Any {

            return "TEST STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()

        }

        override fun getCount(): Int {
            try{
                return 1
            }
            catch(e:Exception){
                print(e)
            }
            return 1



        }

    }
}
