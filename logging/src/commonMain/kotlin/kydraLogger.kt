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

@SharedImmutable
val logLevel = arrayOf(
    ru.pocketbyte.kydra.log.LogLevel.DEBUG,
    ru.pocketbyte.kydra.log.LogLevel.INFO,
    ru.pocketbyte.kydra.log.LogLevel.WARNING,
    ru.pocketbyte.kydra.log.LogLevel.ERROR
)

fun LogLevel.logLevel():ru.pocketbyte.kydra.log.LogLevel {
    return logLevel[this.ordinal]
    // optimization for:
    // return when(this) {
    //     LogLevel.DEBUG -> ru.pocketbyte.kydra.log.LogLevel.DEBUG
    //     LogLevel.INFO -> ru.pocketbyte.kydra.log.LogLevel.INFO
    //     LogLevel.WARNING -> ru.pocketbyte.kydra.log.LogLevel.WARNING
    //     LogLevel.ERROR -> ru.pocketbyte.kydra.log.LogLevel.ERROR
    // }
}

class KydraLogger(val kydraLogger: ru.pocketbyte.kydra.log.Logger):Logger {
    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        if (message != null)
            kydraLogger.log(level.logLevel(), tag, message())
        if (throwable != null)
            kydraLogger.log(level.logLevel(), tag, throwable)
    }
}