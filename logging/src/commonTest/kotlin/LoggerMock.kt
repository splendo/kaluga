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

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

class LoggerMock : SynchronizedObject(), Logger {

    val throwableList = mutableListOf<Throwable?>()
    val messageList = mutableListOf<String?>()
    val tagList = mutableListOf<String?>()
    val levelList = mutableListOf<LogLevel?>()

    fun clear() = synchronized(this) {
        levelList.clear()
        messageList.clear()
        tagList.clear()
        throwableList.clear()
    }

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
        synchronized(this) {
            levelList.add(level)
            tagList.add(tag)
            throwableList.add(throwable)
            messageList.add(message?.invoke())
        }
    }
}
