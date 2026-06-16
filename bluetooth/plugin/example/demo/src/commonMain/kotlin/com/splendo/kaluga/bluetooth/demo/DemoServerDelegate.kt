/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.demo

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier

/**
 * Implements the generated [DemoDeviceServer.Delegate] tree, backed by [DemoServerState].
 * The same delegate drives the real Bluetooth server and the simulator.
 */
class DemoServerDelegate(private val state: DemoServerState) : DemoDeviceServer.Delegate {

    override val demoServiceDelegate: LocalDemoService.Delegate = object : LocalDemoService.Delegate {

        override val sensorDelegate: LocalSensorCharacteristic.Delegate = object : LocalSensorCharacteristic.Delegate {
            override suspend fun LocalSensorCharacteristic.onReadReading(identifier: Identifier): SensorCharacteristicReadResponse =
                SensorCharacteristicReadResponse.Success(state.reading.value)

            override fun LocalSensorCharacteristic.onSubscribeToLive(identifier: Identifier) = Unit
            override fun LocalSensorCharacteristic.onUnsubscribeToLive(identifier: Identifier) = Unit
        }

        override val configDelegate: LocalConfigCharacteristic.Delegate = object : LocalConfigCharacteristic.Delegate {

            override val infoDelegate: LocalConfigCharacteristic.LocalInfo.Delegate = object : LocalConfigCharacteristic.LocalInfo.Delegate {
                override suspend fun LocalConfigCharacteristic.LocalInfo.onReadName(identifier: Identifier): RemoteConfigCharacteristic.InfoReadResponse =
                    RemoteConfigCharacteristic.InfoReadResponse.Success(state.name.value)
            }

            override suspend fun LocalConfigCharacteristic.onWriteThreshold(threshold: Int, identifier: Identifier): GattResponse.WriteResponse {
                state.lastThresholdWritten.value = threshold
                return GattResponse.WriteSuccess.Acknowledged
            }

            override suspend fun LocalConfigCharacteristic.onFailedToWriteThreshold(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
                GattResponse.ValueNotAllowed

            override fun LocalConfigCharacteristic.onSubscribeToStatus(identifier: Identifier) = Unit
            override fun LocalConfigCharacteristic.onUnsubscribeToStatus(identifier: Identifier) = Unit
        }
    }
}
