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

import com.splendo.kaluga.base.collections.concurrentMutableListOf

class LoggerMock : Logger {

    private data class Log(val logLevel: LogLevel, val tag: String?, val throwable: Throwable?, val message: (() -> String)?)

    private val logList = concurrentMutableListOf<Log>()

    val throwableList get() = logList.map { it.throwable }
    val messageList get() = logList.map { it.message }
    val tagList get() = logList.map { it.tag }
    val levelList get() = logList.map { it.logLevel }

    fun clear() = logList.clear()

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        logList.add(Log(level, tag, throwable, message))
    }
}
