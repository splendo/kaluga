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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Returns a list of flow items captured for [duration]
 * @param duration of capturing
 * @return a list of flow items captured for [duration]
 */
suspend fun <T> Flow<T>.captureFor(duration: Duration): List<T> {
    val output = concurrentMutableListOf<T>()

    try {
        withTimeout(duration) {
            collect { output += it }
        }
    } catch (e: TimeoutCancellationException) {
        // ignore
    }
    return output
}

/** @return the first element matching [condition] within [timeout]. */
private suspend fun <T> Flow<T>.awaitFirst(message: String, expectedResult: String, timeout: Duration, condition: suspend (T) -> Boolean = { true }): T {
    val output = concurrentMutableListOf<T>()
    return try {
        withTimeout(timeout) {
            onEach { element ->
                output += element
            }.first(condition)
        }
    } catch (e: TimeoutCancellationException) {
        fail(
            listOf(message, expectedResult, "Elements emitted: $output")
                .filter { it.isNotEmpty() }
                .joinToString(separator = ". "),
        )
    }
}

private val defaultTimeout = 5.seconds

/** @return the first element matching [condition] within [timeout]. */
suspend fun <T> Flow<T>.awaitFirst(message: String = "", timeout: Duration = defaultTimeout, condition: suspend (T) -> Boolean = { true }): T =
    awaitFirst(message, "No element matching condition found.", timeout, condition)

/** Asserts that the flow emits an element matching [condition] within [timeout]. */
suspend fun <T> Flow<T>.assertEmits(message: String = "", timeout: Duration = defaultTimeout, condition: suspend (T) -> Boolean) {
    awaitFirst(message, timeout, condition)
}

/** Asserts that the flow emits an element matching [expected] within [timeout]. */
suspend fun <T> Flow<T>.assertEmits(expected: T, message: String = "", timeout: Duration = defaultTimeout) {
    awaitFirst(message, "Expected to emit $expected.", timeout) { it == expected }
}
