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

package com.splendo.kaluga.test.base

import com.splendo.kaluga.logging.debug
import platform.CoreFoundation.CFRunLoopRun
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.internal.test.testLauncherEntryPoint
import kotlin.system.exitProcess

@OptIn(ObsoleteWorkersApi::class)
fun mainBackground(args: Array<String>) {
    debug("using background thread for iOS tests")
    val worker = Worker.start(name = "kaluga-test-background")
    worker.execute(TransferMode.SAFE, { args }) { // TransferMode has no effect with the new memory manager in use
        val result = testLauncherEntryPoint(it)
        exitProcess(result)
    }
    CFRunLoopRun()
    error("CFRunLoopRun should never return")
}
