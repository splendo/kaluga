/*
Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.splendo.kaluga.keyboard

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.BeforeTest

class MockKeyboardTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
        const val INTERVAL = 100L
    }

    var activity:TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }


    @Test
    fun testShowKeyboard() = runBlockingTest {
        val keyboardManager = KeyboardManagerBuilder(activity!!).create()
        val keyboardHostingView = activity!!.textView

        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {

            keyboardManager.show(keyboardHostingView)
            validateKeyboard(true)

            keyboardManager.hide()
            validateKeyboard(false)
        }
        Unit
    }

    private suspend fun validateKeyboard(expected: Boolean, timeout: Long = DEFAULT_TIMEOUT): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var totalDelay = 0L
        while (totalDelay < timeout && inputMethodManager.isAcceptingText != expected) {
            totalDelay += INTERVAL
            delay(INTERVAL)
        }
        return inputMethodManager.isAcceptingText == expected
    }
}
