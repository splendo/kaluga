/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.concurrency.AtomicLong
import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.e
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.test.UIThreadTest.EmptyTestContext
import com.splendo.kaluga.test.UIThreadTest.TestContext
import com.splendo.kaluga.test.koin.KoinUIThreadTest
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.AfterTest

typealias TestBlock<TC, T> = suspend TC.(T) -> Unit
typealias ActionBlock = suspend() -> Unit
typealias ScopeActionBlock<TC> = suspend TC.() -> Unit

typealias FlowTestBlockWithContext<TC, T, F> = suspend BaseFlowTest<TC, T, F>.(F) -> Unit

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

abstract class FlowTest<T, F : Flow<T>>(scope: CoroutineScope = MainScope()) : BaseFlowTest<EmptyTestContext, T, F>(scope) {
    override val createTestContext: suspend (CoroutineScope) -> EmptyTestContext = { EmptyTestContext.INSTANCE }

    override val flowFromTestContext: suspend EmptyTestContext.() -> F =
        { flow() }

    abstract val flow: suspend () -> F

    fun testWithFlow(block: FlowTestBlock<T, F>) =
        super.testWithFlowAndTestContext(createFlowInMainScope = false, retainContextAfterTest = false) {
            block(this@FlowTest, it)
        }
}

abstract class BaseKoinFlowTest<TC : KoinUIThreadTest.KoinTestContext, T, F : Flow<T>> : BaseFlowTest<TC, T, F>()

/*
Context for each tests needs to be created and kept on the main thread for iOS.

Since the class itself is created in the test thread
*/

@ThreadLocal // thread local on native, global on non-native, but still accessed from only the main thread.
val contextMap = mutableMapOf<Long, TestContext>()

private suspend fun <TC : TestContext> testContext(cookie: Long, testContext: suspend () -> TC): TC {
    if (!contextMap.containsKey(cookie))
        contextMap[cookie] = testContext()
    return contextMap[cookie] as TC
}

@SharedImmutable
private val cookieTin = AtomicLong(0L)

abstract class BaseFlowTest<TC : TestContext, T, F : Flow<T>>(val scope: CoroutineScope = MainScope()) : UIThreadTest<TC>(), CoroutineScope by scope {

    // used for thread local map access to store the testContext
    private val cookie = cookieTin.incrementAndGet()

    init {
        ensureNeverFrozen()
    }

    abstract val flowFromTestContext: suspend TC.() -> F

    open val filter: (Flow<T>) -> Flow<T> = { it }

    private val tests: MutableList<EmptyCompletableDeferred> = mutableListOf()

    var job: Job? = null

    private lateinit var testChannel: Channel<Pair<TestBlock<TC, T>, EmptyCompletableDeferred>>

    @AfterTest
    override fun afterTest() {
        runBlocking {
            disposeContext(cookie)
        }
        super.afterTest()
    }

    suspend fun resetFlow() {
        awaitTestBlocks() // get the final test blocks that were executed and check for exceptions
        debug("job: $job")
        job?.cancel()
        debug("Ending flow, job canceled")

        firstTestBlock = true
        testChannel.close()

        debug("test channel closed")

        tests.clear()
        testChannel = Channel(Channel.UNLIMITED)
    }

    protected val waitForTestToSucceed = 6000L * 10

    private suspend fun awaitTestBlocks() {

        tests.removeAll { !it.isActive }

        if (tests.size == 0) {
            debug("await all test blocks, but none found, skip waiting")
            return
        }

        withTimeout(waitForTestToSucceed) {
            try {
                debug("await all test blocks (${tests.size}), give it $waitForTestToSucceed milliseconds")
                tests.awaitAll()
            } finally {
                tests.removeAll { !it.isActive }
            }
        }
    }

    private var lateflow: F? = null

    fun testWithFlowAndTestContext(
        createFlowInMainScope: Boolean = true,
        retainContextAfterTest: Boolean = false,
        blockWithContext: FlowTestBlockWithContext<TC, T, F>
    ) {

        runBlocking {
            try {
                testChannel = Channel(Channel.UNLIMITED)

                // startFlow is only called when the first test block is offered

                val flow = flowFromTestContext

                val cookie = cookie
                val scope = scope

                val createTestContext = createTestContext

                val f =
                    if (createFlowInMainScope) {
                        flow.freeze()
                        scope.freeze()
                        withContext(Dispatchers.Main.immediate) {
                            (flow(testContext(cookie) { createTestContext(scope) })).freeze()
                        }
                    } else {
                        createTestContext.freeze()
                        flow(
                            withContext(Dispatchers.Main.immediate) {
                                (testContext(cookie) { createTestContext(scope) }).freeze()
                            }
                        )
                    }

                lateflow = f
                blockWithContext(f)
                resetFlow()
            } finally {
                if (!retainContextAfterTest) {
                    disposeContext(cookie)
                }
            }
        }
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun startFlow(flow: F) {
        this.ensureNeverFrozen()
        debug("launch flow scope...")
        val testChannel = testChannel
        val started = EmptyCompletableDeferred()
        val filter = filter
        val scope = scope
        val createTestContext = createTestContext
        val cookie = cookie
        createTestContext.freeze()
        try {
            job = launch(Dispatchers.Main.immediate) {
                started.complete()
                val testContext = testContext(cookie) { createTestContext(scope) }
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

    suspend fun mainAction(action: ScopeActionBlock<TC>) {
        debug("start mainAction")
        awaitTestBlocks()
        val cookie = cookie
        val createTestContext = createTestContext
        val scope = scope
        withContext(Dispatchers.Main.immediate) {
            debug("in main scope for mainAction")
            action(testContext(cookie) { createTestContext(scope) })
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

    suspend fun test(skip: Int = 0, test: TestBlock<TC, T>) {
        test.freeze()
        if (firstTestBlock) {
            firstTestBlock = false
            tests.ensureNeverFrozen()
            debug("first test offered, starting collection")
            require(lateflow != null) { "Only use test from inside `testWith` methods" }
            startFlow(lateflow!!)
        }
        repeat(skip) {
            test {}
        }
        val completable = EmptyCompletableDeferred()
        tests.add(completable)
        debug("${tests.size} in collection (including this one), offering")
        testChannel.trySend(Pair(test, completable)).isSuccess
    }

    private suspend fun disposeContext(cookie: Long) {
        withContext(Dispatchers.Main.immediate) {
            val testContext = contextMap.remove(cookie)
            testContext?.dispose()
        }
    }
}
