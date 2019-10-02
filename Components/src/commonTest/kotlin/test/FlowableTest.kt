package com.splendo.kaluga.test
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

import com.splendo.kaluga.EmptyCompletableDeferred
import com.splendo.kaluga.runBlocking
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.util.flow.Flowable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

typealias TestBlock<T> = suspend(T)->Unit
typealias ActionBlock = suspend()->Unit

abstract class FlowableTest<T>:BaseTest() {

    open val filter:suspend(T)->Boolean = { true }

    abstract val flowable: Flowable<T>

    private val tests:MutableList<EmptyCompletableDeferred> = mutableListOf()

    lateinit var job: Job

    private val mainScope = MainScope()

    private val testChannel = Channel<Pair<TestBlock<T>, CompletableDeferred<Unit>>>(Channel.UNLIMITED)

    private suspend fun endFlow() {
        awaitTestBlocks()// get the final test blocks that were executed and check for exceptions
        debug("Ending flow")
        testChannel.close()
        debug("test channel closed")
        job.cancel()
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

    fun runBlockingWithFlow(block:suspend()->Unit) {
        runBlocking {
            startFlow()
            block()
            endFlow()
        }
    }

    private suspend fun startFlow() {
        debug("start flow...")
        job = mainScope.launch {
            debug("main scope launched, about to flow")
            flowable.flow().filter(filter).collect { value ->
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

