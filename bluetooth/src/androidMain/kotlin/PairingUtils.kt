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

import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest

// TODO: consider moving this to commonMain

/**
 * Pairs a ([Flow] of) [Device] by waiting for it to become connected and calling `[ConnectableDeviceState.Connected.pair]`
 */
suspend fun Flow<Device?>.pair() = state().transformLatest { deviceState ->
    when (deviceState) {
        is ConnectableDeviceState.Connected -> {
            emit(deviceState.pair())
        }
        else -> {}
    }
}.first()

/**
 * Pairs a ([Flow] of) [Device] by calling `[ConnectableDeviceState.unpair]` on the first [ConnectableDeviceState]
 */
suspend fun Flow<Device?>.unpair() = state().transformLatest { deviceState ->
    when (deviceState) {
        is ConnectableDeviceState -> {
            emit(deviceState.unpair())
        }
        else -> {}
    }
}.first()
