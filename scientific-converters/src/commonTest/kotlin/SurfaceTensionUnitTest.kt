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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class SurfaceTensionUnitTest {

    @Test
    fun surfaceTensionFromEnergyAndAreaTest() {
        assertEqualScientificValue(1(Dyne per Centimeter), 2(Erg) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Dyne per Centimeter), 20(Decierg) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Newton per Meter), 2(Joule) / 2(SquareMeter))
        assertEqualScientificValue(1(Newton per Meter), 2(Joule).convert(WattHour) / 2(SquareMeter), round = 32)
        assertEqualScientificValue(1(Poundal per Foot), 2(FootPoundal) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(PoundForce per Inch), 2(InchPoundForce) / 2(SquareInch), round = 32)
        assertEqualScientificValue(1(OunceForce per Inch), 2(InchOunceForce) / 2(SquareInch))
        assertEqualScientificValue(1(PoundForce per Foot), 2(FootPoundForce) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(PoundForce per Foot), 2(FootPoundForce).convert(WattHour) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(Newton per Meter), 2(Joule) / 2(SquareMeter).convert(SquareFoot), round = 32)
    }

    @Test
    fun surfaceTensionFromForceAndLengthTest() {
        assertEqualScientificValue(1(Newton per Meter), 2(Newton) / 2(Meter))
        assertEqualScientificValue(1(PoundForce per Foot), 2(PoundForce) / 2(Foot), round = 32)
        assertEqualScientificValue(1(ImperialTonForce per Foot), 2(ImperialTonForce) / 2(Foot), round = 32)
        assertEqualScientificValue(1(UsTonForce per Foot), 2(UsTonForce) / 2(Foot), round = 32)
        assertEqualScientificValue(1(Newton per Meter), 2(Newton) / 2(Meter).convert(Foot), round = 32)
    }
}
