package com.example.physicscalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.KeyStore

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddEquationFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        view.findViewById<Button>(R.id.saveEquationButton).setOnClickListener {




            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}
