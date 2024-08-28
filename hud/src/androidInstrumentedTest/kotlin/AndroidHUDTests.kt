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

package com.splendo.kaluga.hud

import android.util.Log.d
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.splendo.kaluga.architecture.lifecycle.subscribe
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.hud.AndroidHUDTests.AndroidHUDTestContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

val DEFAULT_TIMEOUT = 30.seconds.inWholeMilliseconds

fun UiDevice.assertTextAppears(text: String) {
    waitForIdle()
    assertNotNull(this.wait(Until.findObject(By.text(text)), DEFAULT_TIMEOUT))
}

fun UiDevice.assertTextDisappears(text: String) {
    waitForIdle()
    assertTrue(wait(Until.gone(By.text(text)), DEFAULT_TIMEOUT))
}

class AndroidHUDTests : HUDTests<AndroidHUDTestContext>() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    lateinit var activity: TestActivity

    inner class AndroidHUDTestContext(coroutineScope: CoroutineScope) : HUDTestContext(
        coroutineScope,
    ) {
        override val builder get() = activity.viewModel.builder

        init {
            builder.subscribe(activity)
        }
    }

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity {
            activity = it
        }
    }

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    companion object {
        const val LOADING = "Loading..."
        const val PROCESSING = "Processing..."
    }

    @Test
    fun indicatorShow() = testOnUIThread(cancelScopeAfterTest = true) {
        val indicator = builder.build(this) {
            setTitle(LOADING)
        }
        launch {
            indicator.present()
        }
        withContext(Dispatchers.Default) {
            device.assertTextAppears(LOADING)
            assertTrue(indicator.isVisible)
        }
    }

    @Test
    fun indicatorDismiss() = testOnUIThread(cancelScopeAfterTest = true) {
        val indicator = builder.build(this) {
            setTitle(LOADING)
        }

        launch {
            indicator.present()
        }

        withContext(Dispatchers.Default) {
            device.assertTextAppears(LOADING)
            assertTrue(indicator.isVisible)
            indicator.dismiss()
            device.assertTextDisappears(LOADING)
            assertFalse(indicator.isVisible)
        }
    }

    @Test
    fun indicatorDismissAfter() = testOnUIThread(cancelScopeAfterTest = true) {
        val indicator = builder.build(this) {
            setTitle(LOADING)
        }

        launch {
            indicator.present()
        }

        withContext(Dispatchers.Default) {
            device.assertTextAppears(LOADING)
            assertTrue(indicator.isVisible)

            indicator.dismissAfter(500)

            device.assertTextDisappears(LOADING)
            assertFalse(indicator.isVisible)
        }
    }

    @Test
    fun testPresentDuring() = testOnUIThread {
        val presenting = EmptyCompletableDeferred()
        val loading1 = EmptyCompletableDeferred()
        val loading2 = EmptyCompletableDeferred()
        val processing = EmptyCompletableDeferred()

        val deferredIndicatorLoading = CompletableDeferred<BaseHUD>()
        val indicatorProcessing = CompletableDeferred<BaseHUD>()
        val job = launch(Dispatchers.Main.immediate) {
            val indicatorLoading = builder.build(this) {
                setTitle(LOADING)
            }
            deferredIndicatorLoading.complete(indicatorLoading)
            indicatorLoading.presentDuring {
                presenting.complete()
                loading1.await()
                val test = launch {
                    val processingDialog = builder.build(this) {
                        setTitle(PROCESSING)
                    }
                    indicatorProcessing.complete(processingDialog)
                    processingDialog.presentDuring {
                        processing.await()
                    }
                }

                loading2.await()
                test.cancel()
            }
        }

        presenting.await()
        // check the Loading dialog pops up and is reported as visible

        withContext(Dispatchers.Default) {
            device.assertTextAppears(LOADING)
            assertTrue(deferredIndicatorLoading.await().isVisible)
            loading1.complete()

            // check the Processing dialog is popped on top
            device.assertTextDisappears(LOADING)
            device.assertTextAppears(PROCESSING)
            assertTrue(indicatorProcessing.await().isVisible)
            processing.complete()

            // check the Loading dialog appears again
            device.assertTextDisappears(PROCESSING)
            device.assertTextAppears(LOADING)
            loading2.complete()
        }
        job.cancel()
    }

    @Test
    @Ignore("Rotating in test framework is unstable")
    fun rotateActivity() = testOnUIThread {
        val indicator = builder.build(this) {
            setTitle(LOADING)
        }
        launch {
            indicator.present()
        }
        device.assertTextAppears(LOADING)
        assertTrue(indicator.isVisible)

        // Rotate screen

        for (times in 4 downTo 0) {
            try {
                d("HUD", "$times left to try for rotation test")
                device.setOrientationLeft()
                delay(200)
                // HUD should be on screen
                device.assertTextAppears(LOADING)
                assertTrue(indicator.isVisible)
                break
            } catch (e: java.lang.AssertionError) {
                if (times == 0) throw e
            } finally {
                device.setOrientationNatural()
                delay(200)
            }
        }

        indicator.dismiss()
        // Finally should be gone
        device.assertTextDisappears(LOADING)
    }

    @Test
    fun testBuilderFromActivity() = runBlocking {
        val job = launch(Dispatchers.Main.immediate) { activity.showHUD() }
        device.assertTextAppears("Activity")
        job.cancel()
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> AndroidHUDTestContext = { AndroidHUDTestContext(it) }
}
