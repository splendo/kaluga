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

package com.splendo.kaluga.test.koin

import com.splendo.kaluga.test.com.splendo.kaluga.test.koin.KoinUIThreadTest
import org.koin.core.inject
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals

class KoinUIThreadTestTest : KoinUIThreadTest<KoinUIThreadTestTest.MyKoinTestContext>() {

    inner class MyKoinTestContext : KoinUIThreadTest.KoinTestContext(
        module {
            single { "K" }
        }
    ) {
        // test injection into context
        val k: String by inject()
    }

    override fun createTestContext(): MyKoinTestContext = MyKoinTestContext()

    @Test
    fun testKoinUIThreadViewModelTest() = testOnUIThread {
        assertEquals("K", k)
    }
}
