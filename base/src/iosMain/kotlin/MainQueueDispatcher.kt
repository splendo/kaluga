package com.splendo.kaluga.base

import kotlinx.coroutines.*
import platform.Foundation.NSThread
import platform.darwin.*
import kotlin.coroutines.CoroutineContext

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

internal class NsQueueDispatcher(private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher(), Delay {

    // Dispatch block on given queue
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (NSThread.currentThread().isMainThread) {
               println("running direct...")
                block.run()
            }
        else
            dispatch_async(dispatchQueue) {
                    println("running async...")
                    block.run()
                }
    }

    // Support Delay
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, timeMillis * 1_000_000), dispatchQueue) {
            with(continuation) {
                resumeUndispatched(Unit)
            }
        }
    }
}

actual val MainQueueDispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())
