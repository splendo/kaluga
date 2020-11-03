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

@file:JvmName("AndroidAwaitCurrentValue")
package com.splendo.kaluga.test.utils

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.ObservableOptional
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

actual suspend fun <T> Observable<T>.awaitCurrentValue(
    timeOut: Long
): T = withTimeout(timeOut) {
    val value by this@awaitCurrentValue
    while (value is ObservableOptional.Nothing) {
        delay(100)
    }
    (value as ObservableOptional.Value).value
}
