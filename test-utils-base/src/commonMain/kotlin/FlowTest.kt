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

package com.splendo.kaluga.test.base

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.e
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.test.base.BaseUIThreadTest.EmptyTestContext
import com.splendo.kaluga.test.base.BaseUIThreadTest.TestContext
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.test.AfterTest
import kotlin.time.Duration.Companion.seconds

typealias TestBlock<Context, T> = suspend Context.(T) -> Unit
typealias ActionBlock = suspend() -> Unit
typealias ScopeActionBlock<Context> = suspend Context.() -> Unit

typealias FlowTestBlockWithContext<Configuration, Context, T, F> = suspend BaseFlowTest<Configuration, Context, T, F>.(F) -> Unit

typealias FlowTestBlock<T, F> = suspend FlowTest<T, F>.(F) -> Unit

abstract class FlowableTest<T> : BaseTest() {

    fun testWithFlow(block: FlowTestBlock<T, MutableSharedFlow<T>>) = runBlocking {
        object : FlowTest<T, MutableSharedFlow<T>>(this@runBlocking) {
            override val flow = suspend { mutableSharedFlow() }
        }.testWithFlow(block)
    }
    abstract fun mutableSharedFlow(): MutableSharedFlow<T>
}

abstract class SimpleFlowTest<T>(scope: CoroutineScope = MainScope()) : FlowTest<T, Flow<T>>(scope)

abstract class FlowTest<T, F : Flow<T>>(scope: CoroutineScope = MainScope()) : BaseFlowTest<Unit, EmptyTestContext, T, F>(scope) {
    override val createTestContextWithConfiguration: suspend (configuration: Unit, scope: CoroutineScope) -> EmptyTestContext = { _, _ -> EmptyTestContext.INSTANCE }

    override val flowFromTestContext: suspend EmptyTestContext.() -> F =
        { flow() }

    abstract val flow: suspend () -> F

    fun testWithFlow(block: FlowTestBlock<T, F>) =
        super.testWithFlowAndTestContext(Unit, createFlowInMainScope = false, retainContextAfterTest = false) {
            block(this@FlowTest, it)
        }
}

