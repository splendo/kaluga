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

package com.splendo.kaluga.keyboard

import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ActivityKeyboardManagerBuilderTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    lateinit var activity: TestActivity

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity {
            activity = it
        }
    }

    @Test
    fun testActivityKeyboardManagerBuilder() = runBlocking {
        withContext(Dispatchers.Main) {
            activity.showKeyboard()
        }
        // not crashing is good enough since we are testing the builder, not the keyboard
    }
}
