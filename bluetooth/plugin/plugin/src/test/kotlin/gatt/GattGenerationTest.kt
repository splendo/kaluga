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

package com.splendo.kaluga.bluetooth.plugin.gatt

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class GattGenerationTest {

    private fun resourceFile(path: String) = File(checkNotNull(javaClass.getResource(path)) { "missing $path" }.toURI())

    @Test
    fun generatesAllDefinitionsFromMixedXmlFiles() {
        val files = listOf(
            resourceFile("/gatt/environmental_sample.xml"),
            resourceFile("/gatt/environmental_sensing_service.xml"),
        )
        val code = GattGeneration.generate(files, deviceName = "Environmental Sensor", packageName = "com.example.generated")
            .joinToString("\n") { it.toString() }

        assertTrue("@Bluetooth" in code, code)
        assertTrue("@BluetoothService(\"181A\")" in code, code)
        assertTrue("@BluetoothCharacteristic(\"2BCE\")" in code, code)
        assertTrue("data class EnvironmentalSampleValue" in code, code)
    }
}
