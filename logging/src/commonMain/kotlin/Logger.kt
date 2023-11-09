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
 * An interface for managing logging messages of various [LogLevel]
 */
interface Logger {

    /**
     * Writes log with provided level and tag.
     * @param level Log level
     * @param tag Tag of the log record. Nullable
     * @param throwable Throwable to be written into log
     * @param message Message to be written into log
     */
    fun log(level: LogLevel, tag: String? = null, throwable: Throwable? = null, message: (() -> String)? = null)
}

/**
 * A [Logger] that transforms the logs of a given [logger] before outputting them.
 * @param logger the [Logger] whose results should be transformed
 * @param transformLogLevel When provided, changes the [LogLevel] using this transformation method
 * @param transformLogLevel When provided, changes the tag using this transformation method
 * @param transformLogLevel When provided, changes the [Throwable] using this transformation method
 * @param transformLogLevel When provided, changes the message using this transformation method
 */
open class TransformLogger(
    private val logger: Logger,
    private val transformLogLevel: ((LogLevel) -> LogLevel)? = null,
    private val transformTag: ((String?) -> String?)? = null,
    private val transformThrowable: ((Throwable?) -> Throwable?)? = null,
    private val transformMessage: ((String?) -> String?)? = null,
) : Logger {

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        val logLevel = transformLogLevel?.invoke(level) ?: level
        val logTag = transformTag?.invoke(tag) ?: tag
        val logThrowable = transformThrowable?.invoke(throwable) ?: throwable
        val logMessage = transformMessage?.invoke(message?.invoke())?.let { { it } } ?: message
        logger.log(
            logLevel,
            logTag,
            logThrowable,
            // already resolve the lazy message if it needs transformation (else we cannot transform it)
            // after transform wrap it in a "lazy" closure again {{🤗}}
            logMessage,
        )
    }
}

