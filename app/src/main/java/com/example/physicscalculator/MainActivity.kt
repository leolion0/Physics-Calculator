package com.example.physicscalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.Serializable
import java.lang.Exception
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editLinearLayout = findViewById<LinearLayout>(R.id.editTextLinearLayout)
        setSupportActionBar(toolbar)

        val intent = intent
        val demo = intent.getSerializableExtra("Eq") as? Equation

        val factEditTexts: MutableList<EditText> = mutableListOf()
        val demoFactors = mutableListOf<Factor>()
        demoFactors.add(Factor('P', 0f, true, true))
        demoFactors.add(Factor('m', 0f, true, false))
        demoFactors.add(Factor('g', 9.81f, true, true))
        demoFactors.add(Factor('h', 0f, true, false ))

//        val demo: Equation = Equation("Physics", "Potential Energy Gravity", demoFactors, mutableListOf<Char>('p', '=', 'm', 'g', 'h'))
        var counter = 0
        if (demo != null) {
            for (i in demo.factors) {
                factEditTexts.add(EditText(this))
                factEditTexts[counter].hint = i.symbol.toString()
                factEditTexts[counter].layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                factEditTexts[counter].layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                factEditTexts[counter].setPadding(20, 100, 20, 20)

                editLinearLayout?.addView(factEditTexts[counter])
                counter++
            }
        }


//        // Create EditText
//        val editText = EditText(this)
//        editText.setHint("Enter something")
//        editText.layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT)
//        editText.setPadding(20, 20, 20, 20)
//
//        // Add EditText to LinearLayout
//        editLinearLayout?.addView(editText)

        eval_button.setOnClickListener{
            var one : Float =  editText.text.toString().toFloat()
            var two : Float =  editText2.text.toString().toFloat()
            var ans = one * 9.81F * two
            answer_textView.text = ans.toString()
        }
        newEQButton.setOnClickListener {
            val intent = Intent(this, AddEquationActivity::class.java)
            startActivity(intent)
        }
        equationListButton.setOnClickListener {
            val intent = Intent(this, EquationsListActivity::class.java)
            startActivity(intent)
        }

        }




}

class Factor constructor(symbol:Char, number:Float, displaySymbol: Boolean, isConst: Boolean){

    val symbol: Char = symbol
    val number: Float = number
    val displaySymbol: Boolean = displaySymbol
    val isConst: Boolean = isConst
}

class StackWithList{
    val elements: MutableList<Any> = mutableListOf()

    fun isEmpty() = elements.isEmpty()

    fun size() = elements.size

    fun push(item: Any) = elements.add(item)

    fun pop() : Any? {
        val item = elements.lastOrNull()
        if (!isEmpty()){
            elements.removeAt(elements.size -1)
        }
        return item
    }
    fun peek() : Any? = elements.lastOrNull()

    override fun toString(): String = elements.toString()
}
class Equation constructor(library: String, eqName: String, factors: MutableList<Factor>, tokenization: MutableList<Char>): Serializable{


    var library: String = library
    var eqName : String = eqName
    var factors = factors
    var tokenization = tokenization

    fun eval(): Float{
        // assume first term and equals are skipped
        var ops = mutableListOf<Char>('*', '/', '+', '-')
        var workStack = StackWithList()
        var counter = 2
        while (counter < tokenization.size){
            var nextEl = tokenization[counter]
            if (!(nextEl == '*' || nextEl == '/' ||nextEl == '+' ||nextEl == '-')) {
                try{
                    var theFact : Factor = nextEl as Factor
                    workStack.push(theFact.number)
                }
                finally {
                    throw Exception("Element in stack was not a factor")
                    return -1F
                }

            }
            else {
                var a = workStack.pop()
                var b = workStack.pop()
                if (a !is Float || b !is Float){
                    throw Exception("A or B is not a float")
                    return -1F
                }
                var value = 0F
                when (nextEl) {
                    '*' -> value = b * a
                    '/' -> value = b / a
                    '+' -> value = b + a
                    '-' -> value = b - a
                    else -> throw IllegalArgumentException()
                }
                workStack.push(value)
            }
            counter++
        }
        var res = workStack.pop()
        if (res is Float){
            return res
        }
        else{
            throw Exception("Final answer not float")
            return -1F
        }
    }
}




















