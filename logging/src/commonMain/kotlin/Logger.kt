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
     * @param throwable Throwable to be written into log
     * @param message Message to be written into log
     */
    fun log(level: LogLevel, tag: String? = null, throwable: Throwable? = null, message: (() -> String)? = null)
}

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
            logMessage
        )
    }
}
