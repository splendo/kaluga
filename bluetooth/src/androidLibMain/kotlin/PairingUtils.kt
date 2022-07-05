/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.device.ConnectibleDeviceState
import com.splendo.kaluga.bluetooth.device.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest

// TODO: consider moving this to commonMain

/**
 * Unpairs Device by calling `removeBond`
 * if bond state != BluetoothDevice.BOND_NONE
 */
suspend fun Flow<Device?>.pair() {
    return state().transformLatest { deviceState ->
        when (deviceState) {
            is ConnectibleDeviceState.Connected -> {
                emit(deviceState.pair())
            }
            else -> {}
        }
    }.first()
}

/**
 * Pairs Device by calling `createBond`
 * if bond state == BluetoothDevice.BOND_NONE
 */
suspend fun Flow<Device?>.unpair() {
    return state().transformLatest { deviceState ->
        when (deviceState) {
            is ConnectibleDeviceState -> {
                emit(deviceState.unpair())
            }
            else -> {}
        }
    }.first()
}
