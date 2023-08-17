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

package com.splendo.kaluga.test.koin

import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KoinUIThreadTestTest : KoinUIThreadTest<KoinUIThreadTestTest.MyKoinTestContext>() {

    class MyKoinTestContext : KoinTestContext(
        module {
            single { "K" }
        },
    ) {
        // test injection into context
        val k: String by inject()
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> MyKoinTestContext = { MyKoinTestContext() }

    @Test
    fun testKoinUIThreadTest() = testOnUIThread {
        assertNotNull(KoinPlatformTools.defaultContext().getOrNull())
        assertEquals("K", k)
    }

    @AfterTest
    fun testCleared() {
        assertNull(KoinPlatformTools.defaultContext().getOrNull())
    }
}
