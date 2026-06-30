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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GattUnitsTest {

    @Test
    fun mapsSimpleUnits() {
        assertEquals(ScientificUnit("Energy", "Joule", "Joule"), bluetoothScientificUnits["org.bluetooth.unit.energy.joule"])
        assertEquals(ScientificUnit("Temperature", "Celsius", "Celsius"), bluetoothScientificUnits["org.bluetooth.unit.thermodynamic_temperature.degree_celsius"])
        assertEquals(ScientificUnit("Frequency", "BeatsPerMinute", "BeatsPerMinute"), bluetoothScientificUnits["org.bluetooth.unit.period.beats_per_minute"])
        assertEquals(ScientificUnit("Angle", "ArcMinute", "ArcMinute"), bluetoothScientificUnits["org.bluetooth.unit.plane_angle.minute"])
        assertEquals(ScientificUnit("Energy", "Calorie", "Calorie"), bluetoothScientificUnits["org.bluetooth.unit.energy.gram_calorie"])
    }

    @Test
    fun mapsCompoundUnits() {
        assertEquals(ScientificUnit("Speed", "MetricSpeed", "Meter per Second"), bluetoothScientificUnits["org.bluetooth.unit.velocity.metres_per_second"])
        assertEquals(
            ScientificUnit("Acceleration", "MetricAcceleration", "Meter per Second per Second"),
            bluetoothScientificUnits["org.bluetooth.unit.acceleration.metres_per_second_squared"],
        )
        assertEquals(
            ScientificUnit("DynamicViscosity", "MetricDynamicViscosity", "Pascal x Second"),
            bluetoothScientificUnits["org.bluetooth.unit.dynamic_viscosity.pascal_second"],
        )
        // constructed from existing units rather than named ones
        assertEquals(
            ScientificUnit("Speed", "MetricSpeed", "NauticalMile per Hour"),
            bluetoothScientificUnits["org.bluetooth.unit.velocity.knot"],
        )
        assertEquals(
            ScientificUnit("AngularVelocity", "AngularVelocity", "Turn per Minute"),
            bluetoothScientificUnits["org.bluetooth.unit.angular_velocity.revolution_per_minute"],
        )
    }

    @Test
    fun leavesUnsupportedUnitsUnmapped() {
        // no Kaluga equivalent -> absent, so the generator falls back to a plain numeric value
        assertNull(bluetoothScientificUnits["org.bluetooth.unit.time.day"])
        assertNull(bluetoothScientificUnits["org.bluetooth.unit.time.year"])
        assertNull(bluetoothScientificUnits["org.bluetooth.unit.logarithmic_radio_quantity.decibel"])
    }
}
