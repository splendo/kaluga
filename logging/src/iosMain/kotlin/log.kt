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

package com.splendo.kaluga.logging

import co.touchlab.stately.concurrency.value
import io.github.aakira.napier.DebugAntilog
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
actual val defaultLogger: Logger = NapierLogger(DebugAntilog())

@SharedImmutable
private val _logger = co.touchlab.stately.concurrency.AtomicReference(defaultLogger)
actual var logger
    get() = _logger.value
    set(value) { _logger.value = value }
