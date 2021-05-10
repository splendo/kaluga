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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

typealias TestBlock<T> = suspend(T) -> Unit
typealias ActionBlock = suspend() -> Unit
typealias FlowTestBlock<T, F> = suspend FlowTest<T, F>.(F) -> Unit

abstract class FlowableTest<T> : BaseTest() {

    fun testWithFlow(block: FlowTestBlock<T, MutableSharedFlow<T>>) = runBlocking {
       object:FlowTest<T, MutableSharedFlow<T>>(this) {
           override val flow: () -> MutableSharedFlow<T>
               get() =  ::mutableSharedFlow
       }.testWithFlow(block)
    }

    abstract fun mutableSharedFlow(): MutableSharedFlow<T>

}

abstract class FlowTest<T, F:Flow<T>>(scope: CoroutineScope = MainScope()):BaseTest(), CoroutineScope by scope {

    abstract val flow: () -> F
    private val _flow by lazy { flow() }

    @Deprecated("use flow() instead", ReplaceWith("flow"))
    val flowable: ()->F
        get() = flow

    open var filter: suspend(T) -> Boolean = { true }

    private val tests: MutableList<EmptyCompletableDeferred> = mutableListOf()

    var job: Job? = null

    private lateinit var testChannel: Channel<Pair<TestBlock<T>, EmptyCompletableDeferred>>

    suspend fun resetFlow() {
        awaitTestBlocks() // get the final test blocks that were executed and check for exceptions
        println("job: $job")
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
            println("await all test blocks, but none found")
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

    fun testWithFlow(block: FlowTestBlock<T, F>) {
        runBlocking {
            testChannel = Channel(Channel.UNLIMITED)
            // startFlow is only called when the first test block is offered
            block(this@FlowTest, _flow)
            resetFlow()
        }
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun startFlow() {
        this.ensureNeverFrozen()

        val flow = _flow
        val testChannel = testChannel
        val filter = filter

        debug("launch flow scope...")
        val started = EmptyCompletableDeferred()
        try {
            job = launch(Dispatchers.Main.immediate) {
                started.complete()
                debug("main scope launched, about to flow, test channel ${if (testChannel.isEmpty) "" else "not "}empty ")
                flow.filter(filter).collect { value ->
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

    suspend fun test(skip: Int = 0, test: TestBlock<T>) {
        if (firstTestBlock) {
            firstTestBlock = false
            tests.ensureNeverFrozen()
            startFlow()
        }
        repeat(skip) {
            test {}
        }
        val completable = EmptyCompletableDeferred()
        tests.add(completable)
        debug("${tests.size} in collection, offering")
        testChannel.offer(Pair(test, completable))
    }
}
