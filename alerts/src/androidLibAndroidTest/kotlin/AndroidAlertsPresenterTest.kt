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

package com.splendo.kaluga.alerts

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.AlertsPresenterTests
import kotlinx.coroutines.CompletableDeferred
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.time.Duration

class AndroidAlertsPresenterTest : AlertsPresenterTests() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    var activity:TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }

    override val builder get() = activity!!.viewModel.alertBuilder

    companion object {
        const val DEFAULT_TIMEOUT = 5_000L
    }

    @Test
    fun testBuilderReuse() = runBlocking {

        launch(Dispatchers.Main) {
            builder.buildAlert(MainScope()) {
                setTitle("Test")
                setPositiveButton("OK")
            }.show()
        }

        device.wait(Until.findObject(By.text("Test")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Test")), DEFAULT_TIMEOUT))

        launch(Dispatchers.Main) {
            builder.buildAlert(MainScope()) {
                setTitle("Hello")
                setNegativeButton("CANCEL") // sometimes this ends up uppercased in the dialog
            }.show()
        }

        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testConcurrentBuilders() = runBlocking {
        val alerts = Array(10) { CompletableDeferred<BaseAlertPresenter>() }

        (0..9).forEach { i ->
            launch(Dispatchers.Default) {
                alerts[i].complete(
                    builder.buildAlert(MainScope()) {
                        setTitle("Alert$i")
                        setPositiveButton("OK$i")
                    }
                )
            }
        }
        (0..9).forEach { i ->
            launch(Dispatchers.Main) {
                alerts[i].await().show()
            }
            device.wait(Until.findObject(By.text("Alert$i")), DEFAULT_TIMEOUT)
            device.wait(Until.findObject(By.text("OK$i")), DEFAULT_TIMEOUT).click()
            assertTrue(device.wait(Until.gone(By.text("Alert$i")), DEFAULT_TIMEOUT))
        }
    }

    @Test
    fun testAlertShow() = runBlocking {
        launch(Dispatchers.Main) {
            builder.buildAlert(MainScope()) {
                setTitle("Hello")
                setPositiveButton("OK")
            }.show()
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testAlertFlowWithCoroutines() = runBlocking {
        launch(Dispatchers.Main) {
            val action = Alert.Action("OK")
            val presenter = builder.buildAlert(MainScope()) {
                setTitle("Hello")
                addActions(listOf(action))
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(result, action)
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }

    @Test
    fun rotateActivity() = runBlocking {
        val job = launch(Dispatchers.Main) {
            val presenter = builder.buildAlert(this) {
                setTitle("Hello")
                setPositiveButton("OK")
                setNegativeButton("Cancel")
            }

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)

        // Rotate screen
        device.setOrientationLeft()
        // HUD should be on screen
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)

        device.setOrientationNatural()

        job.cancel()
        // Finally should be gone
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }
}
