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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestResult
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Runs [block] as the body of a test and returns a [TestResult] that the Kotlin test framework awaits:
 * it blocks until completion on JVM/Native, and returns the backing `Promise` on js/wasmJs (which is
 * single-threaded and cannot block). Returning the result from a `@Test` function is what lets
 * suspending tests actually run on the JS family, rather than firing-and-forgetting as a bare
 * [com.splendo.kaluga.base.runBlocking] would.
 * @param context the context of the coroutine. The default value is an event loop on the current thread.
 * @param block the test body.
 */
expect fun testRunBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): TestResult
