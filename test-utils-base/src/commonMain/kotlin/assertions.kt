/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.isFrozen
import kotlin.native.concurrent.SharedImmutable
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SharedImmutable
private val WARNING = "Checking if a Boolean is frozen, did you accidentally add `.isFrozen` in your assert? If intentional add `allowBoolean = true`."

fun assertFrozen(any: Any, message: String? = null, allowBoolean: Boolean = false) {
    if (!allowBoolean && any is Boolean)
        error(WARNING)
    assertTrue(any.isFrozen, message)
}

fun assertNotFrozen(any: Any, message: String? = null, suppressWarning: Boolean = false) {
    if (!suppressWarning && any is Boolean)
        error(WARNING)
    assertFalse(any.isFrozen, message)
}
