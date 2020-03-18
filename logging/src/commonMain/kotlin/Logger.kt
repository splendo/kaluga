package com.splendo.kaluga.logging
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

interface Logger {

    /**
     * Writes log with provided level and tag.
     * @param level Log level
     * @param tag Tag of the log record. Nullable
     * @param message Message to be written into log
     */
    fun log(level: LogLevel, tag: String?, message: String)

    /**
     * Writes exception log with provided level and tag.
     * @param level Log level
     * @param tag Tag of the log record. Nullable
     * @param exception Exception to be written into log
     */
    fun log(level: LogLevel, tag: String?, exception: Throwable)

}

internal inline fun ru.pocketbyte.kydra.log.LogLevel.getLogLevel(): LogLevel {
    return when (this) {
        ru.pocketbyte.kydra.log.LogLevel.DEBUG -> LogLevel.DEBUG
        ru.pocketbyte.kydra.log.LogLevel.INFO -> LogLevel.INFO
        ru.pocketbyte.kydra.log.LogLevel.WARNING -> LogLevel.WARNING
        ru.pocketbyte.kydra.log.LogLevel.ERROR -> LogLevel.ERROR
    }
}
