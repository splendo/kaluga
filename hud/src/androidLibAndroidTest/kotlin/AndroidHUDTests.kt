package com.splendo.kaluga.hud

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
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

class AndroidHUDTests {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val builder get() = activityRule.activity.viewModel.builder

    companion object {
        const val DEFAULT_TIMEOUT = 1_000L
    }

    @Test
    fun builderInitializer() {
        assertNotNull(builder.build())
    }

    @Test
    fun indicatorShow() {
        val indicator = builder.build {
            setTitle("Loading...")
        }.present()
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)
    }

    @Test
    fun indicatorDismiss() {
        val indicator = builder.build {
            setTitle("Loading...")
        }.present()
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)
        indicator.dismiss()
        assertTrue(device.wait(Until.gone(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertFalse(indicator.isVisible)
    }

    @Test
    fun indicatorDismissAfter() {
        val indicator = builder.build {
            setTitle("Loading...")
        }.present()
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)
        indicator.dismissAfter(500)
        assertTrue(device.wait(Until.gone(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertFalse(indicator.isVisible)
    }

    @Test
    fun testPresentDuring() = runBlockingTest {
        val indicator = builder.build {
            setTitle("Loading...")
        }.presentDuring {
            delay(1000)
        }
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)
        assertTrue(device.wait(Until.gone(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertFalse(indicator.isVisible)
    }

    @Test
    fun rotateActivity() {
        val indicator = builder.build {
            setTitle("Loading...")
        }.present()
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)

        // Rotate screen
        device.setOrientationLeft()
        // HUD should be on screen
        assertNotNull(device.wait(Until.findObject(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(indicator.isVisible)

        device.setOrientationNatural()
        indicator.dismiss()
        // Finally should gone
        assertTrue(device.wait(Until.gone(By.text("Loading...")), DEFAULT_TIMEOUT))
        assertTrue(!indicator.isVisible)
    }
}
