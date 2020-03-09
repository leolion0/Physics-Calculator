package com.example.physicscalculator

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        eval_button.setOnClickListener{
            var one : Float =  editText.text.toString().toFloat()
            var two : Float =  editText2.text.toString().toFloat()
            var ans = one * 9.81F * two
            answer_textView.text = ans.toString()
        }
    }


}

class Factor {
    var symbol : Char = '&'
    var number : Float = 0F
    var displaySymbol : Boolean = true
    var isConst : Boolean = false
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
class Equation {
    var library: String = ""
    var eqName : String = ""
    var factors = mutableListOf<Factor>()
    var tokenization = mutableListOf<Any>()

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




















