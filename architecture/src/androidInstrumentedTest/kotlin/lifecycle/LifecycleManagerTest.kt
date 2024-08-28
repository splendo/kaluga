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

package com.splendo.kaluga.architecture.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.splendo.kaluga.architecture.TestActivity
import com.splendo.kaluga.test.architecture.lifecycleManagerObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LifecycleManagerTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    var activity: TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }

    @Test
    fun testLifecycleManagerObserver() = runBlocking(Dispatchers.Main) {
        val activity = activity!!
        val observer = activity.lifecycleManagerObserver()
        assertEquals(
            ActivityLifecycleSubscribable.LifecycleManager(
                activity,
                activity,
                activity.supportFragmentManager,
            ),
            observer.managerState.value,
        )

        withContext(Dispatchers.Default) {
            activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)
            assertNull(observer.managerState.value)
        }
    }
}
