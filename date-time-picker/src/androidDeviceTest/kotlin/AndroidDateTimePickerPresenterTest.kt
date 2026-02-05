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

package com.splendo.kaluga.datetimepicker

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.base.utils.DefaultKalugaDate.Companion.epoch
import com.splendo.kaluga.test.DateTimePickerPresenterTests
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

class AndroidDateTimePickerPresenterTest : DateTimePickerPresenterTests() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    var activity: TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }

    override val builder get() = activity!!.viewModel.dateTimePickerBuilder

    companion object {
        val DEFAULT_TIMEOUT = 30.seconds.inWholeMilliseconds
    }

    @Test
    fun testBuilderReuse() = runBlocking {
        val deferredPickerPresenter = CompletableDeferred<BaseDateTimePickerPresenter>()
        val job1 = launch(Dispatchers.Main.immediate) {
            val pickerPresenter = builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("Cancel")
            }
            deferredPickerPresenter.complete(pickerPresenter)
            pickerPresenter.show()
        }

        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
        deferredPickerPresenter.await().dismiss(false)
        job1.cancel()

        val job2 = launch(Dispatchers.Main.immediate) {
            builder.buildDatePicker(this) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }

        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("CANCEL")), DEFAULT_TIMEOUT))
        job2.cancel()
    }

    @Test
    fun testConcurrentBuilders() = runBlocking {
        val alerts = Array(10) { CompletableDeferred<BaseDateTimePickerPresenter>() }

        val jobs = (0..9).map { i ->
            launch(Dispatchers.Default) {
                alerts[i].complete(
                    builder.buildTimePicker(CoroutineScope(coroutineContext + Dispatchers.Main.immediate)) {
                        setMessage("DateTimePicker$i")
                        setConfirmButtonTitle("OK$i")
                        setCancelButtonTitle("CANCEL$i")
                    },
                )
            }
        }
        (0..9).forEach { i ->
            val job = launch(Dispatchers.Main.immediate) {
                alerts[i].await().show()
            }
            device.wait(Until.findObject(By.text("DateTimePicker$i")), DEFAULT_TIMEOUT)
            device.wait(Until.findObject(By.text("OK$i")), DEFAULT_TIMEOUT).click()
            assertTrue(device.wait(Until.gone(By.text("DateTimePicker$i")), DEFAULT_TIMEOUT))
            job.cancel()
        }
        jobs.forEach { it.cancel() }
    }

    @Test
    fun testDatePickerShow() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            builder.buildDatePicker(this) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("OK")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testTimePickerShow() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testTimePickerFlowWithCoroutines() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val selectedTime = epoch().apply {
                hour = 23
                minute = 42
            }
            val presenter = builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
                setSelectedDate(selectedTime)
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(selectedTime, result)
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testTimePickerFlowCancelledWithCoroutines() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val presenter = builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertNull(result)
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testDatePickerFlowWithCoroutines() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val selectedDate = epoch().apply {
                year = 2000
                month = 5
                day = 23
            }
            val presenter = builder.buildDatePicker(this) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
                setSelectedDate(selectedDate)
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(selectedDate, result)
        }
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("OK")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    fun testDatePickerFlowCancelledWithCoroutines() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val presenter = builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertNull(result)
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
        job.cancel()
    }

    @Test
    @Ignore("test framework for rotation is unstable")
    fun rotateActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) {
            val presenter = builder.buildTimePicker(this) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)

        // Rotate screen
        device.setOrientationLeft()
        // HUD should be on screen
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)

        device.setOrientationNatural()

        job.cancel()
        // Finally should be gone
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testDateBuilderFromActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) { activity?.showDatePicker() }
        device.wait(Until.findObject(By.text("Activity")), DEFAULT_TIMEOUT)
        job.cancel()
    }

    @Test
    fun testTimeBuilderFromActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) { activity?.showTimePicker() }
        device.wait(Until.findObject(By.text("Activity")), DEFAULT_TIMEOUT)
        job.cancel()
    }
}
