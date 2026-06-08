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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteCharacteristicWrapper
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.defaultLogger

/**
 * Mock implementation of [RemoteCharacteristic]
 */
class MockCharacteristic(characteristic: RemoteCharacteristicWrapper, emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit) :
    RemoteCharacteristic(
        wrapper = characteristic,
        service = RemoteService(characteristic.service, emptyList(), {}, ContextualLogger(defaultLogger, "MockService")),
        emitNewAction = emitNewAction,
        logger = ContextualLogger(defaultLogger, "MockCharacteristic"),
    )
