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

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest

/**
 * Unpairs Device by calling `removeBond`
 * if bond state != BluetoothDevice.BOND_NONE
 */
suspend fun Flow<Device?>.unpair() = transformLatest { device ->
    device?.useState { it.unpair() }
    emit(Unit)
}.first()

/**
 * Pairs Device by calling `createBond`
 * if bond state == BluetoothDevice.BOND_NONE
 */
suspend fun Flow<Device?>.pair() = transformLatest { device ->
    device?.useState {
        when (it) {
            is DeviceState.Connected -> it.pair()
            else -> Unit
        }
    }
    emit(Unit)
}.first()
