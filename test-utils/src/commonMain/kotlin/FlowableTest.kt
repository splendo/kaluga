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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.e
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

typealias TestBlock<T> = suspend(T)->Unit
typealias ActionBlock = suspend()->Unit
typealias FlowTestBlock<T> =  suspend FlowTest<T>.() -> Unit

abstract class FlowableTest<T>: BaseTest() {

    fun testWithFlow(block: FlowTestBlock<T>) = runBlocking {
        FlowTest(this, ::flowable).testWithFlow(block)
    }

    abstract fun flowable():Flowable<T>

}

open class FlowTest<T>(scope: CoroutineScope = MainScope(), val flowable:()->Flowable<T>):CoroutineScope by scope {

    open var filter:suspend(T)->Boolean = { true }

    private val tests:MutableList<EmptyCompletableDeferred> = mutableListOf()

    var job: Job? = null

    private lateinit var testChannel: Channel<Pair<TestBlock<T>, EmptyCompletableDeferred>>

    suspend fun resetFlow() {
        awaitTestBlocks()// get the final test blocks that were executed and check for exceptions
        job?.cancel()
        debug("Ending flow, job canceled")

        firstTestBlock = true
        testChannel.close()

        debug("test channel closed")

        tests.clear()
        testChannel = Channel(Channel.UNLIMITED)
    }

    protected val waitForTestToSucceed = 6000L * 10 / 10

    private suspend fun awaitTestBlocks() {
        if (tests.size == 0) {
            println("await all test blocks, but none found")
            return
        }

        withTimeout(waitForTestToSucceed) {// only wait for one minute
            try {
                debug("await all test blocks (${tests.size}), give it $waitForTestToSucceed milliseconds")
                tests.awaitAll()
            } finally {
                tests.removeAll { !it.isActive }
            }

        }
    }

    fun testWithFlow(block: FlowTestBlock<T>) {
        runBlocking {
            testChannel = Channel(Channel.UNLIMITED)
            // startFlow is only called when the first test block is offered
            block()
            resetFlow()
        }
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun startFlow() {
        this.ensureNeverFrozen()

        val flowable = flowable()
        val testChannel = testChannel
        val filter = filter

        debug("launch flow scope...")
        val started = EmptyCompletableDeferred()
        try {
            job = launch(Dispatchers.Main) {
                started.complete()
                debug("main scope launched, about to flow, test channel ${if (testChannel.isEmpty) "" else "not "}empty ")
                flowable.flow().filter(filter).collect { value ->
                    debug("in flow received [$value], test channel ${if (testChannel.isEmpty) "" else "not "}empty \"")
                    val test = testChannel.receive()
                    debug("received test block $test")
                    try {
                        test.first(value)
                        debug("ran test block $test")
                        test.second.complete(Unit)
                    } catch (t: Throwable) {
                        warn(throwable = t) { "Exception when testing... $t cause: ${t.cause}" }
                        try {
                            test.second.completeExceptionally(t)
                        } catch (t: Throwable) {
                            e(throwable = t) { "exception in completing completable" }
                        }
                    }
                    debug("handeling value completed [$value]")
                }
                debug("flow collect completed")

            }
        } catch (t:Throwable) {
            e(throwable = t) { "error launching"}
            throw t
        }
        debug("wait for main thread to be launched in $job")
        started.await()
        debug("waited for main thread to be launched")

    }

    suspend fun action(action:ActionBlock) {
        awaitTestBlocks()
        action()
        debug("did action")
    }

    var firstTestBlock = true

    suspend fun test(skip:Int=0, test:TestBlock<T>) {
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
        testChannel.offer(Pair(test, completable))
    }


}

