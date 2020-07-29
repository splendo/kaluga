package com.splendo.kaluga.hud

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

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
const val DEFAULT_TIMEOUT = 2_500L

fun UiDevice.assertTextAppears(text: String) {
    assertNotNull(this.wait(Until.findObject(By.text(text)), DEFAULT_TIMEOUT))
}

fun UiDevice.assertTextDisappears(text: String) {
    assertTrue(wait(Until.gone(By.text(text)), DEFAULT_TIMEOUT))
}

class AndroidHUDTests {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val builder get() = activityRule.activity.viewModel.builder

    companion object {
        const val LOADING = "Loading..."
        const val PROCESSING = "Processing..."
    }

    @Test
    fun builderInitializer() {
        assertNotNull(builder.build())
    }

    @Test
    fun indicatorShow() = runBlockingTest {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)
    }

    @Test
    fun indicatorDismiss() = runBlockingTest {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)
        indicator.dismiss()
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }

    @Test
    fun indicatorDismissAfter() = runBlockingTest {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)
        indicator.dismissAfter(500)
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }

    @Test
    fun testPresentDuring() = runBlockingTest {
        lateinit var indicatorProcessing: HUD

        val loading1 = EmptyCompletableDeferred()
        val loading2 = EmptyCompletableDeferred()
        val processing = EmptyCompletableDeferred()

        val indicatorLoading = builder.build {
            setTitle(LOADING)
        }.presentDuring {
            loading1.await()
            // after appearance of dialog is confirmed, launch another one on top
            launch {
                indicatorProcessing = builder.build {
                    setTitle(PROCESSING)
                }.presentDuring {
                    processing.await()
                }
            }
            loading2.await()
        }

        // check the Loading dialog pops up and is reported as visible
        device.assertTextAppears(LOADING)
        assertTrue(indicatorLoading.isVisible)
        loading1.complete()

        // check the Processing dialog is popped on top
        device.assertTextDisappears(LOADING)
        device.assertTextAppears(PROCESSING)
        assertTrue(indicatorProcessing.isVisible)
        processing.complete()

        // check the Loading dialog appears again
        device.assertTextDisappears(PROCESSING)
        device.assertTextAppears(LOADING)
        loading2.complete()
    }

    @Test
    fun rotateActivity() = runBlockingTest {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)

        // Rotate screen
        device.setOrientationLeft()
        // HUD should be on screen
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)

        device.setOrientationNatural()
        indicator.dismiss()
        // Finally should be gone
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }
}
