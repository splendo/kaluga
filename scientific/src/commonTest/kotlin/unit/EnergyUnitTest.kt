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

package com.splendo.kaluga.scientific.unit

import kotlin.test.Test

class EnergyUnitTest {

    @Test
    fun jouleConversionTest() {
        assertScientificConversion("1.0", Joule, "1e+9", Nanojoule)
        assertScientificConversion("1.0", Joule, "1e+6", Microjoule)
        assertScientificConversion("1.0", Joule, "1000.0", Millijoule)
        assertScientificConversion("1.0", Joule, "100.0", Centijoule)
        assertScientificConversion("1.0", Joule, "10.0", Decijoule)
        assertScientificConversion("1.0", Joule, "0.1", Decajoule)
        assertScientificConversion("1.0", Joule, "0.01", Hectojoule)
        assertScientificConversion("1.0", Joule, "0.001", Kilojoule)
        assertScientificConversion("1.0", Joule, "1e-6", Megajoule)
        assertScientificConversion("1.0", Joule, "1e-9", Gigajoule)

        assertScientificConversion("1.0", Joule, "0.00027778", WattHour, 8)
        assertScientificConversion("1.0", Joule, "1.0e+7", Erg)
        assertScientificConversion("1.0", Joule, "6.24150907446076260778E18", Electronvolt, 2)
        assertScientificConversion("1.0", Joule, "0.239006", Calorie, 6)
        assertScientificConversion("1.0", Joule, "0.238846", Calorie.IT, 6)
        assertScientificConversion("1.0", Joule, "23.73", FootPoundal, 2)
        assertScientificConversion("1.0", Joule, "0.74", FootPoundForce, 2)
        assertScientificConversion("1.0", Joule, "8.85", InchPoundForce, 2)
        assertScientificConversion("1.0", Joule, "0.0000004", HorsepowerHour, 7)
        assertScientificConversion("1.0", Joule, "0.00094782", BritishThermalUnit, 8)
        assertScientificConversion("1.0", Joule, "0.00094845", BritishThermalUnit.Thermal, 8)
    }

    @Test
    fun ergConversionTest() {
        assertScientificConversion("1.0", Erg, "1e+9", Nanoerg)
        assertScientificConversion("1.0", Erg, "1e+6", Microerg)
        assertScientificConversion("1.0", Erg, "1000.0", Millierg)
        assertScientificConversion("1.0", Erg, "100.0", Centierg)
        assertScientificConversion("1.0", Erg, "10.0", Decierg)
        assertScientificConversion("1.0", Erg, "0.1", Decaerg)
        assertScientificConversion("1.0", Erg, "0.01", Hectoerg)
        assertScientificConversion("1.0", Erg, "0.001", Kiloerg)
        assertScientificConversion("1.0", Erg, "1e-6", Megaerg)
        assertScientificConversion("1.0", Erg, "1e-9", Gigaerg)
    }

    @Test
    fun electronvoltConversionTest() {
        assertScientificConversion("1.0", Electronvolt, "1e+9", Nanoelectronvolt)
        assertScientificConversion("1.0", Electronvolt, "1e+6", Microelectronvolt)
        assertScientificConversion("1.0", Electronvolt, "1000.0", Millielectronvolt)
        assertScientificConversion("1.0", Electronvolt, "100.0", Centielectronvolt)
        assertScientificConversion("1.0", Electronvolt, "10.0", Decielectronvolt)
        assertScientificConversion("1.0", Electronvolt, "0.1", Decaelectronvolt)
        assertScientificConversion("1.0", Electronvolt, "0.01", Hectoelectronvolt, 3)
        assertScientificConversion("1.0", Electronvolt, "0.001", Kiloelectronvolt, 4)
        assertScientificConversion(
            "1.0",
            Electronvolt,
            "1e-6",
            Megaelectronvolt,
            7,
            bidirectional = false,
        )
        assertScientificConversion(
            "1.0",
            Megaelectronvolt,
            "1e6",
            Electronvolt,
            7,
            bidirectional = false,
        )
        assertScientificConversion(
            "1.0",
            Electronvolt,
            "1e-9",
            Gigaelectronvolt,
            10,
            bidirectional = false,
        )
        assertScientificConversion(
            "1.0",
            Gigaelectronvolt,
            "1e9",
            Electronvolt,
            10,
            bidirectional = false,
        )
    }

    @Test
    fun wattHourConversionTest() {
        assertScientificConversion("1.0", WattHour, "1e+9", NanowattHour)
        assertScientificConversion("1.0", WattHour, "1e+6", MicrowattHour)
        assertScientificConversion("1.0", WattHour, "1000.0", MilliwattHour)
        assertScientificConversion("1.0", WattHour, "100.0", CentiwattHour)
        assertScientificConversion("1.0", WattHour, "10.0", DeciwattHour)
        assertScientificConversion("1.0", WattHour, "0.1", DecawattHour)
        assertScientificConversion("1.0", WattHour, "0.01", HectowattHour)
        assertScientificConversion("1.0", WattHour, "0.001", KilowattHour)
        assertScientificConversion("1.0", WattHour, "1e-6", MegawattHour)
        assertScientificConversion("1.0", WattHour, "1e-9", GigawattHour)
        assertScientificConversion("1.0", WattHour, "1.0", WattHour.metric)
        assertScientificConversion("1.0", WattHour, "1.0", WattHour.imperial)
    }

    @Test
    fun calorieConversionTest() {
        assertScientificConversion("1.0", Calorie, "1000.0", Millicalorie)
        assertScientificConversion("1.0", Calorie, "0.001", Kilocalorie)
        assertScientificConversion("1.0", Calorie, "1e-6", Megacalorie)

        assertScientificConversion("1.0", Calorie.IT, "1000.0", Millicalorie.IT)
        assertScientificConversion("1.0", Calorie.IT, "0.001", Kilocalorie.IT)
        assertScientificConversion("1.0", Calorie.IT, "1e-6", Megacalorie.IT)
    }

    @Test
    fun imperialEnergyConversionTest() {
        assertScientificConversion("1.0", FootPoundal, "0.03", FootPoundForce, 2)
        assertScientificConversion("1.0", InchPoundForce, "16.0", InchOunceForce, 2)
        assertScientificConversion("1.0", HorsepowerHour, "1.0", HorsepowerHour)
        assertScientificConversion("1.0", BritishThermalUnit, "1.0", BritishThermalUnit.Thermal, 2)
    }
}
