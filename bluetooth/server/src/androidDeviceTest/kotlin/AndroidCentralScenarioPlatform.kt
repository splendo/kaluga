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

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.splendo.kaluga.base.ApplicationHolder
import org.junit.AssumptionViolatedException

internal val centralScenarioPlatform: CentralScenarioPlatform = AndroidCentralScenarioPlatform

/**
 * Runs the scenarios against the [TestCentral] on the emulator's netsim radio. They run when the instrumentation
 * argument [ARGUMENT] is `true`.
 */
object AndroidCentralScenarioPlatform : CentralScenarioPlatform {

    const val ARGUMENT = "netsimCentral"
    private const val TAG = "CentralTest"

    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext
    private val bluetoothManager by lazy { context.getSystemService(BluetoothManager::class.java) }

    override val isEnabled: Boolean get() = InstrumentationRegistry.getArguments().getString(ARGUMENT) == "true"

    override fun skip(reason: String): Unit = throw AssumptionViolatedException(reason)

    // Granted here rather than by a rule, which would run before the skip on API levels that lack these permissions.
    override fun setUp() {
        listOf(Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT).forEach {
            instrumentation.uiAutomation.grantRuntimePermission(context.packageName, it)
        }
        if (!ApplicationHolder.isInitialized) ApplicationHolder.applicationContext = context
    }

    override fun serverBuilder(): BaseBluetoothServerBuilder = BluetoothServerBuilder(context)

    @SuppressLint("MissingPermission")
    override fun isConnected(central: ConnectedDevice): Boolean = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT_SERVER).any { it.address == central.identifier }

    override fun log(message: String) {
        Log.i(TAG, message)
    }
}
