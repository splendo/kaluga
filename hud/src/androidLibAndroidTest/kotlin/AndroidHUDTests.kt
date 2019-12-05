package com.splendo.kaluga.hud

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import kotlin.test.Test
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

class AndroidHUDTests {

    @get:Rule
    var activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
    }

    @Test
    fun builderInitializer() = runBlockingTest {
        CoroutineScope(Dispatchers.Main).launch {
            val indicator = AndroidHUD
                .Builder(activityRule.activity)
                .create()
            assertNotNull(indicator)
        }
    }

    @Test
    fun indicatorShow() = runBlockingTest {
        CoroutineScope(Dispatchers.Main).launch {
            AndroidHUD
                .Builder(activityRule.activity)
                .create()
            device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT)
        }
    }

    @Test
    fun indicatorDismiss() = runBlockingTest {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            val indicator = AndroidHUD
                .Builder(activityRule.activity)
                .create()
            device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT)
            indicator.dismiss()
            assertTrue(device.wait(Until.gone(By.text("Loading...")), DEFAULT_TIMEOUT))
        }
    }
}
