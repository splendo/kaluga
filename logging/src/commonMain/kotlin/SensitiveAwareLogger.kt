/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.logging.SensitiveAwareLogger.SensitiveAwareContext

/** Logger that correctly handles a sensitive data. */
interface SensitiveAwareLogger {
    val tag: String

    fun debug(throwable: Throwable? = null, message: SensitiveAwareContext.() -> String)
    fun info(throwable: Throwable? = null, message: SensitiveAwareContext.() -> String)
    fun warn(throwable: Throwable? = null, message: SensitiveAwareContext.() -> String)
    fun error(throwable: Throwable? = null, message: SensitiveAwareContext.() -> String)

    fun interface SensitiveAwareContext {
        /**
         * Transforms an [input] in a string acceptable by this logger
         * @param input a sensitive input
         * @return a transformed (possibly redacted) string
         */
        fun sensitive(input: String): String

        /** A convenience extension to call [sensitive] method on an object.*/
        val Any?.sensitive: String get() = sensitive(toString())
    }

    /**
     * Creates a nested logger with a new [tag]
     * @param tag a tag for a new logger
     * @return a new logger with same settings but a new [tag]
     */
    fun withTag(tag: String): SensitiveAwareLogger

    companion object Factory {
        fun of(tag: String, logger: Logger, logSensitiveData: Boolean): SensitiveAwareLogger = DefaultSensitiveAwareLogger(
            tag = tag,
            logger = logger,
            sensitiveDataTransformer = if (logSensitiveData) {
                SensitiveAwareContext { it }
            } else {
                SensitiveAwareContext { "#####" }
            },
        )
    }
}

private class DefaultSensitiveAwareLogger(override val tag: String, private val logger: Logger, private val sensitiveDataTransformer: SensitiveAwareContext) :
    SensitiveAwareLogger {
    override fun debug(throwable: Throwable?, message: SensitiveAwareContext.() -> String) {
        logger.debug(tag, throwable) { message(sensitiveDataTransformer) }
    }

    override fun info(throwable: Throwable?, message: SensitiveAwareContext.() -> String) {
        logger.info(tag, throwable) { message(sensitiveDataTransformer) }
    }

    override fun warn(throwable: Throwable?, message: SensitiveAwareContext.() -> String) {
        logger.warn(tag, throwable) { message(sensitiveDataTransformer) }
    }

    override fun error(throwable: Throwable?, message: SensitiveAwareContext.() -> String) {
        logger.error(tag, throwable) { message(sensitiveDataTransformer) }
    }

    override fun withTag(tag: String): SensitiveAwareLogger = DefaultSensitiveAwareLogger(tag, logger, sensitiveDataTransformer)
}
