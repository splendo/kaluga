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

package com.splendo.kaluga.bluetooth

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BluetoothScanTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.Bluetooth, BluetoothFlowTest.BluetoothContext, Boolean>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.Bluetooth, scope: CoroutineScope) -> BluetoothContext = { configuration, scope ->
        BluetoothContext(configuration, scope)
    }

    override val flowFromTestContext: suspend BluetoothContext.() -> Flow<Boolean> = { bluetooth.isScanning() }

    @Test
    fun testIsScanning() = testWithFlowAndTestContext(
        Configuration.Bluetooth()
    ) {
        val devicesJob = CompletableDeferred<Job>()
        mainAction {
            devicesJob.complete(
                coroutineScope.launch {
                    bluetooth.scannedDevices().collect {}
                }
            )
        }
        test {
            assertFalse(it)
        }
        mainAction {
            bluetooth.startScanning()
        }
        test {
            assertTrue(it)
        }
        mainAction {
            bluetooth.stopScanning()
        }
        test {
            assertFalse(it)
        }
        devicesJob.getCompleted().cancel()
    }
}
