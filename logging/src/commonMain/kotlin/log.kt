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

import ru.pocketbyte.kydra.log.KydraLog

/**
 * Initializes KydraLog with given logger.
 *
 * Be aware that once initialized KydraLog cannot be initialised with new loggers. You will get first logger you have used for initialisation.
 *
 * @param logger - logged to be used for log output
 * @return first logger used for initialisation.
 */
fun initLogger(logger: Logger): Logger {
    try {
        KydraLog.init(InternalLogger(logger))
    } catch (e: IllegalStateException) {
        println("LOG: KydraLog was already initialised. {${e.message}}")
    }

    return logger()
}

/**
 * @return first logger you have used for initialisation.
 */
fun logger(): Logger {
    try {
        return (KydraLog.logger as InternalLogger).logger
    } catch (e: Exception) {
        throw IllegalStateException("You should use initLogger for logging initialisation", e)
    }
}

/**
 * Writes log with provided level and tag.
 * @param level Log level
 * @param tag Log tag
 * @param message Message to be written into log
 */
fun log(level: LogLevel, tag: String? = null, message: String) {
    KydraLog.log(level.logLevel, transformTag(tag), transformMessage(message))
}

/**
 * Writes exception log with provided level and empty tag.
 * @param level Log level
 * @param tag Log tag
 * @param exception Exception to be written into log
 */
fun log(level: LogLevel, tag: String? = null, exception: Throwable) {
    KydraLog.log(level.logLevel, transformTag(tag), exception)
}

/**
* Writes log with provided level and empty tag.
* @param level Log level
* @param tag Log tag
* @param function Function that returns message to be written into log
*/
fun log(level: LogLevel, tag: String? = null, function: () -> String) {
    KydraLog.log(level.logLevel, tag) { transformMessage(function()) }
}

// ================================================================
// == LogLevel.INFO ===============================================
/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun info(tag: String?, message: String) {
    log(LogLevel.INFO, tag, message)
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun info(message: String) {
    log(LogLevel.INFO, null, message)
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun info(tag: String?, exception: Throwable) {
    log(LogLevel.INFO, tag, exception)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun info(exception: Throwable) {
    log(LogLevel.INFO, null, exception)
}

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun info(tag: String?, function: () -> String) {
    log(LogLevel.INFO, tag, function)
}

/**
 * Writes log with INFO log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun info(function: () -> String) {
    KydraLog.log(LogLevel.INFO.logLevel, null, function)
}

// ================================================================
// == LogLevel.DEBUG ==============================================
/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun debug(tag: String?, message: String) {
    log(LogLevel.DEBUG, tag, message)
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun debug(message: String) {
    log(LogLevel.DEBUG, null, message)
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun debug(tag: String?, exception: Throwable) {
    log(LogLevel.DEBUG, tag, exception)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun debug(exception: Throwable) {
    log(LogLevel.DEBUG, null, exception)
}

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun debug(tag: String?, function: () -> String) {
    log(LogLevel.DEBUG, tag, function)
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun debug(function: () -> String) {
    log(LogLevel.DEBUG, null, function)
}

// ================================================================
// == LogLevel.WARNING ============================================
/**
 * Writes log with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun warn(tag: String?, message: String) {
    log(LogLevel.WARNING, tag, message)
}

/**
 * Writes log with WARNING log level and empty tag.
 * @param message Message to be written into log.
 */
fun warn(message: String) {
    log(LogLevel.WARNING, null, message)
}

/**
 * Log exception with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun warn(tag: String?, exception: Throwable) {
    log(LogLevel.WARNING, tag, exception)
}

/**
 * Log exception with WARNING log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun warn(exception: Throwable) {
    log(LogLevel.WARNING, null, exception)
}

/**
 * Writes log with WARNING log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun warn(tag: String?, function: () -> String) {
    log(LogLevel.WARNING, tag, function)
}

/**
 * Writes log with WARNING log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun warn(function: () -> String) {
    log(LogLevel.WARNING, null, function)
}

// ================================================================
// == LogLevel.ERROR ==============================================
/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param message Message to be written into log.
 */
fun error(tag: String?, message: String) {
    log(LogLevel.ERROR, tag, message)
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun error(message: String) {
    log(LogLevel.ERROR, null, message)
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param exception Exception to be written into log.
 */
fun error(tag: String?, exception: Throwable) {
    log(LogLevel.ERROR, tag, exception)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param exception Exception to be written into log.
 */
fun error(exception: Throwable) {
    log(LogLevel.ERROR, null, exception)
}

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Nullable
 * @param function Function that returns message to be written into log
 */
fun error(tag: String?, function: () -> String) {
    log(LogLevel.ERROR, tag, function)
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param function Function that returns message to be written into log
 */
fun error(function: () -> String) {
    log(LogLevel.ERROR, null, function)
}
