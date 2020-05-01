package com.example.physicscalculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.FileOutputStream
import java.io.OutputStreamWriter

import android.content.Context
import android.content.Intent

import android.widget.*
import com.google.gson.Gson

import java.io.*

class EquationsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equations_list)

        val listView = findViewById<ListView>(R.id.equationsListView)

        listView.adapter = MyCustomAdapter(this)

        listView.setOnItemClickListener { parent, view, position, id ->


                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra( "EQUATION" , (listView.adapter as MyCustomAdapter).equationsList[position])
                startActivity(intent)
        }


    }
    private  class MyCustomAdapter constructor(context: Context) : BaseAdapter() {
        private var context = context
        var equationsList: MutableList<Equation> = mutableListOf()
        var eqIt: Iterator<Equation>
        init{
            var gson = Gson()
            var fileNameList = mutableListOf<String>()



            print(fileNameList)

            File(context.applicationContext.filesDir.toString()).walk().forEach {
                if(it.extension == "eq"){
                    var fileInputStream: FileInputStream = context.applicationContext.openFileInput(it.name.toString())
                    var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                    val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                    val stringBuilder: StringBuilder = StringBuilder()
                    var text: String? = null
                    while ({ text = bufferedReader.readLine(); text }() != null) {
                        stringBuilder.append(text)
                    }
                    var inJsonString = stringBuilder.toString()
                    this.equationsList.add(gson.fromJson(inJsonString, Equation::class.java))
                }
            }
            eqIt = equationsList.listIterator().asSequence().iterator()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val textView = TextView(context)
                textView.text = equationsList[position].eqName
                return textView


        }

        override fun getItem(position: Int): Any {

            return "TEST STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()

        }

        override fun getCount(): Int {
            return equationsList.size
        }

    }
}
