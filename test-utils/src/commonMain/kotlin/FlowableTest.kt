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

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.e
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

typealias TestBlock<T> = suspend(T) -> Unit
typealias ActionBlock = suspend() -> Unit
typealias FlowTestBlock<T, F> = suspend FlowTest<T, F>.(F) -> Unit

abstract class FlowableTest<T> : BaseTest() {

    fun testWithFlow(block: FlowTestBlock<T, MutableSharedFlow<T>>) = runBlocking {
       object:FlowTest<T, MutableSharedFlow<T>>(this) {
           override val flow: suspend () -> MutableSharedFlow<T> = suspend { mutableSharedFlow() }
       }.testWithFlow(block)
    }

    abstract suspend fun mutableSharedFlow(): MutableSharedFlow<T>

}

abstract class SimpleFlowTest<T>(scope: CoroutineScope = MainScope()):FlowTest<T, Flow<T>>(scope)

abstract class FlowTest<T, F:Flow<T>>(scope: CoroutineScope = MainScope()):BaseTest(), CoroutineScope by scope {

    init { ensureNeverFrozen() }


    abstract val flow: suspend () -> F

    @Deprecated("use flow instead", ReplaceWith("flow"))
    val flowable: suspend ()->F
        get() = flow

    open val filter:(Flow<T>) -> Flow<T> = { it }

    private val tests: MutableList<EmptyCompletableDeferred> = mutableListOf()

    var job: Job? = null

    private lateinit var testChannel: Channel<Pair<TestBlock<T>, EmptyCompletableDeferred>>

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

    lateinit var lateflow:F

    fun testWithFlow(block: FlowTestBlock<T, F>) {
        runBlocking {
            testChannel = Channel(Channel.UNLIMITED)
            // startFlow is only called when the first test block is offered

            lateflow = flow()
            val f = lateflow
            block(this@FlowTest, f)
            resetFlow()
        }
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun startFlow(flow: F) {
        this.ensureNeverFrozen()
        debug("launch flow scope...")
        val testChannel = testChannel
        val started = EmptyCompletableDeferred()
        val filter = filter
        try {
            job = launch(Dispatchers.Main.immediate) {
                started.complete()
                debug("main scope launched, about to flow, test channel ${if (testChannel.isEmpty) "" else "not "}empty ")
                filter(flow).collect { value ->
                    debug("in flow received [$value], test channel ${if (testChannel.isEmpty) "" else "not "}empty \"")
                    val test = testChannel.receive()
                    debug("received test block $test")
                    try {
                        test.first(value)
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

    suspend fun action(action: ActionBlock) {
        debug("start action")
        awaitTestBlocks()
        action()
        debug("did action")
    }

    var firstTestBlock = true

    suspend fun test(skip: Int = 0, test: TestBlock<T>,) {
        test.freeze()
        if (firstTestBlock) {
            firstTestBlock = false
            tests.ensureNeverFrozen()
            debug("first test offered, starting collection")
            startFlow(lateflow)
        }
        repeat(skip) {
            test {}
        }
        val completable = EmptyCompletableDeferred()
        tests.add(completable)
        debug("${tests.size} in collection (including this one), offering")
        testChannel.offer(Pair(test, completable))
    }
}
