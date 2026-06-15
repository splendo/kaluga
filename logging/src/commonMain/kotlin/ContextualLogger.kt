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

/**
 * A logger appending contextual data to each message
 * @param logger the [Logger] to log to
 * @param tag the tag to use/append when logging within this context
 * @param context the contextual data to append to each message
 */
class ContextualLogger(private val logger: Logger, private val tag: String, private val context: Map<String, Any?> = LinkedHashMap()) : Logger {
    private val contextAsString = if (context.isEmpty()) "" else "$context"

    /**
     * Creates a new [ContextualLogger] that logs the same contextual data as this logger to a new [Logger]
     * @param logger the [Logger] to log to
     * @return the new [ContextualLogger]
     */
    fun withLogger(logger: Logger) = ContextualLogger(logger, tag, context)

    /**
     * Creates a new [ContextualLogger] that logs the same contextual data with a new tag
     * @param tag the new tag to use
     * @return the new [ContextualLogger]
     */
    fun withAppendedTag(tag: String) = ContextualLogger(logger, "${this.tag}:$tag", context)

    /**
     * Creates a new [ContextualLogger] that logs the same contextual data with additional contextual data
     * @param keyValue the contextual data to append
     * @param keysAndValues additional contextual data to append
     * @return the new [ContextualLogger]
     */
    fun withAppendedContext(keyValue: Pair<String, Any?>, vararg keysAndValues: Pair<String, Any?>): ContextualLogger = ContextualLogger(
        logger,
        tag,
        LinkedHashMap(context).apply {
            put(keyValue.first, keyValue.second)
            keysAndValues.forEach { (key, value) -> put(key, value) }
        },
    )

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        logger.log(
            level = level,
            tag = tag?.let { "${this.tag}:$it" } ?: this.tag,
            throwable = throwable,
        ) {
            message?.let {
                "${it()} $contextAsString"
            } ?: contextAsString
        }
    }
}
