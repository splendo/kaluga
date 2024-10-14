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

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DeliberateCancellationException(val result: Any?) : kotlinx.coroutines.CancellationException("Scope canceled deliberately by testAndCancelScope")

inline fun <reified T> testBlockingAndCancelScope(context: CoroutineContext = EmptyCoroutineContext, crossinline block: suspend CoroutineScope.() -> T): T {
    try {
        return runBlocking(context) {
            block().also { cancel(DeliberateCancellationException(it)) }
        }
    } catch (e: Throwable) {
        if (e is DeliberateCancellationException && e.result is T) {
            return e.result
        }
        throw e // not our expected exception
    }
}
