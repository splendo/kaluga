/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import kotlin.test.Test
import kotlin.test.assertEquals

class SensitiveAwareLoggerTest {

    private companion object {
        const val TAG = "a tag"
    }

    private fun checkLevel(level: LogLevel, loggerFun: (SensitiveAwareLogger, Throwable?, SensitiveAwareLogger.SensitiveAwareContext.() -> String) -> Unit) {
        fun checkCall(expectedMessage: String, expectedRedactedMessage: String, log: SensitiveAwareLogger.SensitiveAwareContext.() -> String) {
            listOf(true, false).forEach { logSensitiveData ->
                listOf(null, Throwable()).forEach { throwable ->
                    val loggerMock = LoggerMock()

                    loggerFun(SensitiveAwareLogger.of(TAG, loggerMock, logSensitiveData), throwable, log)

                    assertEquals(level, loggerMock.levelList.first())
                    assertEquals(TAG, loggerMock.tagList.first())
                    assertEquals(throwable, loggerMock.throwableList.first())
                    val message = requireNotNull(loggerMock.messageList.first()).invoke()
                    if (logSensitiveData) {
                        assertEquals(expectedMessage, message)
                    } else {
                        assertEquals(expectedRedactedMessage, message)
                    }
                }
            }
        }

        checkCall(
            expectedMessage = "A regular log",
            expectedRedactedMessage = "A regular log",
        ) {
            "A regular log"
        }

        checkCall(
            expectedMessage = "A sensitive log",
            expectedRedactedMessage = "A ##### log",
        ) {
            "A ${sensitive("sensitive")} log"
        }

        checkCall(
            expectedMessage = "Sensitive data: 123",
            expectedRedactedMessage = "Sensitive data: #####",
        ) {
            "Sensitive data: ${123.sensitive}"
        }
    }

    @Test
    fun debug() = checkLevel(LogLevel.DEBUG, SensitiveAwareLogger::debug)

    @Test
    fun info() = checkLevel(LogLevel.INFO, SensitiveAwareLogger::info)

    @Test
    fun warn() = checkLevel(LogLevel.WARN, SensitiveAwareLogger::warn)

    @Test
    fun error() = checkLevel(LogLevel.ERROR, SensitiveAwareLogger::error)
}
