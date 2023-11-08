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

// the default logger should be set per platform
/**
 * The default [Logger] to be used if no logger is set.
 */
expect val defaultLogger: Logger

/**
 * The standard [Logger] to be used when logging.
 *
 * This is used by the package level logging methods, and might be used directly as well
 */
expect var logger: Logger // how the actual logger is set can also differ per platform (e.g. Atomic references on native) so we expect it

/**
 * Reset [logger] to the default
 */
fun resetLogger() {
    logger = defaultLogger
}

/**
 * Wraps [logger] with a wrapper method
 * @param wrapper a method for wrapping the standard logger into a different logger.
 */
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
fun log(level: LogLevel, tag: String? = null, throwable: Throwable? = null, message: (() -> String)?) {
    logger.log(level, tag, throwable, message)
}

// DEBUG //////////////////////////////////////////////////////////////

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun debug(tag: String?, message: String) {
    logger.debug(tag, message)
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun debug(message: String) {
    logger.debug(message)
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun debug(tag: String? = null, throwable: Throwable) {
    logger.debug(tag, throwable)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun debug(throwable: Throwable) = logger.debug(throwable = throwable)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun debug(tag: String?, message: () -> String) = logger.debug(tag, message = message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun debug(message: () -> String) = logger.debug(message = message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun debug(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.debug(tag, throwable, message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun d(tag: String?, message: String) {
    logger.d(tag) { message }
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun d(message: String) {
    logger.d { message }
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun d(tag: String? = null, throwable: Throwable) {
    logger.d(tag, throwable)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun d(throwable: Throwable) = logger.d(throwable)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun d(tag: String?, message: () -> String) = logger.d(tag, message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun d(message: () -> String) = logger.d(message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun d(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.d(tag, throwable, message)

// INFO /////////////////////////////////////////////////////////////////

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun info(tag: String?, message: String) = logger.info(tag, message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun info(message: String) = logger.info(message)

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun info(tag: String? = null, throwable: Throwable) = logger.info(tag, throwable)

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun info(throwable: Throwable) = logger.info(throwable)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun info(tag: String?, message: () -> String) = logger.info(tag, message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun info(message: () -> String) = logger.info(message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun info(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.info(tag, throwable, message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun i(tag: String?, message: String) = logger.i(tag, message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun i(message: String) = logger.i(message)

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun i(tag: String? = null, throwable: Throwable) = logger.i(tag, throwable)

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun i(throwable: Throwable) = logger.i(throwable)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun i(tag: String?, message: () -> String) = logger.i(tag, message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun i(message: () -> String) = logger.i(message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun i(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.i(tag, throwable, message)

// WARN /////////////////////////////////////////////////////////////////

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun warn(tag: String?, message: String) = logger.warn(tag, message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun warn(message: String) = logger.warn(message)

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun warn(tag: String? = null, throwable: Throwable) = logger.warn(tag, throwable)

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun warn(throwable: Throwable) = logger.warn(throwable)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun warn(tag: String?, message: () -> String) = logger.warn(tag, message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun warn(message: () -> String) = logger.warn(message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun warn(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.warn(tag, throwable, message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun w(tag: String?, message: String) = logger.w(tag, message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun w(message: String) = logger.w(message)

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun w(tag: String? = null, throwable: Throwable) = logger.w(tag, throwable)

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun w(throwable: Throwable) = logger.w(throwable)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun w(tag: String?, message: () -> String) = logger.w(tag, message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun w(message: () -> String) = logger.w(message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun w(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.w(tag, throwable, message)

// ERROR ////////////////////////////////////////////////////////////////

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun error(tag: String?, message: String) = logger.error(tag, message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun error(message: String) = logger.error(message)

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun error(tag: String? = null, throwable: Throwable) = logger.error(tag, throwable)

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun error(throwable: Throwable) = logger.error(throwable)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun error(tag: String?, message: () -> String) = logger.error(tag, message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun error(message: () -> String) = logger.error(message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun error(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.error(tag, throwable, message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun e(tag: String?, message: String) = logger.e(tag, message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun e(message: String) = logger.e(message)

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun e(tag: String? = null, throwable: Throwable) = logger.e(tag, throwable)

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun e(throwable: Throwable) = logger.e(throwable)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun e(tag: String?, message: () -> String) = logger.e(tag, message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun e(message: () -> String) = logger.e(message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun e(tag: String? = null, throwable: Throwable? = null, message: () -> String) = logger.e(tag, throwable, message)
