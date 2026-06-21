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

package com.splendo.kaluga.bluetooth.sharedclient

import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedCharacteristic
import com.splendo.kaluga.bluetooth.sharedcontract.RemoteSharedService
import com.splendo.kaluga.bluetooth.sharedcontract.SharedDeviceClient
import kotlin.test.Test
import kotlin.test.assertNotNull

// This module generates concrete Bluetooth* implementations of the shared contract interfaces (the interfaces live in
// :validation:contract). They need the Bluetooth runtime to instantiate, so instead of building one we prove at compile
// time that each generated implementation exists and conforms to its contract interface: the upcasts below only compile
// if the type exists and actually implements the interface — which compiling the generated sources alone won't catch.
class GeneratedExternalApiClientStructureTest {

    private fun asClient(impl: BluetoothSharedDeviceClient): SharedDeviceClient = impl
    private fun asService(impl: BluetoothRemoteSharedService): RemoteSharedService = impl
    private fun asCharacteristic(impl: BluetoothRemoteSharedCharacteristic): RemoteSharedCharacteristic = impl

    @Test
    fun generatedImplementationsConformToContract() {
        assertNotNull(::asClient)
        assertNotNull(::asService)
        assertNotNull(::asCharacteristic)
    }
}
