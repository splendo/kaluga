package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.state.StateRepoAccesor

abstract class Attribute<R : DeviceAction.Read, W : DeviceAction.Write>(initialValue: ByteArray? = null, protected val stateRepoAccessor: StateRepoAccesor<DeviceState>) : BaseFlowable<ByteArray?>() {
    abstract val uuid: UUID

    init {
        setBlocking(initialValue)
    }

    suspend fun readValue() {
        addAction(createReadAction())
    }

    internal abstract fun createReadAction(): R

    suspend fun writeValue(newValue: ByteArray?) {
        addAction(createWriteAction(newValue))
    }

    internal abstract fun createWriteAction(newValue: ByteArray?): W

    internal suspend fun updateValue() {
        set(getUpdatedValue())
    }

    internal abstract fun getUpdatedValue(): ByteArray?

    protected suspend fun addAction(action: DeviceAction) {
        when (val state = stateRepoAccessor.currentState()) {
            is DeviceState.Connected.Idle -> state.handleAction(action)
            is DeviceState.Connected.HandlingAction -> state.addAction(action)
        }
    }

}