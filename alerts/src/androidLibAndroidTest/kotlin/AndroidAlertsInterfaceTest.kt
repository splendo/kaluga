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

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.test.AlertsInterfaceTests
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test

class AndroidAlertsInterfaceTest : AlertsInterfaceTests() {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    override val builder get() = activityRule.activity.viewModel.alertBuilder

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
    }

    @Test
    fun testBuilderReuse() = runBlockingTest {

        CoroutineScope(Dispatchers.Main).launch {
            builder.buildAlert {
                setTitle("Test")
                setPositiveButton("OK")
            }.show()

            device.wait(Until.findObject(By.text("Test")), DEFAULT_TIMEOUT)
            device.findObject(By.text("OK")).click()
            assertTrue(device.wait(Until.gone(By.text("Test")), DEFAULT_TIMEOUT))

            builder.buildAlert {
                setTitle("Hello")
                setNegativeButton("Cancel")
            }.show()

            device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
            device.findObject(By.text("Cancel")).click()
            assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
        }
    }

    @Test
    fun testConcurrentBuilders() = runBlockingTest {
        val alerts: MutableList<AlertInterface> = mutableListOf()

        CoroutineScope(Dispatchers.Main).launch {
            for (i in 0 until 10) {
                alerts.add(builder.buildAlert {
                    setTitle("Alert$i")
                    setPositiveButton("OK$i")
                })
            }
            for (i in 0 until 10) {
                alerts[i].show()
                device.wait(Until.findObject(By.text("Alert$i")), DEFAULT_TIMEOUT)
                device.findObject(By.text("OK$i")).click()
                assertTrue(device.wait(Until.gone(By.text("Alert$i")), DEFAULT_TIMEOUT))
            }
        }
    }

    @Test
    fun testAlertShow() = runBlockingTest {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            builder.buildAlert {
                setTitle("Hello")
                setPositiveButton("OK")
            }.show()
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.findObject(By.text("OK")).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }

    @Test
    fun testAlertFlowWithCoroutines() = runBlockingTest {
        CoroutineScope(Dispatchers.Main).launch {
            val action = Alert.Action("OK")
            val presenter = builder.buildAlert {
                setTitle("Hello")
                addActions(listOf(action))
            }

            val result = withContext(coroutineContext) { presenter.show() }
            assertEquals(result, action)
        }
        device.wait(Until.findObject(By.text("Hello")), DEFAULT_TIMEOUT)
        device.findObject(By.text("OK")).click()
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }

    @Test
    fun rotateActivity() {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val presenter = builder.buildAlert {
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
        coroutine.cancel()
        // Finally should be gone
        assertTrue(device.wait(Until.gone(By.text("Hello")), DEFAULT_TIMEOUT))
    }
}
