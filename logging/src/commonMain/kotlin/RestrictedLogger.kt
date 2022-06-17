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

sealed class RestrictedLogLevel(val levels: Set<LogLevel>) {

    companion object {
        val Verbose = Info or Error or Debug or Warn
        val None: RestrictedLogLevel = Custom(emptySet())
    }

    object Info : RestrictedLogLevel(setOf(LogLevel.INFO))
    object Error : RestrictedLogLevel(setOf(LogLevel.ERROR))
    object Debug : RestrictedLogLevel(setOf(LogLevel.DEBUG))
    object Warn : RestrictedLogLevel(setOf(LogLevel.WARN))
    internal class Custom(levels: Set<LogLevel>) : RestrictedLogLevel(levels)
}

infix fun RestrictedLogLevel.or(other: RestrictedLogLevel): RestrictedLogLevel = RestrictedLogLevel.Custom(levels + other.levels)

class RestrictedLogger(private val restrictedLogLevel: RestrictedLogLevel) {

    fun info(tag: String, message: () -> String) {
        if (restrictedLogLevel.levels.contains(LogLevel.INFO)) {
            com.splendo.kaluga.logging.info(tag, message)
        }
    }

    fun error(tag: String, message: () -> String) {
        if (restrictedLogLevel.levels.contains(LogLevel.ERROR)) {
            com.splendo.kaluga.logging.error(tag, message)
        }
    }

    fun debug(tag: String, message: () -> String) {
        if (restrictedLogLevel.levels.contains(LogLevel.DEBUG)) {
            com.splendo.kaluga.logging.debug(tag, message)
        }
    }

    fun warn(tag: String, message: () -> String) {
        if (restrictedLogLevel.levels.contains(LogLevel.WARN)) {
            com.splendo.kaluga.logging.warn(tag, message)
        }
    }
}
