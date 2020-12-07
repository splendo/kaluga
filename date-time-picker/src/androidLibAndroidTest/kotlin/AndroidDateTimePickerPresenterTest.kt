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

package com.splendo.kaluga.datetimepicker

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.base.utils.Date.Companion.epoch
import com.splendo.kaluga.test.DateTimePickerPresenterTests
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
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
        const val DEFAULT_TIMEOUT = 5_000L
    }

    @Test
    fun testBuilderReuse() = runBlocking {

        launch(Dispatchers.Main) {
            builder.buildTimePicker(MainScope()) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("Cancel")
            }.show()
        }

        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))

        launch(Dispatchers.Main) {
            builder.buildDatePicker(MainScope()) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }

        device.wait(Until.findObject(By.text("CANCEL")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("CANCEL")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testConcurrentBuilders() = runBlocking {
        val alerts = Array(10) { CompletableDeferred<BaseDateTimePickerPresenter>() }

        (0..9).forEach { i ->
            launch(Dispatchers.Default) {
                alerts[i].complete(
                    builder.buildTimePicker(MainScope()) {
                        setMessage("DateTimePicker$i")
                        setConfirmButtonTitle("OK$i")
                        setCancelButtonTitle("CANCEL$i")
                    }
                )
            }
        }
        (0..9).forEach { i ->
            launch(Dispatchers.Main) {
                alerts[i].await().show()
            }
            device.wait(Until.findObject(By.text("DateTimePicker$i")), DEFAULT_TIMEOUT)
            device.wait(Until.findObject(By.text("OK$i")), DEFAULT_TIMEOUT).click()
            assertTrue(device.wait(Until.gone(By.text("DateTimePicker$i")), DEFAULT_TIMEOUT))
        }
    }

    @Test
    fun testDatePickerShow() = runBlocking {
        launch(Dispatchers.Main) {
            builder.buildDatePicker(MainScope()) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("OK")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testTimePickerShow() = runBlocking {
        launch(Dispatchers.Main) {
            builder.buildTimePicker(MainScope()) {
                setMessage("Message")
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
            }.show()
        }
        device.wait(Until.findObject(By.text("Message")), DEFAULT_TIMEOUT)
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("Message")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testTimePickerFlowWithCoroutines() = runBlocking {
        launch(Dispatchers.Main) {
            val selectedTime = epoch().apply {
                hour = 23
                minute = 42
            }
            val presenter = builder.buildTimePicker(MainScope()) {
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
    }

    @Test
    fun testTimePickerFlowCancelledWithCoroutines() = runBlocking {
        launch(Dispatchers.Main) {
            val presenter = builder.buildTimePicker(MainScope()) {
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
    }

    @Test
    fun testDatePickerFlowWithCoroutines() = runBlocking {
        launch(Dispatchers.Main) {
            val selectedDate = epoch().apply {
                year = 2000
                month = 5
                day = 23
            }
            val presenter = builder.buildDatePicker(MainScope()) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("CANCEL")
                setSelectedDate(selectedDate)
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(selectedDate, result)
        }
        device.wait(Until.findObject(By.text("OK")), DEFAULT_TIMEOUT).click()
        assertTrue(device.wait(Until.gone(By.text("OK")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testDatePickerFlowCancelledWithCoroutines() = runBlocking {
        launch(Dispatchers.Main) {
            val presenter = builder.buildTimePicker(MainScope()) {
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
    }

    @Test
    @Ignore("test framework for rotation is unstable")
    fun rotateActivity() = runBlocking {
        val job = launch(Dispatchers.Main) {
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
    fun testDateBuilderFromActivity() {
        MainScope().launch { activity?.showDatePicker() }
        device.wait(Until.findObject(By.text("Activity")), DEFAULT_TIMEOUT)
    }

    @Test
    fun testTimeBuilderFromActivity() {
        MainScope().launch { activity?.showTimePicker() }
        device.wait(Until.findObject(By.text("Activity")), DEFAULT_TIMEOUT)
    }
}
