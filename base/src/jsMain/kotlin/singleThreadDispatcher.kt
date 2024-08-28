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

import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

/**
 * Creates a coroutine execution context using a single thread.
 * Since JavaScript does not have threading support, this will just return [Dispatchers.Default]
 * @param name The name of the thread to be created.
 */
actual fun singleThreadDispatcher(name: String): CloseableCoroutineDispatcher = CoroutineDispatcherWrapper(Dispatchers.Default)

/**
 * Creates a coroutine execution context using a thread pool.
 * Since JavaScript does not have threading support, this will just return [Dispatchers.Default]
 * @param numberOfThreads The number of the threads that the thread pool should consist of.
 * @param name The name of the thread pool to be created.
 */
actual fun threadPoolDispatcher(numberOfThreads: UInt, name: String): CloseableCoroutineDispatcher = CoroutineDispatcherWrapper(Dispatchers.Default)

private class CoroutineDispatcherWrapper(private val base: CoroutineDispatcher) : CloseableCoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        base.dispatch(context, block)
    }
    override fun close() = Unit
}
