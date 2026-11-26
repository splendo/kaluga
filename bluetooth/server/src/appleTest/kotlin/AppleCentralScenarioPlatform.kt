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

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.CoreBluetooth.CBManager
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.posix.getenv
import kotlin.test.fail

internal val centralScenarioPlatform: CentralScenarioPlatform = AppleCentralScenarioPlatform

/**
 * Runs the scenarios against the [TestCentral] on this machine's own Bluetooth radio, with the central on another
 * controller in range. They run when the environment variable [VARIABLE] names a [TestCentral.Player], and
 * [PLAY_PERIPHERAL_VARIABLE] `true` plays the test peripheral instead.
 */
object AppleCentralScenarioPlatform : CentralScenarioPlatform {

    const val VARIABLE = "KALUGA_BLUETOOTH_TEST_CENTRAL"
    const val PLAY_PERIPHERAL_VARIABLE = "KALUGA_BLUETOOTH_PLAY_TEST_PERIPHERAL"

    @OptIn(ExperimentalForeignApi::class)
    override val player get() = TestCentral.Player.named(getenv(VARIABLE)?.toKString())

    @OptIn(ExperimentalForeignApi::class)
    override val playsTestPeripheral get() = getenv(PLAY_PERIPHERAL_VARIABLE)?.toKString() == "true"

    // The test runner has no notion of a skipped test, so the scenario passes without running.
    override fun skip(reason: String) {
        println("skipped: $reason")
    }

    // A test process cannot answer the Bluetooth permission prompt, so it has to inherit the authorization of the app
    // that starts it, such as the terminal.
    override fun setUp() {
        val authorization = CBManager.authorization
        if (authorization != CBManagerAuthorizationAllowedAlways) {
            fail("Bluetooth is not authorized for this process ($authorization): allow it for the app that starts the tests")
        }
    }

    override fun serverBuilder(): BaseBluetoothServerBuilder = BluetoothServerBuilder()

    // A peripheral manager is not told about links, only about subscriptions and requests.
    override fun isConnected(central: ConnectedDevice): Boolean? = null

    override fun log(message: String) {
        println("[CentralTest] $message")
    }
}
