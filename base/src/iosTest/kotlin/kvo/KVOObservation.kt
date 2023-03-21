/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.kvo

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSKeyValueObservingOptionInitial
import platform.Foundation.NSLocale
import kotlin.test.Test
import kotlin.test.assertEquals

class KVOObservation {

    @Test
    fun testObserveKeyValueAsFlow() = runBlocking {
        val kvoTestClass = NSDateFormatter()
        val flow = kvoTestClass.observeKeyValueAsFlow<NSLocale>("locale", NSKeyValueObservingOptionInitial, coroutineContext)
        assertEquals(kvoTestClass.locale, flow.first())
        kvoTestClass.locale = NSLocale("nl_NL")
        yield()
        assertEquals(NSLocale("nl_NL"), flow.first())
        coroutineContext.cancelChildren()
    }
}
