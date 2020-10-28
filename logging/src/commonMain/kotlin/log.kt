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

// the default logger should be set per platform
expect val defaultLogger: Logger

/**
 * The standard logger.
 *
 * This is used by the package level logging methods, and might be used directly as well
 */
expect var logger:Logger // how the actual logger is set can also differ per platform (e.g. Atomic references on native) so we expect it

/**
 * Reset the standard logger to the default
 */
fun resetLogger() {
    logger = defaultLogger
}
    
fun wrapLogger(wrapper: (Logger) -> Logger) {
    logger = wrapper(logger)
}

/**
 * Writes log with provided level and tag.
 * @param level Log level
 * @param tag Log tag. Optional.
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log
 */
fun log(level: LogLevel, tag: String? = null, throwable: Throwable? = null, message:(()->String)?) {
    logger.log(level, tag, throwable, message)
}

// DEBUG //////////////////////////////////////////////////////////////

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun debug(tag: String?, message: String) {
    log(LogLevel.DEBUG, tag) { message }
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun debug(message: String) {
    log(LogLevel.DEBUG) { message }
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun debug(tag: String? = null, throwable: Throwable) {
    log(LogLevel.DEBUG, tag, throwable, null)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun debug(throwable: Throwable) = log(LogLevel.DEBUG, throwable = throwable, message = null)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun debug(tag: String?, message: () -> String) = log(LogLevel.DEBUG, tag, message = message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun debug(message: () -> String) = log(LogLevel.DEBUG, message = message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun debug(tag: String? = null, throwable: Throwable? = null, message: ()->String) =
    log(LogLevel.DEBUG, tag, throwable, message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun d(tag: String?, message: String) {
    log(LogLevel.DEBUG, tag) { message }
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun d(message: String) {
    log(LogLevel.DEBUG) { message }
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun d(tag: String? = null, throwable: Throwable) {
    log(LogLevel.DEBUG, tag, throwable, null)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun d(throwable: Throwable) = log(LogLevel.DEBUG, throwable = throwable, message = null)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun d(tag: String?, message: () -> String) = log(LogLevel.DEBUG, tag, message = message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun d(message: () -> String) = log(LogLevel.DEBUG, message = message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun d(tag: String? = null, throwable: Throwable? = null, message: ()->String) = log(LogLevel.DEBUG, tag, throwable, message)

// INFO /////////////////////////////////////////////////////////////////

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun info(tag: String?, message: String) {
    log(LogLevel.INFO, tag) { message }
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun info(message: String) {
    log(LogLevel.INFO) { message }
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun info(tag: String? = null, throwable: Throwable) {
    log(LogLevel.INFO, tag, throwable, null)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun info(throwable: Throwable) = log(LogLevel.INFO, throwable = throwable, message = null)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun info(tag: String?, message: () -> String) = log(LogLevel.INFO, tag, message = message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun info(message: () -> String) = log(LogLevel.INFO, message = message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun info(tag: String? = null, throwable: Throwable? = null, message: ()->String) =
    log(LogLevel.INFO, tag, throwable, message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun i(tag: String?, message: String) {
    log(LogLevel.INFO, tag) { message }
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun i(message: String) {
    log(LogLevel.INFO) { message }
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun i(tag: String? = null, throwable: Throwable) {
    log(LogLevel.INFO, tag, throwable, null)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun i(throwable: Throwable) = log(LogLevel.INFO, throwable = throwable, message = null)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun i(tag: String?, message: () -> String) = log(LogLevel.INFO, tag, message = message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun i(message: () -> String) = log(LogLevel.INFO, message = message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun i(tag: String? = null, throwable: Throwable? = null, message: ()->String) = log(LogLevel.INFO, tag, throwable, message)

// WARN /////////////////////////////////////////////////////////////////

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun warn(tag: String?, message: String) {
    log(LogLevel.WARN, tag) { message }
}

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun warn(message: String) {
    log(LogLevel.WARN) { message }
}

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun warn(tag: String? = null, throwable: Throwable) {
    log(LogLevel.WARN, tag, throwable, null)
}

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun warn(throwable: Throwable) = log(LogLevel.WARN, throwable = throwable, message = null)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun warn(tag: String?, message: () -> String) = log(LogLevel.WARN, tag, message = message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun warn(message: () -> String) = log(LogLevel.WARN, message = message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun warn(tag: String? = null, throwable: Throwable? = null, message: ()->String) =
    log(LogLevel.WARN, tag, throwable, message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun w(tag: String?, message: String) {
    log(LogLevel.WARN, tag) { message }
}

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun w(message: String) {
    log(LogLevel.WARN) { message }
}

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun w(tag: String? = null, throwable: Throwable) {
    log(LogLevel.WARN, tag, throwable, null)
}

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun w(throwable: Throwable) = log(LogLevel.WARN, throwable = throwable, message = null)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun w(tag: String?, message: () -> String) = log(LogLevel.WARN, tag, message = message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun w(message: () -> String) = log(LogLevel.WARN, message = message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun w(tag: String? = null, throwable: Throwable? = null, message: ()->String) = log(LogLevel.WARN, tag, throwable, message)

// ERROR ////////////////////////////////////////////////////////////////

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun error(tag: String?, message: String) {
    log(LogLevel.ERROR, tag) { message }
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun error(message: String) {
    log(LogLevel.ERROR) { message }
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun error(tag: String? = null, throwable: Throwable) {
    log(LogLevel.ERROR, tag, throwable, null)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun error(throwable: Throwable) = log(LogLevel.ERROR, throwable = throwable, message = null)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun error(tag: String?, message: () -> String) = log(LogLevel.ERROR, tag, message = message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun error(message: () -> String) = log(LogLevel.ERROR, message = message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun error(tag: String? = null, throwable: Throwable? = null, message: ()->String) =
    log(LogLevel.ERROR, tag, throwable, message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun e(tag: String?, message: String) {
    log(LogLevel.ERROR, tag) { message }
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun e(message: String) {
    log(LogLevel.ERROR) { message }
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun e(tag: String? = null, throwable: Throwable) {
    log(LogLevel.ERROR, tag, throwable, null)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun e(throwable: Throwable) = log(LogLevel.ERROR, throwable = throwable, message = null)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun e(tag: String?, message: () -> String) = log(LogLevel.ERROR, tag, message = message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun e(message: () -> String) = log(LogLevel.ERROR, message = message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun e(tag: String? = null, throwable: Throwable? = null, message: ()->String) = log(LogLevel.ERROR, tag, throwable, message)

