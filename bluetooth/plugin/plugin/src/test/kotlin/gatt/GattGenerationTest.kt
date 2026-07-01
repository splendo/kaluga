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

import com.squareup.kotlinpoet.KModifier
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GattGenerationTest {

    private fun resourceFile(path: String) = File(checkNotNull(javaClass.getResource(path)) { "missing $path" }.toURI())

    @Test
    fun generatesAndLinksDefinitionsFromMixedXmlFiles() {
        val types = GattGeneration.generate(
            listOf(resourceFile("/gatt/environmental_sample.xml"), resourceFile("/gatt/environmental_sensing_service.xml")),
            deviceName = "Environmental Sensor",
            packageName = "com.example.generated",
        ).types()

        assertNotNull(types.getValue("EnvironmentalSensor").annotation("Bluetooth"))
        // the service (one file) links the characteristic (the other file) by its interface type
        val service = types.getValue("EnvironmentalSensingSample")
        assertEquals("\"181A\"", checkNotNull(service.annotation("BluetoothService")).argument)
        assertEquals("EnvironmentalSample", checkNotNull(service.property("environmentalSample")).type.simpleName)
        // the linked characteristic and its value class both exist
        assertEquals("\"2BCE\"", checkNotNull(types.getValue("EnvironmentalSample").annotation("BluetoothCharacteristic")).argument)
        assertEquals("EnvironmentalSampleValue", checkNotNull(types.getValue("EnvironmentalSample").property("value")).type.simpleName)
        assertTrue(KModifier.DATA in types.getValue("EnvironmentalSampleValue").modifiers)
    }
}
