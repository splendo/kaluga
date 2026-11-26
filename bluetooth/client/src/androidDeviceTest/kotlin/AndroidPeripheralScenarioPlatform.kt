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

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.AssumptionViolatedException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

internal val peripheralScenarioPlatform: PeripheralScenarioPlatform = AndroidPeripheralScenarioPlatform

/**
 * Runs the scenarios against the [TestPeripheral] on the emulator's netsim radio. They run when the instrumentation
 * argument [ARGUMENT] is `true`.
 */
object AndroidPeripheralScenarioPlatform : PeripheralScenarioPlatform {

    const val ARGUMENT = "netsimPeripheral"
    private const val TAG = "PeripheralTest"

    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext
    private val bluetoothManager by lazy { context.getSystemService(BluetoothManager::class.java) }

    override val stack = PeripheralScenarioPlatform.Stack.Android

    override val isEnabled: Boolean get() = InstrumentationRegistry.getArguments().getString(ARGUMENT) == "true"

    override val hasBeacon = true

    override fun skip(reason: String): Unit = throw AssumptionViolatedException(reason)

    // Granted here rather than by a rule, which would run before the skip on API levels that lack these permissions.
    override fun setUp() {
        listOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT).forEach {
            instrumentation.uiAutomation.grantRuntimePermission(context.packageName, it)
        }
        if (!ApplicationHolder.isInitialized) ApplicationHolder.applicationContext = context
    }

    override fun clientBuilder(): BaseBluetoothClientBuilder = BluetoothClientBuilder(context)

    override suspend fun awaitScanStart() = ScanBudget.take()

    @SuppressLint("MissingPermission")
    override suspend fun awaitLinkClosed(device: ConnectableDevice) {
        while (bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).any { it.address == device.identifier }) delay(100)
    }

    override fun log(message: String) {
        Log.i(TAG, message)
    }
}

/**
 * Android allows an app five scan starts in 30 seconds and silently returns no results after that. Every test that
 * starts a scan takes a start from here first, so a whole run stays under that limit.
 */
private object ScanBudget {
    private const val MAX_STARTS = 4
    private val window = 32.seconds
    private val mutex = Mutex()
    private val starts = ArrayDeque<TimeMark>()

    suspend fun take() = mutex.withLock {
        while (true) {
            while (starts.isNotEmpty() && starts.first().elapsedNow() > window) starts.removeFirst()
            if (starts.size < MAX_STARTS) break
            delay(window - starts.first().elapsedNow() + 1.seconds)
        }
        starts.addLast(TimeSource.Monotonic.markNow())
    }
}
