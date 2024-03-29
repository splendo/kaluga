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

package com.splendo.kaluga.base.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job

/**
 * Completes an [EmptyCompletableDeferred] with [Unit]
 */
fun EmptyCompletableDeferred.complete() {
    complete(Unit)
}

typealias EmptyCompletableDeferred = CompletableDeferred<Unit>

/**
 * Creates a [CompletableDeferred] in an _active_ state of typed [Unit].
 * In other words, it returns no value (either it will complete or throw an Exception).
 * It is optionally a child of a [parent] job.
 */
@Suppress("FunctionName") // Kotlin convention to provide package methods that look like constructors
fun EmptyCompletableDeferred(parent: Job? = null): EmptyCompletableDeferred = CompletableDeferred(parent)