// DEBUG //////////////////////////////////////////////////////////////

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.debug(tag: String?, message: String) {
    log(level = LogLevel.DEBUG, tag = tag) { message }
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.debug(message: String) {
    log(level = LogLevel.DEBUG) { message }
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.debug(tag: String? = null, throwable: Throwable) {
    log(LogLevel.DEBUG, tag, throwable, null)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.debug(throwable: Throwable) = log(LogLevel.DEBUG, throwable = throwable, message = null)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.debug(tag: String?, message: () -> String) = log(LogLevel.DEBUG, tag, message = message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.debug(message: () -> String) = log(LogLevel.DEBUG, message = message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.debug(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.DEBUG, tag, throwable, message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.d(tag: String?, message: String) {
    log(LogLevel.DEBUG, tag) { message }
}

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.d(message: String) {
    log(LogLevel.DEBUG) { message }
}

/**
 * Log exception with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.d(tag: String? = null, throwable: Throwable) {
    log(LogLevel.DEBUG, tag, throwable, null)
}

/**
 * Log exception with DEBUG log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.d(throwable: Throwable) = log(LogLevel.DEBUG, throwable = throwable, message = null)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.d(tag: String?, message: () -> String) = log(LogLevel.DEBUG, tag, message = message)

/**
 * Writes log with DEBUG log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.d(message: () -> String) = log(LogLevel.DEBUG, message = message)

/**
 * Writes log with DEBUG log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.d(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.DEBUG, tag, throwable, message)

// INFO /////////////////////////////////////////////////////////////////

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.info(tag: String?, message: String) {
    log(LogLevel.INFO, tag) { message }
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.info(message: String) {
    log(LogLevel.INFO) { message }
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.info(tag: String? = null, throwable: Throwable) {
    log(LogLevel.INFO, tag, throwable, null)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.info(throwable: Throwable) = log(LogLevel.INFO, throwable = throwable, message = null)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.info(tag: String?, message: () -> String) = log(LogLevel.INFO, tag, message = message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.info(message: () -> String) = log(LogLevel.INFO, message = message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.info(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.INFO, tag, throwable, message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.i(tag: String?, message: String) {
    log(LogLevel.INFO, tag) { message }
}

/**
 * Writes log with INFO log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.i(message: String) {
    log(LogLevel.INFO) { message }
}

/**
 * Log exception with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.i(tag: String? = null, throwable: Throwable) {
    log(LogLevel.INFO, tag, throwable, null)
}

/**
 * Log exception with INFO log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.i(throwable: Throwable) = log(LogLevel.INFO, throwable = throwable, message = null)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.i(tag: String?, message: () -> String) = log(LogLevel.INFO, tag, message = message)

/**
 * Writes log with INFO log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.i(message: () -> String) = log(LogLevel.INFO, message = message)

/**
 * Writes log with INFO log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.i(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.INFO, tag, throwable, message)

// WARN /////////////////////////////////////////////////////////////////

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.warn(tag: String?, message: String) {
    log(LogLevel.WARN, tag) { message }
}

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.warn(message: String) {
    log(LogLevel.WARN) { message }
}

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.warn(tag: String? = null, throwable: Throwable) {
    log(LogLevel.WARN, tag, throwable, null)
}

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.warn(throwable: Throwable) = log(LogLevel.WARN, throwable = throwable, message = null)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.warn(tag: String?, message: () -> String) = log(LogLevel.WARN, tag, message = message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.warn(message: () -> String) = log(LogLevel.WARN, message = message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.warn(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.WARN, tag, throwable, message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.w(tag: String?, message: String) {
    log(LogLevel.WARN, tag) { message }
}

/**
 * Writes log with WARN log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.w(message: String) {
    log(LogLevel.WARN) { message }
}

/**
 * Log exception with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.w(tag: String? = null, throwable: Throwable) {
    log(LogLevel.WARN, tag, throwable, null)
}

/**
 * Log exception with WARN log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.w(throwable: Throwable) = log(LogLevel.WARN, throwable = throwable, message = null)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.w(tag: String?, message: () -> String) = log(LogLevel.WARN, tag, message = message)

/**
 * Writes log with WARN log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.w(message: () -> String) = log(LogLevel.WARN, message = message)

/**
 * Writes log with WARN log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.w(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.WARN, tag, throwable, message)

// ERROR ////////////////////////////////////////////////////////////////

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.error(tag: String?, message: String) {
    log(LogLevel.ERROR, tag) { message }
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.error(message: String) {
    log(LogLevel.ERROR) { message }
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.error(tag: String? = null, throwable: Throwable) {
    log(LogLevel.ERROR, tag, throwable, null)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.error(throwable: Throwable) = log(LogLevel.ERROR, throwable = throwable, message = null)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.error(tag: String?, message: () -> String) = log(LogLevel.ERROR, tag, message = message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.error(message: () -> String) = log(LogLevel.ERROR, message = message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.error(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.ERROR, tag, throwable, message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Message to be written into log.
 */
fun Logger.e(tag: String?, message: String) {
    log(LogLevel.ERROR, tag) { message }
}

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Message to be written into log.
 */
fun Logger.e(message: String) {
    log(LogLevel.ERROR) { message }
}

/**
 * Log exception with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.e(tag: String? = null, throwable: Throwable) {
    log(LogLevel.ERROR, tag, throwable, null)
}

/**
 * Log exception with ERROR log level and empty tag.
 * @param throwable Error or Exception to be written into log.
 */
fun Logger.e(throwable: Throwable) = log(LogLevel.ERROR, throwable = throwable, message = null)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param message Function that returns message to be written into log
 */
fun Logger.e(tag: String?, message: () -> String) = log(LogLevel.ERROR, tag, message = message)

/**
 * Writes log with ERROR log level and empty tag.
 * @param message Function that returns message to be written into log
 */
fun Logger.e(message: () -> String) = log(LogLevel.ERROR, message = message)

/**
 * Writes log with ERROR log level and provided tag.
 * @param tag Tag of the log record. Optional
 * @param throwable Error or Exception. Optional
 * @param message Message to be written into log.
 */
fun Logger.e(tag: String? = null, throwable: Throwable? = null, message: () -> String) = log(LogLevel.ERROR, tag, throwable, message)
