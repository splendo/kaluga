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

import com.splendo.kaluga.logging.LogLevel as KalugaLogLevel
import io.github.aakira.napier.Antilog as NapierLog
import io.github.aakira.napier.LogLevel as NapierLogLevel

internal val KalugaLogLevel.napierLogLevel: NapierLogLevel get() = when (this) {
    KalugaLogLevel.DEBUG -> NapierLogLevel.DEBUG
    KalugaLogLevel.ERROR -> NapierLogLevel.ERROR
    KalugaLogLevel.WARN -> NapierLogLevel.WARNING
    KalugaLogLevel.INFO -> NapierLogLevel.INFO
}

/**
 * A [Logger] that uses a [NapierLog] for actually logging.
 * @param logger the [NapierLog] handling logging.
 */
class NapierLogger(val logger: NapierLog) : Logger {
    override fun log(level: KalugaLogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        logger.log(priority = level.napierLogLevel, tag = tag, throwable = throwable, message = message?.invoke())
    }
}
