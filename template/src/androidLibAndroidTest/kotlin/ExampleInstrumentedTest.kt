/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.template

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

const val DEFAULT_TIMEOUT = 2_500L

fun UiDevice.assertTextAppears(text: String) {
    assertNotNull(this.wait(Until.findObject(By.text(text)), DEFAULT_TIMEOUT))
}

fun UiDevice.assertTextDisappears(text: String) {
    assertTrue(wait(Until.gone(By.text(text)), DEFAULT_TIMEOUT))
}

class ExampleInstrumentedTest {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    // private val viewModel get() = activityRule.activity.viewModel

    // @Test
    // fun viewModelTitleNotNullTest() {
    //     assertNotNull(viewModel.title)
    // }

    @Test
    fun sampleTextOnScreenTest() {
        device.assertTextAppears("Some text on screen")
    }
}