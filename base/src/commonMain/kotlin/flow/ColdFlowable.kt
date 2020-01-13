package com.splendo.kaluga.base.flow

import co.touchlab.stately.concurrency.AtomicInt
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.flow.FlowConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class ColdFlowable<T>(private val initialize: () -> T, private val deinitialize: (T) -> Unit, channelFactory: () -> BroadcastChannel<T> = { ConflatedBroadcastChannel() }) : BaseFlowable<T>(channelFactory) {

    private val flowingCounter = AtomicInt(0)

    @ExperimentalCoroutinesApi
    override fun flow(flowConfig: FlowConfig): Flow<T> {
        return super.flow(flowConfig).onStart {
                if (flowingCounter.incrementAndGet() <= 1) {
                    set(initialize())
                }
            }.onCompletion {
                if (flowingCounter.decrementAndGet() == 0) {
                    deinitialize(channel.value.asFlow().first())
                }
            }
    }
}