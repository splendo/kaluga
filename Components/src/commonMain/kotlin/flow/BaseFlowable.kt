package com.splendo.mpp.flow

import com.splendo.mpp.runBlocking
import com.splendo.mpp.util.flow.Flowable
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

open class BaseFlowable<T>(private val channel:BroadcastChannel<T> = ConflatedBroadcastChannel()):Flowable<T> {

    final override fun flow(flowConfig: FlowConfig): Flow<T> {
        return flowConfig.apply(channel.asFlow())
    }

    suspend fun set(value:T) {
        channel.send(value)
    }

    fun setBlocking(value:T) {
        // conflated broadcast channels always accept input non-blocking (provided they are open)
        runBlocking {
            channel.send(value)
        }
    }
}