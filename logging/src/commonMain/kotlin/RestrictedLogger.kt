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

/**
 * A LogLevel that only outputs if a [Logger] attempts to log with a [LogLevel] matching the restriction.
 * @property levels the set of [LogLevel] that are allowed to be logged.
 */
sealed class RestrictedLogLevel(val levels: Set<LogLevel>) {

    companion object {

        /**
         * A [RestrictedLogLevel] that logs all [LogLevel]
         */
        val Verbose = Info or Error or Debug or Warn

        /**
         * A [RestrictedLogLevel] that logs none of the [LogLevel]
         */
        val None: RestrictedLogLevel = Custom(emptySet())
    }

    /**
     * A [RestrictedLogLevel] that logs only [LogLevel.INFO]
     */
    data object Info : RestrictedLogLevel(setOf(LogLevel.INFO))

    /**
     * A [RestrictedLogLevel] that logs only [LogLevel.ERROR]
     */
    data object Error : RestrictedLogLevel(setOf(LogLevel.ERROR))

    /**
     * A [RestrictedLogLevel] that logs only [LogLevel.DEBUG]
     */
    data object Debug : RestrictedLogLevel(setOf(LogLevel.DEBUG))

    /**
     * A [RestrictedLogLevel] that logs only [LogLevel.WARN]
     */
    data object Warn : RestrictedLogLevel(setOf(LogLevel.WARN))
    internal class Custom(levels: Set<LogLevel>) : RestrictedLogLevel(levels)
}

/**
 * Creates a [RestrictedLogLevel] by combining the [RestrictedLogLevel.levels] of two Restricted Log Levels.
 * @param other The [RestrictedLogLevel] to combine with
 * @return A [RestrictedLogLevel] whose [RestrictedLogLevel.levels] contains all [LogLevel] of both Restricted Log Levels.
 */
infix fun RestrictedLogLevel.or(other: RestrictedLogLevel): RestrictedLogLevel = RestrictedLogLevel.Custom(levels + other.levels)

/**
 * A [Logger] that logs only when the [LogLevel] is allowed by its [RestrictedLogLevel]
 * @param restrictedLogLevel The [RestrictedLogLevel] to limit the [LogLevel] allowed
 * @param logger The [Logger] to actually log.
 */
class RestrictedLogger(private val restrictedLogLevel: RestrictedLogLevel, private val logger: Logger = defaultLogger) : Logger {

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        if (restrictedLogLevel.levels.contains(level)) {
            logger.log(level = level, tag = tag, throwable = throwable, message = message)
        }
    }
}
