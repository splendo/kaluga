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

package com.splendo.kaluga.bluetooth.server

/**
 * What the scenarios against the [TestCentral] need from the platform they run on. Every test source set that
 * includes the scenarios provides one as `centralScenarioPlatform`.
 */
interface CentralScenarioPlatform {

    /** Who plays the [TestCentral] next to this test run, or `null` when nobody does: the scenarios are skipped then. */
    val player: TestCentral.Player?

    /** Whether this test run plays the [com.splendo.kaluga.bluetooth.TestPeripheral] for the client scenarios on another device. */
    val playsTestPeripheral: Boolean

    /** Ends a scenario that does not run here. A platform whose test runner knows skipped tests reports it as one. */
    fun skip(reason: String)

    /** Prepares the platform for a scenario, such as granting the permissions the server needs. */
    fun setUp()

    fun serverBuilder(): BaseBluetoothServerBuilder

    /** Whether the link to [central] is up, or `null` when the platform does not tell. */
    fun isConnected(central: ConnectedDevice): Boolean?

    fun log(message: String)
}
