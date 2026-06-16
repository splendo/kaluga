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

import com.splendo.kaluga.base.externals.LuxonDateTime

// Kotlin/Wasm has no `kotlin.js.Date`; the holder keeps the epoch milliseconds, which is all the
// common API exposes (the value is opaque — no members are accessed through `KalugaDate.date`).
actual class KalugaDateHolder internal constructor(internal val epochMilliseconds: Double)

internal actual fun luxonToDateHolder(dateTime: LuxonDateTime): KalugaDateHolder = KalugaDateHolder(dateTime.toMillis())
