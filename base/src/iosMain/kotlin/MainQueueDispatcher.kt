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

package com.splendo.kaluga.base

import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSThread
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_sync_f
import kotlin.native.concurrent.Continuation0
import kotlin.native.concurrent.Continuation1
import kotlin.native.concurrent.Continuation2
import kotlin.native.concurrent.callContinuation0
import kotlin.native.concurrent.callContinuation1
import kotlin.native.concurrent.callContinuation2

actual val MainQueueDispatcher: CoroutineDispatcher = Dispatchers.Main

inline fun mainContinuation(singleShot: Boolean = true, noinline block: () -> Unit) = Continuation0(
    block, 
    staticCFunction { invokerArg ->
        if (NSThread.isMainThread()) {
            invokerArg!!.callContinuation0()
        } else {
            dispatch_sync_f(
                dispatch_get_main_queue(), 
                invokerArg, 
                staticCFunction { args ->
                    args!!.callContinuation0() 
                }
            )
        }
    }, 
    singleShot)

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
