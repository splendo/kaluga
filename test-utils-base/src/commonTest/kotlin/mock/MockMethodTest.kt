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

interface MockableTestMethods {
    fun methodWithoutParamsAndReturnType()
    fun methodWithoutParamsButWithReturnType(): String
    fun methodWithParamsAndReturnType(string: String): String
    fun methodWithMultipleParamsAndReturnType(first: Int, second: String, third: String?, fourth: Int?): String
    suspend fun suspendMethodWithParamsAndReturnType(string: String): String
}

class MockableTestMethodsImpl : MockableTestMethods {

    override fun methodWithoutParamsAndReturnType() {
        throw NotImplementedError()
    }

    override fun methodWithoutParamsButWithReturnType(): String {
        throw NotImplementedError()
    }

    override fun methodWithParamsAndReturnType(string: String): String {
        throw NotImplementedError()
    }

    override fun methodWithMultipleParamsAndReturnType(first: Int, second: String, third: String?, fourth: Int?): String {
        throw NotImplementedError()
    }

    override suspend fun suspendMethodWithParamsAndReturnType(string: String): String {
        throw NotImplementedError()
    }
}

class MockMethodTest {

    private class TestException : Exception()
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
        val result = "Result"
        mock.on().doReturn(result)
        assertEquals(result, mock.call())
        mock.verify()
        mock.on().doThrow(TestException())
        assertFailsWith(TestException::class) { mock.call() }
        mock.verify(times(2))
    }

    @Test
    fun testMockMethodWithParamsAndReturnType() {
        val mock = mockableTestMethods::methodWithParamsAndReturnType.mock()
        mock.verify(rule = never())
        val paramA = "A"
        val paramB = "B"
        assertEquals("", mock.call(paramA))
        mock.verify()
        mock.verify(eq(paramA))
        mock.verify(eq(paramB), never())
        mock.reset()
        val resultA = "Result A"
        val resultB = "Result B"
        val resultNotA = "Result Not A"
        val resultNotAOrB = "Result A nor B"
        mock.on(eq(paramA)).doReturn(resultA)
        mock.on(eq(paramB)).doReturn(resultB)
        mock.on(notEq(paramA)).doReturn(resultNotA)
        mock.on(any()).doReturn(resultNotAOrB)
        assertEquals(resultA, mock.call(paramA))
        assertEquals(resultB, mock.call(paramB))
        mock.verify(times = 2)
        mock.verify(eq(paramA))
        mock.verify(notEq(paramA))
        mock.verify(eq(paramB))
        mock.reset()
        mock.on(eq(paramB)).doExecute { (parameter) ->
            assertEquals(paramB, parameter)
            resultB
        }
        assertEquals(resultB, mock.call(paramB))
        val captor = AnyCaptor<String>()
        mock.verify(captor)
        assertEquals(paramB, captor.lastCaptured)
    }

    @Test
    fun testMockMethodWithMultipleParamsAndReturnType() {
        val mock = mockableTestMethods::methodWithMultipleParamsAndReturnType.mock()
        mock.verify(rule = never())
        val paramA1 = 1
        val paramA2 = "A"
        val paramA3 = "Something"
        val paramA4 = 10
        val paramB1 = 2
        assertEquals("", mock.call(paramA1, paramA2, paramA3, paramA4))
        mock.verify()
        mock.verify(eq(paramA1), eq(paramA2), eq(paramA3), eq(paramA4))
        mock.verify(first = eq(paramB1), rule = never())
        mock.reset()
        val result1 = "Result 1"
        val result2 = "Result 2"
        mock.on(eq(paramA1), eq(paramA2), notNull(), notNull()).doReturn(result1)
        mock.on(any(), eq(paramA2), eq(paramA3), eq(paramA4)).doReturn(result2)
        assertEquals(result2, mock.call(paramA1, paramA2, paramA3, paramA4))
    }

    @Test
    fun testMockSuspendMethodWithParamsAndReturnType() = runBlocking {
        val mock = mockableTestMethods::suspendMethodWithParamsAndReturnType.mock()
        mock.verify(rule = never())
        assertEquals("", mock.call(""))
        mock.reset()
        val result = "Result"
        val deferred = CompletableDeferred<String>()
        val didCall = mock.on().doAwait(deferred)
        launch {
            assertEquals(result, mock.call("Value"))
        }
        didCall.await()
        deferred.complete(result)
        mock.verify()
    }
}
