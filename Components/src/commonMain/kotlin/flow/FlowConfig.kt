package com.splendo.mpp.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.conflate

typealias FlowModifier = (Flow<*>) -> Unit

sealed class FlowConfig(val config:FlowModifier) {

    object None:FlowConfig({})
    object Conflate:FlowConfig({it.conflate()})
    object Infinite:FlowConfig({it.buffer(Channel.UNLIMITED)})
    class Custom(options:FlowModifier):FlowConfig(options)

    fun <T, F:Flow<T>> apply(flow:F):F {
        config(flow)
        return flow
    }
}




