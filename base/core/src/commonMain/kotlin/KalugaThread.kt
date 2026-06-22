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

/**
 * `true` if called from the main thread.
 */
val isOnMainThread: Boolean
    get() = KalugaThread.currentThread.isMainThread

/**
 * The thread of execution
 */
expect class KalugaThread {

    companion object {
        /**
         * The [KalugaThread] the calling method is running on
         */
        val currentThread: KalugaThread
    }

    /**
     * Name of the thread
     */
    var name: String

    /**
     * When `true` this thread is the main thread.
     */
    val isMainThread: Boolean
}
