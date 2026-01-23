/*
Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class AndroidAlertPresenterTest : AlertPresenterTests() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    var activity: TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }

    override val builder get() = activity!!.viewModel.alertBuilder

    companion object {
        val DEFAULT_TIMEOUT = 30.seconds.inWholeMilliseconds
    }

    @Test
    fun testBuilderReuse() = runBlocking {
        val job1 = launch(Dispatchers.Main.immediate) {
            builder.buildAlert(this) {
                setTitle("Test")
                setPositiveButton("OK")
            }.show()
        }

        device.wait(Until.findObject(By.text("Test")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Test")), DEFAULT_TIMEOUT))
        job1.cancel()

        val job2 = launch(Dispatchers.Main.immediate) {
            builder.buildAlert(this) {
                setTitle("Hello")
                setNegativeButton("CANCEL") // sometimes this ends up uppercased in the dialog
            }.show()
        }

        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
        job2.cancel()
    }

    @Test
    fun testConcurrentBuilders() = runBlocking {
        val alerts = Array(10) { CompletableDeferred<BaseAlertPresenter>() }

        val jobs = (0..9).map { i ->
            launch(Dispatchers.Default) {
                alerts[i].complete(
                    builder.buildAlert(CoroutineScope(coroutineContext + Dispatchers.Main.immediate)) {
                        setTitle("Alert$i")
                        setPositiveButton("OK$i")
                    },
                )
            }
        }
        (0..9).forEach { i ->
            val job = launch(Dispatchers.Main.immediate) {
                alerts[i].await().show()
            }
            device.wait(Until.findObject(By.text("Alert$i")), DEFAULT_TIMEOUT)
            device.wait(Until.findObject(By.text("OK$i")), DEFAULT_TIMEOUT).click()
            assertTrue(device.wait(Until.gone(By.text("Alert$i")), DEFAULT_TIMEOUT))
            job.cancel()
        }
        jobs.forEach { it.cancel() }
    }

    @Test
    fun testAlertShow() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            builder.buildAlert(this) {
                setTitle("Hello")
                setPositiveButton("OK")
            }.show()
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testAlertFlowWithCoroutines() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val action = Alert.Action("OK")
            val presenter = builder.buildAlert(this) {
                setTitle("Hello")
                addActions(listOf(action))
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(result, action)
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    @Ignore("test framework for rotation is unstable")
    fun rotateActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
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

    @Test
    fun testBuilderFromActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) { activity?.showAlert() }
        device.wait(Until.findObject(By.text("Activity")), DEFAULT_TIMEOUT)
        job.cancel()
    }
}
