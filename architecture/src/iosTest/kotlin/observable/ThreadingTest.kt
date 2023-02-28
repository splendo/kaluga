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

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ThreadingTest {

    class Value

    @Test
    fun testThreadingMainOnly() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        val value = Value()

        s.set(value)
        assertEquals(value, s.currentOrNull)
        assertEquals(value, s.stateFlow.value)
    }

    @Test
    fun testThreadingDefaultThenMain() = runBlocking(Dispatchers.Default) {
        val s = subjectOf<Value?>(null)
        withContext(Dispatchers.Main) {
            val value = Value()
            s.set(value)
        }
    }

    @Test
    fun testThreadingMainThenDefault() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        val value = Value()
        s.set(value)
        withContext(Dispatchers.Default) {
            assertEquals(value, s.currentOrNull)
            assertEquals(value, s.stateFlow.value)
        }
    }

    @Test
    fun testThreadingMainThenDefaultObservers() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        val observer: (Value?) -> Unit = { }
        val disposable = s.observe(observer)

        withContext(Dispatchers.Default) {
            val observer2: (Value?) -> Unit = { }
            val disposable2 = s.observe(observer2)

            disposable2.dispose()
        }

        disposable.dispose()
    }
}
