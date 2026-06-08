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
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.TestResult
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Runs [block] as a test and then cancels any coroutines it left running in the runner's scope, so a
 * test that deliberately launches a never-completing child (e.g. an infinite collector) does not hang.
 *
 * Returns a [TestResult] that the test framework awaits — it blocks until completion on JVM/Native and
 * returns the backing `Promise` on js/wasmJs (which is single-threaded and cannot block). Use it the
 * same way as [testRunBlocking]: `@Test fun foo() = testBlockingAndCancelScope { … }`.
 * @param context the context of the coroutine. The default value is an event loop on the current thread.
 * @param block the test body.
 */
fun testBlockingAndCancelScope(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): TestResult = testRunBlocking(context) {
    block()
    // Cancel the children the block left running (not this scope itself) so the runner can complete.
    coroutineContext.cancelChildren()
}
