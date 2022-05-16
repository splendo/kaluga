// /*
//  Copyright 2022 Splendo Consulting B.V. The Netherlands
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.
//
//  */
//
// package com.splendo.kaluga.bluetooth
//
// import com.splendo.kaluga.bluetooth.device.DeviceState
// import kotlinx.coroutines.flow.filterIsInstance
// import kotlinx.coroutines.flow.first
// import kotlin.test.Test
// import kotlin.test.assertEquals
//
// class BluetoothRequestMtuTest : BluetoothFlowTest<Int>() {
//
//     override val flow = suspend {
//         setup(Setup.DEVICE)
//         bluetooth.devices()[device.identifier].mtu()
//     }
//
//     @Test
//     fun testRequestMtu() = testWithFlow {
//         val newMtu = 512
//
//         assertEquals(-1, connectionManager.mtu)
//
//         scanDevice()
//         bluetooth.startScanning()
//         action {
//             connectDevice(device)
//             bluetooth.devices()[device.identifier].requestMtu(newMtu)
//             connectionManager.requestMtuCompleted.get().await()
//             device.filterIsInstance<DeviceState.Connected>().first()
//             connectionManager.handleNewMtu(newMtu)
//         }
//
//         assertEquals(newMtu, connectionManager.mtu)
//
//         resetFlow()
//
//         permissionManager.hasStoppedMonitoring.await()
//         mockBaseScanner().stopMonitoringPermissionsCompleted.get().await()
//     }
// }
