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
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class PressureUnitTest {

    @Test
    fun pressureConversionTest() {
        assertScientificConversion(1, Pascal, 1e+9, Nanopascal)
        assertScientificConversion(1, Pascal, 1e+6, Micropascal)
        assertScientificConversion(1, Pascal, 1000.0, Millipascal)
        assertScientificConversion(1, Pascal, 100.0, Centipascal)
        assertScientificConversion(1, Pascal, 10.0, Decipascal)
        assertScientificConversion(1, Pascal, 0.1, Decapascal)
        assertScientificConversion(1, Pascal, 0.01, Hectopascal)
        assertScientificConversion(1, Pascal, 0.001, Kilopascal)
        assertScientificConversion(1, Pascal, 1e-6, Megapascal)
        assertScientificConversion(1, Pascal, 1e-9, Gigapascal)

        assertScientificConversion(1, Pascal, 1e-5, Bar)
        assertScientificConversion(1, Pascal, 10.0, Barye)
        assertScientificConversion(1, Pascal, 0.00750062, Torr, 8)
        assertScientificConversion(1, Pascal, 9.86923e-6, Atmosphere, 11)
        assertScientificConversion(1, Pascal, 0.00750062, MillimeterOfMercury, 8)
        assertScientificConversion(1, Pascal, 0.10197162, MillimeterOfWater, 8)
        assertScientificConversion(1, MillimeterOfWater, 0.1, CentimeterOfWater)

        assertScientificConversion(1, Pascal, 0.0001450377, PoundSquareInch, 10)
    }

    @Test
    fun testBarConversion() {
        assertScientificConversion(1, Bar, 1e+9, Nanobar)
        assertScientificConversion(1, Bar, 1e+6, Microbar)
        assertScientificConversion(1, Bar, 1000.0, Millibar)
        assertScientificConversion(1, Bar, 100.0, Centibar)
        assertScientificConversion(1, Bar, 10.0, Decibar)
        assertScientificConversion(1, Bar, 0.1, Decabar)
        assertScientificConversion(1, Bar, 0.01, Hectobar)
        assertScientificConversion(1, Bar, 0.001, Kilobar)
        assertScientificConversion(1, Bar, 1e-6, Megabar)
        assertScientificConversion(1, Bar, 1e-9, Gigabar)
    }

    @Test
    fun testBaryeConversion() {
        assertScientificConversion(1, Barye, 1e+9, Nanobarye)
        assertScientificConversion(1, Barye, 1e+6, Microbarye)
        assertScientificConversion(1, Barye, 1000.0, Millibarye)
        assertScientificConversion(1, Barye, 100.0, Centibarye)
        assertScientificConversion(1, Barye, 10.0, Decibarye)
        assertScientificConversion(1, Barye, 0.1, Decabarye)
        assertScientificConversion(1, Barye, 0.01, Hectobarye)
        assertScientificConversion(1, Barye, 0.001, Kilobarye)
        assertScientificConversion(1, Barye, 1e-6, Megabarye)
        assertScientificConversion(1, Barye, 1e-9, Gigabarye)
    }

    @Test
    fun testTorrConversion() {
        assertScientificConversion(1, Torr, 1e+9, Nanotorr)
        assertScientificConversion(1, Torr, 1e+6, Microtorr)
        assertScientificConversion(1, Torr, 1000.0, Millitorr)
        assertScientificConversion(1, Torr, 100.0, Centitorr)
        assertScientificConversion(1, Torr, 10.0, Decitorr)
        assertScientificConversion(1, Torr, 0.1, Decatorr)
        assertScientificConversion(1, Torr, 0.01, Hectotorr)
        assertScientificConversion(1, Torr, 0.001, Kilotorr)
        assertScientificConversion(1, Torr, 1e-6, Megatorr)
        assertScientificConversion(1, Torr, 1e-9, Gigatorr)
    }

    @Test
    fun testImperialPressure() {
        assertScientificConversion(1, PoundSquareInch, 144, PoundSquareFoot)
        assertScientificConversion(1, PoundSquareInch, 16, OunceSquareInch)
        assertScientificConversion(1, PoundSquareInch, 0.001, KiloPoundSquareInch)
        assertScientificConversion(1, MillimeterOfMercury, 0.03937, InchOfMercury, 5)
        assertScientificConversion(1, MillimeterOfWater, 0.03937, InchOfWater, 5)
        assertScientificConversion(1, MillimeterOfWater, 0.00328084, FootOfWater, 8)
    }

    @Test
    fun testUSCustomaryPressure() {
        assertScientificConversion(1, PoundSquareInch, 0.001, KipSquareInch)
        assertScientificConversion(1, PoundSquareFoot, 0.001, KipSquareFoot)
        assertScientificConversion(1, PoundSquareInch, 0.0005, USTonSquareInch)
        assertScientificConversion(1, PoundSquareFoot, 0.0005, USTonSquareFoot)
        assertScientificConversion(1, PoundSquareInch, 1.0, PoundSquareInch.usCustomary)
    }

    @Test
    fun testUKImperialPressure() {
        assertScientificConversion(1, PoundSquareInch, 0.00044643, ImperialTonSquareInch, 8)
        assertScientificConversion(1, PoundSquareFoot, 0.00044643, ImperialTonSquareFoot, 8)
        assertScientificConversion(1, PoundSquareInch, 1.0, PoundSquareInch.ukImperial)
    }

    @Test
    fun pressureFromDynamicViscosityAndTimeTest() {
        assertEqualScientificValue(1(Pascal), 2(Pascal x Second) / 2(Second))
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundSquareFoot x Second) / 2(Second))
        assertEqualScientificValue(1(USTonSquareFoot), 2(USTonSquareFoot x Second) / 2(Second))
        assertEqualScientificValue(
            1(ImperialTonSquareFoot),
            2(ImperialTonSquareFoot x Second) / 2(Second)
        )
        assertEqualScientificValue(
            1(Pascal),
            2(Pascal x Second).convert((PoundSquareFoot x Second) as DynamicViscosity) / 2(Second)
        )
    }

    @Test
    fun pressureFromEnergyAndVolumeTest() {
        assertEqualScientificValue(1(Barye), 2(Erg) / 2(CubicCentimeter))
        assertEqualScientificValue(1(Barye), 20(Decierg) / 2(CubicCentimeter))
        assertEqualScientificValue(1(Pascal), 2(Joule) / 2(CubicMeter))
        assertEqualScientificValue(
            1(PoundSquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(FootPoundal) / 2(CubicFoot)
        )
        assertEqualScientificValue(1(PoundSquareFoot), 2(FootPoundForce) / 2(CubicFoot))
        assertEqualScientificValue(1(PoundSquareInch), 2(InchPoundForce) / 2(CubicInch))
        assertEqualScientificValue(
            1(PoundSquareInch),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch)
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(InchPoundForce) / 2(CubicInch.ukImperial),
            8
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(InchPoundForce) / 2(CubicInch.usCustomary),
            8
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch.ukImperial),
            8
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch.usCustomary),
            8
        )
        assertEqualScientificValue(1(Pascal), 2(Joule) / 2(CubicMeter).convert(CubicFoot), 8)
    }

    @Test
    fun pressureFromForceAndAreaTest() {
        assertEqualScientificValue(1(Barye), 2(Dyne) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Barye), 20(Decidyne) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Pascal), 2(Newton) / 2(SquareMeter))
        assertEqualScientificValue(
            1(PoundSquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Poundal) / 2(SquareFoot)
        )
        assertEqualScientificValue(
            1(PoundSquareInch),
            (2 * ImperialStandardGravityAcceleration.value)(Poundal) / 2(SquareInch)
        )
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundForce) / 2(SquareFoot))
        assertEqualScientificValue(1(PoundSquareInch), 2(PoundForce) / 2(SquareInch))
        assertEqualScientificValue(1(OunceSquareInch), 2(OunceForce) / 2(SquareInch))
        assertEqualScientificValue(
            1(OunceSquareInch),
            2(OunceForce).convert(GrainForce) / 2(SquareInch)
        )
        assertEqualScientificValue(1(KipSquareFoot), 2(Kip) / 2(SquareFoot))
        assertEqualScientificValue(1(KipSquareInch), 2(Kip) / 2(SquareInch))
        assertEqualScientificValue(1(USTonSquareFoot), 2(UsTonForce) / 2(SquareFoot))
        assertEqualScientificValue(1(USTonSquareInch), 2(UsTonForce) / 2(SquareInch))
        assertEqualScientificValue(1(ImperialTonSquareFoot), 2(ImperialTonForce) / 2(SquareFoot))
        assertEqualScientificValue(1(ImperialTonSquareInch), 2(ImperialTonForce) / 2(SquareInch))
        assertEqualScientificValue(
            1(PoundSquareInch),
            2(PoundForce) / 2(SquareInch).convert(SquareYard)
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(PoundForce.ukImperial) / 2(SquareInch)
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(PoundForce.usCustomary) / 2(SquareInch)
        )
        assertEqualScientificValue(1(Pascal), 2(Newton) / 2(SquareMeter).convert(SquareFoot))
    }
}