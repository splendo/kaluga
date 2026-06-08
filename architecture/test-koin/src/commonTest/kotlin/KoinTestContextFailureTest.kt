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

package com.splendo.kaluga.test.koin

import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.error.InstanceCreationException
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class KoinTestContextFailureTest {

    class MockClass

    interface MockKoinComponent {
        val mockClass: MockClass
    }

    class MockKoinComponentImpl(override val mockClass: MockClass) : MockKoinComponent

    class MockKoinUIThreadTest : KoinUIThreadTest<MockKoinUIThreadTest.Context>() {
        object Context : KoinUIThreadTest.KoinTestContext(
            koinModules = listOf(
                module {
                    single<MockKoinComponent> { MockKoinComponentImpl(get()) }
                },
            ),
        ) {
            val component: MockKoinComponent by inject()
            val mockClass = component.mockClass
        }

        override val createTestContext: suspend (scope: CoroutineScope) -> Context = { Context }
    }

    @AfterTest
    fun validateKoinCleaned() {
        assertNull(KoinPlatformTools.defaultContext().getOrNull())
    }

    @Test
    fun testKoinAlreadyStarted() {
        startKoin {}
        val context = BaseKoinUIThreadTest.KoinTestContext()
        context.dispose()
    }

    @Test
    fun testModuleContainsMissingDefinitions() {
        assertFailsWith<InstanceCreationException> {
            BaseKoinUIThreadTest.KoinTestContext(
                koinModules = listOf(
                    module {
                        single<MockKoinComponent>(createdAtStart = true) { MockKoinComponentImpl(get()) }
                    },
                ),
            )
        }
    }

    @Test
    fun testKoinUIThreadTestContextContainsMissingDefinitions() {
        val testClass = MockKoinUIThreadTest()
        assertFails { testClass.testOnUIThread { } }
    }
}
