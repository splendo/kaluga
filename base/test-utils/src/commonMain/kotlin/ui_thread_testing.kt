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

package com.splendo.kaluga.base.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.yield

open class SimpleUIThreadTest : UIThreadTest<SimpleUIThreadTest.SimpleTestContext>() {
    class SimpleTestContext(coroutineScope: CoroutineScope) :
        TestContext,
        CoroutineScope by coroutineScope

    override val createTestContext: suspend (CoroutineScope) -> SimpleTestContext = { SimpleTestContext(it) }
}

/**
 * This class allows a test block to be run on the UI thread inside a custom context
 * that is also created in the UI Thread.
 *
 * While normally iOS tests already run on the UI thread, this prevents the Main dispatcher from working as long as that Thread is blocked,
 * so this class is mostly used in conjunction with the [com.splendo.kaluga.base.test.mainBackground] entry point.
 *
 * It is useful for Kotlin Native classes that only work when created on the UI thread,
 * as it eases dealing with immutability and allows a shared context.
 *
 */
abstract class UIThreadTest<Context : UIThreadTest.TestContext> : BaseUIThreadTest<Unit, Context>() {
    interface TestContext : BaseUIThreadTest.TestContext

    class EmptyTestContext private constructor() : TestContext {
        companion object {
            val INSTANCE = EmptyTestContext()
        }
    }

    /**
     * Creates the Test Context based on a CoroutineScope
     */
    abstract val createTestContext: suspend (scope: CoroutineScope) -> Context
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> Context get() {
        val createTestContext = this.createTestContext
        return { _, scope ->
            createTestContext(scope)
        }
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
    fun testOnUIThread(cancelScopeAfterTest: Boolean = false, block: suspend Context.() -> Unit) = testOnUIThread(Unit, cancelScopeAfterTest, block)
}

/**
 * This class allows a test block to be run on the UI thread inside a custom context
 * that is also created in the UI Thread using a configuration.
 *
 * While normally iOS tests already run on the UI thread, this prevents the Main dispatcher from working as long as that Thread is blocked,
 * so this class is mostly used in conjunction with the [com.splendo.kaluga.base.test.mainBackground] entry point.
 *
 * It is useful for Kotlin Native classes that only work when created on the UI thread,
 * as it eases dealing with immutability and allows a shared context.
 *
 */
abstract class BaseUIThreadTest<Configuration, Context : BaseUIThreadTest.TestContext> : BaseTest() {

    interface TestContext {
        fun dispose() {}
    }

    class EmptyTestContext private constructor() : TestContext {
        companion object {
            val INSTANCE = EmptyTestContext()
        }
    }

    /**
     * Creates the TestContext based on Configuration and CoroutineScope
     */
    abstract val createTestContextWithConfiguration: suspend (configuration: Configuration, scope: CoroutineScope) -> Context
    open val onFailedToCreateTestContextWithConfiguration: (configuration: Configuration) -> Unit = {}

    /**
     * Run your test block on the UI thread and inside the TestContext.
     *
     * Optionally with [cancelScopeAfterTest] you can run your test block inside a separately launched job, which
     * is canceled after your block is run. The scope waits till this is completed by joining.
     *
     * This can be handy for testing classes that rely on scopes that eventually get canceled,
     * e.g. because they are canceled automatically by a lifecycle event.
     *
     * @param configuration The [Configuration] to configure the TestContext.
     * @param block test block to be run on the UI thread and in the test context.
     *
     * @param cancelScopeAfterTest whether to cancel the coroutinescope your block ran in after it's done.
     *
     */
    fun testOnUIThread(configuration: Configuration, cancelScopeAfterTest: Boolean = false, block: suspend Context.() -> Unit): TestResult {
        val createTestContextWithConfiguration = createTestContextWithConfiguration
        val onFailedToCreateTestContextWithConfiguration = onFailedToCreateTestContextWithConfiguration
        val test: suspend CoroutineScope.() -> Unit = {
            val testContext = try {
                createTestContextWithConfiguration(configuration, this)
            } catch (e: Throwable) {
                onFailedToCreateTestContextWithConfiguration(configuration)
                throw e
            }
            yield()
            try {
                block(testContext)
            } finally {
                testContext.dispose()
            }
        }

        // Return the runner's `TestResult` so the js/wasm frameworks await it (they do not block, and
        // do not await a fire-and-forget `runBlocking`). `cancelScopeAfterTest` additionally cancels any
        // coroutines the block left running once it completes.
        return if (cancelScopeAfterTest) {
            testBlockingAndCancelScope(Dispatchers.Main, block = test)
        } else {
            testRunBlocking(Dispatchers.Main, test)
        }
    }
}
