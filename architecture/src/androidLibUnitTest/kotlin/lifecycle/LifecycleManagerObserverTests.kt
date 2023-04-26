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

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class LifecycleManagerObserverTests : BaseTest() {

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var fragmentManager: FragmentManager

    @Before
    override fun beforeTest() {
        super.beforeTest()
        MockitoAnnotations.openMocks(this).close()
    }

    @Test
    fun testLifecycleManagerObserverHandlerCalled() = runBlocking {
        val observer = LifecycleManagerObserver()
        val data: ActivityLifecycleSubscribable.LifecycleManager = ActivityLifecycleSubscribable.LifecycleManager(
            activity,
            lifecycleOwner,
            fragmentManager,
        )

        val deferredLifecycleManager = MutableList(3) { CompletableDeferred<ActivityLifecycleSubscribable.LifecycleManager?>() }
        val job = launch(Dispatchers.Main) {
            observer.managerState.collect { context ->
                deferredLifecycleManager.firstOrNull { !it.isCompleted }?.complete(context)
            }
        }

        assertNull(deferredLifecycleManager[0].await())
        observer.subscribe(activity, lifecycleOwner, fragmentManager)
        assertEquals(data, deferredLifecycleManager[1].await())
        observer.unsubscribe()
        assertNull(deferredLifecycleManager[2].await())
        job.cancel()
    }
}
