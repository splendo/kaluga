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

package com.splendo.kaluga.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

/**
 * Runs a new coroutine and blocks the current thread interruptibly until its completion.
 * Since JavaScript does not support threading, this will just run block on [GlobalScope]
 * @param context the context of the coroutine. The default value is an event loop on the current thread.
 * @param block the coroutine code.
 */
actual fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T {
    // this does not wait for the result unfortunately
    val result = GlobalScope.async { block(this) }
    while (!result.isCompleted) {
        // DO NOTHING
    }
    return result.getCompleted()
}
