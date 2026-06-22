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
 * The thread of execution
 * On JavaScript there is only one thread, so everything is the main thread
 */
actual class KalugaThread {

    actual companion object {
        /**
         * The [KalugaThread] the calling method is running on.
         * Since on JavaScript there is only one thread, this always results in the same thread.
         */
        actual val currentThread: KalugaThread get() = KalugaThread()
    }

    /**
     * Name of the thread
     */
    actual var name: String = "Main Thread"
        set(_) = Unit

    /**
     * Always `true` since on JavaScript there is only one thread.
     */
    actual val isMainThread: Boolean = true

    override fun equals(other: Any?): Boolean = other is KalugaThread

    override fun hashCode(): Int = this::class.js.hashCode()
}
