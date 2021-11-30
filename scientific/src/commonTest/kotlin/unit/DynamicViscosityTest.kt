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
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicViscosityTest {

    @Test
    fun dynamicViscosityConversionTest() {
        assertScientificConversion(
            1.0,
            (Pascal x Second),
            0.1450377,
            PoundSquareInch x Millisecond,
            7
        )
    }

    @Test
    fun dynamicViscosityFromMomentumAndAreaTest() {
        assertEquals(1(Pascal x Second), 2(Kilogram x (Meter per Second)) / 2(SquareMeter))
        assertEqualScientificValue(
            1(PoundSquareInch x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(
                SquareInch
            ),
            5
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)) / 2(
                SquareInch
            ),
            5
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)) / 2(
                SquareInch
            ),
            5
        )
        assertEquals(
            1(Pascal x Second),
            2(Kilogram x (Meter per Second)) / 2(SquareMeter).convert(SquareFoot)
        )
    }

    @Test
    fun dynamicViscosityFromPressureAndTimeTest() {
        assertEquals(4(Pascal x Second), 2(Pascal) * 2(Second))
        assertEquals(4(Pascal x Second), 2(Second) * 2(Pascal))
        assertEquals(4(PoundSquareFoot x Second), 2(PoundSquareFoot) * 2(Second))
        assertEquals(4(PoundSquareFoot x Second), 2(Second) * 2(PoundSquareFoot))
        assertEquals(4(ImperialTonSquareFoot x Second), 2(ImperialTonSquareFoot) * 2(Second))
        assertEquals(4(ImperialTonSquareFoot x Second), 2(Second) * 2(ImperialTonSquareFoot))
        assertEquals(4(USTonSquareFoot x Second), 2(USTonSquareFoot) * 2(Second))
        assertEquals(4(USTonSquareFoot x Second), 2(Second) * 2(USTonSquareFoot))
        assertEqualScientificValue(4(Pascal x Second), 2(Pascal).convert(PoundSquareInch as Pressure) * 2(Second), 8)
    }
}
