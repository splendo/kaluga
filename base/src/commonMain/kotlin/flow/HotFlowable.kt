package com.splendo.kaluga.base.flow

import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

/**
 * A [BaseFlowable] that represents a Hot flow. This flowable will contain data even if not observed.
 *
 * @param T the type of the value to flow on
 * @param initialValue the initial value of the flow
 * @param channelFactory Factory for generating a [BroadcastChannel] on which the data is flown
 */
class HotFlowable<T>(initialValue: T, channelFactory: () -> BroadcastChannel<T> = {ConflatedBroadcastChannel()}) : BaseFlowable<T>(channelFactory) {

    init {
        setBlocking(initialValue)
    }

}