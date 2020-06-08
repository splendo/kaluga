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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.test.BeforeTest

typealias TestBlock<T> = suspend(T)->Unit
typealias ActionBlock = suspend()->Unit

abstract class FlowableTest<T>: BaseTest() {

    @BeforeTest
    open fun setUp() {
        super.beforeTest()

        flowable = CompletableDeferred()
        flowable.invokeOnCompletion { flowTest = FlowTest(flowable.getCompleted().flow()) }
    }

    lateinit var flowable: CompletableDeferred<Flowable<T>>
    lateinit var flowTest: FlowTest<T>

    var filter:suspend(T)->Boolean
        get() = flowTest.filter
        set(newValue) {flowTest.filter = newValue }


    fun testWithFlow(block: suspend FlowTest<T>.() -> Unit) {
        flowTest.testWithFlow(block)
    }
}

open class FlowTest<T>(private val flow: Flow<T>, private val coroutineScope: CoroutineScope = MainScope()) {

    open var filter:suspend(T)->Boolean = { true }

    private val tests:MutableList<EmptyCompletableDeferred> = mutableListOf()

    lateinit var job: Job

    private lateinit var testChannel: Channel<Pair<TestBlock<T>, EmptyCompletableDeferred>>

    private suspend fun endFlow() {
        awaitTestBlocks()// get the final test blocks that were executed and check for exceptions
        debug("test channel closed")
        job.cancel()
        debug("Ending flow")
        testChannel.close()
        tests.clear()
    }

    protected val waitForTestToSucceed = 6000L * 10

    suspend fun awaitTestBlocks() {
        withTimeout(waitForTestToSucceed) {// only wait for one minute
            try {
                debug("await all test blocks (${tests.size}), give it $waitForTestToSucceed milliseconds")
                tests.awaitAll()
            } finally {
                tests.removeAll { !it.isActive }
            }

        }
    }

    fun testWithFlow(block:suspend FlowTest<T>.()->Unit) = runBlocking {
        testChannel = Channel(Channel.UNLIMITED)
        startFlow()
        block(this@FlowTest)
        endFlow()
    }

    private suspend fun startFlow() {
        debug("start flow...")
        job = coroutineScope.launch {
            debug("main scope launched, about to flow")
            flow.filter(filter).collect { value ->
                debug("in flow received $value")
                val test = testChannel.receive()
                debug("receive test $test")
                try {
                    test.first(value)
                    test.second.complete(Unit)
                } catch (e: Throwable) {
                    debug("Exception when testing...")
                    test.second.completeExceptionally(e)
                }
                debug("flow completed")
            }
            debug("flow start scope launched")
        }
    }

    suspend fun action(action:ActionBlock) {
        awaitTestBlocks()
        action()
        debug("did action")
    }

    fun test(skip:Int=0, test:TestBlock<T>) {
        repeat(skip) {
            test {}
        }
        val completable = EmptyCompletableDeferred()
        tests.add(completable)
        testChannel.offer(Pair(test, completable))
    }


}

