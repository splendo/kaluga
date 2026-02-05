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

import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class KinematicViscosityTest {

    @Test
    fun kinematicViscosityFromAreaByTimeTest() {
        assertEqualScientificValue(1(SquareMeter per Second), (2(SquareMeter)) / 2(Second))
        assertEqualScientificValue(1(SquareMeter per Second), (2(Meter) * 1(Meter)) / 2(Second))

        assertEqualScientificValue(1(SquareFoot per Second), (2(SquareFoot)) / 2(Second))
        assertEqualScientificValue(1(SquareFoot per Second), (2(Foot) * 1(Foot)) / 2(Second))

        assertEqualScientificValue(1(SquareMeter per Second), 2(SquareMeter as Area) / 2(Second))
        assertEqualScientificValue(1(SquareMeter per Second), 2(Meter) * 1(Meter as Length) / 2(Second))
    }

    @Test
    fun kinematicViscosityFromDynamicViscosityAndDensity() {
        assertEqualScientificValue(1(SquareMeter per Second), (2(Pascal x Second)) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second), (2(PoundSquareFoot x Second)) / 2(Pound per CubicFoot), round = 28)
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot x Second)) / 2(Pound.ukImperial per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot x Second)) / 2(Pound.usCustomary per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot.ukImperial x Second)) / 2(Pound per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot.ukImperial x Second)) / 2(Pound.ukImperial per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot.usCustomary x Second)) / 2(Pound per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(SquareFoot per Second),
            (2(PoundSquareFoot.usCustomary x Second)) / 2(Pound.usCustomary per CubicFoot),
            round = 28,
        )
        assertEqualScientificValue(1(SquareMeter per Second), (2(Pascal x Second)) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot), round = 30)
    }

    @Test
    fun kinematicViscosityFromSpecificEnergyAndTime() {
        assertEqualScientificValue(4(SquareMeter per Second), (2(Joule per Kilogram)) * 2(Second))
        assertEqualScientificValue(4(SquareMeter per Second), (2(Second) * 2(Joule per Kilogram)))
        assertEqualScientificValue((4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second), (2(FootPoundForce per Pound)) * 2(Second), round = 28)
        assertEqualScientificValue((4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second), (2(Second) * 2(FootPoundForce per Pound)), round = 28)
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second),
            (2(FootPoundForce per Pound.ukImperial)) * 2(Second),
            round = 28,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second),
            (2(Second) * 2(FootPoundForce per Pound.ukImperial)),
            round = 28,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second),
            (2(FootPoundForce per Pound.usCustomary)) * 2(Second),
            round = 28,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second),
            (2(Second) * 2(FootPoundForce per Pound.usCustomary)),
            round = 28,
        )
        assertEqualScientificValue(4(SquareMeter per Second), (2((Joule per Kilogram) as SpecificEnergy)) * 2(Second))
        assertEqualScientificValue(4(SquareMeter per Second), (2(Second) * 2((Joule per Kilogram) as SpecificEnergy)))
    }
}
