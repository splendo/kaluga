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
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.kinematicViscosity.times
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class DynamicViscosityTest {

    @Test
    fun dynamicViscosityFromKinematicViscosityAndDensity() {
        assertEqualScientificValue(4(Pascal x Second), 2(SquareMeter per Second) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Pascal x Second), 2(Kilogram per CubicMeter) * 2(SquareMeter per Second))

        assertEqualScientificValue(4(PoundSquareInch x Second), 2(SquareInch per Second) * (24 * ImperialStandardGravityAcceleration.value)(Pound per CubicInch), 5)
        assertEqualScientificValue(4(PoundSquareInch x Second), (24 * ImperialStandardGravityAcceleration.value)(Pound per CubicInch) * 2(SquareInch per Second), 5)

        assertEqualScientificValue(
            4(PoundSquareInch.ukImperial x Second),
            2(SquareInch per Second) * (24 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial per CubicInch),
            5,
        )
        assertEqualScientificValue(
            4(PoundSquareInch.ukImperial x Second),
            (24 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial per CubicInch) * 2(SquareInch per Second),
            5,
        )

        assertEqualScientificValue(
            4(PoundSquareInch.usCustomary x Second),
            2(SquareInch per Second) * (24 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary per CubicInch),
            5,
        )
        assertEqualScientificValue(
            4(PoundSquareInch.usCustomary x Second),
            (24 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary per CubicInch) * 2(SquareInch per Second),
            5,
        )

        assertEqualScientificValue(4(Pascal x Second), 2(SquareMeter per Second) * 2(Kilogram per CubicMeter).convert(Pound per CubicFoot))
        assertEqualScientificValue(4(Pascal x Second), 2(Kilogram per CubicMeter).convert(Pound per CubicFoot) * 2(SquareMeter per Second))
    }

    @Test
    fun dynamicViscosityFromMomentumAndAreaTest() {
        assertEqualScientificValue(1(Pascal x Second), 2(Kilogram x (Meter per Second)) / 2(SquareMeter))
        assertEqualScientificValue(
            1(PoundSquareInch x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(
                SquareInch,
            ),
            5,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)) / 2(
                SquareInch,
            ),
            5,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary x Second),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)) / 2(
                SquareInch,
            ),
            5,
        )
        assertEqualScientificValue(
            1(Pascal x Second),
            2(Kilogram x (Meter per Second)) / 2(SquareMeter).convert(SquareFoot),
        )
    }

    @Test
    fun dynamicViscosityFromPressureAndTimeTest() {
        assertEqualScientificValue(4(Pascal x Second), 2(Pascal) * 2(Second))
        assertEqualScientificValue(4(Pascal x Second), 2(Second) * 2(Pascal))
        assertEqualScientificValue(4(PoundSquareFoot x Second), 2(PoundSquareFoot) * 2(Second))
        assertEqualScientificValue(4(PoundSquareFoot x Second), 2(Second) * 2(PoundSquareFoot))
        assertEqualScientificValue(4(ImperialTonSquareFoot x Second), 2(ImperialTonSquareFoot) * 2(Second))
        assertEqualScientificValue(4(ImperialTonSquareFoot x Second), 2(Second) * 2(ImperialTonSquareFoot))
        assertEqualScientificValue(4(USTonSquareFoot x Second), 2(USTonSquareFoot) * 2(Second))
        assertEqualScientificValue(4(USTonSquareFoot x Second), 2(Second) * 2(USTonSquareFoot))
        assertEqualScientificValue(4(Pascal x Second), 2(Pascal).convert(PoundSquareInch as Pressure) * 2(Second), 8)
    }
}
