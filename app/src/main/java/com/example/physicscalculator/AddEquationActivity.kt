package com.example.physicscalculator

import android.content.Context
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_first.*

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.KeyStore

class AddEquationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_equation)
        val editLinearLayout = findViewById<LinearLayout>(R.id.editTextLinearLayout)

        val factEditTexts: MutableList<EditText> = mutableListOf()
        factEditTexts.add(EditText(this))

        factEditTexts[0].hint = "Enter first Factor here"
        var etCounter = 1
        editLinearLayout?.addView(factEditTexts[0])

        addFactorButton.setOnClickListener{
            factEditTexts.add(createDynamicEditText("Next factor or = "))

            editLinearLayout?.addView(factEditTexts[etCounter])
            etCounter++
        }

        saveEquationButton.setOnClickListener {
            val filename = "physics.lib"
            val factList = mutableListOf<Factor>()
            for (i in factEditTexts){
                val txt = i.text.toString()
                //TODO: add discerning between constant
                factList.add(Factor(txt.toString()[0], 0f, true, false))
            }
            //TODO: add field for equation name
            //TODO: fix tokenzation
            val newEQ = Equation("physics", "Potential Energy Gravity", factList, mutableListOf<Char>(' '))

            var gson = Gson()
            var jsonString:String = gson.toJson(newEQ)


            try{
                val fos = openFileOutput(filename, Context.MODE_PRIVATE)
                fos.write(jsonString.toByteArray())
            }
            catch (ex:Exception){
                print(ex.message)
            }

            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(filename)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            var inJsonString = stringBuilder.toString()
            print(inJsonString)



        }



    }

    fun createDynamicEditText(hint: String): EditText {
        val hint = hint
        val temp = EditText(this)
        temp.hint = hint
        temp.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        temp.setPadding(20, 20, 20, 20)
        return temp

    }


}
