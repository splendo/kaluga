/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LogTest {

    private lateinit var mockLogger: LoggerMock

    @BeforeTest
    fun setUp() {
        mockLogger = initLogger(LoggerMock()) as LoggerMock
    }

    @AfterTest
    fun tearDown() {
        mockLogger.clear()
    }

    private fun assertLogWithMessageWasCalled(
        logLevel: LogLevel,
        tag: String? = null,
        exception: Exception? = null
    ) {
        assertEquals(tag, mockLogger.tagList[0])
        assertEquals(logLevel, mockLogger.levelList[0])
        assertEquals(logLevel.name, mockLogger.messageList[0])
        assertEquals(exception, mockLogger.exceptionsList[0])
    }

    private fun assertLogWithExceptionWasCalled(
        logLevel: LogLevel,
        tag: String? = null,
        message: String? = null
    ) {
        assertEquals(tag, mockLogger.tagList[0])
        assertEquals(logLevel, mockLogger.levelList[0])
        assertEquals(message, mockLogger.messageList[0])
        assertEquals(logLevel.name, mockLogger.exceptionsList[0]?.message)
    }

    @Test
    fun testLogDebugAndMessage() {
        log(LogLevel.DEBUG, LogLevel.DEBUG.name)
        assertLogWithMessageWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testLogDebugAndException() {
        log(LogLevel.DEBUG, RuntimeException(LogLevel.DEBUG.name))
        assertLogWithExceptionWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testLogDebugAndFunction() {
        log(LogLevel.DEBUG) { LogLevel.DEBUG.name }
        assertLogWithMessageWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testInfoAndMessage() {
        info(LogLevel.INFO.name)
        assertLogWithMessageWasCalled(LogLevel.INFO)
    }

    @Test
    fun testInfoAndException() {
        info(RuntimeException(LogLevel.INFO.name))
        assertLogWithExceptionWasCalled(LogLevel.INFO)
    }

    @Test
    fun testInfoAndFunction() {
        info { LogLevel.INFO.name }
        assertLogWithMessageWasCalled(LogLevel.INFO)
    }

    @Test
    fun testInfoAndTagAndMessage() {
        info(LogLevel.INFO.name, LogLevel.INFO.name)
        assertLogWithMessageWasCalled(LogLevel.INFO, LogLevel.INFO.name)
    }

    @Test
    fun testInfoAndTagAndException() {
        info(LogLevel.INFO.name, RuntimeException(LogLevel.INFO.name))
        assertLogWithExceptionWasCalled(LogLevel.INFO, LogLevel.INFO.name)
    }

    @Test
    fun testInfoAndTagAndFunction() {
        info(LogLevel.INFO.name) { LogLevel.INFO.name }
        assertLogWithMessageWasCalled(LogLevel.INFO, LogLevel.INFO.name)
    }

    @Test
    fun testDebugAndMessage() {
        debug(LogLevel.DEBUG.name)
        assertLogWithMessageWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testDebugAndException() {
        debug(RuntimeException(LogLevel.DEBUG.name))
        assertLogWithExceptionWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testDebugAndFunction() {
        debug { LogLevel.DEBUG.name }
        assertLogWithMessageWasCalled(LogLevel.DEBUG)
    }

    @Test
    fun testDebugAndTagAndMessage() {
        debug(LogLevel.DEBUG.name, LogLevel.DEBUG.name)
        assertLogWithMessageWasCalled(LogLevel.DEBUG, LogLevel.DEBUG.name)
    }

    @Test
    fun testDebugAndTagAndException() {
        debug(LogLevel.DEBUG.name, RuntimeException(LogLevel.DEBUG.name))
        assertLogWithExceptionWasCalled(LogLevel.DEBUG, LogLevel.DEBUG.name)
    }

    @Test
    fun testDebugAndTagAndFunction() {
        debug(LogLevel.DEBUG.name) { LogLevel.DEBUG.name }
        assertLogWithMessageWasCalled(LogLevel.DEBUG, LogLevel.DEBUG.name)
    }

    @Test
    fun testWarnAndMessage() {
        warn(LogLevel.WARNING.name)
        assertLogWithMessageWasCalled(LogLevel.WARNING)
    }

    @Test
    fun testWarnAndException() {
        warn(RuntimeException(LogLevel.WARNING.name))
        assertLogWithExceptionWasCalled(LogLevel.WARNING)
    }

    @Test
    fun testWarnAndFunction() {
        warn { LogLevel.WARNING.name }
        assertLogWithMessageWasCalled(LogLevel.WARNING)
    }

    @Test
    fun testWarnAndTagAndMessage() {
        warn(LogLevel.WARNING.name, LogLevel.WARNING.name)
        assertLogWithMessageWasCalled(LogLevel.WARNING, LogLevel.WARNING.name)
    }

    @Test
    fun testWarnAndTagAndException() {
        warn(LogLevel.WARNING.name, RuntimeException(LogLevel.WARNING.name))
        assertLogWithExceptionWasCalled(LogLevel.WARNING, LogLevel.WARNING.name)
    }

    @Test
    fun testWarnAndTagAndFunction() {
        warn(LogLevel.WARNING.name) { LogLevel.WARNING.name }
        assertLogWithMessageWasCalled(LogLevel.WARNING, LogLevel.WARNING.name)
    }

    @Test
    fun testErrorAndMessage() {
        error(LogLevel.ERROR.name)
        assertLogWithMessageWasCalled(LogLevel.ERROR)
    }

    @Test
    fun testErrorAndException() {
        error(RuntimeException(LogLevel.ERROR.name))
        assertLogWithExceptionWasCalled(LogLevel.ERROR)
    }

    @Test
    fun testErrorAndFunction() {
        error { LogLevel.ERROR.name }
        assertLogWithMessageWasCalled(LogLevel.ERROR)
    }

    @Test
    fun testErrorAndTagAndMessage() {
        error(LogLevel.ERROR.name, LogLevel.ERROR.name)
        assertLogWithMessageWasCalled(LogLevel.ERROR, LogLevel.ERROR.name)
    }

    @Test
    fun testErrorAndTagAndException() {
        error(LogLevel.ERROR.name, RuntimeException(LogLevel.ERROR.name))
        assertLogWithExceptionWasCalled(LogLevel.ERROR, LogLevel.ERROR.name)
    }

    @Test
    fun testErrorAndTagAndFunction() {
        error(LogLevel.ERROR.name) { LogLevel.ERROR.name }
        assertLogWithMessageWasCalled(LogLevel.ERROR, LogLevel.ERROR.name)
    }
}
