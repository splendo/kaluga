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

package com.splendo.kaluga.architecture.observation

import androidx.lifecycle.MutableLiveData
import com.splendo.kaluga.architecture.observable.liveDataObserver
import com.splendo.kaluga.architecture.observable.observeOnCoroutine
import com.splendo.kaluga.architecture.observable.toDefaultObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.testBlockingAndCancelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ObservationLiveDataTest : BaseTest() {

    @Test
    fun testLiveData() = testBlockingAndCancelScope {
        val flow = MutableStateFlow("initial")
        val subject = flow.toInitializedSubject(this, Dispatchers.Main.immediate)
        val channel = Channel<String>(4)

        launch(Dispatchers.Main) {
            subject.liveData.observeForever {
                channel.trySend(it).isSuccess
            }
        }

        assertEquals("initial", channel.receive())

        flow.value = "value"
        yield()
        assertEquals("value", channel.receive())
    }

    @Test
    fun testLiveDataWithNull() = testBlockingAndCancelScope {
        val flow = MutableStateFlow<String?>(null)
        val observable = flow.toInitializedObservable(this)
        val channel = Channel<String?>(4)

        launch(Dispatchers.Main) {
            observable.liveData.observeForever {
                channel.trySend(it).isSuccess
            }
        }

        assertNull(channel.receive())

        flow.value = "value"
        assertEquals("value", channel.receive())

        flow.value = null
        assertNull(channel.receive())
    }

    @Test
    fun testLiveDataWithNullAndDefault() = testBlockingAndCancelScope {
        val flow = MutableStateFlow<String?>(null)
        val observable = flow.toDefaultObservable("default", coroutineScope = this)
        val channel = Channel<String?>(4)

        launch(Dispatchers.Main) {
            observable.liveData.observeForever {
                channel.trySend(it).isSuccess
            }
        }

        assertEquals("default", channel.receive())

        flow.value = "value"
        assertEquals("value", channel.receive())

        flow.value = null
        assertEquals("default", channel.receive())
    }

    @Test
    fun testLiveDataObserver() = testBlockingAndCancelScope {
        val flow = MutableStateFlow("initial")
        val subject = flow.toInitializedSubject(this, Dispatchers.Main.immediate)

        val liveData = MutableLiveData("value")

        withContext(Dispatchers.Main) {
            liveData.observeForever(subject.liveDataObserver)
            assertEquals("value", subject.current)

            liveData.value = "foo"
            assertEquals("foo", subject.current)
        }
    }

    @Test
    fun testMutableLiveData() = testBlockingAndCancelScope {
        val flow = MutableStateFlow("initial")
        val subject = flow.toInitializedSubject(this)
        withContext(Dispatchers.Main) {
            val liveData = subject.mutableLiveData
            liveData.value = "value"
            assertEquals("value", flow.value, "value should be updated")
        }
    }

    @Test
    fun testLiveDataObserveOnCoroutine() = testBlockingAndCancelScope {
        val liveData = MutableLiveData("value")
        assertFalse(liveData.hasObservers())
        val completedObserving = EmptyCompletableDeferred()
        val job = launch(Dispatchers.Main) {
            liveData.observeOnCoroutine(
                this,
                observer = {},
            )
            assertTrue(liveData.hasObservers(), "LiveData has observers (inside launch)")
            completedObserving.complete()
        }
        completedObserving.await()
        assertTrue(liveData.hasObservers(), "LiveData has observers (outer test)")
        job.cancel()
        // yield on the main thread so we know the observer can be removed
        withContext(Dispatchers.Main) {
            yield()
        }
        assertFalse(liveData.hasObservers())
    }
}
