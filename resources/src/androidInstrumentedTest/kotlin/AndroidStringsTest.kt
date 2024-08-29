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

package com.splendo.kaluga.resources

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import kotlin.test.BeforeTest

class AndroidStringsTest : StringsTests() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    private lateinit var activity: TestActivity

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity {
            activity = it
        }
    }

    override val stringLoader by lazy { DefaultStringLoader(activity) }
}