abstract class BaseFlowTest<Configration, Context : TestContext, T, F : Flow<T>>(
    val scope: CoroutineScope = MainScope(),
    val logger: Logger? = null,
) :
    BaseUIThreadTest<Configration, Context>(),
    CoroutineScope by scope {

        abstract val flowFromTestContext: suspend Context.() -> F

        open val filter: (Flow<T>) -> Flow<T> = { it }

        private val tests: MutableList<EmptyCompletableDeferred> = concurrentMutableListOf()

        var job: Job? = null

        private val testContext = atomic<Context?>(null)
        private lateinit var testChannel: Channel<Pair<TestBlock<Context, T>, EmptyCompletableDeferred>>

        @AfterTest
        override fun afterTest() {
            runBlocking {
                disposeContext()
            }
            super.afterTest()
        }

        suspend fun resetFlow() {
            awaitTestBlocks() // get the final test blocks that were executed and check for exceptions
            debug("job: $job")
            job?.cancelAndJoin()
            debug("Ending flow, job canceled")

            firstTestBlock = true
            testChannel.close()

            debug("test channel closed")

            tests.clear()
            testChannel = Channel(Channel.UNLIMITED)
        }

        protected val waitForTestToSucceed = 60.seconds

        private suspend fun awaitTestBlocks() {
            val tests = this.tests

            if (tests.size == 0) {
                debug("await all test blocks, but none found, skip waiting")
                return
            }

            withTimeout(waitForTestToSucceed) {
                try {
                    debug("await all test blocks (${tests.size}), give it $waitForTestToSucceed milliseconds")
                    tests.awaitAll()
                } finally {
                    this@BaseFlowTest.tests.removeAll(tests)
                }
            }
        }

        private var lateflow: F? = null
        private var lateConfiguration: Configration? = null

        fun testWithFlowAndTestContext(
            configuration: Configration,
            createFlowInMainScope: Boolean = true,
            retainContextAfterTest: Boolean = false,
            blockWithContext: FlowTestBlockWithContext<Configration, Context, T, F>,
        ) {
            runBlocking {
                try {
                    testChannel = Channel(Channel.UNLIMITED)

                    // startFlow is only called when the first test block is offered

                    val flow = flowFromTestContext
                    val scope = scope

                    val createTestContextWithConfiguration = createTestContextWithConfiguration

                    val f =
                        if (createFlowInMainScope) {
                            withContext(Dispatchers.Main.immediate) {
                                (flow(getOrCreateContext { createTestContextWithConfiguration(configuration, scope) }))
                            }
                        } else {
                            flow(
                                withContext(Dispatchers.Main.immediate) {
                                    (getOrCreateContext { createTestContextWithConfiguration(configuration, scope) })
                                },
                            )
                        }

                    lateflow = f
                    lateConfiguration = configuration
                    blockWithContext(f)
                    resetFlow()
                } finally {
                    if (!retainContextAfterTest) {
                        disposeContext()
                    }
                }
            }
        }

        @Suppress("SuspendFunctionOnCoroutineScope")
        private suspend fun startFlow(configuration: Configration, flow: F) {
            debug("launch flow scope...")
            val testChannel = testChannel
            val started = EmptyCompletableDeferred()
            val filter = filter
            val scope = scope
            val createTestContextWithConfiguration = createTestContextWithConfiguration
            try {
                job = launch(Dispatchers.Main.immediate) {
                    started.complete()
                    val testContext = getOrCreateContext { createTestContextWithConfiguration(configuration, scope) }
                    debug("main scope launched, about to flow, test channel ${if (testChannel.isEmpty) "" else "not "}empty ")
                    filter(flow).collect { value ->
                        debug("in flow received [$value], test channel ${if (testChannel.isEmpty) "" else "not "}empty \"")
                        val test = testChannel.receive()
                        debug("received test block $test")
                        try {
                            test.first(testContext, value)
                            debug("ran test block $test")
                            test.second.complete(Unit)
                            debug("completed $test")
                        } catch (t: Throwable) {
                            warn(throwable = t) { "Exception when testing... $t cause: ${t.cause}" }
                            try {
                                test.second.completeExceptionally(t)
                                debug("completed exceptionally $test")
                            } catch (t: Throwable) {
                                e(throwable = t) { "exception in completing completable" }
                            }
                        }
                        debug("handeling value completed [$value]")
                    }
                    debug("flow collect completed")
                }
            } catch (t: Throwable) {
                e(throwable = t) { "error launching" }
                throw t
            }
            debug("wait for main thread to be launched in $job")
            started.await()
            debug("waited for main thread to be launched")
        }

        suspend fun mainAction(action: ScopeActionBlock<Context>) {
            debug("start mainAction")
            awaitTestBlocks()
            val createTestContextWithConfiguration = createTestContextWithConfiguration
            val scope = scope
            require(lateConfiguration != null) { "Only use mainAction from inside `testWith` methods" }
            val configuration = lateConfiguration!!
            withContext(Dispatchers.Main.immediate) {
                debug("in main scope for mainAction")
                action(getOrCreateContext { createTestContextWithConfiguration(configuration, scope) })
            }
            debug("did mainAction")
        }

        suspend fun action(action: ActionBlock) {
            debug("start action")
            awaitTestBlocks()
            action()
            debug("did action")
        }

        var firstTestBlock = true

        suspend fun test(skip: Int = 0, test: TestBlock<Context, T>) {
            if (firstTestBlock) {
                firstTestBlock = false
                debug("first test offered, starting collection")
                require(lateflow != null && lateConfiguration != null) { "Only use test from inside `testWith` methods" }
                startFlow(lateConfiguration!!, lateflow!!)
            }
            repeat(skip) {
                test {}
            }
            val completable = EmptyCompletableDeferred()
            tests.add(completable)
            debug("${tests.size} in collection (including this one), offering")
            testChannel.trySend(Pair(test, completable)).isSuccess
        }

        private suspend fun disposeContext() {
            withContext(Dispatchers.Main.immediate) {
                testContext.getAndUpdate { null }?.dispose()
            }
        }

        private suspend fun getOrCreateContext(create: suspend () -> Context): Context {
            // updateAndGet doesnt work properly on JVM so we built it ourselves
            while (true) {
                val currentContext = testContext.value
                val nextContext = currentContext ?: create()
                if (testContext.compareAndSet(currentContext, nextContext)) {
                    return nextContext
                }
            }
        }

        private fun debug(message: String) {
            logger?.debug(message)
        }
    }
