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
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class PressureUnitTest {

    @Test
    fun pressureConversionTest() {
        assertEquals(1e-5, Pascal.convert(1, Bar))
        assertEquals(10.0, Pascal.convert(1, Barye))
        assertEquals(0.00750062, Pascal.convert(1, Torr, 8))
        assertEquals(9.86923e-6, Pascal.convert(1, Atmosphere, 11))
        assertEquals(0.00750062, Pascal.convert(1, MillimeterOfMercury, 8))
        // assertEquals(0.020885434273039, Pascal.convert(1, PoundSquareFoot,15)) FIXME
        // assertEquals(0.0101972, Pascal.convert(1, CentimeterOfWater)) FIXME
        assertEquals(1.4504E-7, Pascal.convert(1, KiloPoundSquareInch, 11))
        assertEquals(0.0002953, Pascal.convert(1, InchOfMercury, 7))
        // assertEquals(0.00401865, Pascal.convert(1, InchOfWater,8)) FIXME

        assertEquals(1000000.0, Bar.convert(1, Barye))
        assertEquals(750.062, Bar.convert(1, Torr, 3))
        assertEquals(0.986923, Bar.convert(1, Atmosphere, 6))
        assertEquals(750.062, Bar.convert(1, MillimeterOfMercury, 3))
        // assertEquals(1019.72, Bar.convert(1, CentimeterOfWater,2)) FIXME
        assertEquals(2088.54, Bar.convert(1, PoundSquareFoot, 2))
        assertEquals(0.01450377, Bar.convert(1, KiloPoundSquareInch, 8))
        assertEquals(29.53, Bar.convert(1, InchOfMercury, 2))
        // assertEquals(401.865, Bar.convert(1, InchOfWater,3)) FIXME

        assertEquals(0.000750062, Barye.convert(1, Torr, 9))
        assertEquals(9.86923e-7, Barye.convert(1, Atmosphere, 12))
        assertEquals(0.000750062, Barye.convert(1, MillimeterOfMercury, 9))
        // assertEquals(0.00101972, Barye.convert(1, CentimeterOfWater,8)) FIXME
        // assertEquals(0.0020885434304802, Barye.convert(1, PoundSquareFoot,16)) FIXME
        // assertEquals(0.000000014503774389728, Barye.convert(1, KiloPoundSquareInch,21)) FIXME
        assertEquals(2.953e-5, Barye.convert(1, InchOfMercury, 8))
        // assertEquals(0.000401865, Barye.convert(1, InchOfWater,9)) FIXME

        assertEquals(0.00131579, Torr.convert(1, Atmosphere, 8))
        // assertEquals(1.0, Torr.convert(1, MillimeterOfMercury)) FIXME
        // assertEquals(1.35951, Torr.convert(1, CentimeterOfWater,5)) FIXME
        assertEquals(2.7845, Torr.convert(1, PoundSquareFoot, 4))
        // assertEquals(0.000019336767111886182, Torr.convert(1, KiloPoundSquareInch,21)) FIXME
        assertEquals(0.0393701, Torr.convert(1, InchOfMercury, 7))
        // assertEquals(0.535775, Torr.convert(1, InchOfWater,6)) FIXME

        assertEquals(760.0, Atmosphere.convert(1, MillimeterOfMercury, 0))
        // assertEquals(1033.23, Atmosphere.convert(1, CentimeterOfWater,2)) FIXME
        assertEquals(2116.22, Atmosphere.convert(1, PoundSquareFoot, 2))
        // assertEquals(0.000019336767111886182, Atmosphere.convert(1, KiloPoundSquareInch)) FIXME
        assertEquals(29.9213, Atmosphere.convert(1, InchOfMercury, 4))
        // assertEquals(407.189, Atmosphere.convert(1, InchOfWater,3)) FIXME

        // assertEquals(1.35951, MillimeterOfMercury.convert(1, CentimeterOfWater)) FIXME
        assertEquals(2.78, MillimeterOfMercury.convert(1, PoundSquareFoot, 2))
        // assertEquals(0.000019336777871316, MillimeterOfMercury.convert(1, KiloPoundSquareInch,18)) FIXME
        assertEquals(0.0393701, MillimeterOfMercury.convert(1, InchOfMercury, 7))
        // assertEquals(0.535776, MillimeterOfMercury.convert(1, InchOfWater,6)) FIXME

        // assertEquals(2.048105429691234, CentimeterOfWater.convert(1, PoundSquareFoot,15)) FIXME
        // assertEquals(1.0, CentimeterOfWater.convert(1, KiloPoundSquareInch)) FIXME
        // assertEquals(0.028959017998228, CentimeterOfWater.convert(1, InchOfMercury,15)) FIXME
        // assertEquals(0.394095, CentimeterOfWater.convert(1, InchOfWater,6)) FIXME

        // assertEquals(0.0000069444448876153, PoundSquareFoot.convert(1, KiloPoundSquareInch,19)) FIXME
        assertEquals(0.0141, PoundSquareFoot.convert(1, InchOfMercury, 4))
        assertEquals(0.19, PoundSquareFoot.convert(1, InchOfWater, 2))

        // assertEquals(2036.027185, KiloPoundSquareInch.convert(1, InchOfMercury,6)) FIXME
        // assertEquals(1.0, KiloPoundSquareInch.convert(1, InchOfWater)) FIXME

        // assertEquals(13.6087, (InchOfMercury).convert(1, InchOfWater,4)) FIXME
    }

    @Test
    fun pressureFromDynamicViscosityAndTimeTest() {
        assertEqualScientificValue(1(Bar), 2(Bar x Second) / 2(Second))
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundSquareFoot x Second) / 2(Second))
        assertEqualScientificValue(1(USTonSquareFoot), 2(USTonSquareFoot x Second) / 2(Second))
        assertEqualScientificValue(
            1(ImperialTonSquareFoot),
            2(ImperialTonSquareFoot x Second) / 2(Second)
        )
    }

    @Test
    fun pressureFromEnergyAndVolumeTest() {
        // assertEqualScientificValue(0.00001(Bar), 2(Joule) / 2(CubicMeter)) FIXME https://www.calculand.com/unit-converter/?gruppe=Pressure&einheit=Joule+per+cubic+metre+%5BJ%2Fm%C2%B3%5D
        // assertEqualScientificValue(1(PoundSquareFoot), 2(WattHour) / 2(CubicFoot)) FIXME
        // assertEqualScientificValue(1(USTonSquareFoot), 2(WattHour) / 2(CubicFoot)) FIXME
        // assertEqualScientificValue(1(ImperialTonSquareFoot), 2(FootPoundForce) / 2(CubicFoot)) FIXME
    }

    @Test
    fun pressureFromForceAndAreaTest() {
        assertEqualScientificValue(1(Pascal), 2(Newton) / 2(SquareMeter))
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundForce) / 2(SquareFoot))
        assertEqualScientificValue(1(USTonSquareFoot), 2(UsTonForce) / 2(SquareFoot))
        assertEqualScientificValue(1(ImperialTonSquareFoot), 2(ImperialTonForce) / 2(SquareFoot))
    }
}