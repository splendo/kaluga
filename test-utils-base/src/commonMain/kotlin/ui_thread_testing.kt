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

package com.splendo.kaluga.test

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.yield

open class SimpleUIThreadTest : UIThreadTest<SimpleUIThreadTest.SimpleTestContext>() {
    class SimpleTestContext(coroutineScope: CoroutineScope) : TestContext, CoroutineScope by coroutineScope

    override val createTestContext: suspend (CoroutineScope) -> SimpleTestContext = { SimpleTestContext(it) }
}

/**
 * This class allows a test block to be run on the UI thread inside a custom context
 * that is also created in the UI Thread.
 *
 * While normally iOS tests already run on the UI thread, this prevents the Main dispatcher from working as long as that Thread is blocked,
 * so this class is mostly used in conjunction with the [com.splendo.kaluga.test.mainBackground] entry point.
 *
 * It is useful for Kotlin Native classes that only work when created on the UI thread,
 * as it eases dealing with immutability and allows a shared context.
 *
 */
abstract class UIThreadTest<TC : UIThreadTest.TestContext>(allowFreezing: Boolean = false) : BaseUIThreadTest<Unit, TC>(allowFreezing) {
    interface TestContext : BaseUIThreadTest.TestContext

    class EmptyTestContext private constructor() : TestContext {
        companion object {
            val INSTANCE = EmptyTestContext()
        }
    }

    abstract val createTestContext: suspend (scope: CoroutineScope) -> TC
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> TC = { _, scope ->
        createTestContext(scope)
    }

    fun testOnUIThread(cancelScopeAfterTest: Boolean = false, block: suspend TC.() -> Unit) = testOnUIThread(Unit, cancelScopeAfterTest, block)
}

/**
 * This class allows a test block to be run on the UI thread inside a custom context
 * that is also created in the UI Thread.
 *
 * While normally iOS tests already run on the UI thread, this prevents the Main dispatcher from working as long as that Thread is blocked,
 * so this class is mostly used in conjunction with the [com.splendo.kaluga.test.mainBackground] entry point.
 *
 * It is useful for Kotlin Native classes that only work when created on the UI thread,
 * as it eases dealing with immutability and allows a shared context.
 *
 */
abstract class BaseUIThreadTest<CONF, TC : BaseUIThreadTest.TestContext>(allowFreezing: Boolean = false) : BaseTest() {

    init {
        if (!allowFreezing) ensureNeverFrozen()
    }

    interface TestContext {
        fun dispose() {}
    }

    class EmptyTestContext private constructor() : TestContext {
        companion object {
            val INSTANCE = EmptyTestContext()
        }
    }

    abstract val createTestContextWithConfiguration: suspend (configuration: CONF, scope: CoroutineScope) -> TC

    private companion object {
        val cancellationException = CancellationException("Scope canceled by testOnUIThread because cancelScopeAfterTest was set to true.")
    }

    /**
     * Run your test block on the UI thread and inside the TestContext.
     *
     * Optionally with [cancelScopeAfterTest] you can run your test block inside a separately launched job, which
     * is canceled after your block is run. The scope waits till this is completed by joining.
     *
     * This can be handy for testing classes that rely on scopes that eventually get canceled,
     * e.g. because they are canceled automatically by a lifecycle event.
     *
     * @param block test block to be run on the UI thread and in the test context.
     *
     * @param cancelScopeAfterTest whether to cancel the coroutinescope your block ran in after it's done.
     *
     */
    fun testOnUIThread(configuration: CONF, cancelScopeAfterTest: Boolean = false, block: suspend TC.() -> Unit) {
        try {

            val createTestContextWithConfiguration = createTestContextWithConfiguration
            val test: suspend (CoroutineScope) -> Unit = {
                val testContext = createTestContextWithConfiguration(configuration, it)
                yield()
                try {
                    block(testContext)
                } finally {
                    testContext.dispose()
                }
            }
            createTestContextWithConfiguration.freeze()
            test.freeze()

            if (cancelScopeAfterTest)
                testBlockingAndCancelScope(Dispatchers.Main) { test(this) }
            else
                runBlocking(Dispatchers.Main) { test(this) }
        } catch (c: CancellationException) {
            if (!cancelScopeAfterTest || c !== cancellationException)
                throw c
        }
    }
}
