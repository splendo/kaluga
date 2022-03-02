/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import kotlin.native.concurrent.SharedImmutable
import com.splendo.kaluga.logging.LogLevel as KalugaLogLevel
import io.github.aakira.napier.Antilog as NapierLog
import io.github.aakira.napier.LogLevel as NapierLogLevel

@SharedImmutable
val logLevel = arrayOf(
    NapierLogLevel.VERBOSE,
    NapierLogLevel.DEBUG,
    NapierLogLevel.INFO,
    NapierLogLevel.WARNING,
    NapierLogLevel.ERROR,
    NapierLogLevel.ASSERT
)

fun KalugaLogLevel.logLevel(): NapierLogLevel {
    return logLevel[this.ordinal]
}

class NapierLogger(val logger: NapierLog) : Logger {
    override fun log(level: KalugaLogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        if (message != null)
            logger.log(priority = level.logLevel(), tag = tag, throwable = null, message = message())
        if (throwable != null)
            logger.log(priority = level.logLevel(), tag = tag, throwable = throwable, message = null)
    }
}
