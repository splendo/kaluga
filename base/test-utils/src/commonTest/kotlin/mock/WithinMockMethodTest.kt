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

import com.splendo.kaluga.test.base.mock.matcher.AnyCaptor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.any
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.notEq
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.times
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

@Suppress("SuspendFunctionOnCoroutineScope")
class WithinMockMethodTest {

    private val mockableTestMethods: MockableTestMethods = MockableTestMethodsImpl()

    private suspend fun CoroutineScope.short(fn: suspend () -> Unit) {
        launch {
            delay(500)
            fn()
        }
    }

    private suspend fun CoroutineScope.long(fn: suspend () -> Unit) {
        launch {
            delay(60000) // since runTest is used the delay is actually not that long.
            fn()
        }
    }

    @Test
    fun testMockMethodWithoutParamsAndReturnTypeMultipleInvocationsWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock().also {
            it.verifyWithin(rule = never())
        }
        coroutineScope {
            assertEquals(Unit, mock.call())
            launch {
                delay(500)
                assertEquals(Unit, mock.call())
                yield() // without this yield the count would jump from 1 to 3
                assertEquals(Unit, mock.call()) // despite this being the third call, by now verifyWith should have succeeded
            }
            mock.verifyWithin(times = 2)
        }
    }

    @Test
    fun testMockMethodWithoutParamsAndReturnTypeInsufficientInvocationsWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock().also {
            it.verifyWithin(rule = never())
        }
        coroutineScope {
            assertEquals(Unit, mock.call())
            launch {
                delay(500)
                assertEquals(Unit, mock.call())
                delay(250)
                assertEquals(Unit, mock.call())
                delay(2000)
                assertEquals(Unit, mock.call())
                assertEquals(Unit, mock.call())
            }
            val assertionError = assertFailsWith<AssertionError> {
                mock.verifyWithin(duration = 2.seconds, times = 4)
            }
            assertEquals("Expected Exactly(times=4) matches but got 1 matches. Then got 2 matches. Then got 3 matches. Then got a timeout after 2s.", assertionError.message)
        }
    }

    @Test
    fun testMockMethodWithoutParamsAndReturnTypeWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock().also {
            it.verifyWithin(rule = never())
        }
        assertEquals(Unit, mock.call())
        mock.verifyWithin()
        mock.reset()
        mock.on().doThrow(TestException())
        assertFailsWith(TestException::class) { mock.call() }
        mock.verifyWithin()
    }

    @Test
    fun testMockMethodWithoutParamsAndReturnTypeShortDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        short {
            assertEquals(Unit, mock.call())
        }
        mock.verifyWithin()
        mock.reset()
        mock.on().doThrow(TestException())
        short {
            assertFailsWith(TestException::class) { mock.call() }
        }
        mock.verifyWithin()
    }

    @Test
    fun testMockMethodWithoutParamsAndReturnTypeLongDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        long {
            assertEquals(Unit, mock.call())
        }
        assertFailsWith<AssertionError> {
            mock.verifyWithin()
        }
    }

    @Test
    fun testMockMethodWithoutParamsButWithReturnTypeWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsButWithReturnType.mock()
        mock.verifyWithin(rule = never())
        assertEquals("", mock.call())
        mock.verifyWithin()
        mock.reset()
        val result = "Result"
        mock.on().doReturn(result)
        assertEquals(result, mock.call())
        mock.verifyWithin()
        mock.on().doThrow(TestException())
        assertFailsWith(TestException::class) { mock.call() }
        mock.verify(times(2))
    }

    @Test
    fun testMockMethodWithoutParamsButWithReturnTypeShortDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsButWithReturnType.mock()
        mock.verifyWithin(rule = never())
        short {
            assertEquals("", mock.call())
        }
        mock.verifyWithin()

        mock.reset()

        val result = "Result"
        mock.on().doReturn(result)
        short {
            assertEquals(result, mock.call())
        }
        mock.verifyWithin()

        mock.on().doThrow(TestException())
        short {
            assertFailsWith(TestException::class) { mock.call() }
        }
        mock.verifyWithin(times = 2)
    }

    @Test
    fun testMockMethodWithoutParamsButWithReturnTypeLongDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithoutParamsButWithReturnType.mock()
        mock.verifyWithin(rule = never())
        coroutineScope {
            long {
                assertEquals("", mock.call())
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin()
            }
        }

        mock.reset()

        coroutineScope {
            val result = "Result"
            mock.on().doReturn(result)

            long {
                assertEquals(result, mock.call())
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin()
            }
        }

        coroutineScope {
            mock.on().doThrow(TestException())
            long {
                assertFailsWith(TestException::class) { mock.call() }
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin(times = 2)
            }
        }
    }

    @Test
    fun testMockMethodWithParamsAndReturnTypeWithin() = runTest {
        val mock = mockableTestMethods::methodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        assertEquals("", mock.call(PARAM_A))
        mock.verifyWithin()
        mock.verifyWithin(value = eq(PARAM_A))
        mock.verifyWithin(value = eq(PARAM_B), rule = never())
        mock.reset()
        mock.on(eq(PARAM_A)).doReturn(RESULT_A)
        mock.on(eq(PARAM_B)).doReturn(RESULT_B)
        mock.on(notEq(PARAM_A)).doReturn(RESULT_NOT_A)
        mock.on(any()).doReturn(RESULT_NOT_A_OR_B)
        assertEquals(RESULT_A, mock.call(PARAM_A))
        assertEquals(RESULT_B, mock.call(PARAM_B))
        mock.verifyWithin(times = 2)
        mock.verifyWithin(value = eq(PARAM_A))
        mock.verifyWithin(value = notEq(PARAM_A))
        mock.verifyWithin(value = eq(PARAM_B))
        mock.reset()
        mock.on(eq(PARAM_B)).doExecute { (parameter) ->
            assertEquals(PARAM_B, parameter)
            RESULT_B
        }
        assertEquals(RESULT_B, mock.call(PARAM_B))
        val captor = AnyCaptor<String>()
        mock.verifyWithin(value = captor)
        assertEquals(PARAM_B, captor.lastCaptured)
    }

    @Test
    fun testMockMethodWithParamsAndReturnTypeShortDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())

        coroutineScope {
            short {
                assertEquals("", mock.call(PARAM_A))
            }
            mock.verifyWithin()
            mock.verifyWithin(value = eq(PARAM_A))
            mock.verifyWithin(value = eq(PARAM_B), rule = never())
        }

        mock.reset()

        coroutineScope {
            short {
                mock.on(eq(PARAM_A)).doReturn(RESULT_A)
                mock.on(eq(PARAM_B)).doReturn(RESULT_B)
                mock.on(notEq(PARAM_A)).doReturn(RESULT_NOT_A)
                mock.on(any()).doReturn(RESULT_NOT_A_OR_B)
                assertEquals(RESULT_A, mock.call(PARAM_A))
                assertEquals(RESULT_B, mock.call(PARAM_B))
            }
            mock.verifyWithin(times = 2)
            mock.verifyWithin(value = eq(PARAM_A))
            mock.verifyWithin(value = notEq(PARAM_A))
            mock.verifyWithin(value = eq(PARAM_B))
        }

        mock.reset()

        coroutineScope {
            short {
                mock.on(eq(PARAM_B)).doExecute { (parameter) ->
                    assertEquals(PARAM_B, parameter)
                    RESULT_B
                }
                assertEquals(RESULT_B, mock.call(PARAM_B))
            }
            val captor = AnyCaptor<String>()
            mock.verifyWithin(value = captor)
            assertEquals(PARAM_B, captor.lastCaptured)
        }
    }

    @Test
    fun testMockMethodWithParamsAndReturnTypeLongDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())

        coroutineScope {
            long {
                assertEquals("", mock.call(PARAM_A))
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin()
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin(value = eq(PARAM_A))
            }
            mock.verifyWithin(value = eq(PARAM_B), rule = never()) // works because we instantly get "never"
        }

        mock.reset()

        coroutineScope {
            long {
                mock.on(eq(PARAM_A)).doReturn(RESULT_A)
                mock.on(eq(PARAM_B)).doReturn(RESULT_B)
                mock.on(notEq(PARAM_A)).doReturn(RESULT_NOT_A)
                mock.on(any()).doReturn(RESULT_NOT_A_OR_B)
                assertEquals(RESULT_A, mock.call(PARAM_A))
                assertEquals(RESULT_B, mock.call(PARAM_B))
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin(times = 2)
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin(value = eq(PARAM_A))
            }

            assertFailsWith<AssertionError> {
                mock.verifyWithin(value = notEq(PARAM_A)) // works because we instantly get a non-match
            }

            assertFailsWith<AssertionError> {
                mock.verifyWithin(value = eq(PARAM_B))
            }
        }

        mock.reset()

        coroutineScope {
            long {
                mock.on(eq(PARAM_B)).doExecute { (parameter) ->
                    assertEquals(PARAM_B, parameter)
                    RESULT_B
                }
                assertEquals(RESULT_B, mock.call(PARAM_B))
            }
            assertFailsWith<AssertionError> {
                val captor = AnyCaptor<String>()
                mock.verifyWithin(value = captor)
            }
        }
    }

    @Test
    fun testMockMethodWithMultipleParamsAndReturnTypeWithin() = runTest {
        val mock = mockableTestMethods::methodWithMultipleParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        assertEquals("", mock.call(PARAM_A1, PARAM_A2, PARAM_A3, PARAM_A4))
        mock.verifyWithin()
        mock.verifyWithin(first = eq(PARAM_A1), second = eq(PARAM_A2), third = eq(PARAM_A3), fourth = eq(PARAM_A4))
        mock.verifyWithin(first = eq(PARAM_B1), rule = never())
        mock.reset()
    }

    @Test
    fun testMockMethodWithMultipleParamsAndReturnTypeShortDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithMultipleParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        short {
            assertEquals("", mock.call(PARAM_A1, PARAM_A2, PARAM_A3, PARAM_A4))
        }
        mock.verifyWithin()
        mock.verifyWithin(first = eq(PARAM_A1), second = eq(PARAM_A2), third = eq(PARAM_A3), fourth = eq(PARAM_A4))
        mock.verifyWithin(first = eq(PARAM_B1), rule = never())
    }

    @Test
    fun testMockMethodWithMultipleParamsAndReturnTypeLongDelayWithin() = runTest {
        val mock = mockableTestMethods::methodWithMultipleParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        coroutineScope {
            long {
                assertEquals("", mock.call(PARAM_A1, PARAM_A2, PARAM_A3, PARAM_A4))
            }

            assertFailsWith<AssertionError> {
                mock.verifyWithin()
            }
            assertFailsWith<AssertionError> {
                mock.verifyWithin(first = eq(PARAM_A1), second = eq(PARAM_A2), third = eq(PARAM_A3), fourth = eq(PARAM_A4))
            }
            mock.verifyWithin(first = eq(PARAM_B1), rule = never()) // works because we instantly get "never"
        }
    }

    @Test
    fun testMockSuspendMethodWithParamsAndReturnTypeWithin() = runTest {
        val mock = mockableTestMethods::suspendMethodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())
        assertEquals("", mock.call(""))

        mock.reset()

        val deferred = CompletableDeferred<String>()
        val didCall = mock.on().doAwait(deferred)
        launch {
            assertEquals(RESULT, mock.call("Value"))
        }
        didCall.await()
        deferred.complete(RESULT)
        mock.verifyWithin()
    }

    @Test
    fun testMockSuspendMethodWithParamsAndReturnTypeShortDelayWithin() = runTest {
        val mock = mockableTestMethods::suspendMethodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())

        short {
            assertEquals("", mock.call(""))
            mock.reset()

            val deferred = CompletableDeferred<String>()
            val didCall = mock.on().doAwait(deferred)
            launch {
                assertEquals(RESULT, mock.call("Value"))
            }
            didCall.await()
            deferred.complete(RESULT)
        }

        mock.verifyWithin()
    }

    @Test
    fun testMockSuspendMethodWithParamsAndReturnTypeLongDelayWithin() = runTest {
        val mock = mockableTestMethods::suspendMethodWithParamsAndReturnType.mock()
        mock.verifyWithin(rule = never())

        long {
            assertEquals("", mock.call(""))
            mock.reset()

            val deferred = CompletableDeferred<String>()
            val didCall = mock.on().doAwait(deferred)
            launch {
                assertEquals(RESULT, mock.call("Value"))
            }
            didCall.await()
            deferred.complete(RESULT)
        }

        assertFailsWith<AssertionError> {
            mock.verifyWithin()
        }
    }
}
