/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.concurrency.AtomicBoolean
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.native.concurrent.ThreadLocal
import kotlin.native.internal.GC

@SharedImmutable
private val isCollecting: AtomicBoolean = AtomicBoolean(false)

@ThreadLocal
object GCScheduler {

    /**
     * Schedules the Garbage Collector only if no other garbage collection is active.
     * This is useful in case calling the Garbage collector could trigger another call to [GC.collect].
     */
    fun schedule() {
        MainScope().launch { collectIfNotCollecting() }
    }

    /**
     * Runs the Garbage Collector only if no other garbage collection is active.
     * This is useful in case calling the Garbage collector could trigger another call to [GC.collect].
     */
    fun collectIfNotCollecting() {
        if (isCollecting.compareAndSet(expected = false, new = true)) {
            GC.collect()
            isCollecting.value = false
        }
    }
}
