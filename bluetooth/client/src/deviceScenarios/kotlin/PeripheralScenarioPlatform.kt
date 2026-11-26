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

import com.splendo.kaluga.bluetooth.device.ConnectableDevice

/**
 * What the scenarios against the [TestPeripheral] need from the platform they run on. Every test source set that
 * includes the scenarios provides one as `peripheralScenarioPlatform`.
 */
interface PeripheralScenarioPlatform {

    /** Whether the [TestPeripheral] runs next to this test run. Without it the scenarios are skipped. */
    val isEnabled: Boolean

    /** Whether the beacon of the [TestPeripheral] can advertise next to it, which takes a second controller. */
    val hasBeacon: Boolean

    /** Ends a scenario that does not run here. A platform whose test runner knows skipped tests reports it as one. */
    fun skip(reason: String)

    /** Prepares the platform for a scenario, such as granting the permissions the client needs. */
    fun setUp()

    fun clientBuilder(): BaseBluetoothClientBuilder

    /** Waits until a scan can start without the platform holding back its results. */
    suspend fun awaitScanStart()

    /** Waits until the platform has closed its link to [device], after the client disconnected. */
    suspend fun awaitLinkClosed(device: ConnectableDevice)

    fun log(message: String)
}
