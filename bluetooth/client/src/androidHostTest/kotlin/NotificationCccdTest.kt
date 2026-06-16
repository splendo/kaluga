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

package com.splendo.kaluga.bluetooth

import android.content.ContextWrapper
import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.base.test.mock.verifyWithin
import com.splendo.kaluga.base.test.testBlockingAndCancelScope
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.test.AndroidMockCharacteristicWrapper
import com.splendo.kaluga.bluetooth.test.MockBluetoothGattWrapper
import com.splendo.kaluga.bluetooth.test.MockCharacteristic
import com.splendo.kaluga.bluetooth.test.MockDeviceWrapper
import com.splendo.kaluga.bluetooth.test.MockRemoteServiceWrapper
import org.junit.Test

class NotificationCccdTest : BaseTest() {

    companion object {
        const val NAME = "MockDevice"
    }

    /**
     * Enabling notifications must write ENABLE_NOTIFICATION_VALUE to the characteristic's
     * Client Characteristic Configuration Descriptor. The CCCD is hidden from
     * [RemoteCharacteristic.descriptors] (it is owned by the notify state machine), so the
     * connection manager has to resolve it through the platform wrapper instead.
     */
    @Test
    fun enableNotificationWritesCccd(): Unit = testBlockingAndCancelScope {
        val deviceWrapper = MockDeviceWrapper(NAME, NAME, DeviceWrapper.BondState.NONE, setupMocks = false)
        val gatt = MockBluetoothGattWrapper(setupMocks = true)
        deviceWrapper.connectGattMock.on().doReturn(gatt)

        val manager = DefaultDeviceConnectionManager(ContextWrapper(null), deviceWrapper, ConnectionSettings(), this)
        manager.connect()

        val characteristic = MockCharacteristic(
            AndroidMockCharacteristicWrapper(
                descriptorUUIDs = listOf(Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR),
                service = MockRemoteServiceWrapper(),
                properties = setOf(CharacteristicProperty.Notify),
            ),
        ) {}

        manager.performAction(DeviceAction.Notification.Enable(characteristic))

        gatt.setCharacteristicNotificationMock.verify()
        gatt.writeDescriptorMock.verifyWithin()
    }
}
