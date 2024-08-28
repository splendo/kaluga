/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.base

import kotlinx.coroutines.Dispatchers

/**
 * The thread of execution
 */
actual data class KalugaThread(val thread: Thread) {

    actual companion object {

        /**
         * The [KalugaThread] the calling method is running on
         */
        actual val currentThread: KalugaThread get() = KalugaThread(Thread.currentThread())
    }

    /**
     * Name of the thread
     */
    actual var name: String by thread::name

    /**
     * When `true` this thread is the main thread.
     */
    actual val isMainThread: Boolean get() = runBlocking(Dispatchers.Main.immediate) {
        // safest way also for synthetic main threads
        thread == Thread.currentThread()
    }
}
