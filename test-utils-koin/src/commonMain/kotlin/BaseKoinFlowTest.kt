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

import com.splendo.kaluga.test.base.BaseFlowTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.core.context.stopKoin

typealias KoinFlowTestBlock<TC, T, F> = suspend KoinFlowTest<TC, T, F>.(F) -> Unit

abstract class KoinFlowTest<TC : KoinUIThreadTest.KoinTestContext, T, F : Flow<T>> : BaseKoinFlowTest<Unit, TC, T, F>() {
    abstract val createTestContext: suspend (scope: CoroutineScope) -> TC
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> TC get() {
        val createTestContext = this.createTestContext
        return { _, scope ->
            createTestContext(scope)
        }
    }

    fun testWithFlow(block: KoinFlowTestBlock<TC, T, F>) = super.testWithFlowAndTestContext(Unit, createFlowInMainScope = false, retainContextAfterTest = false) {
        block(this@KoinFlowTest, it)
    }
}
abstract class BaseKoinFlowTest<C, TC : BaseKoinUIThreadTest.KoinTestContext, T, F : Flow<T>> : BaseFlowTest<C, TC, T, F>() {
    override val onFailedToCreateTestContextWithConfiguration: (configuration: C) -> Unit = { stopKoin() }
}
