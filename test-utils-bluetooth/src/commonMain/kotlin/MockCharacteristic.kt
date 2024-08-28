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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.defaultLogger
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock

/**
 * Mock implementation of [Characteristic]
 */
class MockCharacteristic(characteristic: CharacteristicWrapper, emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit) :
    Characteristic(
        wrapper = characteristic,
        emitNewAction = emitNewAction,
        parentLogTag = "",
        logger = defaultLogger,
    ) {

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [updateValue]
     */
    val updateMock = ::updateValue.mock()
    override fun updateValue(): Unit = updateMock.call()
}
