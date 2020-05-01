package com.example.physicscalculator

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.physicscalculator", appContext.packageName)
    }

    @Test
    fun evaluateEquation(){
        // Test that all values that are not constants are numbers
        // Test that answer is properly displayed
    }

    @Test
    fun addEquation(){
        // Test that no illegal characters are created
        // Test that each field only has one character
        // Test that equations aren't one character long


    }
}
