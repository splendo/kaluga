package com.splendo.kaluga.alerts

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlinx.coroutines.*
import org.junit.*
import org.junit.Test
import kotlin.test.*

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

class MockAlertsTest {

    @get:Rule var activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun testAlertBuilderExceptionNoActions() {
        assertFailsWith<IllegalArgumentException> {
            AlertBuilder(activityRule.activity)
                .setTitle("OK")
                .create()
        }
    }

    @Test
    fun testAlertBuilderExceptionNoTitleOrMessage() {
        assertFailsWith<IllegalArgumentException> {
            AlertBuilder(activityRule.activity)
                .setPositiveButton("OK") { }
                .create()
        }
    }

    @Test
    fun testAlertShow() = runBlocking {
        GlobalScope.launch(Dispatchers.Main) {
            AlertBuilder(activityRule.activity)
                .setTitle("Hello")
                .setPositiveButton("OK") { }
                .create()
                .show()
        }
        assertNotNull(device.findObject(By.text("Hello")))
        device.findObject(UiSelector().text("OK")).click()
        assertNull(device.findObject(By.text("Hello")))
    }

    @Test
    fun testAlertFlowWithCoroutines() = runBlocking {
        CoroutineScope(Dispatchers.Main).launch {
            val action = Alert.Action("OK") { }
            val presenter = AlertBuilder(activityRule.activity)
                .setTitle("Hello")
                .addActions(listOf(action))
                .create()

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(result, action)
        }
        assertNotNull(device.findObject(By.text("Hello")))
        device.findObject(UiSelector().text("OK")).click()
        assertNull(device.findObject(By.text("Hello")))
    }

    @Test
    fun testAlertFlowCancel() = runBlocking {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val action = Alert.Action("OK") { }
            val presenter = AlertBuilder(activityRule.activity)
                .setTitle("Hello")
                .addActions(listOf(action))
                .create()

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        assertNotNull(device.findObject(By.text("Hello")))
        // On cancel we expect dialog to be dismissed
        coroutine.cancel()
        assertNull(device.findObject(By.text("Hello")))
    }
}
