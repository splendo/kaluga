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
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.architecture.getOrPutAndRemoveOnDestroyFromCache
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame

class GetOrPutAndRemoveOnDestroyFromCacheTest : BaseTest() {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestActivity::class.java)

    var activity: TestActivity? = null

    @BeforeTest
    fun activityInit() {
        activityRule.scenario.onActivity { activity = it }
    }

    @Test
    fun testGetOrPutAndRemoveOnDestroyFromCache() = runBlocking(Dispatchers.Main) {
        val onCreated = EmptyCompletableDeferred()
        val onDestroyed = EmptyCompletableDeferred()
        val mutableList = mutableListOf("first")
        val returnedFirstTime = activity?.getOrPutAndRemoveOnDestroyFromCache(
            onCreate = { onCreated.complete() },
            onDestroy = { onDestroyed.complete() },
            defaultValue = { mutableList },
        )

        assertSame(returnedFirstTime, mutableList)
        onCreated.await()

        // cache some other kind of class
        val dummy = arrayOf("some array")
        val returnedDummy = activity?.getOrPutAndRemoveOnDestroyFromCache { dummy }

        assertSame(dummy, returnedDummy)

        val defaultValueSecondTime = EmptyCompletableDeferred()
        val returnedSecondTime = activity?.getOrPutAndRemoveOnDestroyFromCache {
            defaultValueSecondTime.complete()
            mutableListOf("second")
        }

        assertSame(returnedFirstTime, returnedSecondTime)
        assertFalse(defaultValueSecondTime.isCompleted)

        withContext(Dispatchers.Default) {
            activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)
            onDestroyed.await()
        }

        // normally you should not use this method after onDestroy, but this way we can test if the previous object was cleaned up
        val returnedThirdTime = activity?.getOrPutAndRemoveOnDestroyFromCache {
            defaultValueSecondTime.complete()
            mutableListOf("third")
        }

        assertEquals("third", returnedThirdTime!![0], "After onDestroy the previous entry should be removed")
    }
}
