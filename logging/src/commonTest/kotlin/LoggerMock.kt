package com.splendo.kaluga.log
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

class LoggerMock : Logger {

    val exceptionsList: MutableList<Throwable?> = mutableListOf()
    val messageList: MutableList<String?> = mutableListOf()
    val tagList: MutableList<String?> = mutableListOf()
    val levelList: MutableList<LogLevel?> = mutableListOf()

    override fun log(level: LogLevel, tag: String?, message: String) {
        this.levelList.add(level)
        this.tagList.add(tag)
        this.messageList.add(message)
        this.exceptionsList.add(null)
    }

    override fun log(level: LogLevel, tag: String?, exception: Throwable) {
        this.levelList.add(level)
        this.tagList.add(tag)
        this.messageList.add(null)
        this.exceptionsList.add(exception)
    }

    fun clear() {
        levelList.clear()
        messageList.clear()
        tagList.clear()
        exceptionsList.clear()
    }

}