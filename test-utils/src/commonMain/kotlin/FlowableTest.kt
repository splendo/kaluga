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

import co.touchlab.stately.concurrency.AtomicInt
import co.touchlab.stately.ensureNeverFrozen
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CoroutineScope
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

open class FlowTest<T>(scope: CoroutineScope, flowable:()->Flowable<T>):CoroutineScope by scope {

    val flowable: Flowable<T> = flowable()

    open var filter:suspend(T)->Boolean = { true }

    private val tests:MutableList<EmptyCompletableDeferred> = mutableListOf()

    var job: Job? = null

    private lateinit var testChannel: Channel<Pair<TestBlock<T>, EmptyCompletableDeferred>>

    private suspend fun endFlow() {
        awaitTestBlocks()// get the final test blocks that were executed and check for exceptions
        debug("test channel closed")
        job?.cancel()
        debug("Ending flow")
       // TODO: freezing issue on iOS 
       // if(!testChannel.isClosedForReceive) testChannel.close()
       // tests.clear()
    }

    protected val waitForTestToSucceed = 6000L * 10

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
            endFlow()
        }
    }

    private suspend fun startFlow() {
        this.ensureNeverFrozen()

        debug("start flow...")

        val flowable = flowable
        val testChannel = testChannel
        val filter = filter

        job = MainScope().launch {
            debug("main scope launched, about to flow, test channel ${if (testChannel.isEmpty) "" else "not "}empty ")
            flowable.flow().filter(filter).collect { value ->
                debug("in flow received [$value]")
                val test = testChannel.receive()
                debug("received test block $test")
                try {
                    test.first(value)
                    debug("ran test block $test")
                    test.second.complete(Unit)
                } catch (e: Throwable) {
                    warn("Exception when testing... $e cause: ${e.cause}", e)
                    e.printStackTrace()
                    try {
                        test.second.completeExceptionally(e)
                    } catch (e:Throwable) {
                        com.splendo.kaluga.logging.error("exception in completing", e)

                    }
                }
                debug("flow completed")
            }
            debug("flow start scope was launched")
        }
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

