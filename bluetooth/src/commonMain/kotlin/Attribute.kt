/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.state.StateRepo

abstract class Attribute<R : DeviceAction.Read, W : DeviceAction.Write>(initialValue: ByteArray? = null, protected val stateRepo: StateRepo<DeviceState>) : BaseFlowable<ByteArray?>() {
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

    open suspend fun updateValue() {
        val nextValue = getUpdatedValue()
        set(nextValue)
    }

    internal abstract fun getUpdatedValue(): ByteArray?

    protected suspend fun addAction(action: DeviceAction) {
        stateRepo.takeAndChangeState { state ->
            when (state) {
                is DeviceState.Connected.Idle -> {
                    state.handleAction(action)
                }
                is DeviceState.Connected.HandlingAction -> {
                    state.addAction(action)
                }
                else -> {
                    state.remain
                }
            }
        }
    }
}
