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

package com.splendo.kaluga.test.base.mock

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.mock.matcher.AnyCaptor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.any
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.notEq
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.notNull
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.times
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MockMethodTest {

    private val mockableTestMethods: MockableTestMethods = MockableTestMethodsImpl()

    @Test
    fun testMockMethodWithoutParamsAndReturnType() {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock()
        mock.verify(never())
        assertEquals(Unit, mock.call())
        mock.verify()
        mock.reset()
        mock.on().doThrow(TestException())
        assertFailsWith(TestException::class) { mock.call() }
        mock.verify()
    }

    @Test
    fun testMockMethodWithoutParamsButWithReturnType() {
        val mock = mockableTestMethods::methodWithoutParamsButWithReturnType.mock()
        mock.verify(never())
        assertEquals("", mock.call())
        mock.verify()
        mock.reset()
        mock.on().doReturn(RESULT)
        assertEquals(RESULT, mock.call())
        mock.verify()
        mock.on().doThrow(TestException())
        assertFailsWith(TestException::class) { mock.call() }
        mock.verify(times(2))
    }

    @Test
    fun testMockMethodWithParamsAndReturnType() {
        val mock = mockableTestMethods::methodWithParamsAndReturnType.mock()
        mock.verify(rule = never())
        assertEquals("", mock.call(PARAM_A))
        mock.verify()
        mock.verify(eq(PARAM_A))
        mock.verify(eq(PARAM_B), never())
        mock.reset()
        mock.on(eq(PARAM_A)).doReturn(RESULT_A)
        mock.on(eq(PARAM_B)).doReturn(RESULT_B)
        mock.on(notEq(PARAM_A)).doReturn(RESULT_NOT_A)
        mock.on(any()).doReturn(RESULT_NOT_A_OR_B)
        assertEquals(RESULT_A, mock.call(PARAM_A))
        assertEquals(RESULT_B, mock.call(PARAM_B))
        mock.verify(times = 2)
        mock.verify(eq(PARAM_A))
        mock.verify(notEq(PARAM_A))
        mock.verify(eq(PARAM_B))
        mock.reset()
        mock.on(eq(PARAM_B)).doExecute { (parameter) ->
            assertEquals(PARAM_B, parameter)
            RESULT_B
        }
        assertEquals(RESULT_B, mock.call(PARAM_B))
        val captor = AnyCaptor<String>()
        mock.verify(captor)
        assertEquals(PARAM_B, captor.lastCaptured)
    }

    @Test
    fun testMockMethodWithMultipleParamsAndReturnType() {
        val mock = mockableTestMethods::methodWithMultipleParamsAndReturnType.mock()
        mock.verify(rule = never())
        assertEquals("", mock.call(PARAM_A1, PARAM_A2, PARAM_A3, PARAM_A4))
        mock.verify()
        mock.verify(eq(PARAM_A1), eq(PARAM_A2), eq(PARAM_A3), eq(PARAM_A4))
        mock.verify(first = eq(PARAM_B1), rule = never())
        mock.reset()
        mock.on(eq(PARAM_A1), eq(PARAM_A2), notNull(), notNull()).doReturn(RESULT_1)
        mock.on(any(), eq(PARAM_A2), eq(PARAM_A3), eq(PARAM_A4)).doReturn(RESULT_2)
        assertEquals(RESULT_2, mock.call(PARAM_A1, PARAM_A2, PARAM_A3, PARAM_A4))
    }

    @Test
    fun testMockSuspendMethodWithParamsAndReturnType() = runBlocking {
        val mock = mockableTestMethods::suspendMethodWithParamsAndReturnType.mock()
        mock.verify(rule = never())
        assertEquals("", mock.call(""))
        mock.reset()
        val deferred = CompletableDeferred<String>()
        val didCall = mock.on().doAwait(deferred)
        launch {
            assertEquals(RESULT, mock.call("Value"))
        }
        didCall.await()
        deferred.complete(RESULT)
        mock.verify()
    }
}
