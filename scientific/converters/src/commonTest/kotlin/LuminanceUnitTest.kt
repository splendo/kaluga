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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.illuminance.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Candela
import com.splendo.kaluga.scientific.unit.Deciphot
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Steradian
import com.splendo.kaluga.scientific.unit.Stilb
import kotlin.test.Test

class LuminanceUnitTest {

    @Test
    fun luminanceFromIlluminanceAndSolidAngleTest() {
        assertEqualScientificValue(1(Stilb), 2(Phot) / 2(Steradian))
        assertEqualScientificValue(1(Stilb), 20(Deciphot) / 2(Steradian))
        assertEqualScientificValue(1(Nit), 2(Lux) / 2(Steradian))
        assertEqualScientificValue(Decimal.PI(FootLambert), 2(FootCandle) / 2(Steradian), round = 32)
        assertEqualScientificValue(1(Nit), 2(Lux).convert(FootCandle as Illuminance) / 2(Steradian))
    }

    @Test
    fun luminanceFromLuminousIntensityAndAreaTest() {
        assertEqualScientificValue(1(Stilb), 2(Candela) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Nit), 2(Candela) / 2(SquareMeter))
        assertEqualScientificValue(Decimal.PI(FootLambert), (2(Candela) / 2(SquareFoot)), round = 32)
        assertEqualScientificValue(1(Nit), 2(Candela) / 2(SquareMeter).convert(SquareFoot as Area), round = 32)
    }
}
