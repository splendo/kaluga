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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.dynamicViscosity.times
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class MomentumUnitTest {

    @Test
    fun momentumFromDynamicViscosityAndAreaTest() {
        assertEqualScientificValue(4(Kilogram x (Meter per Second)), 2(Pascal x Second) * 2(SquareMeter))
        assertEqualScientificValue(4(Kilogram x (Meter per Second)), 2(SquareMeter) * 2(Pascal x Second))
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound x (Foot per Second)),
            2(PoundSquareFoot x Second) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot x Second),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.ukImperial x (Foot per Second)),
            2(PoundSquareFoot.ukImperial x Second) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.ukImperial x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot.ukImperial x Second),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(PoundSquareFoot.usCustomary x Second) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot.usCustomary x Second),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(Pascal x Second).convert((PoundSquareFoot x Second) as DynamicViscosity) * 2(
                SquareMeter,
            ),
            round = 30,
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(SquareMeter) * 2(Pascal x Second).convert((PoundSquareFoot x Second) as DynamicViscosity),
            round = 30,
        )
    }

    @Test
    fun momentumFromForceAndTimeTest() {
        assertEqualScientificValue(4(Gram x (Centimeter per Second)), 2(Dyne) * 2(Second))
        assertEqualScientificValue(4(Gram x (Centimeter per Second)), 2(Second) * 2(Dyne))
        assertEqualScientificValue(4(Gram x (Centimeter per Second)), 20(Decidyne) * 2(Second))
        assertEqualScientificValue(4(Gram x (Centimeter per Second)), 2(Second) * 20(Decidyne))
        assertEqualScientificValue(4(Kilogram x (Meter per Second)), 2(Newton) * 2(Second))
        assertEqualScientificValue(4(Kilogram x (Meter per Second)), 2(Second) * 2(Newton))
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Tonne x (Meter per Second)),
            2(TonneForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Tonne x (Meter per Second)),
            2(Second) * 2(TonneForce),
        )
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Gram x (Meter per Second)),
            2(GramForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Gram x (Meter per Second)),
            2(Second) * 2(GramForce),
        )
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Milligram x (Meter per Second)),
            2(MilligramForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Milligram x (Meter per Second)),
            2(Second) * 2(MilligramForce),
        )

        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound x (Foot per Second)),
            2(PoundForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound x (Foot per Second)),
            2(Second) * 2(PoundForce),
        )
        assertEqualScientificValue(4(Pound x (Foot per Second)), 2(Poundal) * 2(Second))
        assertEqualScientificValue(4(Pound x (Foot per Second)), 2(Second) * 2(Poundal))
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Ounce x (Foot per Second)),
            2(OunceForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Ounce x (Foot per Second)),
            2(Second) * 2(OunceForce),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Grain x (Foot per Second)),
            2(GrainForce) * 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Grain x (Foot per Second)),
            2(Second) * 2(GrainForce),
            round = 32,
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.ukImperial x (Foot per Second)),
            2(PoundForce.ukImperial) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.ukImperial x (Foot per Second)),
            2(Second) * 2(PoundForce.ukImperial),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(PoundForce.usCustomary) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(Second) * 2(PoundForce.usCustomary),
        )
        assertEqualScientificValue(
            (4000.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(Kip) * 2(Second),
        )
        assertEqualScientificValue(
            (4000.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)),
            2(Second) * 2(Kip),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(UsTon x (Foot per Second)),
            2(UsTonForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(UsTon x (Foot per Second)),
            2(Second) * 2(UsTonForce),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(ImperialTon x (Foot per Second)),
            2(ImperialTonForce) * 2(Second),
        )
        assertEqualScientificValue(
            (4.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(ImperialTon x (Foot per Second)),
            2(Second) * 2(ImperialTonForce),
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(Newton).convert(PoundForce as Force) * 2(Second),
            round = 30,
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(Second) * 2(Newton).convert(PoundForce as Force),
            round = 30,
        )
    }

    @Test
    fun momentumFromMassAndSpeedTest() {
        assertEqualScientificValue(4(Kilogram x (Meter per Second)), 2(Kilogram) * 2(Meter per Second))
        assertEqualScientificValue(4(Pound x (Foot per Second)), 2(Pound) * 2(Foot per Second))
        assertEqualScientificValue(4(ImperialTon x (Foot per Second)), 2(ImperialTon) * 2(Foot per Second))
        assertEqualScientificValue(4(UsTon x (Foot per Second)), 2(UsTon) * 2(Foot per Second))
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(Kilogram) * 2(Meter per Second).convert(Foot per Second),
            round = 32,
        )
    }
}
