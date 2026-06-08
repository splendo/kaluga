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

import com.splendo.kaluga.bluetooth.device.WebService

/**
 * Accessor to a Web Bluetooth `BluetoothRemoteGATTService`. A structural snapshot built during the
 * (async) service discovery in the connection manager.
 */
actual interface RemoteServiceWrapper {
    actual val uuid: UUID
    actual val type: Service.Type
    actual val includedServices: List<RemoteServiceWrapper>
    actual val characteristics: List<RemoteCharacteristicWrapper>
}

internal class WebServiceWrapper(service: WebService) : RemoteServiceWrapper {
    override val uuid: UUID = uuidFrom(service.uuid)
    override val type: Service.Type = if (service.isPrimary) Service.Type.PRIMARY else Service.Type.SECONDARY

    // Web Bluetooth does not surface included services through the discovery used here.
    override val includedServices: List<RemoteServiceWrapper> = emptyList()
    override val characteristics: List<RemoteCharacteristicWrapper> = service.characteristics.map { WebCharacteristicWrapper(it, this) }
}
