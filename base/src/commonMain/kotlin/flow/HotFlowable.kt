package com.splendo.kaluga.base.flow

import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class HotFlowable<T>(initialValue: T, channelFactory: () -> BroadcastChannel<T> = {ConflatedBroadcastChannel()}) : BaseFlowable<T>(channelFactory) {

    init {
        setBlocking(initialValue)
    }

}