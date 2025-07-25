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

import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.luminance.times
import com.splendo.kaluga.scientific.converter.luminousExposure.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.Lambert
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Steradian
import com.splendo.kaluga.scientific.unit.Stilb
import com.splendo.kaluga.scientific.unit.x
import kotlin.math.PI
import kotlin.test.Test

class IlluminanceUnitTest {

    @Test
    fun illuminanceFromLuminanceAndSolidAngleTest() {
        assertEqualScientificValue(4(Lux), 2(Nit) * 2(Steradian))
        assertEqualScientificValue(4(Lux), 2(Steradian) * 2(Nit))
        assertEqualScientificValue(4(Phot), 2(Stilb) * 2(Steradian))
        assertEqualScientificValue(4(Phot), 2(Steradian) * 2(Stilb))
        assertEqualScientificValue((4.toDecimal() / PI.toDecimal())(Phot), 2(Lambert) * 2(Steradian))
        assertEqualScientificValue((4.toDecimal() / PI.toDecimal())(Phot), 2(Steradian) * 2(Lambert))
        assertEqualScientificValue((4.toDecimal() / PI.toDecimal())(FootCandle), 2(FootLambert) * 2(Steradian))
        assertEqualScientificValue((4.toDecimal() / PI.toDecimal())(FootCandle), 2(Steradian) * 2(FootLambert))
        assertEqualScientificValue(4(Lux), 2(Nit).convert(FootLambert as Luminance) * 2(Steradian))
        assertEqualScientificValue(4(Lux), 2(Steradian) * 2(Nit).convert(FootLambert as Luminance))
    }

    @Test
    fun illuminanceFromLuminousExposureAndTimeTest() {
        assertEqualScientificValue(1(Lux), 2(Lux x Second) / 2(Second))
        assertEqualScientificValue(1(FootCandle), 2(FootCandle x Second) / 2(Second))
        assertEqualScientificValue(1(Lux), 2((Lux x Second) as LuminousExposure) / 2(Second))
    }

    @Test
    fun illuminanceFromLuminousFluxAndAreaTest() {
        assertEqualScientificValue(1(Phot), 2(Lumen) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Lux), 2(Lumen) / 2(SquareMeter))
        assertEqualScientificValue(1(FootCandle), 2(Lumen) / 2(SquareFoot))
        assertEqualScientificValue(1(Lux), 2(Lumen) / 2(SquareMeter).convert(SquareFoot as Area))
    }
}
