package com.splendo.kaluga.loadingIndicator

import android.app.Dialog
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import kotlinx.coroutines.*
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

class AndroidLoadingIndicatorTests {

    @get:Rule
    var activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
    }

    @Test
    fun builderMissingViewException() {

        assertFailsWith<IllegalArgumentException> {
            AndroidLoadingIndicator
                .Builder()
                .create()
        }
    }

    @Test
    fun builderInitializer() = runBlocking {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            val view = Dialog(activityRule.activity)
            val indicator = AndroidLoadingIndicator
                .Builder()
                .setView(view)
                .create()
            assertNotNull(indicator)
        }
        Unit
    }

    @Test
    fun indicatorShow() = runBlocking {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            val view = Dialog(activityRule.activity)
            view.setTitle("HUD")
            AndroidLoadingIndicator
                .Builder()
                .setView(view)
                .create()
                .present()
            device.wait(Until.findObject(By.text("HUD")), DEFAULT_TIMEOUT)
        }
        Unit
    }

    @Test
    fun indicatorDismiss() = runBlocking {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            val view = Dialog(activityRule.activity)
            view.setTitle("HUD")
            val indicator = AndroidLoadingIndicator
                .Builder()
                .setView(view)
                .create()
            indicator.present()
            device.wait(Until.findObject(By.text("HUD")), DEFAULT_TIMEOUT)
            indicator.dismiss()
            assertTrue(device.wait(Until.gone(By.text("HUD")), DEFAULT_TIMEOUT))
        }
        Unit
    }
}
