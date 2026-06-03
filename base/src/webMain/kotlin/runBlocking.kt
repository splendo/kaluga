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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

/**
 * Runs a new coroutine. The JS family (js + wasmJs) is single-threaded and cannot block, so [block]
 * is launched on [GlobalScope] and the (not-yet-completed) [kotlinx.coroutines.Deferred] is returned
 * cast to [T] — i.e. this does not actually block. Suitable only for fire-and-forget bridging such as
 * [com.splendo.kaluga.base.state.KalugaState.peekState]; to drive suspending tests use the
 * `TestResult`-returning runner in `test-utils-base` instead.
 * @param context the context of the coroutine. The default value is an event loop on the current thread.
 * @param block the coroutine code.
 */
@OptIn(DelicateCoroutinesApi::class)
actual fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T {
    @Suppress("UNCHECKED_CAST")
    return GlobalScope.async(context) { block(this) } as T
}
