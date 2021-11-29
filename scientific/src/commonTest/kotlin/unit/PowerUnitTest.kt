/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.converter.temperature.div
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class PowerUnitTest {

    @Test
    fun powerConversionTest() {
        assertScientificConversion(1, Watt, 1e+9, Nanowatt)
        assertScientificConversion(1, Watt, 1e+6, Microwatt)
        assertScientificConversion(1, Watt, 1000.0, Milliwatt)
        assertScientificConversion(1, Watt, 100.0, Centiwatt)
        assertScientificConversion(1, Watt, 10.0, Deciwatt)
        assertScientificConversion(1, Watt, 0.1, Decawatt)
        assertScientificConversion(1, Watt, 0.01, Hectowatt)
        assertScientificConversion(1, Watt, 0.001, Kilowatt)
        assertScientificConversion(1, Watt, 1e-6, Megawatt)
        assertScientificConversion(1, Watt, 1e-9, Gigawatt)
        assertScientificConversion(1, Watt, 10000000.0, ErgPerSecond)

        assertScientificConversion(1, Watt, 0.00135962, MetricHorsepower, 8)

        assertScientificConversion(1, Watt, 0.74, FootPoundForcePerSecond, 2)
        assertScientificConversion(1, Watt, 44.25, FootPoundForcePerMinute, 2)
        assertScientificConversion(1, Watt, 0.0013, Horsepower, 4)
        assertScientificConversion(1, Watt, 0.00095, BritishThermalUnitPerSecond, 5)
        assertScientificConversion(1, Watt, 0.06, BritishThermalUnitPerMinute, 2)
        assertScientificConversion(1, Watt, 3.41, BritishThermalUnitPerHour, 2)

        assertScientificConversion(1.0, Watt, 10.0, Deciwatt.metric)
        assertScientificConversion(1.0, Watt, 10.0, Deciwatt.imperial)
    }

    @Test
    fun powerFromEnergyAndTimeTest() {
        assertEquals(1(Watt), 2(WattHour) / 2(Hour))
        assertEquals(1(Nanowatt), 2(NanowattHour) / 2(Hour))
        assertEquals(1(Microwatt), 2(MicrowattHour) / 2(Hour))
        assertEquals(1(Milliwatt), 2(MilliwattHour) / 2(Hour))
        assertEquals(1(Centiwatt), 2(CentiwattHour) / 2(Hour))
        assertEquals(1(Deciwatt), 2(DeciwattHour) / 2(Hour))
        assertEquals(1(Decawatt), 2(DecawattHour) / 2(Hour))
        assertEquals(1(Hectowatt), 2(HectowattHour) / 2(Hour))
        assertEquals(1(Kilowatt), 2(KilowattHour) / 2(Hour))
        assertEquals(1(Megawatt), 2(MegawattHour) / 2(Hour))
        assertEquals(1(Gigawatt), 2(GigawattHour) / 2(Hour))

        assertEquals(1(Watt.metric), 2(Joule) / 2(Second))
        assertEquals(1(Nanowatt.metric), 2(Nanojoule) / 2(Second))
        assertEquals(1(Microwatt.metric), 2(Microjoule) / 2(Second))
        assertEquals(1(Milliwatt.metric), 2(Millijoule) / 2(Second))
        assertEquals(1(Centiwatt.metric), 2(Centijoule) / 2(Second))
        assertEquals(1(Deciwatt.metric), 2(Decijoule) / 2(Second))
        assertEquals(1(Decawatt.metric), 2(Decajoule) / 2(Second))
        assertEquals(1(Hectowatt.metric), 2(Hectojoule) / 2(Second))
        assertEquals(1(Kilowatt.metric), 2(Kilojoule) / 2(Second))
        assertEquals(1(Megawatt.metric), 2(Megajoule) / 2(Second))
        assertEquals(1(Gigawatt.metric), 2(Gigajoule) / 2(Second))

        assertEquals(1(ErgPerSecond), 2(Erg) / 2(Second))
        assertEquals(1(ErgPerSecond), 20(Decierg) / 2(Second))

        assertEqualScientificValue(
            1(FootPoundForcePerSecond),
            (2 * ImperialStandardGravityAcceleration.value)(FootPoundal) / 2(Second),
            8
        )
        assertEqualScientificValue(
            1(FootPoundForcePerMinute),
            (2 * ImperialStandardGravityAcceleration.value)(FootPoundal) / 2(Minute),
            8
        )
        assertEqualScientificValue(1(FootPoundForcePerSecond), 2(FootPoundForce) / 2(Second), 8)
        assertEqualScientificValue(1(FootPoundForcePerMinute), 2(FootPoundForce) / 2(Minute), 8)
        assertEqualScientificValue(1(InchPoundForcePerSecond), 2(InchPoundForce) / 2(Second), 8)
        assertEqualScientificValue(1(InchPoundForcePerMinute), 2(InchPoundForce) / 2(Minute), 8)
        assertEqualScientificValue(
            1(BritishThermalUnitPerSecond),
            2(BritishThermalUnit) / 2(Second),
            8
        )
        assertEqualScientificValue(
            1(BritishThermalUnitPerMinute),
            2(BritishThermalUnit) / 2(Minute),
            8
        )
        assertEqualScientificValue(1(BritishThermalUnitPerHour), 2(BritishThermalUnit) / 2(Hour), 8)
        assertEquals(1(Horsepower), 2(HorsepowerHour) / 2(Hour))

        assertEquals(1(Watt.imperial), 2(WattHour.imperial) / 2(Hour))
        assertEquals(1(Watt), 2(Joule).convert(WattHour as Energy) / 2(Second))
    }

    @Test
    fun powerFromForceAndSpeedTest() {
        assertEqualScientificValue(4(ErgPerSecond), 2(Dyne) * 2(Centimeter per Second))
        assertEqualScientificValue(4(ErgPerSecond), 2(Centimeter per Second) * 2(Dyne))
        assertEqualScientificValue(4(ErgPerSecond), 20(Decidyne) * 2(Centimeter per Second))
        assertEqualScientificValue(4(ErgPerSecond), 2(Centimeter per Second) * 20(Decidyne))
        assertEqualScientificValue(4(Watt), 2(Newton) * 2(Meter per Second))
        assertEqualScientificValue(4(Watt), 2(Meter per Second) * 2(Newton))
        assertEquals(4(FootPoundForcePerSecond), 2(PoundForce) * 2(Foot per Second))
        assertEquals(4(FootPoundForcePerSecond), 2(Foot per Second) * 2(PoundForce))
        assertEquals(4(FootPoundForcePerSecond), 2(PoundForce.ukImperial) * 2(Foot per Second))
        assertEquals(4(FootPoundForcePerSecond), 2(Foot per Second) * 2(PoundForce.ukImperial))
        assertEquals(4(FootPoundForcePerSecond), 2(PoundForce.usCustomary) * 2(Foot per Second))
        assertEquals(4(FootPoundForcePerSecond), 2(Foot per Second) * 2(PoundForce.usCustomary))
        assertEqualScientificValue(4(Watt), 2(Newton).convert(PoundForce) * 2(Meter per Second))
        assertEqualScientificValue(4(Watt), 2(Meter per Second) * 2(Newton).convert(PoundForce))
    }

    @Test
    fun powerFromTemperatureAndThermalResistanceDefaultTest() {
        assertEqualScientificValue(1(Watt), 2(Kelvin) / 2(Kelvin per Watt))
        assertEqualScientificValue(1(Watt), 2(Celsius) / 2(Celsius per Watt))
        assertEqualScientificValue(1(Watt.metric), 2(Celsius) / 2(Celsius per Watt.metric))
        assertEqualScientificValue(1(Watt.imperial), 2(Celsius) / 2(Celsius per Watt.imperial))
        assertEqualScientificValue(1(Watt.imperial), 2(Fahrenheit) / 2(Fahrenheit per Watt))
        assertEqualScientificValue(1(Watt), Fahrenheit.deltaValue(2(Celsius)) / 2(Celsius per Watt))
    }

    @Test
    fun powerFromVoltageAndCurrentTest() {
        assertEquals(4(ErgPerSecond), 2(Abvolt) * 2(Abampere))
        assertEquals(4(ErgPerSecond), 2(Abampere) * 2(Abvolt))
        assertEquals(4(ErgPerSecond), 2(Abvolt) * 2(Biot))
        assertEquals(4(ErgPerSecond), 2(Biot) * 2(Abvolt))
        assertEquals(4(Watt), 2(Volt) * 2(Ampere))
        assertEquals(4(Watt), 2(Ampere) * 2(Volt))
    }
}