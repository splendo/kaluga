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

import com.splendo.kaluga.bluetooth.CharacteristicWrapper
import com.splendo.kaluga.bluetooth.DescriptorWrapper
import com.splendo.kaluga.bluetooth.device.BluetoothGattWrapper
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock

class MockBluetoothGattWrapper(setupMocks: Boolean = true) : BluetoothGattWrapper {

    val connectMock = ::connect.mock()
    val discoverServicesMock = ::discoverServices.mock()
    val disconnectMock = ::disconnect.mock()
    val closeMock = ::close.mock()
    val readRemoteRssiMock = ::readRemoteRssi.mock()
    val requestMtuMock = ::requestMtu.mock()
    val readCharacteristicMock = ::readCharacteristic.mock()
    val readDescriptorMock = ::readDescriptor.mock()
    val writeCharacteristicMock = ::writeCharacteristic.mock()
    val writeDescriptorMock = ::writeDescriptor.mock()
    val setCharacteristicNotificationMock = ::setCharacteristicNotification.mock()

    init {
        if (setupMocks) {
            connectMock.on().doReturn(true)
            discoverServicesMock.on().doReturn(true)
            readRemoteRssiMock.on().doReturn(true)
            requestMtuMock.on().doReturn(true)
            readCharacteristicMock.on().doReturn(true)
            readDescriptorMock.on().doReturn(true)
            writeCharacteristicMock.on().doReturn(true)
            writeDescriptorMock.on().doReturn(true)
            setCharacteristicNotificationMock.on().doReturn(true)
        }
    }

    override fun connect(): Boolean = connectMock.call()

    override fun discoverServices(): Boolean = discoverServicesMock.call()

    override fun disconnect(): Unit = disconnectMock.call()

    override fun close(): Unit = closeMock.call()

    override fun readRemoteRssi(): Boolean = readRemoteRssiMock.call()

    override fun requestMtu(mtu: Int): Boolean = requestMtuMock.call(mtu)

    override fun readCharacteristic(wrapper: CharacteristicWrapper): Boolean = readCharacteristicMock.call(wrapper)

    override fun readDescriptor(wrapper: DescriptorWrapper): Boolean = readDescriptorMock.call(wrapper)

    override fun writeCharacteristic(wrapper: CharacteristicWrapper, value: ByteArray): Boolean = writeCharacteristicMock.call(wrapper, value)

    override fun writeDescriptor(wrapper: DescriptorWrapper, value: ByteArray): Boolean = writeDescriptorMock.call(wrapper, value)

    override fun setCharacteristicNotification(wrapper: CharacteristicWrapper, enable: Boolean): Boolean = setCharacteristicNotificationMock.call(wrapper, enable)
}
