package com.splendo.kaluga.hud

import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
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

    private fun buildAndPresentLoadingHUD(): HUD {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)
        return indicator
    }

    private fun rotateScreen() {
        device.setOrientationLeft()
    }

    @Test
    fun `builder_should_build_HUD`() {
        assertNotNull(builder.build())
    }

    @Test
    fun `present_should_show_indicator_with_appropriate_text`() {
        val indicator = builder.build {
            setTitle(LOADING)
        }.present()
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)
    }

    @Test
    fun `dismiss_dismisses_indicator`() {
        val indicator = buildAndPresentLoadingHUD()
        indicator.dismiss()
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }

    @Test
    fun `dismissAfter_dismisses_indicator`() {
        val indicator = buildAndPresentLoadingHUD()
        indicator.dismissAfter(500)
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }

    @Test
    fun `presentDuring_shows_indicator_while_closure_is_executing`() = runBlockingTest {
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
    fun `hud_should_stay_on_screen_if_it_rotates`() {
        val indicator = buildAndPresentLoadingHUD()

        rotateScreen()

        // HUD should be on screen
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)

        device.setOrientationNatural()
        indicator.dismiss()
        // Finally should be gone
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }

    @Test
    fun `hud_should_keep_showing_if_app_gos_to_background_and_returns_back`() {
        val indicator = buildAndPresentLoadingHUD()

        val appLabel = activityRule.activity.localClassName
        device.pressHome()
        device.pressRecentApps()
        device.findObject(UiSelector().text(appLabel)).click()

        // HUD should be on screen
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)


        indicator.dismiss()
        // Finally should be gone
        device.assertTextDisappears(LOADING)
        assertFalse(indicator.isVisible)
    }
}
