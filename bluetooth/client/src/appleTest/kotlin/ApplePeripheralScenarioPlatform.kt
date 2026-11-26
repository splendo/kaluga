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
import com.splendo.kaluga.bluetooth.server.TestCentral
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManager
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.darwin.NSObject
import platform.darwin.dispatch_queue_create
import platform.posix.fflush
import platform.posix.getenv
import platform.posix.stdout
import kotlin.test.fail

internal val peripheralScenarioPlatform: PeripheralScenarioPlatform = ApplePeripheralScenarioPlatform

/**
 * Runs the scenarios against the [TestPeripheral] on this machine's own Bluetooth radio, with the peripheral on another
 * controller in range. They run when the environment variable [VARIABLE] names a [TestPeripheral.Player], and the
 * scenarios that need the beacon when [BEACON_VARIABLE] is `true` as well. [PLAY_CENTRAL_VARIABLE] `true` plays the test
 * central instead.
 */
object ApplePeripheralScenarioPlatform : PeripheralScenarioPlatform {

    const val VARIABLE = "KALUGA_BLUETOOTH_TEST_PERIPHERAL"
    const val BEACON_VARIABLE = "KALUGA_BLUETOOTH_TEST_BEACON"
    const val PLAY_CENTRAL_VARIABLE = "KALUGA_BLUETOOTH_PLAY_TEST_CENTRAL"

    override val stack = PeripheralScenarioPlatform.Stack.CoreBluetooth

    @OptIn(ExperimentalForeignApi::class)
    override val player get() = TestPeripheral.Player.named(getenv(VARIABLE)?.toKString())

    @OptIn(ExperimentalForeignApi::class)
    override val playsTestCentral get() = getenv(PLAY_CENTRAL_VARIABLE)?.toKString() == "true"

    @OptIn(ExperimentalForeignApi::class)
    override val hasBeacon: Boolean get() = getenv(BEACON_VARIABLE)?.toKString() == "true"

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

    override fun clientBuilder(): BaseBluetoothClientBuilder = BluetoothClientBuilder()

    override suspend fun awaitScanStart() = Unit

    override suspend fun awaitLinkClosed(device: ConnectableDevice) {
        while (systemConnections.isConnected(device)) delay(100)
    }

    // Flushed per line, as a runner reads the log of a peer while it plays.
    @OptIn(ExperimentalForeignApi::class)
    override fun log(message: String) {
        println("[PeripheralTest] $message")
        fflush(stdout)
    }

    private val systemConnections by lazy { SystemConnections() }
}

/** The peripherals connected to this machine, whichever process connected them. */
private class SystemConnections :
    NSObject(),
    CBCentralManagerDelegateProtocol {

    private val poweredOn = CompletableDeferred<Unit>()
    private val manager = CBCentralManager(this, dispatch_queue_create("SystemConnections", null))

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state == CBManagerStatePoweredOn) poweredOn.complete(Unit)
    }

    suspend fun isConnected(device: ConnectableDevice): Boolean {
        poweredOn.await()
        return manager.retrieveConnectedPeripheralsWithServices(listOf(TestPeripheral.service, TestCentral.service)).any { (it as CBPeripheral).identifier == device.identifier }
    }
}
