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

package com.splendo.kaluga.logging

import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class LogTest : UIThreadTest<LogTest.Context>() {

    class Context(scope: CoroutineScope) : TestContext {
        val mockLogger = LoggerMock(scope)

        init {
            logger = mockLogger
        }

        suspend fun assertLog(
            logLevel: LogLevel,
            tag: String? = null,
            throwable: Exception? = null,
            message: String? = null
        ) {
            yield()
            listOf(
                mockLogger.tagList.size,
                mockLogger.levelList.size,
                mockLogger.throwableList.size,
                mockLogger.messageList.size,
                mockLogger.levelList.size
            ).forEach {
                assertEquals(1, it)
            }

            assertEquals(logLevel, mockLogger.levelList[0])
            assertEquals(tag, mockLogger.tagList[0])
            assertEquals(throwable, mockLogger.throwableList[0])
            assertEquals(message, mockLogger.messageList[0])

            mockLogger.clear()
        }

        suspend fun test(
            logLevel: LogLevel,
            method1: (tag: String?, message: String) -> Unit,
            method2: (message: String) -> Unit,
            method3: (tag: String?, throwable: Throwable) -> Unit,
            method4: (throwable: Throwable) -> Unit,
            method5: (tag: String?, message: () -> String) -> Unit,
            method6: (message: () -> String) -> Unit,
            method7: (tag: String?, throwable: Throwable?, message: () -> String) -> Unit,
        ) {
            val tag = logLevel.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            val message = logLevel.name.uppercase()
            val throwable = RuntimeException(logLevel.name.lowercase())

            method1(tag, message)
            assertLog(logLevel, tag, message = message)
            method2.invoke(message)
            assertLog(logLevel, message = message)
            method3(tag, throwable)
            assertLog(logLevel, tag = tag, throwable = throwable)
            method4(throwable)
            assertLog(logLevel, throwable = throwable)
            method5(tag) { message }
            assertLog(logLevel, tag = tag, message = message)
            method6 { message }
            assertLog(logLevel, message = message)
            method7(tag, throwable) { message }
            assertLog(logLevel, tag, throwable, message)
        }

        override fun dispose() {
            super.dispose()
            resetLogger()
        }
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> Context = { Context(it) }

    @Test
    fun testTransformer() = testOnUIThread {
        val exception = RuntimeException("original exception for testing")
        val wrappedException = RuntimeException("wrapped exception for testing", exception)
        val nullPointerException = NullPointerException()

        wrapLogger { logger ->
            TransformLogger(
                logger,
                transformLogLevel = { LogLevel.ERROR },
                transformTag = { it?.uppercase() ?: "NULL" },
                transformThrowable = { it?.let { wrappedException } ?: nullPointerException },
                transformMessage = {
                    it
                        ?.lowercase()
                        ?.replaceFirstChar { firstChar -> if (firstChar.isLowerCase()) firstChar.titlecase() else firstChar.toString() }
                        ?: "Null"
                }
            )
        }

        debug("tAg", exception) { "mEsSaGe" }
        assertLog(LogLevel.ERROR, "TAG", wrappedException, "Message")

        debug { "mEsSaGe" }
        assertLog(LogLevel.ERROR, "NULL", nullPointerException, "Message")

        resetLogger()
        logger = mockLogger
        debug("tAg", exception) { "mEsSaGe" }
        assertLog(LogLevel.DEBUG, "tAg", exception, "mEsSaGe")
        resetLogger()
        debug("we can still log")
    }

    @Test
    fun testDebug() = testOnUIThread {
        test(
            logLevel = LogLevel.DEBUG,
            method1 = ::debug,
            method2 = ::debug,
            method3 = ::debug,
            method4 = ::debug,
            method5 = ::debug,
            method6 = ::debug,
            method7 = ::debug
        )
    }

    @Test
    fun testD() = testOnUIThread {
        test(
            logLevel = LogLevel.DEBUG,
            method1 = ::d,
            method2 = ::d,
            method3 = ::d,
            method4 = ::d,
            method5 = ::d,
            method6 = ::d,
            method7 = ::d
        )
    }

    @Test
    fun testInfo() = testOnUIThread {
        test(
            logLevel = LogLevel.INFO,
            method1 = ::info,
            method2 = ::info,
            method3 = ::info,
            method4 = ::info,
            method5 = ::info,
            method6 = ::info,
            method7 = ::info
        )
    }

    @Test
    fun testI() = testOnUIThread {
        test(
            logLevel = LogLevel.INFO,
            method1 = ::i,
            method2 = ::i,
            method3 = ::i,
            method4 = ::i,
            method5 = ::i,
            method6 = ::i,
            method7 = ::i
        )
    }

    @Test
    fun testWarn() = testOnUIThread {
        test(
            logLevel = LogLevel.WARN,
            method1 = ::warn,
            method2 = ::warn,
            method3 = ::warn,
            method4 = ::warn,
            method5 = ::warn,
            method6 = ::warn,
            method7 = ::warn
        )
    }

    @Test
    fun testW() = testOnUIThread {
        test(
            logLevel = LogLevel.WARN,
            method1 = ::w,
            method2 = ::w,
            method3 = ::w,
            method4 = ::w,
            method5 = ::w,
            method6 = ::w,
            method7 = ::w
        )
    }

    @Test
    fun testError() = testOnUIThread {
        test(
            logLevel = LogLevel.ERROR,
            method1 = ::error,
            method2 = ::error,
            method3 = ::error,
            method4 = ::error,
            method5 = ::error,
            method6 = ::error,
            method7 = ::error
        )
    }

    @Test
    fun testE() = testOnUIThread {
        test(
            logLevel = LogLevel.ERROR,
            method1 = ::e,
            method2 = ::e,
            method3 = ::e,
            method4 = ::e,
            method5 = ::e,
            method6 = ::e,
            method7 = ::e
        )
    }
}
