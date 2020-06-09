package com.splendo.kaluga.base

import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.*
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSThread
import platform.darwin.*
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.*

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

inline fun <reified T> executeAsync(queue: NSOperationQueue, crossinline producerConsumer: () -> Pair<T, (T) -> Unit>) {
    dispatch_async_f(queue.underlyingQueue, DetachedObjectGraph {
        producerConsumer()
    }.asCPointer(), staticCFunction { it ->
        val result = DetachedObjectGraph<Pair<T, (T) -> Unit>>(it).attach()
        result.second(result.first)
    })
}

inline fun mainContinuation(singleShot: Boolean = true, noinline block: () -> Unit) = Continuation0(
    block, staticCFunction { invokerArg ->
        if (NSThread.isMainThread()) {
            invokerArg!!.callContinuation0()
        } else {
            dispatch_sync_f(dispatch_get_main_queue(), invokerArg, staticCFunction { args ->
                args!!.callContinuation0()
            })
        }
    }, singleShot)

inline fun <T1> mainContinuation(singleShot: Boolean = true, noinline block: (T1) -> Unit) = Continuation1(
    block, staticCFunction { invokerArg ->
        if (NSThread.isMainThread()) {
            invokerArg!!.callContinuation1<T1>()
        } else {
            dispatch_sync_f(dispatch_get_main_queue(), invokerArg, staticCFunction { args ->
                args!!.callContinuation1<T1>()
            })
        }
    }, singleShot)

inline fun <T1, T2> mainContinuation(singleShot: Boolean = true, noinline block: (T1, T2) -> Unit) = Continuation2(
    block, staticCFunction { invokerArg ->
        if (NSThread.isMainThread()) {
            invokerArg!!.callContinuation2<T1, T2>()
        } else {
            dispatch_sync_f(dispatch_get_main_queue(), invokerArg, staticCFunction { args ->
                args!!.callContinuation2<T1, T2>()
            })
        }
    }, singleShot)
