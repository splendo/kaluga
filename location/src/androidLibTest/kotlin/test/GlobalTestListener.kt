package com.splendo.kaluga.test
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

import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.Logger
import com.splendo.kaluga.log.initLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

// Android Studio will shown an error when parsing this file because it's also defined in the unit test module
// To clear the error close the file and restart Android Studio ¯\_(ツ)_/¯
actual class GlobalTestListener {

    private val mainDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext("UI thread")

    actual fun beforeTest() {
        Dispatchers.setMain(mainDispatcher)
        initLogger(object : Logger {
            override fun log(level: LogLevel, tag: String?, message: String) {
                println("${level.name}\t:$tag\t:$message")
            }

            override fun log(level: LogLevel, tag: String?, exception: Throwable) {
                println("${level.name}\t:$tag\t:${exception.message}")
                exception.printStackTrace()
            }
        })
    }

    actual fun afterTest() {
        Dispatchers.resetMain()
        mainDispatcher.close()
    }
}