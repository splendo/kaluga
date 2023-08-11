/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.testBlockingAndCancelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

class MutableStateFlowTest : BaseTest() {

    @Test
    fun testInitializedSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow(0)
        val subject = stateFlow.toInitializedSubject(scope)

        var value: Int by subject.valueDelegate

        assertEquals(0, subject.current)
        assertEquals(0, subject.stateFlow.value)
        assertEquals(0, value)

        subject.post(1)
        delay(10)
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        subject.set(2)
        delay(10)
        assertEquals(2, subject.current)
        assertEquals(2, subject.stateFlow.value)
        assertEquals(2, stateFlow.value)
        assertEquals(2, value)

        subject.stateFlow.value = 3
        delay(10)
        assertEquals(3, subject.current)
        assertEquals(3, subject.stateFlow.value)
        assertEquals(3, stateFlow.value)
        assertEquals(3, value)

        value = 4
        delay(10)
        assertEquals(4, subject.current)
        assertEquals(4, subject.stateFlow.value)
        assertEquals(4, stateFlow.value)
        assertEquals(4, value)

        scope.cancel()
    }

    @Test
    fun testPostBeforeObservingInitializedSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow(0)
        val subject = stateFlow.toInitializedSubject(scope)

        subject.post(1)
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testSetBeforeObservingInitializedSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow(0)
        val subject = stateFlow.toInitializedSubject(scope)

        subject.set(1)
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testUpdateStateflowBeforeObservingInitializedSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow(0)
        val subject = stateFlow.toInitializedSubject(scope)

        val subjectStateFlow = subject.stateFlow
        subjectStateFlow.value = 1
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testUpdateDelegatedValueBeforeObservingInitializedSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow(0)
        val subject = stateFlow.toInitializedSubject(scope)

        var value: Int by subject.valueDelegate
        value = 1
        delay(10)
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testDefaultSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow<Int?>(null)
        val subject = stateFlow.toDefaultSubject(0, scope)

        var value: Int by subject.valueDelegate

        assertEquals(0, subject.current)
        assertEquals(0, subject.stateFlow.value)
        assertEquals(0, value)

        subject.post(1)
        delay(10)
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        subject.set(2)
        delay(10)
        assertEquals(2, subject.current)
        assertEquals(2, subject.stateFlow.value)
        assertEquals(2, stateFlow.value)
        assertEquals(2, value)

        subject.stateFlow.value = 3
        delay(10)
        assertEquals(3, subject.current)
        assertEquals(3, subject.stateFlow.value)
        assertEquals(3, stateFlow.value)
        assertEquals(3, value)

        value = 4
        delay(10)
        assertEquals(4, subject.current)
        assertEquals(4, subject.stateFlow.value)
        assertEquals(4, stateFlow.value)
        assertEquals(4, value)

        scope.cancel()
    }

    @Test
    fun testPostBeforeObservingDefaultSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow<Int?>(null)
        val subject = stateFlow.toDefaultSubject(0, scope)

        subject.post(1)
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testSetBeforeObservingDefaultSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow<Int?>(null)
        val subject = stateFlow.toDefaultSubject(0, scope)

        subject.set(1)
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testUpdateStateFlowBeforeObservingDefaultSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow<Int?>(null)
        val subject = stateFlow.toDefaultSubject(0, scope)

        subject.stateFlow.value = 1
        delay(10)
        val value: Int by subject.valueDelegate
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }

    @Test
    fun testUpdateDelegatedValueBeforeObservingDefaultSubject() = testBlockingAndCancelScope(Dispatchers.Main) {
        val scope = CoroutineScope(Dispatchers.Main)
        val stateFlow = MutableStateFlow<Int?>(null)
        val subject = stateFlow.toDefaultSubject(0, scope)

        var value: Int by subject.valueDelegate
        value = 1
        delay(10)
        assertEquals(1, subject.current)
        assertEquals(1, subject.stateFlow.value)
        assertEquals(1, stateFlow.value)
        assertEquals(1, value)

        scope.cancel()
    }
}