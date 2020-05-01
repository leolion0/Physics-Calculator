package com.example.physicscalculator

import android.content.Context

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_equation.*

import kotlinx.android.synthetic.main.fragment_first.addFactorButton
import kotlinx.android.synthetic.main.fragment_first.saveEquationButton
import java.io.*
import java.lang.Exception

class AddEquationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_equation)
        val editLinearLayout = findViewById<LinearLayout>(R.id.editTextLinearLayout)

        val factEditTexts: MutableList<EditText> = mutableListOf()
        factEditTexts.add(EditText(this))

        factEditTexts[0].hint = "Enter Result name here"
        var etCounter = 1
        editLinearLayout?.addView(factEditTexts[0])

        addFactorButton.setOnClickListener{

            if (etCounter == 1){ //Calculator cannot perform algebraic simplification or reorganizing, answer must be alone on left side of equals sign.
                var equalsEditText = createDynamicEditText("=")
                equalsEditText.isEnabled = false
                factEditTexts.add(equalsEditText)
            }
            else{
                factEditTexts.add(createDynamicEditText("Next variable, [const = #], or operator "))
            }
            editLinearLayout?.addView(factEditTexts[etCounter])
            etCounter++
        }

        saveEquationButton.setOnClickListener {
            fun reEntryToast(msg: String,context:Context = applicationContext, len: Int = Toast.LENGTH_SHORT){
                val toast = Toast.makeText(context, msg, len)
                toast.show()
            }

            val filename = eqNameEditText.text.toString() + ".eq"
            val factorList = mutableListOf<Oper>()

            try{
                val eqName = eqNameEditText.text.toString()
                if(eqName.trim() == "")
                    throw IllegalEquationEntryField("Answer field cannot be empty")
                val answer = Oper(factEditTexts[0].text.toString(), Oper.SymbolType.VARIABLE, null, displaySymbol = true)


                for (i in 2 until factEditTexts.size){
                    val txt = factEditTexts[i].text.toString()
                    val possibleConstInfo = checkConst(txt)
                    val isConst = possibleConstInfo.isNotEmpty()
                    if (isConst){
                        factorList.add(Oper(possibleConstInfo[0] as String, Oper.SymbolType.CONST,
                            possibleConstInfo[1] as Float?,
                            displaySymbol = true))
                    }
                    else{
                        /*
                        See if operator
                            regex check for operator match
                            if  matches found > 1 :
                                Throw only one operator
                            else if matches found == 1:
                                exprList.add(match.value)
                            else:
                                it must be factor
                                regex grab A followed by 0 to 5 more A/#
                                if matches == 0:
                                    Throw invalid Factor name
                                if matches > 1:
                                    Throw Only one factor per line
                                else:
                                    exprList.add(Factor(match.value))
                         */

                        //Search for factor
                        val padTxt = "$txt "
                        val factorMatchList = regExEr(padTxt,
                            "([A-Z]|[a-z])(\\w|\\d)*\\s")
                        when{
                            factorMatchList.size > 1 ->
                                throw IllegalEquationEntryField("Only one variable per line")
                            factorMatchList.size == 1 ->
                            {
                                var match = factorMatchList[0].first
                                match = match.trim()
                                if(match.length > 6)
                                    throw IllegalEquationEntryField("Variables can only be 6 characters long")
                                else{
                                    factorList.add(Oper(match, Oper.SymbolType.VARIABLE,
                                        0F, displaySymbol = true ))
                                }

                            }

                            else -> {
                                //search for operator
                                val opMatchList = regExEr(txt, "[/*\\-+^()]")
                                when{
                                    opMatchList.size > 1 ->
                                        throw IllegalEquationEntryField("Only one operator allowed")
                                    opMatchList.size == 1 ->
                                        factorList.add(Oper(opMatchList[0].first, Oper.SymbolType.OPERATOR, number = null, displaySymbol = true))
                                    else -> throw IllegalEquationEntryField("Unknown")
                                }
                            }
                        }
                    }
                }
                val newEQ = Equation(eqName, answer, factorList)

                var gson = Gson()
                var equationJsonString:String = gson.toJson(newEQ)


                try{
                    val fos = openFileOutput(filename, Context.MODE_PRIVATE)
                    fos.write(equationJsonString.toByteArray())
                    fos.close()
                }
                catch (ex:Exception){
                    print(ex.message)
                }

                var fileInputStream: FileInputStream = openFileInput(filename)
                var inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                var inJsonString = stringBuilder.toString()
                fileInputStream.close()
            }
            catch(ex:IllegalEquationEntryField){
                when(ex.message){
                    "Unknown" ->
                        reEntryToast("Something was wrong with your equation, and it could not be " +
                            "saved. Have you followed the format guidelines?", len = Toast.LENGTH_LONG)
                        //You did something wrong, try again
                    null ->
                        true
                    else ->
                        //print out ex.message
                        reEntryToast(ex.message)
                }
            }
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

    fun checkConst(maybeConstStr: String): List<Any> {
        val regex = "\\[(.*?)\\]".toRegex()
        val match = regex.find(maybeConstStr)
        if (match != null) {
            var constExpression:String = match.value
            constExpression = constExpression.replace("[","")
            constExpression = constExpression.replace("]", "")
            val equalsIndex = constExpression.indexOf("=")
            if(equalsIndex != -1){
                try{
                    val constName:String = constExpression.substring(0 until equalsIndex).trim()
                    val constVal:Float = constExpression.substringAfter("=").trim().toFloat()
                    return listOf<Any>(constName, constVal)
                }
                catch (ex:Exception){
                    throw IllegalEquationEntryField("Const is not in correct format")
                }
            }
        }
        return emptyList()


    }

    fun regExEr(searchString: String, query: String): MutableList<Pair<String, Int>> {
        val regex = query.toRegex()

        var match =  regex.find(searchString)
        var matchValueList = mutableListOf<Pair<String, Int>>()

        while(match != null){
            matchValueList.add(Pair(match.value, match.range.first))
            match = match.next()
        }
        return matchValueList
    }
}



class IllegalEquationEntryField(message: String) : Exception(message)
