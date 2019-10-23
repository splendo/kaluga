package com.splendo.kaluga.alerts

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
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

    @get:Rule
    var activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
    }

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
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            AlertBuilder(activityRule.activity)
                .setTitle("Hello")
                .setPositiveButton("OK") { }
                .create()
                .show()
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.findObject(By.text("OK")).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
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
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.findObject(By.text("OK")).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
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
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        // On cancel we expect dialog to be dismissed
        coroutine.cancel()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }
}
