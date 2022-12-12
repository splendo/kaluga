/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.test

import com.splendo.kaluga.architecture.viewmodel.ViewModel
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.base.captureFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

@Deprecated("Moved to test-utils-base", ReplaceWith("BaseTest", "com.splendo.kaluga.test.base.BaseTest"))
typealias BaseTest = com.splendo.kaluga.test.base.BaseTest
@Deprecated("Moved to test-utils-base", ReplaceWith("DeliberateCancellationException", "com.splendo.kaluga.test.base.DeliberateCancellationException"))
typealias DeliberateCancellationException = com.splendo.kaluga.test.base.DeliberateCancellationException

@Deprecated("Moved to test-utils-base", ReplaceWith("SimpleUIThreadTest", "com.splendo.kaluga.test.base.SimpleUIThreadTest"))
open class SimpleUIThreadTest : UIThreadTest<SimpleUIThreadTest.SimpleTestContext>() {
    @Deprecated("Moved to test-utils-base", ReplaceWith("SimpleUIThreadTest.SimpleTestContext", "com.splendo.kaluga.test.base.SimpleUIThreadTest"))
    class SimpleTestContext(coroutineScope: CoroutineScope) : TestContext, CoroutineScope by coroutineScope

    override val createTestContext: suspend (CoroutineScope) -> SimpleTestContext = { SimpleTestContext(it) }
}
@Deprecated("Moved to test-utils-base", ReplaceWith("UIThreadTest", "com.splendo.kaluga.test.base.UIThreadTest"))
abstract class UIThreadTest<TC : UIThreadTest.TestContext> : com.splendo.kaluga.test.base.UIThreadTest<TC>() {
    @Deprecated("Moved to test-utils-base", ReplaceWith("TestContext", "com.splendo.kaluga.test.base.UIThreadTest.TestContext"))
    interface TestContext : com.splendo.kaluga.test.base.UIThreadTest.TestContext
}

@Deprecated("Moved to test-utils-base", ReplaceWith("FlowTestBlockWithContext", "com.splendo.kaluga.test.base.FlowTestBlockWithContext"))
typealias FlowTestBlockWithContext<TC, T, F> = com.splendo.kaluga.test.base.FlowTestBlockWithContext<Unit, TC, T, F>
@Deprecated("Moved to test-utils-base", ReplaceWith("FlowableTest", "com.splendo.kaluga.test.base.FlowableTest"))
typealias FlowableTest<T> = com.splendo.kaluga.test.base.FlowableTest<T>
@Deprecated("Moved to test-utils-base", ReplaceWith("FlowTest", "com.splendo.kaluga.test.base.FlowTest"))
typealias FlowTest<T, F> = com.splendo.kaluga.test.base.FlowTest<T, F>
@Deprecated("Moved to test-utils-base", ReplaceWith("BaseFlowTest", "com.splendo.kaluga.test.base.BaseFlowTest"))
abstract class BaseFlowTest<TC : UIThreadTest.TestContext, T, F : Flow<T>> : com.splendo.kaluga.test.base.BaseFlowTest<Unit, TC, T, F>() {
    fun testWithFlowAndTestContext(
        createFlowInMainScope: Boolean = true,
        retainContextAfterTest: Boolean = false,
        blockWithContext: FlowTestBlockWithContext<TC, T, F>
    ) = super.testWithFlowAndTestContext(Unit, createFlowInMainScope, retainContextAfterTest, blockWithContext)
}

@Deprecated("Moved to test-utils-koin", ReplaceWith("KoinUIThreadTest", "com.splendo.kaluga.test.koin.KoinUIThreadTest"))
abstract class KoinUIThreadTest<TC : KoinUIThreadTest.KoinTestContext> : com.splendo.kaluga.test.koin.KoinUIThreadTest<TC>() {
    @Deprecated("Moved to test-utils-koin", ReplaceWith("KoinUIThreadTest.KoinTestContext", "com.splendo.kaluga.test.koin.KoinUIThreadTest"))
    open class KoinTestContext(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>
    ) : com.splendo.kaluga.test.koin.KoinUIThreadTest.KoinTestContext(appDeclaration, koinModules)
}

@Deprecated("Moved to test-utils-koin", ReplaceWith("KoinFlowTestBlock", "com.splendo.kaluga.test.koin.KoinFlowTestBlock"))
typealias KoinFlowTestBlock<TC, T, F> = suspend KoinFlowTest<TC, T, F>.(F) -> Unit
@Deprecated("Moved to test-utils-koin", ReplaceWith("KoinFlowTest", "com.splendo.kaluga.test.koin.KoinFlowTest"))
abstract class KoinFlowTest<TC : KoinUIThreadTest.KoinTestContext, T, F : Flow<T>> : com.splendo.kaluga.test.koin.BaseKoinFlowTest<Unit, TC, T, F>() {
    abstract val createTestContext: suspend (scope: CoroutineScope) -> TC
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> TC get() {
        val createTestContext = this.createTestContext
        return { _, scope ->
            createTestContext(scope)
        }
    }

    fun testWithFlow(block: KoinFlowTestBlock<TC, T, F>) =
        super.testWithFlowAndTestContext(Unit, createFlowInMainScope = false, retainContextAfterTest = false) {
            block(this@KoinFlowTest, it)
        }
}

@Deprecated("Moved to test-utils-koin", ReplaceWith("KoinUIThreadViewModelTest", "com.splendo.kaluga.test.koin.KoinUIThreadViewModelTest"))
abstract class KoinUIThreadViewModelTest<KVMC : KoinUIThreadViewModelTest.KoinViewModelTestContext<VM>, VM : ViewModel> :
    KoinUIThreadTest<KVMC>() {

    @Deprecated("Moved to test-utils-koin", ReplaceWith("KoinViewModelTestContext", "com.splendo.kaluga.test.koin.KoinUIThreadViewModelTest"))
    abstract class KoinViewModelTestContext<VM : ViewModel>(
        appDeclaration: KoinAppDeclaration? = null,
        koinModules: List<Module>
    ) : KoinUIThreadTest.KoinTestContext(appDeclaration, koinModules),
        UIThreadViewModelTest.ViewModelTestContext<VM> {
        constructor(vararg koinModules: Module) : this(null, koinModules.toList())
        constructor(appDeclaration: KoinAppDeclaration, vararg koinModules: Module) : this(
            appDeclaration,
            koinModules.toList()
        )
    }
}

@Deprecated("Moved to test-utils-base", ReplaceWith("awaitAllBlocking(deferreds)", "com.splendo.kaluga.test.base.awaitAllBlocking"))
fun <T> awaitAllBlocking(vararg deferreds: Deferred<T>): List<T> = com.splendo.kaluga.test.base.awaitAllBlocking(*deferreds)
@Deprecated("Moved to test-utils-base", ReplaceWith("captureFor(duration)", "com.splendo.kaluga.test.base.captureFor"))
suspend fun <T> Flow<T>.captureFor(duration: Duration): List<T> = this.captureFor(duration)
@Deprecated("Moved to test-utils-base", ReplaceWith("testBlockingAndCancelScope(context, block)", "com.splendo.kaluga.test.base.testBlockingAndCancelScope"))
inline fun <reified T> testBlockingAndCancelScope(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> T
) = com.splendo.kaluga.test.base.testBlockingAndCancelScope(context, block)
