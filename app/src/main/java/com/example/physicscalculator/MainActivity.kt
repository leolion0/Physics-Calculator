package com.example.physicscalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.Serializable
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editLinearLayout = findViewById<LinearLayout>(R.id.editTextLinearLayout)
        setSupportActionBar(toolbar)


        var eq : Equation? = intent.getSerializableExtra("EQUATION") as? Equation


        val factEditTexts: MutableList<EditText> = mutableListOf()
        val demoFactors = mutableListOf<Oper>()


//        val demo: Equation = Equation("Physics", "Potential Energy Gravity", demoFactors, mutableListOf<Char>('p', '=', 'm', 'g', 'h'))

        if (eq != null) {
            factEditTexts.add(evalDynamicEditText(eq.answer, forceDisable = true))
            var equalsSignET = EditText(this)
            equalsSignET.hint = "="
            equalsSignET.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            equalsSignET.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            equalsSignET.setPadding(20, 100, 20, 20)
            equalsSignET.isEnabled = false
            factEditTexts.add(equalsSignET)
            editLinearLayout?.addView(factEditTexts[0])
            editLinearLayout?.addView(factEditTexts[1])


            var counter = 2
            for (i in eq.inFixOpers) {
                factEditTexts.add(evalDynamicEditText(i))
                editLinearLayout?.addView(factEditTexts[counter])
                counter++
            }
            counter = 0
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
            fun reEntryToast(msg: String, context: Context = applicationContext, len: Int = Toast.LENGTH_SHORT){
                val toast = Toast.makeText(context, msg, len)
                toast.show()
            }
            var counter = 2
            if (eq != null) {
                for (i in eq.inFixOpers) {
                    try {
                        var input = factEditTexts[counter].text.toString()
                        input = input.replace("\\s".toRegex(), "")
                        if (factEditTexts[counter].isEnabled == false) {
                            counter++
                            continue
                        }
                        else
                            i.number = input.toFloat()
                    }
                    catch (ex:Exception){
                        print(ex.message)
                        reEntryToast("Only digits and decimals are allowed")
                        return@setOnClickListener
                    }
                    counter++
                }
                    val answer = eq.eval()

                    answerTextView.text = eq.eval().toString()

            }
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

    fun evalDynamicEditText( inOp : Oper, forceDisable: Boolean = false): EditText {
        val sym = inOp
        val temp = EditText(this)
        if (inOp.typeOfFactor == Oper.SymbolType.VARIABLE){
                temp.hint = inOp.symbol.toString()
                temp.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                temp.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                temp.setPadding(20, 100, 20, 20)
        }
        else if (inOp.typeOfFactor == Oper.SymbolType.CONST){
            temp.hint = inOp.symbol.toString()
            temp.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            temp.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            temp.setPadding(20, 100, 20, 20)
            temp.isEnabled = false
        }
        else if (inOp.typeOfFactor == Oper.SymbolType.OPERATOR){
            temp.hint = inOp.symbol.toString()
            temp.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            temp.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            temp.setPadding(20, 100, 20, 20)
            temp.isEnabled = false
        }
        if(forceDisable)
            temp.isEnabled = false
        return temp
    }




}




class Oper constructor(symbol:String, inType: SymbolType, number:Float?, displaySymbol: Boolean ): Serializable{

    val symbol: String = symbol
    var number: Float? = number
    val displaySymbol: Boolean = displaySymbol
    val typeOfFactor: SymbolType = inType



    enum class SymbolType(val inType: Int){
        VARIABLE(1),
        CONST(2),
        OPERATOR(3)
    }
}

class StackWithList<T>{
    val elements: MutableList<T> = mutableListOf()

    fun isEmpty() = elements.isEmpty()

    fun size() = elements.size

    fun push(item: T) = elements.add(item)

    fun pop() : T? {
        val item = elements.lastOrNull()
        if (!isEmpty()){
            elements.removeAt(elements.size -1)
        }
        return item
    }
    fun top() : T? = elements.lastOrNull()

    override fun toString(): String = elements.toString()
}
class Equation constructor(eqName: String, answer: Oper, opers: List<Oper>): Serializable{



    var eqName : String = eqName
    val answer = answer
    var inFixOpers: List<Oper> = opers
    var postFixOpers: List<Oper>
   // private var inToPostMap: Map<Oper, Oper>

    init{
        postFixOpers = inFixListToPostFix(opers)
       // inToPostMap = mapInFixToPostFix()/
    }

    private fun mapInFixToPostFix(): Map<Oper, Oper> {
        var workMap =  mutableMapOf<Oper, Oper>()
        for(i in inFixOpers){
            for(p in postFixOpers)
                if(i == p){
                    workMap[i] = p
                    break
                }
        }
        return workMap.toMap()
    }

    fun inFixListToPostFix(s: List<Oper>): List<Oper> {
        fun prec(oper: Oper): Int {
            return when(oper.symbol){
                "^" -> 3
                "*" -> 2
                "/" -> 2
                "+" -> 1
                "-" -> 1
                else -> -1
            }
        }

        var st = StackWithList<Oper>()
        val endOfStack = Oper("END_OF_STACK", Oper.SymbolType.OPERATOR, null, false)
        st.push(endOfStack);
        var l: Int = s.size;
        var ns = mutableListOf<Oper>()
        for(i in  0 until l)
        {
            // If the scanned character is an operand, add it to output string.
            if(s[i].typeOfFactor != Oper.SymbolType.OPERATOR )
                ns.add(s[i]);

            // If the scanned character is an ‘(‘, push it to the stack.
            else if(s[i].symbol == "(")

                st.push(s[i]);

            // If the scanned character is an ‘)’, pop and to output string from the stack
            // until an ‘(‘ is encountered.
            else if(s[i].symbol == ")")
            {
                while(st.top() != endOfStack && st.top()!!.symbol != "(")
                {
                    val c = st.top();
                    st.pop();
                    if (c != null) {
                        ns.add(c)
                    }
                }
                if(st.top()!!.symbol == "(")
                {
                    val c = st.top();
                    st.pop()
                }
            }

            //If an operator is scanned
            else{
                while(st.top() != endOfStack && prec(s[i]) <= prec(st.top()!!))
                {
                    val c = st.top();
                    st.pop();
                    if (c != null) {
                        ns.add(c)
                    }
                }
                st.push(s[i]);
            }

        }
        //Pop all the remaining elements from the stack
        while(st.top() != endOfStack)
        {
            val c = st.top();
            st.pop();
            if (c != null) {
                ns.add(c)
            }
        }
        return ns

    }

    fun eval(): Float{
        postFixOpers = inFixListToPostFix(inFixOpers)
        val ERROR = -1F

        var ops = mutableListOf<Char>('*', '/', '+', '-')
        var workStack = StackWithList<Float>()
        var counter = 0
        while (counter < postFixOpers.size){
            var nextEl =  postFixOpers[counter]
            var nextElSymbol = postFixOpers[counter].symbol
            if (nextEl.typeOfFactor != Oper.SymbolType.OPERATOR) {
                try{
                    var theFact : Oper = nextEl
                    theFact.number?.let { workStack.push(it) }
                }
                catch(ex:Exception){
                    throw ex
                }

            }
            else {
                var a = workStack.pop()
                var b = workStack.pop()
                if (a !is Float || b !is Float){
                    throw Exception("A or B is not a float")
                    return ERROR
                }
                var value = 0F
                when (nextElSymbol) {
                    "*" -> value = b * a
                    "/" -> value = b / a
                    "+" -> value = b + a
                    "-" -> value = b - a
                    "^" -> value = b.pow(a)
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
            return ERROR
        }
    }


}
























