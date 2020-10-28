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

package com.splendo.kaluga.test.utils

import kotlinx.coroutines.delay

val DefaultTimeout = 2000

suspend fun <T> List<T>.awaitElementAt(index: Int, timeOut: Int = DefaultTimeout): T {
    var totalWaitTime = 0
    while (totalWaitTime < timeOut) {
        if (size > index) {
            return get(index)
        }
        delay(100)
        totalWaitTime += 100
    }
    throw TestFailedWithTimeoutException
}

suspend fun <T : Comparable<T>> awaitEquals(expected: T, current: (() -> T), timeOut: Int = DefaultTimeout) {
    var totalWaitTime = 0
    while (totalWaitTime < timeOut) {
        if (expected == current()) {
            return
        }
        delay(100)
        totalWaitTime += 100
    }
    throw TestFailedWithTimeoutException
}

object TestFailedWithTimeoutException : Exception()
