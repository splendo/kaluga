package com.splendo.mpp.test

import com.splendo.mpp.EmptyCompletableDeferred
import com.splendo.mpp.runBlocking
import com.splendo.mpp.log.debug
import com.splendo.mpp.util.flow.Flowable
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
            tests.removeAll { !it.isActive }
            debug("await all test blocks (${tests.size}), give it $waitForTestToSucceed milliseconds")
            tests.awaitAll()

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

