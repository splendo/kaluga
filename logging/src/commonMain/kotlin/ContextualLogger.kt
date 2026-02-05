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
 */
class ContextualLogger(private val logger: Logger, private val tag: String, private val context: Map<String, Any?> = LinkedHashMap()) : Logger {
    private val contextAsString = if (context.isEmpty()) "" else "$context"

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
