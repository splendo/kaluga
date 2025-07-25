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

class ForceUnitTest {

    @Test
    fun forceConversionTest() {
        assertScientificConversion("1", Newton, "1e+9", Nanonewton)
        assertScientificConversion("1", Newton, "1e+6", Micronewton)
        assertScientificConversion("1", Newton, "1000.0", Millinewton)
        assertScientificConversion("1", Newton, "100.0", Centinewton)
        assertScientificConversion("1", Newton, "10.0", Decinewton)
        assertScientificConversion("1", Newton, "0.1", Decanewton)
        assertScientificConversion("1", Newton, "0.01", Hectonewton)
        assertScientificConversion("1", Newton, "0.001", Kilonewton)
        assertScientificConversion("1", Newton, "1e-6", Meganewton)
        assertScientificConversion("1", Newton, "1e-9", Giganewton)

        assertScientificConversion("1", Newton, "100000.0", Dyne)
        assertScientificConversion("1", Newton, "0.101972", KilogramForce, 6)
        assertScientificConversion("1", Newton, "7.23301", Poundal, 5)
        assertScientificConversion("1", Newton, "0.224809", PoundForce, 6)

        assertScientificConversion("1", Dyne, "1e+9", Nanodyne)
        assertScientificConversion("1", Dyne, "1e+6", Microdyne)
        assertScientificConversion("1", Dyne, "1000.0", Millidyne)
        assertScientificConversion("1", Dyne, "100.0", Centidyne)
        assertScientificConversion("1", Dyne, "10.0", Decidyne)
        assertScientificConversion("1", Dyne, "0.1", Decadyne)
        assertScientificConversion("1", Dyne, "0.01", Hectodyne)
        assertScientificConversion("1", Dyne, "0.001", Kilodyne)
        assertScientificConversion("1", Dyne, "1e-6", Megadyne)
        assertScientificConversion("1", Dyne, "1e-9", Gigadyne)

        assertScientificConversion("1", KilogramForce, "1000.0", GramForce)
        assertScientificConversion("1", KilogramForce, "1000000.0", MilligramForce)
        assertScientificConversion("1", KilogramForce, "0.001", TonneForce)

        assertScientificConversion("1", PoundForce, "16", OunceForce)
        assertScientificConversion("1", PoundForce, "7000", GrainForce)
        assertScientificConversion("1", PoundForce, "0.001", Kip)
        assertScientificConversion("1", PoundForce, "0.00044643", ImperialTonForce, 8)
        assertScientificConversion("1", PoundForce, "1.0", PoundForce.ukImperial)
        assertScientificConversion("1", PoundForce, "0.0005", UsTonForce)
        assertScientificConversion("1", PoundForce, "1.0", PoundForce.usCustomary)
    }
}
