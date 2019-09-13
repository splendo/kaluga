package com.splendo.mpp.log.flow

import com.splendo.mpp.flow.FlowConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface Flowable<T> {
    @ExperimentalCoroutinesApi
    fun flow(flowConfig: FlowConfig = FlowConfig.Conflate): Flow<T>
}