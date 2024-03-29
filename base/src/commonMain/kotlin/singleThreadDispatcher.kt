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

/**
 * Creates a coroutine execution context using a single thread.
 *
 * **NOTE: The resulting [CloseableCoroutineDispatcher] owns native resources (its thread).
 * Resources are reclaimed by [CloseableCoroutineDispatcher.close].**
 * @param name The name of the thread to be created.
 */
expect fun singleThreadDispatcher(name: String): CloseableCoroutineDispatcher

/**
 * Creates a coroutine execution context using a thread pool.
 *
 * **NOTE: The resulting [CloseableCoroutineDispatcher] owns native resources (its thread).
 * Resources are reclaimed by [CloseableCoroutineDispatcher.close].**
 * @param numberOfThreads The number of the threads that the thread pool should consist of.
 * @param name The name of the thread pool to be created.
 */
expect fun threadPoolDispatcher(numberOfThreads: UInt, name: String): CloseableCoroutineDispatcher
